package model;

import java.util.ArrayList;

public class entityList {
    public ArrayList<entity> entList(){
        ArrayList<entity> list = new ArrayList<>();
        list.add(new entity("ENEMY1", 100, 10));
        list.add(new entity("ENEMY2", 120, 20));
        list.add(new entity("ENEMY3", 150, 30));
        list.add(new entity("ENEMY4", 200, 40));
        list.add(new entity("ENEMY4", 250, 50));
        list.add(new entity("BOSS", 1000, 70));
        return list;
    };
}
