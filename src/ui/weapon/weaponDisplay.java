package ui.weapon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import method.ActivePane;
import method.CharDamageMain;
import method.Refreshable;
import model.player;

public class weaponDisplay {
    weaponComp comp = new weaponComp();
    Refreshable refresh;
    ActivePane activePane;
    CharDamageMain charDamage;

    private static final String[] WEAPON_NAMES = {
            "DULL BLADE",
            "GREATSWORD",
            "EXCALIBUR",
            "ANCHORSWORD",
            "PROTOTYPE#2"
    };

    public weaponDisplay(Refreshable refresh, ActivePane activePane, CharDamageMain charDamage) {
        this.refresh = refresh;
        this.activePane = activePane;
        this.charDamage = charDamage;
    }

    public VBox WEAPON(player mainchar, Scene mainscene, Parent mainroot) {
        weaponReader wr = new weaponReader(mainchar);

        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        // --- Up Section ---
        HBox upsection = new HBox();
        upsection.setMinSize(520, 50);
        upsection.setAlignment(Pos.CENTER_RIGHT);
        upsection.setPadding(new Insets(10, 14, 10, 14));

        Button exit = comp.ExitButton();
        exit.setOnMouseClicked(e -> {
            mainscene.setRoot(mainroot);
            activePane.setActivePane(false);
        });
        upsection.getChildren().add(exit);

        HBox weaponContainer = new HBox(43);
        weaponContainer.setAlignment(Pos.CENTER_LEFT);
        weaponContainer.setPadding(new Insets(16, 44, 16, 44));

        for (int i = 0; i < WEAPON_NAMES.length; i++) {
            if (mainchar.getCharAtkLVL() >= i) {
                Button useButton = comp.equipButton();
                VBox labelItem = comp.labelItem(WEAPON_NAMES[i]);

                VBox weaponBox = new VBox(10);
                weaponBox.setAlignment(Pos.CENTER);
                weaponBox.getChildren().addAll(labelItem, useButton);
                weaponContainer.getChildren().add(weaponBox);

                setupWeaponButton(useButton, i + 1, wr, mainscene, mainroot);
            }
        }

        VBox scrollContent = new VBox();
        scrollContent.getChildren().add(weaponContainer);

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

        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(0);
        });

        // --- Down Section ---
        HBox downSection = new HBox();
        downSection.setMinSize(520, 50);

        root.getChildren().addAll(upsection, scrollPane, downSection);
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

    private void setupWeaponButton(Button button, int index, weaponReader wr,
                                   Scene mainscene, Parent mainroot) {
        button.setOnMouseClicked(e -> {
            charDamage.setDamageChar(wr.getDamageChange(index));
            alert(wr.getName(index));
            refresh.refreshCharStat();
            mainscene.setRoot(mainroot);
            activePane.setActivePane(false);
        });
    }

    public void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("USING " + message);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }
}