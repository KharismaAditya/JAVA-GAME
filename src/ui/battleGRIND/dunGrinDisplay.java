package ui.battleGRIND;
import javafx.animation.PauseTransition;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import method.*;
import model.*;
import ui.battleBOSS.dunBossComp;
import ui.shop.*;
import save_and_load.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import ui.skills.skillsDisplay;
import ui.weapon.weaponDisplay;

import java.util.ArrayList;
import java.util.Random;

public class dunGrinDisplay implements Refreshable, ActivePane, potionDamageChangeTemp, CharDamageMain{
    Font font = Font.loadFont(getClass().getResourceAsStream("/font/PressStart2P.ttf"), 9);

    dunBossComp comp = new dunBossComp();
    shopDisplay shop = new shopDisplay(this, this, this);
    skillsDisplay skills = new skillsDisplay(this, this);
    weaponDisplay weapons = new weaponDisplay(this,this,this);

    boolean activePane = false;
    private boolean enemyAttack = false;

    player Mainchar;   // ✅ player dari ngDisplay
    //if bossDungeon active
    dungeonEntityList enmList = new dungeonEntityList();
    ArrayList<entity> arrEnt = enmList.dungeonList();

    PauseTransition pause = new PauseTransition(Duration.seconds(1));

    HBox display = new HBox();
    HBox winOrLose = new HBox();
    StackPane displayPane = new StackPane();

    Label defenseRNG;
    Label nameEnt;
    Label statEntHP;
    Label statEntATK;

    HBox statdisplay = new HBox();
    Label statName;
    Label statHP;
    Label statATK;
    Label statCoin;
    Label savingFile = new Label("SAVING...");

    VBox buttonsButton = new VBox();
    HBox buttonsRow1 = new HBox();
    HBox buttonsRow2 = new HBox();

    Button row1col1 = comp.row1("ATTACK");
    Button row1col2 = comp.row1("DEFENSE");
    Button row1col3 = comp.row1("SHOP");

    Button row2col1 = comp.row1("EXIT");
    Button row2col2 = comp.row1("SKILLS");
    Button row2col3 = comp.row1("WEAPONS");

    // ✅ Constructor menerima player
    public dunGrinDisplay(player Mainchar) {
        this.Mainchar = Mainchar;
    }
    private int damagePotion = 0;
    private int damageChar;


    @Override
    public boolean getActivePane() { return activePane; }
    public void setActivePane(boolean activePane) {
        this.activePane = activePane;
        buttonsButton.setVisible(!activePane);
    }

    public boolean isEnemyAttack() { return enemyAttack; }
    public void setEnemyAttack(boolean enemyAttack) { this.enemyAttack = enemyAttack; }

    private int current;
    private int tempHP;

    public HBox start(Scene menuScene, Parent menuRoot) throws Exception {
        Stage stage = new Stage();
        System.out.println("Font loaded: " + font.getName());
        setCurrent(randomEnt(3));
        entity currentEnt = arrEnt.get(getCurrent());
        setTempHP(currentEnt.getEntHP());

        HBox mainroot = new HBox();
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(15, 20, 5, 20));
        root.setStyle("-fx-background-color: #FFFFFF");

        display.setStyle("-fx-background-color: #D9D9D9");
        display.setMinSize(480, 200);

        winOrLose.setMinSize(480, 200);
        winOrLose.setAlignment(Pos.CENTER);
        defenseRNG = new Label("");
        winOrLose.getChildren().add(defenseRNG);

        displayPane.setPrefSize(480, 200);
        displayPane.getChildren().addAll(display, winOrLose);

        display.setAlignment(Pos.BOTTOM_CENTER);
        display.setSpacing(30);
        nameEnt = new Label(arrEnt.get(getCurrent()).getEntName());
        statEntHP = new Label("Enemy HP: " + getTempHP());
        statEntATK = new Label("Enemy ATK: " + arrEnt.get(getCurrent()).getEntAtk());
        display.getChildren().addAll(nameEnt, statEntHP, statEntATK);

        buttonsButton.setPrefSize(480, 120);

        statdisplay.setStyle("-fx-background-color: #D9D9D9");
        statdisplay.setPrefSize(480, 20);
        statdisplay.setAlignment(Pos.CENTER);
        statdisplay.setSpacing(30);

        statName = new Label(Mainchar.getName());
        statHP = new Label("HP: " + Mainchar.getCharHP());
        statATK = new Label("WEAPON: " + weaponType());
        statCoin = new Label("Coin: " + Mainchar.getCharCoin());
        statdisplay.getChildren().addAll(statName, statHP, statATK, statCoin);

        buttonsRow1.setMinSize(480, 60);
        buttonsRow1.setAlignment(Pos.TOP_CENTER);
        buttonsRow1.setPadding(new Insets(18, 0, 8, 0));
        buttonsRow1.setSpacing(30);
        buttonsRow2.setMinSize(480, 60);
        buttonsRow2.setAlignment(Pos.TOP_CENTER);
        buttonsRow2.setPadding(new Insets(8, 0, 8, 0));
        buttonsRow2.setSpacing(30);

        buttonsRow1.getChildren().addAll(row1col1, row1col2, row1col3);
        buttonsRow2.getChildren().addAll(row2col1, row2col2, row2col3);
        buttonsButton.getChildren().addAll(buttonsRow1, buttonsRow2);

        root.getChildren().addAll(displayPane, buttonsButton, statdisplay);
        mainroot.getChildren().add(root);

