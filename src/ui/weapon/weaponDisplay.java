package ui.weapon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    Button use1 = comp.equipButton();
    Button use2 = comp.equipButton();
    Button use3 = comp.equipButton();
    Button exit = comp.ExitButton();

    public VBox WEAPON(player mainchar, Scene mainscene, Parent mainroot){
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        HBox upsection = new HBox(); upsection.setMinSize(520,50);
        upsection.setAlignment(Pos.CENTER_RIGHT);
        upsection.setPadding(new Insets(10,14,10,14));
        Button exit = comp.ExitButton();
        upsection.getChildren().add(exit);

        HBox middleSection = new HBox(); middleSection.setMinSize(520,196);
        middleSection.setAlignment(Pos.CENTER);
        middleSection.setPadding(new Insets(16,44,0,44));
        middleSection.setSpacing(43);

        HBox middleSection2 = new HBox(); middleSection2.setMinSize(520,65);
        middleSection2.setAlignment(Pos.CENTER);
        middleSection2.setPadding(new Insets(16,44,16,44));
        middleSection2.setSpacing(43);

        if(mainchar.getCharAtkLVL() >= 0){middleSection.getChildren().add(item1);
            middleSection2.getChildren().add(use1);}
        if(mainchar.getCharAtkLVL() >= 1){middleSection.getChildren().add(item2);
            middleSection2.getChildren().add(use2);}
        if (mainchar.getCharAtkLVL() >= 2){middleSection.getChildren().add(item3);
            middleSection2.getChildren().add(use3);}

        HBox downSection = new HBox(); downSection.setMinSize(520,50);
        root.getChildren().addAll(upsection,middleSection,middleSection2,downSection);


        use1.setOnMouseClicked(e -> {
            charDamage.setDamageChar(100);alert("DULL BLADE");
            refresh.refreshCharStat();mainscene.setRoot(mainroot);activePane.setActivePane(false);
        });
        use2.setOnMouseClicked(e -> {
            charDamage.setDamageChar(150); alert("GREATSWORD");
            refresh.refreshCharStat();mainscene.setRoot(mainroot);activePane.setActivePane(false);
        });
        use3.setOnMouseClicked(e -> {
            charDamage.setDamageChar(200); alert("EXCALIBUR");
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
