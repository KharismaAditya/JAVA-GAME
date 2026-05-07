package ui.battleGRIND;

import javafx.animation.PauseTransition;
import javafx.scene.Parent;
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

public class dunGrinDisplay implements Refreshable, ActivePane, potionDamageChangeTemp, CharDamageMain {

    Font font = Font.loadFont(getClass().getResourceAsStream("/font/PressStart2P.ttf"), 9);

    dunBossComp comp  = new dunBossComp();
    shopDisplay shop  = new shopDisplay(this, this, this);
    skillsDisplay skills   = new skillsDisplay(this, this);
    weaponDisplay weapons  = new weaponDisplay(this, this, this);

    // ── Handler yang dipisahkan ──────────────────────────────────────────────
    AttackHandler  attackHandler;
    DefenseHandler defenseHandler;
    // ────────────────────────────────────────────────────────────────────────

    boolean activePane   = false;
    private boolean enemyAttack = false;

    player Mainchar;
    dungeonEntityList enmList = new dungeonEntityList();
    ArrayList<entity> arrEnt  = enmList.dungeonList();

    PauseTransition pause = new PauseTransition(Duration.seconds(1));

    // UI nodes — diakses oleh handler (package-private)
    HBox      display     = new HBox();
    HBox      winOrLose   = new HBox();
    StackPane displayPane = new StackPane();

    Label defenseRNG;
    Label nameEnt;
    Label statEntHP;
    Label statEntATK;

    HBox  statdisplay = new HBox();
    Label statName;
    Label statHP;
    Label statATK;
    Label statCoin;
    Label savingFile = new Label("SAVING...");

    VBox buttonsButton = new VBox();
    HBox buttonsRow1   = new HBox();
    HBox buttonsRow2   = new HBox();

    Button row1col1 = comp.row1("ATTACK");
    Button row1col2 = comp.row1("DEFENSE");
    Button row1col3 = comp.row1("SHOP");

    Button row2col1 = comp.row1("EXIT");
    Button row2col2 = comp.row1("SKILLS");
    Button row2col3 = comp.row1("WEAPONS");

    public dunGrinDisplay(player Mainchar) {
        this.Mainchar = Mainchar;
        // Buat handler setelah field diinisialisasi
        this.attackHandler  = new AttackHandler(this, this, this);
        this.defenseHandler = new DefenseHandler(this, this, this, comp);
    }

    private int damagePotion = 0;
    private int damageChar;

    // ── ActivePane ───────────────────────────────────────────────────────────
    @Override public boolean getActivePane() { return activePane; }
    public void setActivePane(boolean activePane) {
        this.activePane = activePane;
        buttonsButton.setVisible(!activePane);
    }

    public boolean isEnemyAttack()                  { return enemyAttack; }
    public void    setEnemyAttack(boolean v)        { this.enemyAttack = v; }

    private int current;
    private int tempHP;

    // ── Scene utama ──────────────────────────────────────────────────────────
    public HBox start(Scene menuScene, Parent menuRoot) throws Exception {
        Stage stage = new Stage();
        System.out.println("Font loaded: " + font.getName());
        setCurrent(randomEnt(4));
        entity currentEnt = arrEnt.get(getCurrent());
        setTempHP(currentEnt.getEntHP());

        HBox mainroot = new HBox();
        VBox root     = new VBox();
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
        nameEnt    = new Label(arrEnt.get(getCurrent()).getEntName());
        statEntHP  = new Label("Enemy HP: " + getTempHP());
        statEntATK = new Label("Enemy ATK: " + arrEnt.get(getCurrent()).getEntAtk());
        display.getChildren().addAll(nameEnt, statEntHP, statEntATK);

        buttonsButton.setPrefSize(480, 120);

        statdisplay.setStyle("-fx-background-color: #D9D9D9");
        statdisplay.setPrefSize(480, 20);
        statdisplay.setAlignment(Pos.CENTER);
        statdisplay.setSpacing(30);

        statName  = new Label(Mainchar.getName());
        statHP    = new Label("HP: " + Mainchar.getCharHP());
        statATK   = new Label("WEAPON: " + weaponType());
        statCoin  = new Label("Coin: " + Mainchar.getCharCoin());
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

        // ── Button listeners ────────────────────────────────────────────────
        row1col1.setOnMouseClicked(e -> attackHandler.execute(arrEnt, pause, shop));
        row1col2.setOnMouseClicked(e -> defenseHandler.execute(arrEnt, pause));

        row1col3.setOnMouseClicked(e -> {
            if (!getActivePane()) {
                setActivePane(true);
                menuScene.setRoot(shop.SHOP(Mainchar, menuScene, mainroot));
            }
        });

        row2col3.setOnMouseClicked(e -> {
            if (!getActivePane()) {
                setActivePane(true);
                menuScene.setRoot(weapons.WEAPON(Mainchar, menuScene, mainroot));
            }
        });

        row2col1.setOnMouseClicked(e -> {
            saveloadSystem save = new saveloadSystem();
            mainroot.getChildren().clear();
            mainroot.setAlignment(Pos.CENTER);
            mainroot.getChildren().add(savingFile);
            pause.setOnFinished(e1 -> {
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

    // ── Helpers yang masih di sini karena dipakai oleh handler ──────────────

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

    public int randomEnt(int a) {
        return new Random().nextInt(a);
    }

    public void coinWin() {
        int coinReward = new Random().nextInt(100);
        Mainchar.setCharCoin(Mainchar.getCharCoin() + coinReward);
        refreshCharStat();
    }

    @Override
    public void refreshCharStat() {
        statName.setText(Mainchar.getName());
        statHP.setText("HP: " + Mainchar.getCharHP());
        statATK.setText("WEAPON: " + weaponType());
        statCoin.setText("Coin: " + Mainchar.getCharCoin());
    }

    @Override public void refreshEntStat() {}

    public void refreshEntStat2() {
        entity currentEnt = arrEnt.get(getCurrent());
        nameEnt.setText(currentEnt.getEntName());
        statEntHP.setText("Enemy HP : " + getTempHP());
        statEntATK.setText("Enemy ATK: " + currentEnt.getEntAtk());
    }

    public String weaponType() {
        if (getDamageChar() == 0)   return "not selected";
        if (getDamageChar() == 100) return "DULL BLADE";
        if (getDamageChar() == 150) return "GREAT SWORD";
        if (getDamageChar() == 200) return "EXCALIBUR";
        return "ERROR";
    }

    // ── Interface implementations ────────────────────────────────────────────
    @Override public int  getDamageChange()      { return damagePotion; }
    @Override public void setDamageChange(int b) { this.damagePotion = b; }
    @Override public int  getDamageChar()        { return damageChar; }
    @Override public void setDamageChar(int b)   { this.damageChar = b; }

    public int  getCurrent()         { return current; }
    public void setCurrent(int v)    { this.current = v; }
    public int  getTempHP()          { return tempHP; }
    public void setTempHP(int v)     { this.tempHP = v; }
}