        mainroot.getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );

        row1col1.setOnMouseClicked(e -> { attackScene(); });
        row1col2.setOnMouseClicked(e -> { defenseScene(); });
        row1col3.setOnMouseClicked(e -> {
            if (!getActivePane()) {
                setActivePane(true);
                menuScene.setRoot(shop.SHOP(Mainchar,menuScene,mainroot));
            }
        });

        row2col3.setOnMouseClicked(e -> {
            if (!getActivePane()) {
                setActivePane(true);
                menuScene.setRoot(weapons.WEAPON(Mainchar,menuScene,mainroot));
            }
        });

        row2col1.setOnMouseClicked(e -> {
            saveloadSystem save = new saveloadSystem();
            mainroot.getChildren().clear();
            mainroot.setAlignment(Pos.CENTER);
            mainroot.getChildren().add(savingFile);
            pause.setOnFinished(e1 ->{
                save.addPlayer(Mainchar);
                menuScene.setRoot(menuRoot);
            });

            pause.play();
        });


        row2col2.setOnMouseClicked(e -> {
            if (!getActivePane()) {
                setActivePane(true);
                menuScene.setRoot(skills.SKILLS(Mainchar, currentEnt, menuScene, mainroot));
            }
        });


        stage.setResizable(false);
        return mainroot;
    }


    public void entAttackScene(entity currentEnt) {
        if (isEnemyAttack()) {
            Mainchar.setCharHP(Mainchar.getCharHP() - currentEnt.getEntAtk());
            refreshCharStat();

            statdisplay.getChildren().remove(statName);
            statHP.setText("");
            statATK.setText("ENEMY ATTACKING");
            statCoin.setText("");
            nameEnt.setText(currentEnt.getEntName());
            statEntHP.setText("Enemy HP: " + currentEnt.getEntHP());
            statEntATK.setText("Enemy ATK: " + currentEnt.getEntAtk());

            pause.setOnFinished(e -> {
                statdisplay.getChildren().add(0, statName);
                refreshCharStat();
                refreshEntStat();
            });
            pause.play();
            setEnemyAttack(false);
        }
    }

    public void transition(){
        nameEnt.setText("...");statEntHP.setText("STAGE CLEAR"); statEntATK.setText("...");
        pause.setOnFinished(e->{
            refreshEntStat2();
            refreshCharStat();
        });
        pause.play();
    }


    public int randomEnt(int a){
        Random rand = new Random();
        int setEny = rand.nextInt(a);
        return setEny;
    }

    public void coinWin(){
        Random rand = new Random();
        int coinReward = rand.nextInt(100);
        Mainchar.setCharCoin(Mainchar.getCharCoin() + coinReward);
        refreshCharStat();
    }

    @Override
    public void refreshCharStat(){
        statName.setText(Mainchar.getName());
        statHP.setText("HP: " + Mainchar.getCharHP());
        statATK.setText("WEAPON: " + weaponType());
        statCoin.setText("Coin: " + Mainchar.getCharCoin());
    }

    @Override
    public void refreshEntStat(){}

    public void refreshEntStat2(){
        entity currentEnt = arrEnt.get(getCurrent());
        nameEnt.setText(currentEnt.getEntName());
        statEntHP.setText("Enemy HP : " + getTempHP());
        statEntATK.setText("Enemy ATK: " + currentEnt.getEntAtk());
    }

    public void attackScene(){
        entity currentEnt = arrEnt.get(getCurrent());
        if(getDamageChar() != 0){
            int mainDamage = getDamageChar() + getDamageChange();
            setTempHP(getTempHP() - mainDamage);
            setEnemyAttack(true);
            entAttackScene(arrEnt.get(getCurrent()));

            if (shop.isPotionActive()) {
                shop.increaseAttackCount(mainDamage);
            }

            if(getTempHP() <= 0){
                transition();coinWin();
                setCurrent(randomEnt(3));setTempHP(currentEnt.getEntHP());
                refreshEntStat2();refreshCharStat();
            }
        }else{
            alertWeapon();
        }
    }

    public void defenseScene(){
        if(getDamageChar() != 0){
            displayPane.getChildren().clear();
            buttonsButton.getChildren().clear();

            Random rand = new Random();
            entity currentEnt = arrEnt.get(getCurrent());

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
                        int mainDamage = getDamageChar() + getDamageChange();
                        setTempHP(getTempHP() - mainDamage);

                        if(getTempHP() <= 0){
                            transition();
                            coinWin();
                            setCurrent(randomEnt(3));setTempHP(currentEnt.getEntHP());
                            refreshEntStat2();refreshCharStat();
                        } else {
                            refreshEntStat2();refreshCharStat();
                            pause.setOnFinished(e2 -> defenseRNG.setVisible(false));
                            pause.play();
                        }
                    } else {
                        defenseRNG.setText("YOU LOSE");
                        setEnemyAttack(true);
                        refreshCharStat();

                        pause.setOnFinished(e2 -> {
                            defenseRNG.setVisible(false);
                            entAttackScene(currentEnt);
                        });
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
        }else{
            alertWeapon();
        }

    }

    public void alertWeapon(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setHeaderText(null);
        alert.setContentText("SELECT WEAPON FIRST");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }

    public String weaponType(){
        if(getDamageChar() == 0){return "not selected";}
        if(getDamageChar() == 100){return "DULL BLADE";}
        if(getDamageChar() == 150){return "GREAT SWORD";}
        if(getDamageChar() == 200){return "EXCALIBUR";}
        return "ERROR";
    }

    @Override
    public int getDamageChange() {
        return damagePotion;
    }

    @Override
    public void setDamageChange(int b) {
        this.damagePotion = b;
    }

    @Override
    public int getDamageChar() {
        return damageChar;
    }

    @Override
    public void setDamageChar(int b) {
        this.damageChar = b;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTempHP() {
        return tempHP;
    }

    public void setTempHP(int tempHP) {
        this.tempHP = tempHP;
    }
}
