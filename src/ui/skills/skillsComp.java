package ui.skills;
import javafx.scene.control.Button;

public class skillsComp {
    public Button buyButton(String message){
        Button button = new Button(message);
        button.setMinSize(160,35);
        onStyle(button);
        return button;
    }

    public Button ExitButton(){
        Button button = new Button("X"); button.setMinSize(40,40);
        button.setStyle("-fx-background-color: #D9D9D9;");
        onStyle(button);
        return button;
    }

    public void onStyle(Button btn){
        btn.setStyle("-fx-background-color: #D9D9D9;-fx-font-size: 8px; -fx-text-alignment: CENTER;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: GRAY; -fx-font-size: 8px;-fx-text-alignment: CENTER;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #D9D9D9;-fx-font-size: 8px;-fx-text-alignment: CENTER;"));
    }
}
