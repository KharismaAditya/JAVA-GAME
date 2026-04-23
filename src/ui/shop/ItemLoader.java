package ui.shop;

import com.google.gson.*;
import method.Refreshable;
import method.potionDamageChangeTemp;
import model.*;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemLoader {
    private final potionDamageChangeTemp temp;
    private final Refreshable refresh;
    private final String filepath = System.getProperty("user.dir") + "/JAVA-GAME/saveFile/Items.json";

    public ItemLoader(potionDamageChangeTemp temp, Refreshable refresh) {
        this.temp = temp;
        this.refresh = refresh;
    }

    public List<ShopItem> loadItems() {
        List<ShopItem> items = new ArrayList<>();

        try (FileReader reader = new FileReader(filepath)) {

            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement el : array) {
                JsonObject obj = el.getAsJsonObject();

                String id         = obj.get("id").getAsString();
                String name       = obj.get("name").getAsString();
                int price         = obj.get("price").getAsInt();
                String effectType = obj.get("effectType").getAsString();
                int effectValue   = obj.get("effectValue").getAsInt();
                String effectDesc = obj.get("effectDescription").getAsString();

                ItemEffect effect = resolveEffect(effectType, effectValue, id);
                items.add(new ShopItem(id, name, price, effectDesc, effect));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    private ItemEffect resolveEffect(String type, int value, String id) {
        return switch (type) {
            case "heal" -> character -> character.setCharHP(character.getCharHP() + value);
            case "damage_boost" -> character -> {
                temp.setDamageChange(value);
                refresh.refreshCharStat();
            };
            // Tambah tipe baru di sini saja
            default -> character -> System.out.println("Unknown effect: " + id);
        };
    }
}
