package ui.weapon;

import model.player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class weaponReader {
    String filePath = System.getProperty("user.dir") + "/JAVA-GAME/saveFile/weaponList.csv";
    player mc;

    public weaponReader(player mc) {
        this.mc = mc;
    }

    public int getDamageChange(int index) {
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while((line = br.readLine()) != null){
                String[] data = line.split(",");

                if(Integer.parseInt(data[2].trim()) == index){
                    return Integer.parseInt(data[1]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public String getName(int index) {
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if(Integer.parseInt(data[2].trim()) == index){
                    return data[0];
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "null";
    }
}
