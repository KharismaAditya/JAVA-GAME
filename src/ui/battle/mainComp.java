package ui.battle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;


public class mainComp {
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Button row1(String message){
        Button btn = new Button(message);
        btn.setMinSize(140, 35);
        onStyle(btn);
        return btn;
    }

    public Button row2(String message){
        Button btn = new Button(message);
        btn.setMinSize(140, 35);
        onStyle2(btn);
        return btn;
    }

    public VBox defenseGuess(int n1, int n2){
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D9D9D9;");
        box.setMinSize(480, 120);
        box.setSpacing(10); box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 40, 0, 40));

        Label guess = new Label(n1 +" + " +n2); guess.setAlignment(Pos.CENTER);
        guess.setMinSize(120, 20);
        guess.setStyle("-fx-font-size: 12; -fx-background-color: white; -fx-text-fill: black;");

        TextField playerAnswer = new TextField(); playerAnswer.setAlignment(Pos.CENTER);
        playerAnswer.setStyle("-fx-font-size: 12; -fx-text-fill: black; -fx-background-color: white;");
        playerAnswer.setPromptText("your answer");

        playerAnswer.textProperty().addListener((obs, oldText, newText) -> {
            setAnswer(newText);
        });

        box.getChildren().addAll(guess, playerAnswer);

        String input = playerAnswer.getText();
        setAnswer(input);
        return box;
    }

    public void onStyle(Button btn){
        btn.setStyle("-fx-background-color: #D9D9D9;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: GRAY;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #D9D9D9;"));
    }

    public void onStyle2(Button btn){
        btn.setStyle("-fx-background-color: #b0b0b0;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: GRAY;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #b0b0b0;"));
    }
}
