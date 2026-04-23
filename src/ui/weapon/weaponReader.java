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

    public boolean thereIsWeaponOnMyFileBLyat001niggaanjingmemek(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public String getListWeapon(int damageChar){
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if(damageChar == Integer.parseInt(data[1])){
                    return data[0];
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "NULL";
    }
}
