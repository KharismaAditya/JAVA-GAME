package ui.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;


public class menuComp {
    public Button PlayButton(){
        Button button = new Button("PLAY GAME");
        button.setMinSize(140, 35);
        onStyle(button);

        return  button;
    }

    public Button buttonComp(String message){
        Button button = new Button(message);
        button.setMinSize(60, 20);
        onStyle(button);
        return  button;
    }


    public void onStyle(Button btn){
        btn.setStyle("-fx-background-color: #D9D9D9;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: GRAY;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #D9D9D9;"));
    }
}
