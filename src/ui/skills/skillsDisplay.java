package ui.skills;

import com.sun.tools.javac.Main;
import javafx.scene.control.Alert;
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

    Button skill1 = comp.buyButton("ARMAGEDDON\n100 HP 120 COIN");
    Button skill2 = comp.buyButton("SONG OF THE SEA\n200 COIN");
    Button skill3 = comp.buyButton("CALL OF DEATH\n300 COIN and Something");

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
        activePane.setActivePane(false);
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
        double HPCost = (double) Mainchar.getCharHP() * 0.1;

        if(Mainchar.getCharCoin() >= 300){
            Mainchar.setCharHP((int) HPCost); Mainchar.setCharCoin(Mainchar.getCharCoin() - 300);
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
