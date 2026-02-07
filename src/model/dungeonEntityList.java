package model;

import java.util.ArrayList;

public class dungeonEntityList {
    public ArrayList<entity> dungeonList(){
        ArrayList<entity> list = new ArrayList<>();
        list.add(new entity("GOBLIN", 100, 5));
        list.add(new entity("SKELETON", 150, 10));
        list.add(new entity("ZOMBIE", 120, 7));
        return list;
    }
}
