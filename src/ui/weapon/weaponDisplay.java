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

    public weaponDisplay(Refreshable refresh, ActivePane activePane, CharDamageMain charDamage) {
        this.refresh = refresh;
        this.activePane = activePane;
        this.charDamage = charDamage;
    }

    VBox item1 = comp.labelItem("DULL BLADE");
    VBox item2 = comp.labelItem("GREATSWORD");
    VBox item3 = comp.labelItem("EXCALIBUR");
    VBox item4 = comp.labelItem("PROTOTYPE#1");
    VBox item5 = comp.labelItem("PROTOTYPE#2");

    Button use1 = comp.equipButton();
    Button use2 = comp.equipButton();
    Button use3 = comp.equipButton();
    Button use4 = comp.equipButton();
    Button use5 = comp.equipButton();
    Button exit = comp.ExitButton();

    public VBox WEAPON(player mainchar, Scene mainscene, Parent mainroot){
        weaponReader wr = new weaponReader(mainchar);

        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        HBox upsection = new HBox(); upsection.setMinSize(520,50);
        upsection.setAlignment(Pos.CENTER_RIGHT);
        upsection.setPadding(new Insets(10,14,10,14));
        Button exit = comp.ExitButton();
        upsection.getChildren().add(exit);

        VBox weapon1 = new VBox(10);
        weapon1.setAlignment(Pos.CENTER);
        weapon1.getChildren().addAll(item1, use1);

        VBox weapon2 = new VBox(10);
        weapon2.setAlignment(Pos.CENTER);
        weapon2.getChildren().addAll(item2, use2);

        VBox weapon3 = new VBox(10);
        weapon3.setAlignment(Pos.CENTER);
        weapon3.getChildren().addAll(item3, use3);

        VBox weapon4 = new VBox(10);
        weapon4.setAlignment(Pos.CENTER);
        weapon4.getChildren().addAll(item4, use4);

        VBox weapon5 = new VBox(10);
        weapon5.setAlignment(Pos.CENTER);
        weapon5.getChildren().addAll(item5, use5);


        HBox weaponContainer = new HBox(43);
        weaponContainer.setAlignment(Pos.CENTER_LEFT);
        weaponContainer.setPadding(new Insets(16, 44, 16, 44));

        if (mainchar.getCharAtkLVL() >= 0) weaponContainer.getChildren().add(weapon1);
        if (mainchar.getCharAtkLVL() >= 1) weaponContainer.getChildren().add(weapon2);
        if (mainchar.getCharAtkLVL() >= 2) weaponContainer.getChildren().add(weapon3);
        if (mainchar.getCharAtkLVL() >= 3) weaponContainer.getChildren().add(weapon4);
        if (mainchar.getCharAtkLVL() >= 4) weaponContainer.getChildren().add(weapon5);

        VBox scrollContent = new VBox();
        scrollContent.getChildren().addAll(weaponContainer);

        ScrollPane scrollPane = new ScrollPane(scrollContent);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);

        scrollPane.setMinSize(520, 260);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        scrollPane.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            scrollPane.setHvalue(scrollPane.getHvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getWidth());
            event.consume();
        });

        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(0);
        });

        HBox downSection = new HBox(); downSection.setMinSize(520,50);
        root.getChildren().addAll(upsection,scrollPane,downSection);


        use1.setOnMouseClicked(e -> {
            charDamage.setDamageChar(wr.getDamageChange(1));alert(wr.getName(1));
            refresh.refreshCharStat();mainscene.setRoot(mainroot);activePane.setActivePane(false);
        });
        use2.setOnMouseClicked(e -> {
            charDamage.setDamageChar(wr.getDamageChange(2));alert(wr.getName(2));
            refresh.refreshCharStat();mainscene.setRoot(mainroot);activePane.setActivePane(false);
        });
        use3.setOnMouseClicked(e -> {
            charDamage.setDamageChar(wr.getDamageChange(3));alert(wr.getName(3));
            refresh.refreshCharStat();mainscene.setRoot(mainroot);activePane.setActivePane(false);
        });
        exit.setOnMouseClicked(e -> {
            mainscene.setRoot(mainroot);
            activePane.setActivePane(false);
        });

        root.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        stage.setOnCloseRequest(event -> {
            event.consume(); // blokir action close
            System.out.println("Close button disabled!");
        });

        stage.setResizable(false);
        return root;
    }

    public void alert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setHeaderText(null);
        alert.setContentText("USING " + message);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }
}
