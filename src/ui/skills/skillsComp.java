package ui.skills;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class skillsComp {
    public VBox labelItem(String message){
        VBox vbox = new VBox(); vbox.setMinSize(115, 180);
        vbox.setStyle("-fx-background-color: #D9D9D9;"); vbox.setAlignment(Pos.CENTER);
        Label label = new Label(message);
        vbox.getChildren().add(label);
        return vbox;
    }

    public Button buyButton(){
        Button button = new Button("BUY");
        button.setMinSize(115,32);
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
        btn.setStyle("-fx-background-color: #D9D9D9;-fx-font-size: 8px; -fx-text-alignment: CENTER;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: GRAY; -fx-font-size: 8px;-fx-text-alignment: CENTER;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #D9D9D9;-fx-font-size: 8px;-fx-text-alignment: CENTER;"));
    }
}
