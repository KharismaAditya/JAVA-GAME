package ui.battle;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import method.Refreshable;
import model.*;
import ui.shop.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import ui.skills.skillsDisplay;

import java.util.ArrayList;
import java.util.Random;

public class mainDisplay extends Application implements Refreshable {
    mainComp comp = new mainComp();
    shopDisplay shop = new shopDisplay(this);
    skillsDisplay skills = new skillsDisplay(this);

    player Mainchar = new player(100, 30, 50);
    int currentenemyindex;
    entityList enmList = new entityList();

    ArrayList<entity> arrEnt = enmList.entList();
    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    int count = 5;

    HBox display = new HBox();
    HBox winOrLose = new HBox();
    StackPane displayPane = new StackPane();

    Label defenseRNG;
    Label nameEnt;
    Label statEntHP;
    Label statEntATK;

    Label statHP;
    Label statATK;
    Label statCoin;

    VBox buttonsButton = new VBox();
    HBox buttonsRow1 = new HBox();
    HBox buttonsRow2 = new HBox();

    Button row1col1 = comp.row1("ATTACK");
    Button row1col2 = comp.row1("DEFENSE");
    Button row1col3 = comp.row1("SHOP");

    Button row2col1 = comp.row1("EXIT");
    Button row2col2 = comp.row1("SKILLS");

    @Override
    public void start(Stage stage) throws Exception {
        entity currentEnt = arrEnt.get(count());

        HBox mainroot = new HBox();
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(15, 20, 5, 20));
        root.setStyle("-fx-background-color: #FFFFFF");

        display.setStyle("-fx-background-color: #D9D9D9");
        display.setMinSize(480,200);

        winOrLose.setMinSize(480,200); winOrLose.setAlignment(Pos.CENTER);
        defenseRNG = new Label("");
        winOrLose.getChildren().add(defenseRNG);

        displayPane.setPrefSize(480,200);
        displayPane.getChildren().addAll(display,winOrLose);

        display.setAlignment(Pos.BOTTOM_CENTER);
        display.setSpacing(30);
        nameEnt = new Label(arrEnt.get(count()).getEntName());
        statEntHP = new Label("Enemy HP: " + arrEnt.get(count()).getEntHP());
        statEntATK = new Label("Enemy ATK: " + arrEnt.get(count()).getEntAtk());
        display.getChildren().addAll(nameEnt,statEntHP,statEntATK);

        buttonsButton.setPrefSize(480, 120);

        HBox statdisplay = new HBox(); statdisplay.setStyle("-fx-background-color: #D9D9D9");
        statdisplay.setPrefSize(480,20);
        statdisplay.setAlignment(Pos.CENTER);
        statdisplay.setSpacing(30);
        statHP = new Label("HP: " + Mainchar.getCharHP());
        statATK = new Label("ATK: " + Mainchar.getCharAtk());
        statCoin = new Label("Coin: " + Mainchar.getCharCoin());
        statdisplay.getChildren().addAll(statHP, statATK, statCoin);

        buttonsRow1.setMinSize(480, 60); buttonsRow1.setAlignment(Pos.TOP_CENTER);
        buttonsRow1.setPadding(new Insets(18, 0, 8, 0)); buttonsRow1.setSpacing(30);
        buttonsRow2.setMinSize(480, 60); buttonsRow2.setAlignment(Pos.TOP_CENTER);
        buttonsRow2.setPadding(new Insets(8, 0, 16, 0)); buttonsRow2.setSpacing(60);

        buttonsRow1.getChildren().addAll(row1col1, row1col2, row1col3);
        buttonsRow2.getChildren().addAll(row2col1, row2col2);
        buttonsButton.getChildren().addAll(buttonsRow1, buttonsRow2);

        root.getChildren().addAll(displayPane, buttonsButton, statdisplay);
        mainroot.getChildren().add(root);
        Scene scene = new Scene(mainroot);

        row1col1.setOnMouseClicked(e -> {attackScene();});
        row1col2.setOnMouseClicked(e -> {defenseScene();});
        row1col3.setOnMouseClicked(e -> {shop.SHOP(Mainchar);});
        row2col1.setOnMouseClicked(e -> {stage.close();});
        row2col2.setOnMouseClicked(e -> skills.SKILLS(Mainchar,currentEnt));

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public int count(){
     if(arrEnt.get(currentenemyindex).getEntHP()<=0){
         nameEnt.setText("...");statEntHP.setText("STAGE CLEAR"); statEntATK.setText("...");
         pause.setOnFinished(e->{
             currentenemyindex++;
             refreshEntStat();
         });
         pause.play();
     }
     return currentenemyindex;
    }
    public void coinWin(){
        Random rand = new Random();
        int coinReward = rand.nextInt(100);
        Mainchar.setCharCoin(Mainchar.getCharCoin() + coinReward);
        refreshCharStat();
    }

