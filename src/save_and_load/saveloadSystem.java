package save_and_load;
import model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class saveloadSystem {
    String filename = System.getProperty("user.dir") + "/JAVA-GAME/saveFile/savegame.txt";
    public void savegame(player p){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))){
            String line = p.getName() + "," + p.getCharHP() + "," + p.getCharAtk() + "," + p.getCharCoin() + "," + p.getEnemyCount();
            bw.write(line);
            bw.newLine();

            System.out.println("SUKSES");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
