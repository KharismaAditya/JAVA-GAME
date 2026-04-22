package ui.shop;

public class ShopItem {
    private String id;
    private String name;
    private int price;
    private String effectDescription;
    private ItemEffect effect;

    public ShopItem(String id, String name, int price, String effectDescription, ItemEffect effect) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.effectDescription = effectDescription;
        this.effect = effect;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getEffectDescription() { return effectDescription; }
    public ItemEffect getEffect() { return effect; }
}
