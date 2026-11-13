package ui.shop;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class shopComp {

    public VBox itemCard(String category, String itemName, int price) {
        VBox vbox = new VBox(8);
        vbox.setMinSize(130, 180);
        vbox.setMaxSize(130, 180);
        vbox.setStyle("-fx-background-color: #D9D9D9; -fx-border-color: #888888; -fx-border-width: 2px; -fx-border-radius: 4px; -fx-background-radius: 4px;");
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 8, 10, 8));

        // Category badge
        Label catLabel = new Label("[" + category + "]");
        catLabel.setStyle("-fx-font-size: 6px; -fx-text-fill: #444444; -fx-font-weight: bold;");
        catLabel.setAlignment(Pos.CENTER);

        // Item name
        Label nameLabel = new Label(itemName);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-font-size: 7.5px; -fx-text-alignment: CENTER; -fx-text-fill: #000000; -fx-font-weight: bold;");
        nameLabel.setAlignment(Pos.CENTER);

        // Price badge
        Label priceLabel = new Label(price + " COINS");
        priceLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: #b8860b; -fx-background-color: #ffffff; -fx-padding: 3 6 3 6; -fx-background-radius: 3;");
        priceLabel.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(catLabel, nameLabel, priceLabel);
        return vbox;
    }

    public Button buyButton() {
        Button button = new Button("BUY ITEM");
        button.setMinSize(130, 32);
        button.setMaxSize(130, 32);
        onStyle(button);
        return button;
    }

    public Button ExitButton() {
        Button button = new Button("X");
        button.setMinSize(35, 35);
        button.setStyle("-fx-background-color: #D9D9D9;");
        onStyle(button);
        return button;
    }

    public void onStyle(Button btn) {
        btn.setStyle("-fx-background-color: #D9D9D9; -fx-font-size: 8px; -fx-text-alignment: CENTER; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #A9A9A9; -fx-font-size: 8px; -fx-text-alignment: CENTER; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #D9D9D9; -fx-font-size: 8px; -fx-text-alignment: CENTER; -fx-cursor: hand;"));
    }

    // Backwards-compatibility
    public VBox labelItem(String message) {
        return itemCard("ITEM", message, 0);
    }
}
