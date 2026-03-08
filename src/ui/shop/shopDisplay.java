package ui.shop;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import method.*;
import model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class shopDisplay {
    private boolean potionActive = false;
    private int attackCount = 0;
    private int baseAtk = 30;

    shopComp comp = new shopComp();
    Refreshable refresh;
    ActivePane activePane;
    potionDamageChangeTemp temp;

    public shopDisplay(Refreshable refresh, ActivePane activePane, potionDamageChangeTemp temp) {
        this.activePane = activePane;
        this.refresh = refresh;
        this.temp = temp;
    }

    VBox item1 = comp.labelItem("BREAD");
    VBox item2 = comp.labelItem("STEAK");
    VBox item3 = comp.labelItem("MAGIC POTION");

    Button buy1 = comp.buyButton();
    Button buy2 = comp.buyButton();
    Button buy3 = comp.buyButton();
    Button exit = comp.ExitButton();

    public VBox SHOP(player mainchar, Scene mainscene, Parent mainroot) {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        HBox upsection = new HBox(); upsection.setMinSize(520,50);
        upsection.setAlignment(Pos.CENTER_RIGHT);
        upsection.setPadding(new Insets(10,14,10,14));
        Button exit = comp.ExitButton();
        upsection.getChildren().add(exit);

        HBox middleSection = new HBox(); middleSection.setMinSize(520,196);
        middleSection.setAlignment(Pos.CENTER);
        middleSection.setPadding(new Insets(16,44,0,44));
        middleSection.setSpacing(43);
        middleSection.getChildren().addAll(item1,item2,item3);

        HBox middleSection2 = new HBox(); middleSection2.setMinSize(520,65);
        middleSection2.setAlignment(Pos.CENTER);
        middleSection2.setPadding(new Insets(16,44,16,44));
        middleSection2.setSpacing(43);
        middleSection2.getChildren().addAll(buy1,buy2,buy3);

        HBox downSection = new HBox(); downSection.setMinSize(520,50);
        root.getChildren().addAll(upsection,middleSection,middleSection2,downSection);


        buy1.setOnMouseClicked(e -> {breadEffect(mainchar);});
        buy2.setOnMouseClicked(e -> {steakEffect(mainchar);});
        buy3.setOnMouseClicked(e -> {magicPotionEffect(mainchar);});
        exit.setOnMouseClicked(e -> {
            mainscene.setRoot(mainroot);
            activePane.setActivePane(false);
        });

        root.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        stage.setOnCloseRequest(event -> {
            event.consume(); // blokir action close
            System.out.println("Close button disabled!");
        });

        stage.setResizable(false);
        return root;
    }

    public void breadEffect(player Char) {
        if(Char.getCharCoin() >= 30){
            Char.setCharHP(Char.getCharHP() + 30);
            Char.setCharCoin(Char.getCharCoin() - 30);
            confirm("BREAD", 30, "PLAYER HP + 30");
        }else{
            alert();
        }
        refresh.refreshCharStat();
    }

    public void steakEffect(player Char) {
        if(Char.getCharCoin() >= 50){
            Char.setCharHP(Char.getCharHP() + 50);
            Char.setCharCoin(Char.getCharCoin() - 50);
            confirm("STEAK",50, "PLATFORM HP + 50");
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
                temp.setDamageChange(50);
            }
            Char.setCharCoin(Char.getCharCoin() - 70);
            confirm("MAGIC POTION", 70, "PLAYER DAMAGE +50");
        }else{
            alert();
        }
        refresh.refreshCharStat();
    }
    public boolean isPotionActive() {
        return potionActive;
    }

    public void increaseAttackCount(int mainDamage) {
        attackCount++;
        if (attackCount >= 5) {
            potionActive = false;
            temp.setDamageChange(0);
            refresh.refreshCharStat();
        }
    }

    public void alert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setHeaderText(null);
        alert.setContentText("NOT ENOUGH COINS");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }

    public void confirm(String item, int price, String effectExplanation) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setHeaderText(null);
        alert.setContentText("BUYING " + item + " FOR " + price + " COIN \n" +
                "EFFECT : " + effectExplanation );
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }
}

