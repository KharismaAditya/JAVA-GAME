package ui.menu.newGame;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.util.Duration;
import ui.battle.mainDisplay;
import ui.menu.*;
import model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class ngDisplay {
    String name; player Player;
    menuComp comp = new menuComp();

    Label addName = new Label("ADD NAME");
    Label startGame = new Label("STARTING GAME..");
    Button addPlayer = comp.buttonComp("ADD");
    Button exit =  comp.buttonComp("EXIT");
    TextField namePlayer = new TextField();

    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    public void newGame() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/font/PressStart2P.ttf"), 9);
        Stage stage = new Stage();
        VBox mainroot = new VBox();
        mainroot.setSpacing(15); mainroot.setAlignment(Pos.CENTER);
        mainroot.setPadding(new Insets(0, 100, 0, 100));

        HBox buttonRoot = new HBox();
        buttonRoot.setSpacing(20); buttonRoot.setAlignment(Pos.CENTER);
        buttonRoot.getChildren().addAll(addPlayer, exit);

        namePlayer.setMinSize(220, 25);
        namePlayer.setMaxHeight(25); namePlayer.setMaxWidth(220);
        namePlayer.setPromptText("ENTER YOUR NAME");namePlayer.setFont(font);
        mainroot.getChildren().addAll(addName,namePlayer, buttonRoot);
        Scene scene = new Scene(mainroot, 520, 360);
        scene.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        addPlayer.setOnAction(e -> {
            name = namePlayer.getText();
            player newP = newPlayer();

            mainroot.getChildren().clear();
            mainroot.getChildren().add(startGame);
            pause.setOnFinished(e1->{
                try {
                    mainDisplay Play = new mainDisplay(newP);
                    Play.start();
                    stage.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            pause.play();
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public player newPlayer() {
        return new player(name,100, 30, 50);
    }
}
