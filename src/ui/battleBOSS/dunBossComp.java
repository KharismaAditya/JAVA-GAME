package ui.battleBOSS;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

public class dunBossComp {
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

    public VBox countdownDisplay(Label countdownLabel) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D9D9D9;");
        box.setMinSize(480, 200);
        box.setSpacing(12);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20, 20, 20, 20));

        Label title = new Label("DEFENSE ACTIVATED!");
        title.setStyle("-fx-font-size: 11px; -fx-text-fill: #111111; -fx-font-weight: bold;");

        Label subtitle = new Label("Math question appears in:");
        subtitle.setStyle("-fx-font-size: 8px; -fx-text-fill: #444444;");

        countdownLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #D32F2F; -fx-font-weight: bold;");

        box.getChildren().addAll(title, subtitle, countdownLabel);
        return box;
    }

    public VBox defenseGuess(int n1, int n2) {
        TextField playerAnswer = new TextField();
        return defenseGuess(n1, n2, playerAnswer);
    }

    public VBox defenseGuess(int n1, int n2, TextField playerAnswer) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #D9D9D9;");
        box.setMinSize(480, 120);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 40, 0, 40));

        Label guess = new Label(n1 + " + " + n2 + " = ?");
        guess.setAlignment(Pos.CENTER);
        guess.setMinSize(160, 25);
        guess.setStyle("-fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: black; -fx-padding: 4px;");

        playerAnswer.setAlignment(Pos.CENTER);
        playerAnswer.setStyle("-fx-font-size: 12px; -fx-text-fill: black; -fx-background-color: white;");
        playerAnswer.setPromptText("Enter your answer...");

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
