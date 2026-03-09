package ui.dialogSCENE;

import javafx.scene.control.Button;

public class dialogCOMP {
    public Button buttonComp(String message){
        Button button = new Button(message); button.setMinSize(50,25);
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
