package method;

import ui.battle.*;
import model.*;

public class bossSkills {

    public void skillList(mainDisplay display, player player, entity boss) {
        switch (display.count()){
            case 0:
                ZylnorSkills(player, boss);
                break;
            case 1:
                EryndraSkills(player, boss);
                break;
             case 2:
                 ThraxxussSkills(player, boss);
                 break;
             case 3:
                 KaelithSkills(player, boss);
                 break;
             case 4:
                 VrakhtarSkills(player, boss);
                 break;
             case 5:
                 ATGOMASkills(player, boss);
                 break;
             case 6:
                 DiandraSkills(player, boss);
                 break;


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
    public void ZylnorSkills(player player, entity boss) {
        player.setCharHP(player.getCharHP() - 20);
    }

    //ONE MORE TIME
    public void  EryndraSkills(player player, entity boss) {
        boss.setEntHP(boss.getEntHP() + 200);
    }

    //BONE OF STEEL
    public void ThraxxussSkills(player player, entity boss) {
        boss.setEntHP(boss.getEntHP() + 50); boss.setEntAtk(boss.getEntAtk() + 10);
    }

    //WHAT'S WRONG WITH ME?
    public void KaelithSkills(player player, entity boss) {
        int count = 0;
        if(count == 0){
            boss.setEntHP(boss.getEntAtk() * 10); boss.setEntAtk(boss.getEntHP() * 10);
            count = 1;
        }
        if(count == 1){
            boss.setEntHP(100); boss.setEntAtk(10);
            count = 0;
        }
    }

    //EMPEROR OF TIME
    public void VrakhtarSkills(player player, entity boss) {
        int count = 0;
        if(count == 0){
            player.setCharHP(10); count = 1;
        }if(count == 1){
            player.setCharHP(100); player.setCharCoin(player.getCharCoin() + 100);
            count = 0;
        }
    }

    public void ATGOMASkills(player player, entity boss) {
        int count = 0;
        if(count == 0){
            player.setCharHP(1); player.setCharAtk(1);player.setCharCoin(1);
            count = 1;
        }if(count == 1){
            player.setCharHP(200); player.setCharAtk(200);player.setCharCoin(200);
            count = 0;
        }
    }

    public void DiandraSkills(player player, entity boss) {
       player.setCharHP(player.getCharHP() - 500);
    }

}
