package model;

public class player {
    private String name;
    private int CharHP;
    private int CharAtk;
    private int CharCoin;

    public player(String name, int CharHP, int CharAtk, int CharCoin) {
        this.name = name;
        this.CharHP = CharHP;
        this.CharAtk = CharAtk;
        this.CharCoin = CharCoin;
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
    public void setCharAtk(int CharAtk) {
        this.CharAtk = CharAtk;
    }
    public void setCharHP(int CharHP) {
        this.CharHP = CharHP;
    }
    public void setCharCoin(int charCoin) {
        this.CharCoin = charCoin;
    }
}
