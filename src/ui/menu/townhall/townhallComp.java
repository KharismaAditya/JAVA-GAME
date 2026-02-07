package ui.menu.townhall;

import javafx.scene.control.Button;

public class townhallComp{
    public Button worldButton(String message){
        Button button = new Button(message); button.setMinSize(115,32);
        button.setStyle("-fx-background-color: #D9D9D9;");
        onStyle(button);
        return button;
    }

    public Button ExitButton(){
        Button button = new Button("BACK TO LOBBY"); button.setMinSize(180,25);
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