    @Override
    public void refreshCharStat(){
        statHP.setText("HP: " + Mainchar.getCharHP());
        statATK.setText("ATK: " + Mainchar.getCharAtk());
        statCoin.setText("Coin: " + Mainchar.getCharCoin());
    }

    @Override
    public void refreshEntStat(){
        entity currentEnt = arrEnt.get(count());
        nameEnt.setText(currentEnt.getEntName());
        statEntHP.setText("Enemy HP : " + currentEnt.getEntHP());
        statEntATK.setText("Enemy ATK: " + arrEnt.get(count()).getEntAtk());
    }

    public void attackScene(){
        entity currentEnt = arrEnt.get(count());
        currentEnt.setEntHP(currentEnt.getEntHP() - Mainchar.getCharAtk());
        Mainchar.setCharHP(Mainchar.getCharHP() - currentEnt.getEntAtk());

        if (shop.isPotionActive()) {
            shop.increaseAttackCount(Mainchar);
        }

        if(currentEnt.getEntHP() <= 0){
            currentEnt.setEntHP(0); coinWin();
            refreshEntStat();
        }
        else {
            statHP.setText(""); statATK.setText("ATTACKING"); statCoin.setText("");
            nameEnt.setText("...");statEntHP.setText("ATTACKING"); statEntATK.setText("...");
            pause.setOnFinished(e->{
                refreshEntStat();
                refreshCharStat();
            });
            pause.play();
        }
    }

    public void defenseScene(){
        displayPane.getChildren().clear();
        buttonsButton.getChildren().clear();

        Random rand = new Random();
        entity currentEnt = arrEnt.get(count());

        int angka1 =  rand.nextInt(50);
        int angka2 = rand.nextInt(50);
        int answerGuess = angka1 + angka2;

        VBox answerDisplay = new VBox();
        answerDisplay.setStyle("-fx-background-color: #D9D9D9");
        answerDisplay.setPadding(new Insets(30, 10, 30, 10));
        answerDisplay.setAlignment(Pos.CENTER);
        answerDisplay.setMinSize(480, 200);
        answerDisplay.setSpacing(5);

        VBox defenseguess = comp.defenseGuess(angka1, angka2);
        Button answerBut = comp.row2("ANSWER");
        answerDisplay.getChildren().addAll(defenseguess, answerBut);
        displayPane.getChildren().addAll(answerDisplay);

        answerBut.setOnMouseClicked(e -> {
            String input = comp.getAnswer();
            if (input != null && !input.trim().isEmpty()) {
                int answer = Integer.parseInt(input);
                if(answer == answerGuess){
                    defenseRNG.setText("YOU WIN");
                    currentEnt.setEntHP(currentEnt.getEntHP() - Mainchar.getCharAtk());

                    if(currentEnt.getEntHP() <= 0){
                        currentEnt.setEntHP(0);
                        coinWin();
                        nameEnt.setText("...");statEntHP.setText("STAGE CLEAR");statEntATK.setText("...");
                        pause.setOnFinished(e2 -> {
                            defenseRNG.setVisible(false);
                            currentenemyindex++;
                            refreshEntStat();
                            refreshCharStat();
                        });
                        pause.play();
                    } else {
                        refreshEntStat();
                        refreshCharStat();
                        pause.setOnFinished(e2 -> defenseRNG.setVisible(false));
                        pause.play();
                    }
                } else {
                    defenseRNG.setText("YOU LOSE");
                    int newHP = Mainchar.getCharHP() - currentEnt.getEntAtk();
                    if (newHP < 0) newHP = 0;
                    Mainchar.setCharHP(newHP);
                    refreshCharStat();

                    pause.setOnFinished(e2 -> defenseRNG.setVisible(false));
                    pause.play();
                }
            } else {
                defenseRNG.setText("Please enter a number!");
                pause.setOnFinished(e2 -> defenseRNG.setVisible(false));
                pause.play();
            }

            buttonsButton.getChildren().addAll(buttonsRow1, buttonsRow2);
            displayPane.getChildren().clear();
            displayPane.getChildren().addAll(display,winOrLose);
        });
    }


    public static void main(String[] args){
        Application.launch(mainDisplay.class, args);
    }
}
