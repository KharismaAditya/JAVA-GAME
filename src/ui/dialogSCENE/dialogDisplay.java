package ui.dialogSCENE;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.player;
import ui.battleBOSS.dungeonBossDisplay;
import ui.menu.menuDisplay;

public class dialogDisplay {
    menuDisplay menu = new menuDisplay();
    dialogCOMP comp = new  dialogCOMP();

    VBox pictPanel = new VBox();
    HBox textPanel = new HBox();
    HBox buttonPanel = new HBox();

    Button next = comp.buttonComp("NEXT");
    Button Skip = comp.buttonComp("SKIP");

    Label transition = new Label("... ENTERING DUNGEON ...");
    PauseTransition pause = new PauseTransition(Duration.seconds(1));

    public VBox DIALOG(player mainchar, Scene mainscene, Parent parentroot) {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360); root.setAlignment(Pos.CENTER);
        root.setSpacing(8); root.setPadding(new Insets(15,20,9,20));

        pictPanel.setMinSize(480,200); pictPanel.setAlignment(Pos.CENTER);
        pictPanel.setStyle("-fx-background-color: #D9D9D9;");

        textPanel.setMinSize(480, 95); textPanel.setAlignment(Pos.CENTER);
        textPanel.setStyle("-fx-background-color: #F8F8F8;");

        buttonPanel.setMinSize(480,25); buttonPanel.setAlignment(Pos.CENTER_RIGHT);
        buttonPanel.setSpacing(14); buttonPanel.getChildren().addAll(next,Skip);

        root.getChildren().addAll(pictPanel,textPanel,buttonPanel);
        root.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        Skip.setOnAction(e -> {
            dungeonBossDisplay Play = new  dungeonBossDisplay(mainchar);
            root.getChildren().clear();
            root.getChildren().add(transition);
            pause.setOnFinished(e1 -> {
                try {
                    menu.setSceneCustom(mainscene, Play.start(mainscene,parentroot));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                stage.close();
            });

            pause.play();
        });

        stage.setOnCloseRequest(event -> {
            event.consume(); // blokir action close
            System.out.println("Close button disabled!");
        });

        stage.setResizable(false);
        return root;
    }

}
