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
     * Tampilkan layar defense dengan countdown timer 3 detik sebelum soal muncul.
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

        // 1. Tampilkan countdown timer 3 detik persiapan
        Label countdownLabel = new Label("3");
        VBox countdownBox = comp.countdownDisplay(countdownLabel);
        display.displayPane.getChildren().add(countdownBox);

        final int[] secondsLeft = {3};
        Timeline countdownTimeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.seconds(1), event -> {
            secondsLeft[0]--;
            if (secondsLeft[0] > 0) {
                countdownLabel.setText(String.valueOf(secondsLeft[0]));
            } else {
                countdownTimeline.stop();
                // 2. Timer 3 detik selesai: soal matematika muncul
                showMathQuestion(arrEnt, pause, onEnemyDefeated);
            }
        });

        countdownTimeline.getKeyFrames().add(kf);
        countdownTimeline.setCycleCount(3);
        countdownTimeline.play();
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

        TextField playerAnswer = new TextField();
        VBox answerDisplay = buildAnswerDisplay(angka1, angka2, playerAnswer);
        Button answerBut = comp.row2("ANSWER");
        answerDisplay.getChildren().add(answerBut);
        display.displayPane.getChildren().add(answerDisplay);

        Runnable submitAction = () -> onAnswerSubmitted(currentEnt, pause, jawaban, onEnemyDefeated);

        answerBut.setOnMouseClicked(e -> submitAction.run());
        playerAnswer.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitAction.run();
            }
        });

        Platform.runLater(playerAnswer::requestFocus);
    }

    private VBox buildAnswerDisplay(int angka1, int angka2, TextField playerAnswer) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D9D9D9");
        box.setPadding(new Insets(30, 10, 30, 10));
        box.setAlignment(Pos.CENTER);
        box.setMinSize(480, 200);
        box.setSpacing(5);
        box.getChildren().add(comp.defenseGuess(angka1, angka2, playerAnswer));
        return box;
    }

    private void onAnswerSubmitted(entity currentEnt,
                                   PauseTransition pause,
                                   int jawaban,
                                   Runnable onEnemyDefeated) {
        String input = comp.getAnswer();

        if (input == null || input.trim().isEmpty()) {
            display.defenseRNG.setVisible(true);
            display.defenseRNG.setText("Please enter a number!");
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
            return;
        }

        int answer;
        try {
            answer = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            display.defenseRNG.setVisible(true);
            display.defenseRNG.setText("Invalid number!");
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
            return;
        }

        if (answer == jawaban) {
            onCorrectAnswer(currentEnt, pause, onEnemyDefeated);
        } else {
            onWrongAnswer(currentEnt, pause);
        }

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
