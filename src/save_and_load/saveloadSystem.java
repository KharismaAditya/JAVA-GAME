package save_and_load;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class saveloadSystem {
    private static final String FILE = System.getProperty("user.dir") + "/JAVA-GAME/saveFile/players.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<ArrayList<player>>() {}.getType();

    // ============================================================
    // ✔ 1. ADD PLAYER (TAMBAH PLAYER BARU TANPA MENGHAPUS YANG LAMA)
    // ============================================================
    public static void addPlayer(player newPlayer) {
        ArrayList<player> players = loadAllPlayers();

        // Jika player dengan nama sama sudah ada → update, bukan duplikat
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equalsIgnoreCase(newPlayer.getName())) {
                players.set(i, newPlayer);  // update data lama
                saveList(players);
                System.out.println("Player existing updated!");
                return;
            }
        }

        // Jika nama belum ada → tambah baru
        players.add(newPlayer);
        saveList(players);
        System.out.println("Player baru ditambahkan!");
    }


    // ============================================================
    // ✔ 2. LOAD SATU PLAYER BERDASARKAN NAMA
    // ============================================================
    public static player loadPlayer(String name) {
        ArrayList<player> players = loadAllPlayers();

        for (player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p; // ketemu
            }
        }
        return null; // tidak ketemu
    }


    // ============================================================
    // ✔ 3. LOAD SEMUA PLAYER (KEMBALIKAN LIST)
    // ============================================================
    public static ArrayList<player> loadAllPlayers() {
        try (Reader reader = new FileReader(FILE)) {
            ArrayList<player> players = gson.fromJson(reader, LIST_TYPE);
            return players != null ? players : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>(); // file belum ada
        }
    }


    // ============================================================
    // ✔ 4. NEW SAVE (MENGGANTI SEMUA DATA PLAYER)
    // ============================================================
    public static void newSave(player p) {
        ArrayList<player> list = new ArrayList<>();
        list.add(p);
        saveList(list); // overwrite
        System.out.println("File save baru dibuat!");
    }


    // ============================================================
    // ✔ 5. SAVE LIST INTERNAL
    // ============================================================
    private static void saveList(ArrayList<player> players) {
        try (Writer writer = new FileWriter(FILE)) {
            gson.toJson(players, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
