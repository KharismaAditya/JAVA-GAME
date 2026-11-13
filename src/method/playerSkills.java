package method;

import model.player;
import model.entity;

public class playerSkills {

    public playerSkills() {
    }
    public int getSkillCount() {
        return 4;
    }

    public String getSkillName(int id) {
        switch (id) {
            case 0: return "INFERNO SACRIFICE";
            case 1: return "DIVINE BLESSING";
            case 2: return "REAPER'S CURSE";
            case 3: return "TITAN AEGIS";
            default: return "UNKNOWN SKILL";
        }
    }

    public String getSkillCategory(int id) {
        switch (id) {
            case 0: return "OFFENSIVE";
            case 1: return "RECOVERY";
            case 2: return "ULTIMATE";
            case 3: return "DEFENSIVE";
            default: return "GENERAL";
        }
    }

    public int getCoinCost(int id) {
        switch (id) {
            case 0: return 120;
            case 1: return 200;
            case 2: return 300;
            case 3: return 150;
            default: return 0;
        }
    }

    public int getHpCost(int id, player player) {
        switch (id) {
            case 0: return 100;
            case 1: return 0;
            case 2:
                // Mengurangi HP hingga tersisa 10%
                int remainingHp = Math.max(1, (int) (player.getCharHP() * 0.1));
                return Math.max(0, player.getCharHP() - remainingHp);
            case 3: return 0;
            default: return 0;
        }
    }

    public String getSkillEffectDescription(int id) {
        switch (id) {
            case 0:
                return "Korbankan 100 HP & 120 Koin untuk membakar musuh hingga HP tersisa 10!";
            case 1:
                return "Pulihkan +200 HP karakter pemain dengan membayar 200 Koin.";
            case 2:
                return "Korbankan HP hingga sisa 10% & 300 Koin untuk menebas HP musuh hingga tersisa 1!";
            case 3:
                return "Aktifkan pelindung Titan untuk memulihkan +80 HP pemain seharga 150 Koin.";
            default:
                return "-";
        }
    }

    public boolean canUseSkill(int id, player player) {
        int coinCost = getCoinCost(id);
        if (player.getCharCoin() < coinCost) {
            return false;
        }

        switch (id) {
            case 0:
                return player.getCharHP() >= 100;
            case 1:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    public String getCannotUseReason(int id, player player) {
        int coinCost = getCoinCost(id);
        if (player.getCharCoin() < coinCost) {
            return "NOT ENOUGH COINS! (Need " + coinCost + " Coins)";
        }
        if (id == 0 && player.getCharHP() < 100) {
            return "NOT ENOUGH HP! (Need at least 100 HP)";
        }
        return "CANNOT USE SKILL!";
    }

    public boolean executeSkill(int id, player player, entity enemy) {
        if (!canUseSkill(id, player)) {
            return false;
        }

        switch (id) {
            case 0:
                return infernoSacrifice(player, enemy);
            case 1:
                return divineBlessing(player);
            case 2:
                return reapersCurse(player, enemy);
            case 3:
                return titanAegis(player);
            default:
                return false;
        }
    }

    // ── Method Individual Skill ──────────────────────────────────────────────

    // INFERNO SACRIFICE: player -100 HP, -120 Coin, enemy HP = 10
    public boolean infernoSacrifice(player player, entity enemy) {
        player.setCharHP(player.getCharHP() - 100);
        player.setCharCoin(player.getCharCoin() - 120);
        enemy.setEntHP(10);
        return true;
    }

    // DIVINE BLESSING: player -200 Coin, player +200 HP
    public boolean divineBlessing(player player) {
        player.setCharCoin(player.getCharCoin() - 200);
        player.setCharHP(player.getCharHP() + 200);
        return true;
    }

    // REAPER'S CURSE: player HP tinggal 10%, -300 Coin, enemy HP = 1
    public boolean reapersCurse(player player, entity enemy) {
        double hpRemaining = (double) player.getCharHP() * 0.1;
        int newHp = Math.max(1, (int) hpRemaining);
        player.setCharHP(newHp);
        player.setCharCoin(player.getCharCoin() - 300);
        enemy.setEntHP(1);
        return true;
    }

    // TITAN AEGIS: player -150 Coin, +80 HP
    public boolean titanAegis(player player) {
        player.setCharCoin(player.getCharCoin() - 150);
        player.setCharHP(player.getCharHP() + 80);
        return true;
    }
}
