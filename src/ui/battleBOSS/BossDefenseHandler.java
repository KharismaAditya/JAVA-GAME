package ui.battleBOSS;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.entity;
import method.potionDamageChangeTemp;
import method.CharDamageMain;

import java.util.ArrayList;
import java.util.Random;

/**
 * Menangani mekanisme DEFENSE (mini-game matematika) pada dungeon boss battle
 * dengan timer countdown 3 detik sebelum soal matematika muncul.
 */
public class BossDefenseHandler {

    private final dungeonBossDisplay display;
    private final potionDamageChangeTemp potionSource;
    private final CharDamageMain charSource;
    private final dunBossComp comp;

    public BossDefenseHandler(dungeonBossDisplay display,
                              potionDamageChangeTemp potionSource,
                              CharDamageMain charSource,
                              dunBossComp comp) {
        this.display      = display;
        this.potionSource = potionSource;
        this.charSource   = charSource;
        this.comp         = comp;
    }

    /**
     * Tampilkan layar defense (soal matematika langsung tanpa countdown).
     *
     * @param arrEnt          list semua boss entity
     * @param pause           PauseTransition bersama dari dungeonBossDisplay
     * @param onEnemyDefeated callback dipanggil saat boss HP habis (untuk dialog transition)
     */
    public void execute(ArrayList<entity> arrEnt,
                        PauseTransition pause,
                        Runnable onEnemyDefeated) {

        display.displayPane.getChildren().clear();
        display.buttonsButton.getChildren().clear();

        showMathQuestion(arrEnt, pause, onEnemyDefeated);
    }

    // -----------------------------------------------------------------------
    //  Tampilan & Logika Soal Matematika
    // -----------------------------------------------------------------------

    private void showMathQuestion(ArrayList<entity> arrEnt,
                                  PauseTransition pause,
                                  Runnable onEnemyDefeated) {
        display.displayPane.getChildren().clear();

        Random rand = new Random();
        int angka1 = rand.nextInt(50);
        int angka2 = rand.nextInt(50);
        int jawaban = angka1 + angka2;

        entity currentEnt = arrEnt.get(display.count());

        // shortcut: countdown linear berbasis detik statis, upgrade jika ada custom game speed atau difficulty setting
        int totalSeconds = Math.max(4, 16 - (display.count() * 2));
        int[] timeLeft = {totalSeconds};
        boolean[] submitted = {false};

        Label timerLabel = new Label("TIME LEFT: " + timeLeft[0] + "s");
        timerLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #D32F2F; -fx-font-weight: bold;");

        TextField playerAnswer = new TextField();
        VBox answerDisplay = buildAnswerDisplay(angka1, angka2, playerAnswer, timerLabel);
        Button answerBut = comp.row2("ANSWER");
        answerDisplay.getChildren().add(answerBut);
        display.displayPane.getChildren().add(answerDisplay);

        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft[0]--;
            timerLabel.setText("TIME LEFT: " + timeLeft[0] + "s");
        }));
        timer.setCycleCount(totalSeconds);
        timer.setOnFinished(e -> {
            if (submitted[0]) return;
            submitted[0] = true;
            onTimeOut(currentEnt, pause);
        });
        timer.play();

        Runnable submitAction = () -> {
            if (submitted[0]) return;
            if (onAnswerSubmitted(currentEnt, pause, jawaban, onEnemyDefeated)) {
                submitted[0] = true;
                timer.stop();
            }
        };

        answerBut.setOnMouseClicked(e -> submitAction.run());
        playerAnswer.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitAction.run();
            }
        });

        Platform.runLater(playerAnswer::requestFocus);
    }

    private VBox buildAnswerDisplay(int angka1, int angka2, TextField playerAnswer, Label timerLabel) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D9D9D9");
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setAlignment(Pos.CENTER);
        box.setMinSize(480, 200);
        box.setSpacing(6);
        VBox guessBox = comp.defenseGuess(angka1, angka2, playerAnswer);
        guessBox.setMinSize(480, 90);
        box.getChildren().addAll(timerLabel, guessBox);
        return box;
    }

    private boolean onAnswerSubmitted(entity currentEnt,
                                      PauseTransition pause,
                                      int jawaban,
                                      Runnable onEnemyDefeated) {
        String input = comp.getAnswer();

        if (input == null || input.trim().isEmpty()) {
            display.defenseRNG.setVisible(true);
            display.defenseRNG.setText("Please enter a number!");
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
            return false;
        }

        int answer;
        try {
            answer = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            display.defenseRNG.setVisible(true);
            display.defenseRNG.setText("Invalid number!");
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
            return false;
        }

        if (answer == jawaban) {
            onCorrectAnswer(currentEnt, pause, onEnemyDefeated);
        } else {
            onWrongAnswer(currentEnt, pause);
        }

        restoreMainView();
        return true;
    }

    private void onTimeOut(entity currentEnt, PauseTransition pause) {
        display.defenseRNG.setVisible(true);
        display.defenseRNG.setText("TIME'S UP!");
        display.setEnemyAttack(true);
        display.refreshCharStat();

        pause.setOnFinished(e2 -> {
            display.defenseRNG.setVisible(false);
            display.entAttackScene(currentEnt);
        });
        pause.play();

        display.entSkillScene(currentEnt);
        restoreMainView();
    }

    private void onCorrectAnswer(entity currentEnt,
                                 PauseTransition pause,
                                 Runnable onEnemyDefeated) {
        display.defenseRNG.setVisible(true);
        display.defenseRNG.setText("YOU WIN");

        int mainDamage = charSource.getDamageChar() + potionSource.getDamageChange();
        currentEnt.setEntHP(currentEnt.getEntHP() - mainDamage);

        if (currentEnt.getEntHP() <= 0) {
            onBossDefeated(currentEnt, pause, onEnemyDefeated);
        } else {
            onBossStillAlive(currentEnt, pause);
        }
    }

    private void onBossDefeated(entity currentEnt,
                                PauseTransition pause,
                                Runnable onEnemyDefeated) {
        currentEnt.setEntHP(0);
        display.coinWin();
        display.Mainchar.setCharAtkLVL(display.Mainchar.getCharAtkLVL() + 1);
        display.refreshEntStat();

        if (onEnemyDefeated != null) {
            pause.setOnFinished(e2 -> onEnemyDefeated.run());
            pause.play();
        }
    }

    private void onBossStillAlive(entity currentEnt, PauseTransition pause) {
        display.refreshEntStat();
        display.refreshCharStat();
        pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
        pause.play();
        display.entSkillScene(currentEnt);
    }

    private void onWrongAnswer(entity currentEnt, PauseTransition pause) {
        display.defenseRNG.setVisible(true);
        display.defenseRNG.setText("YOU LOSE");
        display.setEnemyAttack(true);
        display.refreshCharStat();

        pause.setOnFinished(e2 -> {
            display.defenseRNG.setVisible(false);
            display.entAttackScene(currentEnt);
        });
        pause.play();

        display.entSkillScene(currentEnt);
    }

    private void restoreMainView() {
        display.buttonsButton.getChildren().addAll(
                display.buttonsRow1, display.buttonsRow2
        );
        display.displayPane.getChildren().clear();
        display.displayPane.getChildren().addAll(
                display.display, display.winOrLose
        );
    }
}
