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
     * Tampilkan layar defense dengan countdown timer 3 detik sebelum soal muncul.
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
                showMathQuestion(arrEnt, pause);
            }
        });

        countdownTimeline.getKeyFrames().add(kf);
        countdownTimeline.setCycleCount(3);
        countdownTimeline.play();
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

        TextField playerAnswer = new TextField();
        VBox answerDisplay = buildAnswerDisplay(angka1, angka2, playerAnswer);
        Button answerBut = comp.row2("ANSWER");
        answerDisplay.getChildren().add(answerBut);

        display.displayPane.getChildren().add(answerDisplay);

        Runnable submitAction = () -> onAnswerSubmitted(arrEnt, pause, jawaban);

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

    private void onAnswerSubmitted(ArrayList<entity> arrEnt,
                                   PauseTransition pause,
                                   int jawaban) {
        String input = comp.getAnswer();

        // Input kosong
        if (input == null || input.trim().isEmpty()) {
            display.defenseRNG.setText("Please enter a number!");
            display.defenseRNG.setVisible(true);
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
            restoreMainView();
            return;
        }

        int answer;
        try {
            answer = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            display.defenseRNG.setText("Invalid number!");
            display.defenseRNG.setVisible(true);
            pause.setOnFinished(e2 -> display.defenseRNG.setVisible(false));
            pause.play();
            restoreMainView();
            return;
        }

        if (answer == jawaban) {
            onCorrectAnswer(arrEnt, pause);
        } else {
            onWrongAnswer(pause);
        }

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
        pause.play();
    }

    private void onEnemyDefeated(ArrayList<entity> arrEnt, PauseTransition pause) {
        display.setCurrent(display.randomEnt(4));
        entity newEnt = arrEnt.get(display.getCurrent());
        display.setTempHP(newEnt.getEntHP());
        display.coinWin();

        display.refreshEntStat2();
        display.refreshCharStat();
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
