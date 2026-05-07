package ui.battleGRIND;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.entity;
import method.potionDamageChangeTemp;
import method.CharDamageMain;
import ui.battleBOSS.dunBossComp;

import java.util.ArrayList;
import java.util.Random;

/**
 * Menangani mekanisme DEFENSE (mini-game tebak angka) pada dungeon grind battle.
 * Dipisahkan dari dunGrinDisplay agar tanggung jawab lebih jelas.
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
     * Tampilkan layar defense (mini-game penjumlahan).
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

        // Sembunyikan tampilan utama, tampilkan soal
        display.displayPane.getChildren().clear();
        display.buttonsButton.getChildren().clear();

        Random rand   = new Random();
        int angka1    = rand.nextInt(50);
        int angka2    = rand.nextInt(50);
        int jawaban   = angka1 + angka2;

        VBox answerDisplay = buildAnswerDisplay(angka1, angka2);
        Button answerBut   = comp.row2("ANSWER");
        answerDisplay.getChildren().add(answerBut);

        display.displayPane.getChildren().add(answerDisplay);

        answerBut.setOnMouseClicked(e ->
                onAnswerSubmitted(arrEnt, pause, jawaban)
        );
    }

    // -----------------------------------------------------------------------
    //  Private helpers
    // -----------------------------------------------------------------------

    private VBox buildAnswerDisplay(int angka1, int angka2) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D9D9D9");
        box.setPadding(new Insets(30, 10, 30, 10));
        box.setAlignment(Pos.CENTER);
        box.setMinSize(480, 200);
        box.setSpacing(5);
        box.getChildren().add(comp.defenseGuess(angka1, angka2));
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

        int answer = Integer.parseInt(input.trim());

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("SELECT WEAPON FIRST");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }
}