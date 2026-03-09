package ui.menu.loadGame;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.util.Duration;
import model.player;
import save_and_load.saveloadSystem;
import ui.battleBOSS.dungeonBossDisplay;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ui.menu.menuComp;
import ui.menu.menuDisplay;
import ui.menu.townhall.townhallDisplay;

import java.util.ArrayList;

public class lgDisplay {
    Font font = Font.loadFont(getClass().getResourceAsStream("/font/PressStart2P.ttf"), 10);

    menuComp comp = new menuComp();
    saveloadSystem save = new saveloadSystem();
    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    Label startGame = new Label("STARTING GAME..");
    Label titleLabel = new Label("LOAD GAME");
    Button exit = comp.buttonComp("EXIT");

    public VBox loadDisplay(Scene menuScene, Parent menuRoot, menuDisplay menu) {
        Stage stage = new Stage();
        VBox mainroot = new VBox();
        mainroot.setSpacing(15); mainroot.setAlignment(Pos.CENTER);

        HBox title = new HBox();title.setAlignment(Pos.BASELINE_CENTER);
        titleLabel.setFont(font);
        title.getChildren().add(titleLabel);

        HBox listPlayer =  new HBox();
        listPlayer.setSpacing(15);
        listPlayer.setAlignment(Pos.CENTER);

        ArrayList<player> pList = save.loadAllPlayers();
        for(player p : pList){
            Hyperlink pLink = new Hyperlink(p.getName()); pLink.setFont(font);
            listPlayer.getChildren().add(pLink);
            pLink.setOnAction(e -> {
                alert(p, menuScene, menuRoot, menu);
            });
        }

        HBox exitSection = new HBox();
        exitSection.setSpacing(15);
        exitSection.setAlignment(Pos.CENTER);
        exitSection.getChildren().add(exit);

        mainroot.getChildren().addAll(title,listPlayer,exitSection);
        mainroot.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        exit.setOnAction(e->{
            menuScene.setRoot(menuRoot);
        });

        stage.setResizable(false);
        return mainroot;
    }

    public void alert(player p, Scene menuScene, Parent menuRoot, menuDisplay menu) {
        startGame.setFont(font);
        Stage stage = new Stage();
        VBox mainroot = new VBox();
        mainroot.setSpacing(15);mainroot.setAlignment(Pos.CENTER);
        HBox playerInfo = new HBox();playerInfo.setSpacing(10);playerInfo.setAlignment(Pos.CENTER);
        Label name = new Label("NAME: " + p.getName());name.setFont(font);
        Label HP = new Label("HP : " + p.getCharHP());HP.setFont(font);
        playerInfo.getChildren().addAll(name,HP);

        HBox buttonLayer =  new HBox();buttonLayer.setSpacing(10);buttonLayer.setAlignment(Pos.CENTER);
        Button load = new Button("PLAY");load.setFont(font);
        Button exit = new Button("EXIT");exit.setFont(font);
        load.setOnAction(e -> {
            mainroot.getChildren().clear();
            Label loadText = new Label("... LOAD PLAYER DATA ...");
            mainroot.getChildren().add(loadText);
            pause.setOnFinished(e1->{
                try {
                    stage.close();
                    townhallDisplay townhall = new townhallDisplay();
                    menu.setSceneCustom(menuScene, townhall.townhall(p,menuScene,menuRoot));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            pause.play();
        });
        exit.setOnAction(e -> {
            stage.close();
        });

        mainroot.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        buttonLayer.getChildren().addAll(load,exit);
        mainroot.getChildren().addAll(playerInfo,buttonLayer);
        Scene scene = new Scene(mainroot, 320, 130);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
