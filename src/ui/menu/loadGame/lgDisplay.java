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
import ui.battle.mainDisplay;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ui.menu.menuDisplay;

import java.util.ArrayList;

public class lgDisplay {
    Font font = Font.loadFont(getClass().getResourceAsStream("/font/PressStart2P.ttf"), 10);

    saveloadSystem save = new saveloadSystem();
    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    Label startGame = new Label("STARTING GAME..");
    Label titleLabel = new Label("LOAD GAME");

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

        mainroot.getChildren().addAll(title,listPlayer);
        mainroot.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

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
            mainDisplay Play = new mainDisplay(save.loadPlayer(p.getName()));
            pause.setOnFinished(e1->{
                try {
                    stage.close();
                    menu.setSceneCustom(menuScene, Play.start(menuScene,menuRoot));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            pause.play();
        });
        exit.setOnAction(e -> {
            stage.close();
        });

        buttonLayer.getChildren().addAll(load,exit);
        mainroot.getChildren().addAll(playerInfo,buttonLayer);
        Scene scene = new Scene(mainroot, 320, 130);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
