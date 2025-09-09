package model;

public class entity {
    private String entName;
    private int entHP;
    private int entAtk;

    public  entity(String entName, int entHP, int entAtk) {
        this.entName = entName;
        this.entHP = entHP;
        this.entAtk = entAtk;
    }

    public String getEntName() {return entName;}

    public int getEntHP() {
        return entHP;
    }
    public int getEntAtk() {
        return entAtk;
    }

    public void setEntAtk(int entAtk) {
        this.entAtk = entAtk;
    }

    public void setEntHP(int entHP) {
        this.entHP = entHP;
    }
}
