package ui.battleGRIND;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.entity;
import method.potionDamageChangeTemp;
import method.CharDamageMain;
import ui.battleBOSS.dunBossComp;

import java.util.ArrayList;
import java.util.Random;

/**
 * Menangani mekanisme DEFENSE (mini-game matematika) pada dungeon grind battle
 * dengan timer countdown 3 detik sebelum soal matematika muncul.
 */
public class DefenseHandler {

    private final dunGrinDisplay display;
    private final potionDamageChangeTemp potionSource;
    private final CharDamageMain charSource;
    private final dunBossComp comp;

    public DefenseHandler(dunGrinDisplay display,
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
     * Dipanggil dari dunGrinDisplay saat tombol DEFENSE diklik.
     *
     * @param arrEnt list semua entity dungeon
     * @param pause  PauseTransition bersama dari dunGrinDisplay
     */
    public void execute(ArrayList<entity> arrEnt, PauseTransition pause) {

        if (charSource.getDamageChar() == 0) {
            alertWeapon();
            return;
        }

        // Sembunyikan tampilan utama
        display.displayPane.getChildren().clear();
        display.buttonsButton.getChildren().clear();

        // Langsung tampilkan soal matematika
        showMathQuestion(arrEnt, pause);
    }

    // -----------------------------------------------------------------------
    //  Tampilan & Logika Soal Matematika
    // -----------------------------------------------------------------------

    private void showMathQuestion(ArrayList<entity> arrEnt, PauseTransition pause) {
        display.displayPane.getChildren().clear();

        Random rand = new Random();
        int angka1 = rand.nextInt(50);
        int angka2 = rand.nextInt(50);
        int jawaban = angka1 + angka2;

        // shortcut: timer batas waktu 15 detik untuk dungeon grind, upgrade jika ada custom per-mob timer
        int totalSeconds = 15;
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
            onTimeOut(pause);
        });
        timer.play();

        Runnable submitAction = () -> {
            if (submitted[0]) return;
            if (onAnswerSubmitted(arrEnt, pause, jawaban)) {
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

    private boolean onAnswerSubmitted(ArrayList<entity> arrEnt,
                                      PauseTransition pause,
                                      int jawaban) {
        String input = comp.getAnswer();

        // Input kosong
        if (input == null || input.trim().isEmpty()) {
            display.defenseRNG.setText("Please enter a number!");
            display.defenseRNG.setVisible(true);
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
            return false;
        }

        int answer;
        try {
            answer = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            display.defenseRNG.setText("Invalid number!");
            display.defenseRNG.setVisible(true);
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
            return false;
        }

        if (answer == jawaban) {
            onCorrectAnswer(arrEnt, pause);
        } else {
            onWrongAnswer(pause);
        }

        restoreMainView();
        return true;
    }

    private void onTimeOut(PauseTransition pause) {
        display.defenseRNG.setText("TIME'S UP!");
        display.defenseRNG.setVisible(true);
        display.setEnemyAttack(true);
        display.refreshCharStat();
        pause.setOnFinished(e -> display.defenseRNG.setVisible(false));
        pause.play();
        restoreMainView();
    }

    private void onCorrectAnswer(ArrayList<entity> arrEnt, PauseTransition pause) {
        display.defenseRNG.setText("YOU WIN");
        display.defenseRNG.setVisible(true);

        int mainDamage = charSource.getDamageChar() + potionSource.getDamageChange();
        display.setTempHP(display.getTempHP() - mainDamage);

        if (display.getTempHP() <= 0) {
            onEnemyDefeated(arrEnt, pause);
        } else {
            display.refreshEntStat2();
            display.refreshCharStat();
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
        }
    }

    private void onWrongAnswer(PauseTransition pause) {
        display.defenseRNG.setText("YOU LOSE");
        display.defenseRNG.setVisible(true);
        display.setEnemyAttack(true);
        display.refreshCharStat();
        pause.setOnFinished(e -> display.defenseRNG.setVisible(false));
        pause.play();
    }

    private void onEnemyDefeated(ArrayList<entity> arrEnt, PauseTransition pause) {
        display.nameEnt.setText("...");
        display.statEntHP.setText("ENEMY DEFEAT");
        display.statEntATK.setText("...");

        display.setCurrent(display.randomDifferentEnt(arrEnt.size()));
        entity newEnt = arrEnt.get(display.getCurrent());
        display.setTempHP(newEnt.getEntHP());
        display.coinWin();

        pause.setOnFinished(e -> {
            display.defenseRNG.setVisible(false);
            display.refreshEntStat2();
            display.refreshCharStat();
        });
        pause.play();
    }

    /** Kembalikan tampilan utama setelah layar defense selesai. */
    private void restoreMainView() {
        display.buttonsButton.getChildren().addAll(
                display.buttonsRow1, display.buttonsRow2
        );
        display.displayPane.getChildren().clear();
        display.displayPane.getChildren().addAll(
                display.display, display.winOrLose
        );
    }

    private void alertWeapon() {
        method.GamePopup.showMessage("WEAPON REQUIRED", "SELECT WEAPON FIRST");
    }
}
