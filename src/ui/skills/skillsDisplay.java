package ui.skills;

import com.sun.tools.javac.Main;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;
import method.*;
import model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class skillsDisplay {
    Refreshable refresh;
    ActivePane activePane;
    public skillsDisplay(Refreshable refresh, ActivePane activePane) {
        this.refresh = refresh;
        this.activePane = activePane;
    }
    skillsComp comp = new skillsComp();

    VBox item1 = comp.labelItem("ARMAGEDDON");
    VBox item2 = comp.labelItem("WIND SEA");
    VBox item3 = comp.labelItem("CALL OF DEATH");

    Button buy1 = comp.buyButton();
    Button buy2 = comp.buyButton();
    Button buy3 = comp.buyButton();
    Button exit = comp.ExitButton();

    public VBox SKILLS(player MainChar, entity enemy, Scene mainscene, Parent mainroot) {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        HBox upsection = new HBox(); upsection.setMinSize(520,50);
        upsection.setAlignment(Pos.CENTER_RIGHT);
        upsection.setPadding(new Insets(10,14,10,14));
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

        buy1.setOnAction(e -> {skill1Effect(MainChar, enemy);});
        buy2.setOnAction(e -> {skill2Effect(MainChar, enemy);});
        buy3.setOnAction(e -> {skill3Effect(MainChar, enemy);});
        exit.setOnMouseClicked(e->{
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

    public void skill1Effect(player MainChar, entity enemy){
        if(MainChar.getCharHP() >= 100 && MainChar.getCharCoin() >= 120){
            MainChar.setCharHP(MainChar.getCharHP() - 100); MainChar.setCharCoin(MainChar.getCharCoin() - 120);
            enemy.setEntHP(10);
        }else{
            alert();
        }
        refresh.refreshCharStat(); refresh.refreshEntStat();
    }

    public void skill2Effect(player Mainchar, entity enemy){
        if(Mainchar.getCharCoin() >= 200){
            Mainchar.setCharCoin(Mainchar.getCharCoin() - 200);
            Mainchar.setCharHP(Mainchar.getCharHP() + 200);
        }else{
            alert();
        }
        refresh.refreshCharStat(); refresh.refreshEntStat();
    }

    public void skill3Effect(player Mainchar, entity enemy){
        double HPCost = (double) Mainchar.getCharHP() * 0.1;

        if(Mainchar.getCharCoin() >= 300){
            Mainchar.setCharHP((int) HPCost); Mainchar.setCharCoin(Mainchar.getCharCoin() - 300);
            enemy.setEntHP(1);
        }else {
            alert();
        }

        refresh.refreshCharStat(); refresh.refreshEntStat();
    }

    public void alert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setHeaderText(null);
        alert.setTitle("Not Enough Coins");
        alert.setContentText("NOT ENOUGH COINS");
        alert.showAndWait();
    }
}
