package ui.weapon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class weaponComp {

    public VBox weaponCard(String tierText, String weaponName, int damage) {
        VBox vbox = new VBox(8);
        vbox.setMinSize(130, 180);
        vbox.setMaxSize(130, 180);
        vbox.setStyle("-fx-background-color: #D9D9D9; -fx-border-color: #888888; -fx-border-width: 2px; -fx-border-radius: 4px; -fx-background-radius: 4px;");
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 8, 10, 8));

        // Tier badge
        Label catLabel = new Label("[" + tierText + "]");
        catLabel.setStyle("-fx-font-size: 6px; -fx-text-fill: #444444; -fx-font-weight: bold;");
        catLabel.setAlignment(Pos.CENTER);

        // Weapon name
        Label nameLabel = new Label(weaponName);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-font-size: 7.5px; -fx-text-alignment: CENTER; -fx-text-fill: #000000; -fx-font-weight: bold;");
        nameLabel.setAlignment(Pos.CENTER);

        // Attack power badge
        Label dmgLabel = new Label("+" + damage + " ATK");
        dmgLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: #C62828; -fx-background-color: #ffffff; -fx-padding: 3 6 3 6; -fx-background-radius: 3; -fx-font-weight: bold;");
        dmgLabel.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(catLabel, nameLabel, dmgLabel);
        return vbox;
    }

    public Button equipButton() {
        Button button = new Button("EQUIP");
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

    // Backwards compatibility
    public VBox labelItem(String message) {
        return weaponCard("WEAPON", message, 0);
    }
}
