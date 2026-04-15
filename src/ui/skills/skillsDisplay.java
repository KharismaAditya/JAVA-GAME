package ui.skills;

import com.sun.tools.javac.Main;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
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

        VBox skills1 = new VBox(10); skills1.setAlignment(Pos.CENTER);
        skills1.getChildren().addAll(item1,buy1);

        VBox skills2 = new VBox(10); skills2.setAlignment(Pos.CENTER);
        skills2.getChildren().addAll(item2,buy2);

        VBox skills3 = new VBox(10); skills3.setAlignment(Pos.CENTER);
        skills3.getChildren().addAll(item3,buy3);

        HBox skillsContainer = new  HBox(43);
        skillsContainer.setAlignment(Pos.CENTER_LEFT);
        skillsContainer.setPadding(new Insets(16, 44, 16, 44));
        skillsContainer.getChildren().addAll(skills1,skills2,skills3);

        VBox scrollContent = new VBox();
        scrollContent.getChildren().addAll(skillsContainer);

        ScrollPane scrollPane = new ScrollPane(scrollContent);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);

        scrollPane.setMinSize(520, 260);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        scrollPane.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            scrollPane.setHvalue(scrollPane.getHvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getWidth());
            event.consume();
        });

        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(0);
        });


        HBox downSection = new HBox(); downSection.setMinSize(520,50);
        root.getChildren().addAll(upsection,scrollPane,downSection);

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
        alert.setContentText("NOT ENOUGH COINS");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }
}
