package ui.menu.townhall;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.player;
import ui.battleBOSS.dungeonBossDisplay;
import ui.menu.menuDisplay;

public class townhallDisplay {
    townhallComp comp = new townhallComp();
    menuDisplay menu = new menuDisplay();

    Label statName;
    Label hpStat;
    Label coinStat;
    Label levelStat;

    Button bossEntry = comp.worldButton("BOSS DUNGEON");
    Button dungeonEntry = comp.worldButton("DUNGEON");
    Button exit = comp.ExitButton();

    public VBox townhall(player mainchar, Scene menuscene, Parent menuroot) {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        HBox upsection = new HBox(); upsection.setPrefSize(520,180);
        upsection.setAlignment(Pos.BOTTOM_CENTER);
        upsection.setSpacing(20); upsection.setPadding(new Insets(10));
        upsection.getChildren().addAll(bossEntry,dungeonEntry);

        HBox middlesection = new HBox(); middlesection.setPrefSize(520,120);
        middlesection.setAlignment(Pos.TOP_CENTER);
        middlesection.setSpacing(20); middlesection.setPadding(new Insets(10));
        middlesection.getChildren().add(exit);

        HBox downsection = new HBox(); downsection.setPrefSize(520,60);
        downsection.setAlignment(Pos.CENTER); downsection.setPadding(new Insets(20));

        HBox statChar = new HBox(); statChar.setPrefSize(480,20);
        statChar.setAlignment(Pos.CENTER); statChar.setSpacing(40);

        statName = new Label("NAME:" + mainchar.getName());
        hpStat = new Label("HP:" + mainchar.getCharHP());
        coinStat = new Label("COIN:" + mainchar.getCharCoin());
        levelStat = new Label("LEVEL:" + mainchar.getCharAtkLVL());

        statChar.getChildren().addAll(statName,hpStat,coinStat,levelStat);
        statChar.setStyle("-fx-background-color: #D9D9D9");
        downsection.getChildren().add(statChar);

        root.getChildren().addAll(upsection,middlesection,downsection);
        root.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        bossEntry.setOnAction(e->{
            dungeonBossDisplay Play = new dungeonBossDisplay(mainchar);
            try {
                menu.setSceneCustom(menuscene, Play.start(menuscene,menuroot));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });

        exit.setOnAction(e->{
            menuscene.setRoot(menuroot);
        });

        stage.setResizable(false);
        return root;
    }
}
