package ui.shop;

import javafx.application.Application;
import javafx.scene.control.Alert;
import method.Refreshable;
import model.*;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import ui.battle.mainDisplay;

public class shopDisplay {
    private boolean potionActive = false;
    private int attackCount = 0;
    private int baseAtk = 30;

    shopComp comp = new shopComp();
    Refreshable refresh;

    public shopDisplay(Refreshable refresh) {
        this.refresh = refresh;
    }

    VBox item1 = comp.labelItem("BREAD:30");
    VBox item2 = comp.labelItem("STEAK:50");
    VBox item3 = comp.labelItem("MAGIC POTION:70");

    Button buy1 = comp.buyButton();
    Button buy2 = comp.buyButton();
    Button buy3 = comp.buyButton();
    Button exit = comp.ExitButton();

    public void SHOP(player mainchar) {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(240, 360);

        HBox upSection = new HBox(); upSection.setMinSize(240, 313);

        VBox upSectionRow1 = new VBox();
        upSectionRow1.setMinSize(170, 313);
        upSectionRow1.setAlignment(Pos.TOP_CENTER);
        upSectionRow1.setPadding(new Insets(19, 15, 19, 15));
        upSectionRow1.setSpacing(20);
        upSectionRow1.getChildren().addAll(item1,item2,item3);

        VBox upSectionRow2 = new VBox();
        upSectionRow2.setMinSize(70, 313);
        upSectionRow2.setAlignment(Pos.TOP_CENTER);
        upSectionRow2.setPadding(new Insets(19, 15, 19, 20));
        upSectionRow2.setSpacing(20);
        upSectionRow2.getChildren().addAll(buy1,buy2,buy3);

        upSection.getChildren().addAll(upSectionRow1, upSectionRow2);

        VBox downSection = new VBox();
        downSection.setMinSize(240, 47);
        downSection.setAlignment(Pos.CENTER_RIGHT);
        downSection.setPadding(new Insets(19, 6, 19, 6));
        downSection.getChildren().addAll(exit);

        buy1.setOnMouseClicked(e -> {breadEffect(mainchar);});
        buy2.setOnMouseClicked(e -> {steakEffect(mainchar);});
        buy3.setOnMouseClicked(e -> {magicPotionEffect(mainchar);});
        exit.setOnMouseClicked(e -> {
            stage.close();
        });

        root.getChildren().addAll(upSection, downSection);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
    public void breadEffect(player Char) {
        if(Char.getCharCoin() >= 30){
            Char.setCharHP(Char.getCharHP() + 50);
            Char.setCharCoin(Char.getCharCoin() - 30);
        }else{
            alert();
        }
        refresh.refreshCharStat();
    }

    public void steakEffect(player Char) {
        if(Char.getCharCoin() >= 50){
            Char.setCharHP(Char.getCharHP() + 50);
            Char.setCharCoin(Char.getCharCoin() - 50);
        }else{
            alert();
        }
        refresh.refreshCharStat();
    }

    public void magicPotionEffect(player Char) {
        if(Char.getCharCoin() >= 70){
            if (!potionActive) {
                potionActive = true;
                attackCount = 0;
                Char.setCharAtk(Char.getCharAtk() + 10);
            }
            Char.setCharCoin(Char.getCharCoin() - 70);
        }else{
            alert();
        }
        refresh.refreshCharStat();
    }
    public boolean isPotionActive() {
        return potionActive;
    }

    public void increaseAttackCount(player Char) {
        attackCount++;
        if (attackCount >= 5) {
            // potion habis, reset ke base attack
            potionActive = false;
            Char.setCharAtk(baseAtk);
            refresh.refreshCharStat();
        }
    }

    public void alert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setHeaderText(null);
        alert.setTitle("Not Enough Coins");
        alert.setContentText("NOT ENOUGH COINS");
        alert.showAndWait();
    }
}

