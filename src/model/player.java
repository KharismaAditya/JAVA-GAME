package model;

public class player {
    private String name;
    private int CharHP;
    private int CharAtk;
    private int CharCoin;
    private int enemyCount;

    public player(String name, int CharHP, int CharAtk, int CharCoin, int enemyCount) {
        this.name = name;
        this.CharHP = CharHP;
        this.CharAtk = CharAtk;
        this.CharCoin = CharCoin;
        this.enemyCount = enemyCount;
    }

    public String getName() {return name;}
    public int getCharCoin() {
        return CharCoin;
    }
    public int getCharAtk() {
        return CharAtk;
    }
    public int getCharHP() {
        return CharHP;
    }
    public int getEnemyCount(){return enemyCount;}
    public void setCharAtk(int CharAtk) {
        this.CharAtk = CharAtk;
    }
    public void setCharHP(int CharHP) {
        this.CharHP = CharHP;
    }
    public void setCharCoin(int charCoin) {
        this.CharCoin = charCoin;
    }
    public void setEnemyCount(int enemyCount){this.enemyCount = enemyCount;}
}
