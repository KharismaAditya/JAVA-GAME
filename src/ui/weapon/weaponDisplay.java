package ui.weapon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import method.ActivePane;
import method.CharDamageMain;
import method.GamePopup;
import method.Refreshable;
import model.player;

public class weaponDisplay {
    private final weaponComp comp = new weaponComp();
    private final Refreshable refresh;
    private final ActivePane activePane;
    private final CharDamageMain charDamage;

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

        // Container weapon card dengan layout yang seragam dengan skills & shop
        HBox weaponContainer = new HBox(25);
        weaponContainer.setAlignment(Pos.CENTER_LEFT);
        weaponContainer.setPadding(new Insets(16, 25, 16, 25));

        for (int i = 0; i < WEAPON_NAMES.length; i++) {
            if (mainchar.getCharAtkLVL() >= i) {
                final int weaponIndex = i + 1;
                String weaponName = WEAPON_NAMES[i];
                int damage = wr.getDamageChange(weaponIndex);
                String tier = "TIER " + (i + 1);

                VBox card = comp.weaponCard(tier, weaponName, damage);
                Button equipBtn = comp.equipButton();

                equipBtn.setOnAction(e -> {
                    // Tampilkan Pop-up konfirmasi equip weapon
                    GamePopup.showWeaponConfirmation(tier, weaponName, damage, () -> {
                        charDamage.setDamageChar(damage);
                        refresh.refreshCharStat();
                        GamePopup.showMessage("WEAPON EQUIPPED", "Equipped " + weaponName + "!\nAttack Power: +" + damage);
                        mainscene.setRoot(mainroot);
                        activePane.setActivePane(false);
                    });
                });

                VBox weaponBox = new VBox(8);
                weaponBox.setAlignment(Pos.CENTER);
                weaponBox.getChildren().addAll(card, equipBtn);
                weaponContainer.getChildren().add(weaponBox);
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

        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(0));

        // --- Down Section ---
        HBox downSection = new HBox();
        downSection.setMinSize(520, 50);

        root.getChildren().addAll(upsection, scrollPane, downSection);
        try {
            root.getStylesheets().add(getClass().getResource("/font/styles.css").toExternalForm());
        } catch (Exception ignored) {}

        return root;
    }

    public void alert(String message) {
        GamePopup.showMessage("WEAPON EQUIPPED", "USING " + message);
    }
}
