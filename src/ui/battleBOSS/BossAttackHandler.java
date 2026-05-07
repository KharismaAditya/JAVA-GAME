package ui.battleBOSS;

import javafx.animation.PauseTransition;
import model.entity;
import method.potionDamageChangeTemp;
import method.CharDamageMain;
import ui.shop.shopDisplay;

import java.util.ArrayList;

/**
 * Menangani mekanisme ATTACK pada dungeon boss battle.
 */
public class BossAttackHandler {

    private final dungeonBossDisplay display;
    private final potionDamageChangeTemp potionSource;
    private final CharDamageMain charSource;

    public BossAttackHandler(dungeonBossDisplay display,
                             potionDamageChangeTemp potionSource,
                             CharDamageMain charSource) {
        this.display      = display;
        this.potionSource = potionSource;
        this.charSource   = charSource;
    }

    /**
     * Eksekusi serangan pemain ke boss saat ini.
     *
     * @param arrEnt          list semua boss entity
     * @param pause           PauseTransition bersama dari dungeonBossDisplay
     * @param shopRef         referensi shopDisplay untuk cek potion aktif
     * @param onEnemyDefeated callback dipanggil saat boss HP habis (untuk dialog transition)
     */
    public void execute(ArrayList<entity> arrEnt,
                        PauseTransition pause,
                        shopDisplay shopRef,
                        Runnable onEnemyDefeated) {

        entity currentEnt = arrEnt.get(display.count());
        int mainDamage    = charSource.getDamageChar() + potionSource.getDamageChange();

        currentEnt.setEntHP(currentEnt.getEntHP() - mainDamage);
        display.setEnemyAttack(true);

        if (shopRef != null && shopRef.isPotionActive()) {
            shopRef.increaseAttackCount(mainDamage);
        }

        if (currentEnt.getEntHP() <= 0) {
            onBossDefeated(currentEnt, onEnemyDefeated);
        } else {
            onBossStillAlive(currentEnt, pause);
        }
    }

    private void onBossDefeated(entity currentEnt, Runnable onEnemyDefeated) {
        display.refreshEntStat();
        currentEnt.setEntHP(0);
        display.coinWin();
        display.Mainchar.setCharAtkLVL(display.Mainchar.getCharAtkLVL() + 1);
        if (onEnemyDefeated != null) {
            onEnemyDefeated.run();
        }
    }

    private void onBossStillAlive(entity currentEnt, PauseTransition pause) {
        // Tampilkan animasi "ATTACKING"
        display.statdisplay.getChildren().remove(display.statName);
        display.statHP.setText("");
        display.statATK.setText("ATTACKING");
        display.statCoin.setText("");
        display.nameEnt.setText("...");
        display.statEntHP.setText("ATTACKING");
        display.statEntATK.setText("...");

        pause.setOnFinished(e -> {
            display.statdisplay.getChildren().add(0, display.statName);
            display.refreshEntStat();
            display.refreshCharStat();
            display.entAttackScene(currentEnt);
        });
        pause.play();
    }
}