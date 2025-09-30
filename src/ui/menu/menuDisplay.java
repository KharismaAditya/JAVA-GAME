package ui.menu;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;
import ui.menu.newGame.*;
import ui.battle.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import ui.skills.skillsDisplay;

public class menuDisplay extends Application {
    menuComp comp =  new menuComp();
    ngDisplay ng = new ngDisplay();

    Button playButton = comp.PlayButton();
    Button loadButton = comp.buttonComp("LOAD");
    Button exitButton = comp.buttonComp("EXIT");

    Label startGame = new Label("CREATE NEW GAME..");

    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    @Override
    public void start(Stage stage) throws Exception {
        Font font = Font.loadFont(getClass().getResourceAsStream("/font/PressStart2P.ttf"), 9);
        VBox mainroot = new VBox();
        mainroot.setAlignment(Pos.CENTER);mainroot.setPadding(new Insets(20));

        VBox buttonBox = new VBox(); buttonBox.setPrefSize(140, 65);
        buttonBox.setSpacing(10); buttonBox.setAlignment(Pos.CENTER);

        HBox bottomButtonBox = new  HBox(); bottomButtonBox.setSpacing(20); bottomButtonBox.setAlignment(Pos.CENTER);
        bottomButtonBox.getChildren().addAll(loadButton,exitButton);
        buttonBox.getChildren().addAll(playButton, bottomButtonBox);

        mainroot.getChildren().add(buttonBox);
        Scene scene = new Scene(mainroot,520, 360);

        scene.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        playButton.setOnAction(e -> {
            mainroot.getChildren().clear();
            mainroot.getChildren().add(startGame);
            pause.setOnFinished(e1 ->{
                ng.newGame();
                stage.close();
            });
            pause.play();
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
