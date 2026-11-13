package method;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Pengganti seluruh Alert JavaFX menjadi pop-up JavaFX modal kustom
 * dengan gaya retro pixel art yang seragam di seluruh aplikasi.
 */
public class GamePopup {

    /**
     * Menampilkan pop-up pesan informasi dengan tombol OK.
     */
    public static void showMessage(String titleText, String messageText) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showMessage(titleText, messageText));
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        VBox content = new VBox(14);
        content.setMinSize(340, 180);
        content.setMaxSize(360, 240);
        content.setPadding(new Insets(18, 20, 18, 20));
        content.setAlignment(Pos.CENTER);
        content.setStyle(
                "-fx-background-color: #FFFFFF; " +
                "-fx-border-color: #333333; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px;"
        );

        Label title = new Label(titleText == null || titleText.isEmpty() ? "=== NOTICE ===" : "=== " + titleText + " ===");
        title.setStyle("-fx-font-size: 8.5px; -fx-text-fill: #111111; -fx-font-weight: bold; -fx-text-alignment: CENTER;");
        title.setWrapText(true);

        Label msg = new Label(messageText);
        msg.setStyle("-fx-font-size: 7.5px; -fx-text-fill: #333333; -fx-text-alignment: CENTER;");
        msg.setWrapText(true);
        msg.setAlignment(Pos.CENTER);

        Button okBtn = new Button("OK");
        okBtn.setMinSize(90, 30);
        styleButton(okBtn, "#D9D9D9", "#A9A9A9", "#000000");
        okBtn.setOnAction(e -> dialog.close());

        content.getChildren().addAll(title, msg, okBtn);

        Scene scene = new Scene(content);
        try {
            scene.getStylesheets().add(GamePopup.class.getResource("/font/styles.css").toExternalForm());
        } catch (Exception ignored) {}

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Menampilkan pop-up konfirmasi YES / NO.
     */
    public static void showConfirmation(String titleText, String messageText, Runnable onConfirm) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showConfirmation(titleText, messageText, onConfirm));
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        VBox content = new VBox(14);
        content.setMinSize(350, 190);
        content.setMaxSize(370, 250);
        content.setPadding(new Insets(18, 20, 18, 20));
        content.setAlignment(Pos.CENTER);
        content.setStyle(
                "-fx-background-color: #FFFFFF; " +
                "-fx-border-color: #333333; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px;"
        );

        Label title = new Label("=== " + titleText + " ===");
        title.setStyle("-fx-font-size: 8.5px; -fx-text-fill: #111111; -fx-font-weight: bold;");

        Label msg = new Label(messageText);
        msg.setStyle("-fx-font-size: 7.5px; -fx-text-fill: #333333; -fx-text-alignment: CENTER;");
        msg.setWrapText(true);

        HBox btnBox = new HBox(15);
        btnBox.setAlignment(Pos.CENTER);

        Button yesBtn = new Button("YES");
        styleButton(yesBtn, "#4CAF50", "#388E3C", "#FFFFFF");

        Button noBtn = new Button("NO");
        styleButton(noBtn, "#E53935", "#B71C1C", "#FFFFFF");

        btnBox.getChildren().addAll(yesBtn, noBtn);

        yesBtn.setOnAction(e -> {
            dialog.close();
            if (onConfirm != null) onConfirm.run();
        });
        noBtn.setOnAction(e -> dialog.close());

        content.getChildren().addAll(title, msg, btnBox);

        Scene scene = new Scene(content);
        try {
            scene.getStylesheets().add(GamePopup.class.getResource("/font/styles.css").toExternalForm());
        } catch (Exception ignored) {}

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Menampilkan pop-up konfirmasi pembelian item shop dengan detail stat & harga.
     */
    public static void showItemPurchaseConfirmation(
            String category,
            String itemName,
            int price,
            String effectDesc,
            int playerCoin,
            Runnable onConfirm
    ) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showItemPurchaseConfirmation(category, itemName, price, effectDesc, playerCoin, onConfirm));
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        VBox content = new VBox(12);
        content.setMinSize(360, 270);
        content.setMaxSize(370, 300);
        content.setPadding(new Insets(16, 20, 16, 20));
        content.setAlignment(Pos.CENTER);
        content.setStyle(
                "-fx-background-color: #FFFFFF; " +
                "-fx-border-color: #333333; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px;"
        );

        Label title = new Label("=== SHOP ITEM DETAILS ===");
        title.setStyle("-fx-font-size: 9px; -fx-text-fill: #111111; -fx-font-weight: bold;");

        Label catBadge = new Label("TYPE: [" + category + "]");
        catBadge.setStyle("-fx-font-size: 7px; -fx-text-fill: #555555;");

        Label name = new Label(itemName);
        name.setWrapText(true);
        name.setStyle("-fx-font-size: 9px; -fx-text-fill: #1E88E5; -fx-font-weight: bold; -fx-text-alignment: CENTER;");

        // Info box
        VBox statsBox = new VBox(6);
        statsBox.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 10; -fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-border-radius: 4px;");
        statsBox.setAlignment(Pos.CENTER_LEFT);

        Label priceLabel = new Label("Price  : " + price + " Coins (You: " + playerCoin + ")");
        priceLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: " + (playerCoin >= price ? "#2E7D32" : "#D32F2F") + ";");

        Label effectLabel = new Label("Effect : " + effectDesc);
        effectLabel.setWrapText(true);
        effectLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: #0D47A1;");

        statsBox.getChildren().addAll(priceLabel, effectLabel);

        Label confirmPrompt = new Label("Do you want to buy this item?");
        confirmPrompt.setStyle("-fx-font-size: 7px; -fx-text-fill: #333333;");

        HBox btnBox = new HBox(15);
        btnBox.setAlignment(Pos.CENTER);

        Button buyBtn = new Button("BUY / YES");
        styleButton(buyBtn, "#4CAF50", "#388E3C", "#FFFFFF");

        Button cancelBtn = new Button("CANCEL / NO");
        styleButton(cancelBtn, "#E53935", "#B71C1C", "#FFFFFF");

        btnBox.getChildren().addAll(buyBtn, cancelBtn);

        buyBtn.setOnAction(e -> {
            dialog.close();
            if (onConfirm != null) onConfirm.run();
        });
        cancelBtn.setOnAction(e -> dialog.close());

        content.getChildren().addAll(title, catBadge, name, statsBox, confirmPrompt, btnBox);

        Scene scene = new Scene(content);
        try {
            scene.getStylesheets().add(GamePopup.class.getResource("/font/styles.css").toExternalForm());
        } catch (Exception ignored) {}

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Menampilkan pop-up konfirmasi equip weapon dengan detail ATK.
     */
    public static void showWeaponConfirmation(
            String tierText,
            String weaponName,
            int damage,
            Runnable onConfirm
    ) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showWeaponConfirmation(tierText, weaponName, damage, onConfirm));
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        VBox content = new VBox(12);
        content.setMinSize(360, 260);
        content.setMaxSize(370, 290);
        content.setPadding(new Insets(16, 20, 16, 20));
        content.setAlignment(Pos.CENTER);
        content.setStyle(
                "-fx-background-color: #FFFFFF; " +
                "-fx-border-color: #333333; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px;"
        );

        Label title = new Label("=== WEAPON DETAILS ===");
        title.setStyle("-fx-font-size: 9px; -fx-text-fill: #111111; -fx-font-weight: bold;");

        Label catBadge = new Label("TIER: [" + tierText + "]");
        catBadge.setStyle("-fx-font-size: 7px; -fx-text-fill: #555555;");

        Label name = new Label(weaponName);
        name.setWrapText(true);
        name.setStyle("-fx-font-size: 9.5px; -fx-text-fill: #D84315; -fx-font-weight: bold; -fx-text-alignment: CENTER;");

        VBox statsBox = new VBox(6);
        statsBox.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 10; -fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-border-radius: 4px;");
        statsBox.setAlignment(Pos.CENTER_LEFT);

        Label dmgLabel = new Label("Attack Power : " + damage + " DMG");
        dmgLabel.setStyle("-fx-font-size: 7.5px; -fx-text-fill: #D32F2F; -fx-font-weight: bold;");

        Label effectLabel = new Label("Type         : Primary Weapon");
        effectLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: #333333;");

        statsBox.getChildren().addAll(dmgLabel, effectLabel);

        Label confirmPrompt = new Label("Equip this weapon now?");
        confirmPrompt.setStyle("-fx-font-size: 7px; -fx-text-fill: #333333;");

        HBox btnBox = new HBox(15);
        btnBox.setAlignment(Pos.CENTER);

        Button equipBtn = new Button("EQUIP / YES");
        styleButton(equipBtn, "#4CAF50", "#388E3C", "#FFFFFF");

        Button cancelBtn = new Button("CANCEL / NO");
        styleButton(cancelBtn, "#E53935", "#B71C1C", "#FFFFFF");

        btnBox.getChildren().addAll(equipBtn, cancelBtn);

        equipBtn.setOnAction(e -> {
            dialog.close();
            if (onConfirm != null) onConfirm.run();
        });
        cancelBtn.setOnAction(e -> dialog.close());

        content.getChildren().addAll(title, catBadge, name, statsBox, confirmPrompt, btnBox);

        Scene scene = new Scene(content);
        try {
            scene.getStylesheets().add(GamePopup.class.getResource("/font/styles.css").toExternalForm());
        } catch (Exception ignored) {}

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private static void styleButton(Button btn, String bgColor, String hoverColor, String textColor) {
        String base = "-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 8px; -fx-padding: 7 14 7 14; -fx-cursor: hand;";
        String hover = "-fx-background-color: " + hoverColor + "; -fx-text-fill: " + textColor + "; -fx-font-size: 8px; -fx-padding: 7 14 7 14; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
    }
}
