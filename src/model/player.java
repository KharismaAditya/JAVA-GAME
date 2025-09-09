package model;

public class player {
    private int CharHP;
    private int CharAtk;
    private int CharCoin;

    public player(int CharHP, int CharAtk, int CharCoin) {
        this.CharHP = CharHP;
        this.CharAtk = CharAtk;
        this.CharCoin = CharCoin;
    }

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
