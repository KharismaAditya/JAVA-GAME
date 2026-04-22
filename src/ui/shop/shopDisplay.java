package ui.shop;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import method.*;
import model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.List;

public class shopDisplay {
    private boolean potionActive = false;
    private int attackCount = 0;

    shopComp comp = new shopComp();
    Refreshable refresh;
    ActivePane activePane;
    potionDamageChangeTemp temp;

    public shopDisplay(Refreshable refresh, ActivePane activePane, potionDamageChangeTemp temp) {
        this.activePane = activePane;
        this.refresh = refresh;
        this.temp = temp;
    }


    public VBox SHOP(player mainchar, Scene mainscene, Parent mainroot) {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        // --- Top bar (exit button) ---
        HBox upsection = new HBox();
        upsection.setMinSize(520, 50);
        upsection.setAlignment(Pos.CENTER_RIGHT);
        upsection.setPadding(new Insets(10, 14, 10, 14));

        Button exit = comp.ExitButton();
        upsection.getChildren().add(exit);

        // --- Load items dari JSON dan build UI secara dinamis ---
        ItemLoader loader = new ItemLoader(temp, refresh);
        List<ShopItem> items = loader.loadItems();

        HBox shopItemContainer = new HBox(43);
        shopItemContainer.setAlignment(Pos.CENTER_LEFT);
        shopItemContainer.setPadding(new Insets(16, 44, 16, 44));

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

        // Ubah scroll vertikal menjadi horizontal
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

        root.getChildren().addAll(upsection, scrollPane, downSection);

        // --- Exit action ---
        exit.setOnMouseClicked(e -> {
            mainscene.setRoot(mainroot);
            activePane.setActivePane(false);
        });

        root.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        stage.setOnCloseRequest(event -> {
            event.consume();
            System.out.println("Close button disabled!");
        });

        stage.setResizable(false);
        return root;
    }



    private VBox buildItemBox(ShopItem item, player mainchar) {
        VBox itemLabel = comp.labelItem(item.getName());
        Button buyBtn  = comp.buyButton();

        buyBtn.setOnMouseClicked(e -> handlePurchase(item, mainchar));

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(itemLabel, buyBtn);
        return box;
    }

    private void handlePurchase(ShopItem item, player mainchar) {
        if (mainchar.getCharCoin() < item.getPrice()) {
            alert();
            return;
        }

        item.getEffect().apply(mainchar);

        mainchar.setCharCoin(mainchar.getCharCoin() - item.getPrice());

        if ("damage_boost".equals(item.getEffect())) {
            if (!potionActive) {
                potionActive = true;
                attackCount  = 0;
            }
        }

        confirm(item.getName(), item.getPrice(), item.getEffectDescription());
        refresh.refreshCharStat();
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

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("NOT ENOUGH COINS");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }

    private void confirm(String item, int price, String effectExplanation) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(
                "BUYING " + item + " FOR " + price + " COIN\n" +
                        "EFFECT : " + effectExplanation
        );
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }
}