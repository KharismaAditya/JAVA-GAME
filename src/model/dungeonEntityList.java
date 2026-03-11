package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class dungeonEntityList {
    private static final String FILE = System.getProperty("user.dir") + "/JAVA-GAME/saveFile/dungeonSmallEnemy.csv";
    public ArrayList<entity> dungeonList() {
        ArrayList<entity> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Skip baris kosong atau tidak lengkap
                if (parts.length < 3) continue;

                String name   = parts[0].trim();
                int hp        = Integer.parseInt(parts[1].trim());
                int damage    = Integer.parseInt(parts[2].trim());

                list.add(new entity(name, hp, damage));
            }
        } catch (IOException e) {
            System.out.println("File bosses.csv tidak ditemukan: " + e.getMessage());
        }

        return list;
    }
}