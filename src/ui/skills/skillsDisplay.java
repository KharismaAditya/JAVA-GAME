package ui.skills;

import com.sun.tools.javac.Main;
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

public class skillsDisplay {
    Refreshable refresh;
    public skillsDisplay(Refreshable refresh) {this.refresh = refresh;}
    skillsComp comp = new skillsComp();

    Button skill1 = comp.buyButton("ARMAGEDDON\n100 HP 120 COIN");
    Button skill2 = comp.buyButton("SONG OF THE SEA\n200 COIN");
    Button skill3 = comp.buyButton("CALL OF DEATH\n200 HP 100 COIN");

    Button exit = comp.ExitButton();

    public void SKILLS(player MainChar, entity enemy){
     Stage stage = new Stage();
     VBox root = new VBox(); root.setMinSize(240,360);
     root.setAlignment(Pos.CENTER);

    VBox upperRoot = new VBox(); upperRoot.setAlignment(Pos.TOP_CENTER);
    upperRoot.setMinSize(240, 320);
    upperRoot.setPadding(new Insets(24,40,24,40)); upperRoot.setSpacing(10);
    upperRoot.getChildren().addAll(skill1,skill2,skill3);

    VBox lowerRoot = new VBox(); lowerRoot.setAlignment(Pos.BOTTOM_RIGHT);
    lowerRoot.setPadding(new Insets(0,24,10,24));
    lowerRoot.setMinSize(240, 40);
    lowerRoot.getChildren().addAll(exit);

    skill1.setOnAction(e -> {skill1Effect(MainChar, enemy);});
    skill2.setOnAction(e -> {skill2Effect(MainChar, enemy);});
    skill3.setOnAction(e -> {skill3Effect(MainChar, enemy);});
    exit.setOnMouseClicked(e->{
        stage.close();
    });

     root.getChildren().addAll(upperRoot,lowerRoot);
     stage.setResizable(false);
     stage.setScene(new Scene(root));
     stage.show();
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
        if(Mainchar.getCharCoin() >= 100 && Mainchar.getCharHP() >= 200){
            Mainchar.setCharHP(Mainchar.getCharHP() - 200); Mainchar.setCharCoin(Mainchar.getCharCoin() - 100);
            enemy.setEntHP(0);
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
