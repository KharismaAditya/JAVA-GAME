package ui.menu;
import javafx.scene.Parent;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;
import ui.menu.loadGame.lgDisplay;
import ui.menu.newGame.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class menuDisplay extends Application {
    menuComp comp =  new menuComp();

    Button playButton = comp.PlayButton();
    Button loadButton = comp.buttonComp("LOAD");
    Button exitButton = comp.buttonComp("EXIT");

    VBox mainroot = new VBox();
    Scene scene = new Scene(mainroot,520, 360);

    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    @Override
    public void start(Stage stage) throws Exception {
        Font font = Font.loadFont(getClass().getResourceAsStream("/font/PressStart2P.ttf"), 9);
        mainroot.setAlignment(Pos.CENTER);mainroot.setPadding(new Insets(20));

        VBox buttonBox = new VBox(); buttonBox.setPrefSize(140, 65);
        buttonBox.setSpacing(10); buttonBox.setAlignment(Pos.CENTER);

        HBox bottomButtonBox = new  HBox(); bottomButtonBox.setSpacing(20); bottomButtonBox.setAlignment(Pos.CENTER);
        bottomButtonBox.getChildren().addAll(loadButton,exitButton);
        buttonBox.getChildren().addAll(playButton, bottomButtonBox);

        mainroot.getChildren().add(buttonBox);

        scene.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        playButton.setOnAction(e -> {
            VBox loadingRoot = new VBox(new Label("CREATE NEW GAME.."));
            loadingRoot.setAlignment(Pos.CENTER);
            loadingRoot.getStylesheets().add(getClass().getResource("/font/styles.css").toExternalForm());
            scene.setRoot(loadingRoot);
            pause.setOnFinished(e1 ->{
                ngDisplay ng = new ngDisplay();
                scene.setRoot(ng.newGame(scene,mainroot));
            });
            pause.play();
        });

        loadButton.setOnAction(e -> {
            try {
                lgDisplay lg = new lgDisplay();
                scene.setRoot(lg.loadDisplay(scene,mainroot,this));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        exitButton.setOnAction(e -> {
            stage.close();
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void setSceneCustom(Scene scene, Parent root){
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
