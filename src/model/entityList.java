package model;

import java.util.ArrayList;

public class entityList {
    public ArrayList<entity> entList(){
        ArrayList<entity> list = new ArrayList<>();
        list.add(new entity("Zylnor", 250, 10));
        list.add(new entity("Eryndra", 350, 20));
        list.add(new entity("Thraxxuss", 400, 30));
        list.add(new entity("Kaelith", 450, 40));
        list.add(new entity("Vrakthar", 700, 50));
        list.add(new entity("A.T.G.O.M.A", 1000, 70));
        list.add(new entity("Diancok Goblok UMM Korban Moshing", 1000000, 100));
        return list;
    };
    //Among The Greatest,Only Me Alone
}
