package ui.dialogSCENE;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.player;
import ui.battleBOSS.dungeonBossDisplay;
import ui.menu.menuDisplay;

public class dialogDisplay {
    menuDisplay menu = new menuDisplay();

    public VBox DIALOG(player mainchar, Scene mainscene, Parent parentroot) {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360); root.setAlignment(Pos.CENTER);

        Button next = new Button("DIALOG");
        root.getChildren().add(next);

        root.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        next.setOnAction(e -> {
            dungeonBossDisplay Play = new  dungeonBossDisplay(mainchar);
            try {
                menu.setSceneCustom(mainscene, Play.start(mainscene,parentroot));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });

        stage.setOnCloseRequest(event -> {
            event.consume(); // blokir action close
            System.out.println("Close button disabled!");
        });

        stage.setResizable(false);
        return root;
    }

}
