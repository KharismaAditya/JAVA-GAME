package method;

import ui.battleBOSS.*;
import model.*;

public class bossSkills {
    CharDamageMain charDamageMain;

    public bossSkills(CharDamageMain charDamageMain) {
        this.charDamageMain = charDamageMain;
    }

    public void skillList(dungeonBossDisplay display, player player, entity boss) {
        switch (display.count()){
            case 0:ZylnorSkills(player);break;
            case 1:EryndraSkills(boss);break;
            case 2:ThraxxussSkills(boss);break;
            case 3:KaelithSkills(boss);break;
            case 4:VrakhtarSkills(player);break;
            case 5:ATGOMASkills(player);break;
            case 6:DiandraSkills(player);break;
        }
    }

    public String bossSkillName(int count){
        if (count == 0){return "FIRE STRUCK";} //Zylnor
        if (count == 1){return "ONE MORE TIME";} //Eryndra
        if (count == 2){return "BONE OF STEEL";} //Thraxxuss
        if (count == 3){return "WHAT'S WRONG WITH ME?";} //Kaelith
        if (count == 4){return "EMPEROR OF TIME";} //Vrakthar
        if (count == 5){return "CANT DO ANYTHING?";} //ATGOMA
        if (count == 6){return "!!!MOSHING!!!";} //DIAN
        return null;
    }

    //FIRE STRUCK//
    public void ZylnorSkills(player player) {
        player.setCharHP(player.getCharHP() - 20);
    }

    //ONE MORE TIME
    public void  EryndraSkills(entity boss) {
        boss.setEntHP(boss.getEntHP() + 200);
    }

    //BONE OF STEEL
    public void ThraxxussSkills(entity boss) {
        boss.setEntHP(boss.getEntHP() + 50); boss.setEntAtk(boss.getEntAtk() + 10);
    }

    //WHAT'S WRONG WITH ME?
    public void KaelithSkills(entity boss) {
        boolean active = true;
        if(active){
            boss.setEntHP(boss.getEntAtk() * 10); boss.setEntAtk(boss.getEntHP() * 10);
            active = false;
        }
        if(!active){
            boss.setEntHP(100); boss.setEntAtk(10);
            active = true;
        }
    }

    //EMPEROR OF TIME
    public void VrakhtarSkills(player player) {
        boolean active = true;
        if(active){
            player.setCharHP(10); active = false;
        }if(!active){
            player.setCharHP(100); player.setCharCoin(player.getCharCoin() + 100);
            active = true;
        }
    }

    public void ATGOMASkills(player player) {
        boolean active = true;
        if(active){
            player.setCharHP(1); charDamageMain.setDamageChar(1);player.setCharCoin(1);
            active = false;
        }if(!active){
            player.setCharHP(200); charDamageMain.setDamageChar(player.getCharDamage(player.getCharAtkLVL()));player.setCharCoin(200);
            active = true;
        }
    }

    public void DiandraSkills(player player) {
       player.setCharHP(player.getCharHP() - 500);
    }

}
