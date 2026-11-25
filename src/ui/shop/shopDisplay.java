package ui.shop;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import method.ActivePane;
import method.GamePopup;
import method.Refreshable;
import method.potionDamageChangeTemp;
import model.player;

import java.util.List;

public class shopDisplay {
    private boolean potionActive = false;
    private int attackCount = 0;

    private final shopComp comp = new shopComp();
    private final Refreshable refresh;
    private final ActivePane activePane;
    private final potionDamageChangeTemp temp;

    Label statHP;
    Label statCoin;

    public shopDisplay(Refreshable refresh, ActivePane activePane, potionDamageChangeTemp temp) {
        this.activePane = activePane;
        this.refresh = refresh;
        this.temp = temp;
    }

    public VBox SHOP(player mainchar, Scene mainscene, Parent mainroot) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        HBox upsection = new HBox();
        upsection.setMinSize(520, 50);
        upsection.setAlignment(Pos.CENTER_RIGHT);
        upsection.setPadding(new Insets(10, 14, 10, 14));

        Button exit = comp.ExitButton();
        upsection.getChildren().add(exit);

        ItemLoader loader = new ItemLoader(temp, refresh);
        List<ShopItem> items = loader.loadItems();

        // Menggunakan spacing dan padding yang seragam dengan skills
        HBox shopItemContainer = new HBox(25);
        shopItemContainer.setAlignment(Pos.CENTER_LEFT);
        shopItemContainer.setPadding(new Insets(16, 25, 16, 25));

        for (ShopItem item : items) {
            VBox itemBox = buildItemBox(item, mainchar);
            shopItemContainer.getChildren().add(itemBox);
        }

        VBox scrollContent = new VBox();
        scrollContent.getChildren().add(shopItemContainer);

        ScrollPane scrollPane = new ScrollPane(scrollContent);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setMinSize(520, 260);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        scrollPane.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            scrollPane.setHvalue(
                    scrollPane.getHvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getWidth()
            );
            event.consume();
        });

        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(0));

        HBox downSection = new HBox();
        downSection.setMinSize(520, 50);
        downSection.setAlignment(Pos.CENTER);
        downSection.setSpacing(10);
        statHP = new Label("HP: " + mainchar.getCharHP());
        statCoin = new Label("Coin: " + mainchar.getCharCoin());
        downSection.getChildren().addAll(statHP, statCoin);


        root.getChildren().addAll(upsection, scrollPane, downSection);

        exit.setOnMouseClicked(e -> {
            mainscene.setRoot(mainroot);
            activePane.setActivePane(false);
        });

        try {
            root.getStylesheets().add(getClass().getResource("/font/styles.css").toExternalForm());
        } catch (Exception ignored) {}

        return root;
    }

    private VBox buildItemBox(ShopItem item, player mainchar) {
        String category = getItemCategory(item);
        VBox card = comp.itemCard(category, item.getName(), item.getPrice());
        Button buyBtn = comp.buyButton();

        buyBtn.setOnAction(e -> {
            // Tampilkan UI pop-up konfirmasi pembelian item dengan info stat & harga
            GamePopup.showItemPurchaseConfirmation(
                    category,
                    item.getName(),
                    item.getPrice(),
                    item.getEffectDescription(),
                    mainchar.getCharCoin(),
                    () -> handlePurchase(item, mainchar)
            );
        });

        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(card, buyBtn);
        return box;
    }

    private String getItemCategory(ShopItem item) {
        String id = item.getId().toLowerCase();
        String desc = item.getEffectDescription().toLowerCase();
        if (id.contains("potion") || desc.contains("damage")) {
            return "BUFF POTION";
        } else if (desc.contains("hp") || id.contains("bread") || id.contains("steak") || id.contains("katsu")) {
            return "RECOVERY FOOD";
        }
        return "ITEM";
    }

    private void handlePurchase(ShopItem item, player mainchar) {
        if (mainchar.getCharCoin() < item.getPrice()) {
            GamePopup.showMessage("INSUFFICIENT COINS", "NOT ENOUGH COINS!\nNeed: " + item.getPrice() + " Coins\nYou have: " + mainchar.getCharCoin() + " Coins");
            return;
        }

        item.getEffect().apply(mainchar);
        mainchar.setCharCoin(mainchar.getCharCoin() - item.getPrice());

        if (item.getId().contains("magic_potion") || item.getEffectDescription().toLowerCase().contains("damage")) {
            if (!potionActive) {
                potionActive = true;
                attackCount  = 0;
            }
        }

        refresh.refreshCharStat();
        statHP.setText("HP: " + mainchar.getCharHP());statCoin.setText("Coin: " + mainchar.getCharCoin());
        GamePopup.showMessage("PURCHASE SUCCESSFUL", "Bought " + item.getName() + " for " + item.getPrice() + " Coins!\n" + item.getEffectDescription());
    }

    public boolean isPotionActive() {
        return potionActive;
    }

    public void increaseAttackCount(int mainDamage) {
        if (!potionActive) return;

        attackCount++;
        if (attackCount >= 5) {
            potionActive = false;
            temp.setDamageChange(0);
            refresh.refreshCharStat();
        }
    }
}
