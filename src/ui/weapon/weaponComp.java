package ui.weapon;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.battleBOSS.dunBossComp;

public class weaponComp {
    dunBossComp style = new dunBossComp();

    public VBox labelItem(String message){
        VBox vbox = new VBox(); vbox.setMinSize(115, 180);
        vbox.setStyle("-fx-background-color: #D9D9D9;"); vbox.setAlignment(Pos.CENTER);
        Label label = new Label(message);
        vbox.getChildren().add(label);
        return vbox;
    }

    public Button equipButton(){
        Button button = new Button("EQUIP"); button.setMinSize(115,32);
        button.setStyle("-fx-background-color: #D9D9D9;");
        onStyle(button);
        return button;
    }

    public Button ExitButton(){
        Button button = new Button("X"); button.setMinSize(35,35);
        button.setStyle("-fx-background-color: #D9D9D9;");
        onStyle(button);
        return button;
    }

    public void onStyle(Button btn){
        btn.setStyle("-fx-background-color: #D9D9D9;-fx-font-size: 8px;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: GRAY; -fx-font-size: 8px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #D9D9D9;-fx-font-size: 8px;"));
    }
}
