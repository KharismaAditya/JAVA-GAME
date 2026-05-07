package ui.battleGRIND;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import model.entity;
import method.potionDamageChangeTemp;
import method.CharDamageMain;

import java.util.ArrayList;
import java.util.Random;

/**
 * Menangani mekanisme ATTACK pada dungeon grind battle.
 * Dipisahkan dari dunGrinDisplay agar tanggung jawab lebih jelas.
 */
public class AttackHandler {

    private final dunGrinDisplay display;
    private final potionDamageChangeTemp potionSource;
    private final CharDamageMain charSource;

    public AttackHandler(dunGrinDisplay display,
                         potionDamageChangeTemp potionSource,
                         CharDamageMain charSource) {
        this.display      = display;
        this.potionSource = potionSource;
        this.charSource   = charSource;
    }

    /**
     * Eksekusi serangan pemain ke musuh saat ini.
     * Dipanggil dari dunGrinDisplay saat tombol ATTACK diklik.
     *
     * @param arrEnt   list semua entity dungeon
     * @param pause    PauseTransition bersama dari dunGrinDisplay
     * @param shopRef  referensi shopDisplay untuk cek potion aktif
     */
    public void execute(ArrayList<entity> arrEnt,
                        PauseTransition pause,
                        ui.shop.shopDisplay shopRef) {

        if (charSource.getDamageChar() == 0) {
            alertWeapon();
            return;
        }

        entity currentEnt = arrEnt.get(display.getCurrent());
        int mainDamage    = charSource.getDamageChar() + potionSource.getDamageChange();

        display.setTempHP(display.getTempHP() - mainDamage);
        display.setEnemyAttack(true);
        display.entAttackScene(currentEnt);

        // Catat damage ke potion jika sedang aktif
        if (shopRef != null && shopRef.isPotionActive()) {
            shopRef.increaseAttackCount(mainDamage);
        }

        if (display.getTempHP() <= 0) {
            onEnemyDefeated(arrEnt, pause);
        } else {
            display.refreshEntStat2();
        }
    }

    /** Dipanggil saat HP musuh habis setelah serangan biasa. */
    private void onEnemyDefeated(ArrayList<entity> arrEnt, PauseTransition pause) {
        display.nameEnt.setText("...");
        display.statEntHP.setText("ENEMY DEFEAT");
        display.statEntATK.setText("...");

        display.setCurrent(display.randomEnt(4));
        entity newEnt = arrEnt.get(display.getCurrent());
        display.setTempHP(newEnt.getEntHP());
        display.coinWin();

        pause.setOnFinished(e -> {
            display.refreshEntStat2();
            display.refreshCharStat();
        });
        pause.play();
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