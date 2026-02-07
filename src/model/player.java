package model;

public class player {
    private String name;
    private int CharHP;
    private int CharAtkLVL;
    private int CharCoin;
    private int enemyCount;

    public player(String name, int CharHP, int CharAtk, int CharCoin, int enemyCount) {
        this.name = name;
        this.CharHP = CharHP;
        this.CharAtkLVL = CharAtk;
        this.CharCoin = CharCoin;
        this.enemyCount = enemyCount;
    }

    public String getName() {return name;}
    public int getCharCoin() {
        return CharCoin;
    }
    public int getCharAtkLVL() {
        return CharAtkLVL;
    }
    public int getCharHP() {
        return CharHP;
    }
    public int getEnemyCount(){return enemyCount;}
    public void setCharAtkLVL(int CharAtk) {
        this.CharAtkLVL = CharAtk;
    }
    public void setCharHP(int CharHP) {
        this.CharHP = CharHP;
    }
    public void setCharCoin(int charCoin) {
        this.CharCoin = charCoin;
    }
    public void setEnemyCount(int enemyCount){this.enemyCount = enemyCount;}

    public int getCharDamage(int CharAtkLVL){
        if(CharAtkLVL == 0){return 100;} //DULL BLADE
        if(CharAtkLVL == 1){return 150;} //GREATSWORD
        if(CharAtkLVL == 2){return 200;} //EXCALIBUR
        return 0;
    }
}
