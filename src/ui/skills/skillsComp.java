package ui.skills;

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

public class skillsComp {

    public VBox skillCard(String category, String skillName, int coinCost) {
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

        // Skill name
        Label nameLabel = new Label(skillName);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-font-size: 7.5px; -fx-text-alignment: CENTER; -fx-text-fill: #000000; -fx-font-weight: bold;");
        nameLabel.setAlignment(Pos.CENTER);

        // Coin cost badge
        Label costLabel = new Label(coinCost + " COINS");
        costLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: #b8860b; -fx-background-color: #ffffff; -fx-padding: 3 6 3 6; -fx-background-radius: 3;");
        costLabel.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(catLabel, nameLabel, costLabel);
        return vbox;
    }

    public Button useButton() {
        Button button = new Button("USE SKILL");
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

    public void onConfirmStyle(Button btn) {
        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 8px; -fx-padding: 6 12 6 12; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-font-size: 8px; -fx-padding: 6 12 6 12; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 8px; -fx-padding: 6 12 6 12; -fx-cursor: hand;"));
    }

    public void onCancelStyle(Button btn) {
        btn.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-font-size: 8px; -fx-padding: 6 12 6 12; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #B71C1C; -fx-text-fill: white; -fx-font-size: 8px; -fx-padding: 6 12 6 12; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-font-size: 8px; -fx-padding: 6 12 6 12; -fx-cursor: hand;"));
    }

    /**
     * Menampilkan Modal Pop-up konfirmasi penggunaan skill beserta informasi stat lengkap:
     * Kategori, Nama Skill, Biaya Koin, Biaya HP, Efek Skill, dan Koin Pemain saat ini.
     */
    public void showSkillConfirmationPopup(
            Stage ownerStage,
            String skillName,
            String category,
            int coinCost,
            int hpCost,
            String effectDesc,
            int playerCoin,
            int playerHp,
            Runnable onConfirm
    ) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        if (ownerStage != null) {
            dialog.initOwner(ownerStage);
        }
        dialog.initStyle(StageStyle.UNDECORATED);

        VBox content = new VBox(12);
        content.setMinSize(360, 290);
        content.setMaxSize(360, 310);
        content.setPadding(new Insets(16, 20, 16, 20));
        content.setAlignment(Pos.CENTER);
        content.setStyle(
                "-fx-background-color: #FFFFFF; " +
                "-fx-border-color: #333333; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px;"
        );

        Label title = new Label("=== SKILL DETAILS ===");
        title.setStyle("-fx-font-size: 9px; -fx-text-fill: #111111; -fx-font-weight: bold;");

        Label catBadge = new Label("TYPE: [" + category + "]");
        catBadge.setStyle("-fx-font-size: 7px; -fx-text-fill: #555555;");

        Label name = new Label(skillName);
        name.setWrapText(true);
        name.setStyle("-fx-font-size: 9px; -fx-text-fill: #9C27B0; -fx-font-weight: bold; -fx-text-alignment: CENTER;");

        // Box info stat biaya & efek
        VBox statsBox = new VBox(6);
        statsBox.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 10; -fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-border-radius: 4px;");
        statsBox.setAlignment(Pos.CENTER_LEFT);

        Label coinCostLabel = new Label("Coin Cost : " + coinCost + " Coins (You: " + playerCoin + ")");
        coinCostLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: " + (playerCoin >= coinCost ? "#2E7D32" : "#D32F2F") + ";");

        Label hpCostLabel;
        if (hpCost > 0) {
            hpCostLabel = new Label("HP Cost   : -" + hpCost + " HP (You: " + playerHp + ")");
            hpCostLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: " + (playerHp > hpCost ? "#D32F2F" : "#C2185B") + ";");
        } else {
            hpCostLabel = new Label("HP Cost   : 0 HP (Safe)");
            hpCostLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: #2E7D32;");
        }

        Label effectLabel = new Label("Effect    : " + effectDesc);
        effectLabel.setWrapText(true);
        effectLabel.setStyle("-fx-font-size: 7px; -fx-text-fill: #1A237E;");

        statsBox.getChildren().addAll(coinCostLabel, hpCostLabel, effectLabel);

        Label confirmPrompt = new Label("Are you sure you want to use this skill?");
        confirmPrompt.setStyle("-fx-font-size: 7px; -fx-text-fill: #333333;");

        HBox btnBox = new HBox(15);
        btnBox.setAlignment(Pos.CENTER);

        Button confirmBtn = new Button("USE / YES");
        onConfirmStyle(confirmBtn);

        Button cancelBtn = new Button("CANCEL / NO");
        onCancelStyle(cancelBtn);

        btnBox.getChildren().addAll(confirmBtn, cancelBtn);

        content.getChildren().addAll(title, catBadge, name, statsBox, confirmPrompt, btnBox);

        confirmBtn.setOnAction(e -> {
            dialog.close();
            if (onConfirm != null) {
                onConfirm.run();
            }
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(content);
        try {
            scene.getStylesheets().add(getClass().getResource("/font/styles.css").toExternalForm());
        } catch (Exception ignored) {}

        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
