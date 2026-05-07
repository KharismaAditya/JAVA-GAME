package ui.battleBOSS;

import javafx.animation.PauseTransition;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import method.*;
import model.*;
import ui.dialogSCENE.dialogDisplay;
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
import ui.weapon.weaponReader;

import java.util.ArrayList;
import java.util.Random;

public class dungeonBossDisplay implements Refreshable, ActivePane, potionDamageChangeTemp, CharDamageMain {

    Font font = Font.loadFont(getClass().getResourceAsStream("/font/PressStart2P.ttf"), 9);

    dunBossComp comp       = new dunBossComp();
    shopDisplay shop       = new shopDisplay(this, this, this);
    skillsDisplay skills   = new skillsDisplay(this, this);
    bossSkills bossSkills  = new bossSkills(this);
    weaponDisplay weapons  = new weaponDisplay(this, this, this);

    // ── Handler yang dipisahkan ──────────────────────────────────────────────
    BossAttackHandler  attackHandler;
    BossDefenseHandler defenseHandler;
    // ────────────────────────────────────────────────────────────────────────

    boolean activePane      = false;
    private boolean enemyAttack = false;

    player Mainchar;
    bossEntityList enmList    = new bossEntityList();
    ArrayList<entity> arrEnt  = enmList.bossList();

    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    private Scene  menuScene;
    private Parent menuRoot;
    private HBox   DisplayRoot;

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
    Label savingFile  = new Label("SAVING...");

    VBox buttonsButton = new VBox();
    HBox buttonsRow1   = new HBox();
    HBox buttonsRow2   = new HBox();

    Button row1col1 = comp.row1("ATTACK");
    Button row1col2 = comp.row1("DEFENSE");
    Button row1col3 = comp.row1("SHOP");

    Button row2col1 = comp.row1("EXIT");
    Button row2col2 = comp.row1("SKILLS");
    Button row2col3 = comp.row1("WEAPONS");

    Label winBosses = new Label("... ENTERING NEW ROOM ...");

    public dungeonBossDisplay(player Mainchar) {
        this.Mainchar = Mainchar;
        this.attackHandler  = new BossAttackHandler(this, this, this);
        this.defenseHandler = new BossDefenseHandler(this, this, this, comp);
    }

    private int damagePotion = 0;
    private int damageChar;

    // ── ActivePane ───────────────────────────────────────────────────────────
    @Override public boolean getActivePane() { return activePane; }
    public void setActivePane(boolean activePane) {
        this.activePane = activePane;
        buttonsButton.setVisible(!activePane);
    }

    public boolean isEnemyAttack()           { return enemyAttack; }
    public void    setEnemyAttack(boolean v) { this.enemyAttack = v; }

    // ── Scene utama ──────────────────────────────────────────────────────────
    public HBox start(Scene menuScene, Parent menuRoot) throws Exception {
        this.menuScene  = menuScene;
        this.menuRoot   = menuRoot;
        Stage stage     = new Stage();
        System.out.println("Font loaded: " + font.getName());
        entity currentEnt = arrEnt.get(count());

        HBox mainroot     = new HBox();
        this.DisplayRoot  = mainroot;
        VBox root         = new VBox();
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
        nameEnt    = new Label(arrEnt.get(count()).getEntName());
        statEntHP  = new Label("Enemy HP: " + arrEnt.get(count()).getEntHP());
        statEntATK = new Label("Enemy ATK: " + arrEnt.get(count()).getEntAtk());
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
        row1col1.setOnMouseClicked(e -> dialogTransition1(menuScene, menuRoot, mainroot));
        row1col2.setOnMouseClicked(e -> dialogTransition2(menuScene, menuRoot, mainroot));

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

    // ── Dialog transitions (wrapper dengan weapon check) ─────────────────────

    public void dialogTransition1(Scene menuscene, Parent mainroot, HBox DisplayRoot) {
        if (damageChar != 0) {
            attackHandler.execute(arrEnt, pause, shop, () -> {
                DisplayRoot.getChildren().clear();
                DisplayRoot.getChildren().add(winBosses);
                DisplayRoot.setAlignment(Pos.CENTER);

                PauseTransition enterRoom = new PauseTransition(Duration.seconds(1.5));
                enterRoom.setOnFinished(e -> {
                    winningCondition();
                    dialogDisplay dialog = new dialogDisplay();
                    try {
                        Parent dialogRoot = dialog.DIALOG(Mainchar, menuscene, mainroot);
                        menuscene.setRoot(dialogRoot);
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                });
                enterRoom.play();
            });
        } else {
            alertWeapon();
        }
    }

    public void dialogTransition2(Scene menuscene, Parent mainroot, HBox DisplayRoot) {
        if (damageChar != 0) {
            defenseHandler.execute(arrEnt, pause, () -> {
                DisplayRoot.getChildren().clear();
                DisplayRoot.getChildren().add(winBosses);
                DisplayRoot.setAlignment(Pos.CENTER);

                PauseTransition enterRoom = new PauseTransition(Duration.seconds(1.5));
                enterRoom.setOnFinished(e -> {
                    winningCondition();
                    dialogDisplay dialog = new dialogDisplay();
                    try {
                        Parent dialogRoot = dialog.DIALOG(Mainchar, menuscene, mainroot);
                        menuscene.setRoot(dialogRoot);
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                });
                enterRoom.play();
            });
        } else {
            alertWeapon();
        }
    }

    // ── Logic yang masih di sini karena dipakai oleh handler ─────────────────

    public void entAttackScene(entity currentEnt) {
        if (isEnemyAttack()) {
            Mainchar.setCharHP(Mainchar.getCharHP() - currentEnt.getEntAtk());
            refreshCharStat();

            if (Mainchar.getCharHP() <= 0) {
                playerDefeat(menuScene, menuRoot, DisplayRoot);
                return;
            }

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
                entSkillScene(currentEnt);
            });
            pause.play();
            setEnemyAttack(false);
        }
    }

    public void entSkillScene(entity currentEnt) {
        Random rand       = new Random();
        int skillCount    = rand.nextInt(3);
        System.out.println("skillCount: " + skillCount);

        if (skillCount == 2) {
            bossSkills.skillList(this, Mainchar, currentEnt);

            statdisplay.getChildren().remove(statName);
            statHP.setText("");
            statATK.setText(bossSkills.bossSkillName(count()));
            statCoin.setText("");
            nameEnt.setText(currentEnt.getEntName());
            statEntHP.setText("Enemy HP: " + currentEnt.getEntHP());
            statEntATK.setText("Enemy ATK: " + currentEnt.getEntAtk());

            pause.setOnFinished(e -> {
                statdisplay.getChildren().add(0, statName);
                refreshCharStat();
                if (Mainchar.getCharHP() <= 0) {
                    playerDefeat(menuScene, menuRoot, DisplayRoot);
                    return;
                }
                refreshEntStat();
                statATK.setText("ATK: " + getDamageChar());
            });
            pause.play();
        }
    }

    public int count() {
        return Mainchar.getEnemyCount();
    }

    public void winningCondition() {
        Mainchar.setEnemyCount(Mainchar.getEnemyCount() + 1);
        System.out.println(Mainchar.getEnemyCount());
        refreshEntStat();
    }

    public void coinWin() {
        int coinReward = new Random().nextInt(100);
        Mainchar.setCharCoin(Mainchar.getCharCoin() + coinReward);
        refreshCharStat();
    }

    public void playerDefeat(Scene menuScene, Parent menuRoot, HBox DisplayRoot) {
        if (Mainchar.getCharHP() <= 0) {
            Mainchar.setCharHP(0);
            refreshCharStat();

            Label defeatLabel = new Label("YOU DIED...");
            defeatLabel.setFont(font);
            defeatLabel.setStyle("-fx-text-fill: red;");

            DisplayRoot.getChildren().clear();
            DisplayRoot.setAlignment(Pos.CENTER);
            DisplayRoot.getChildren().add(defeatLabel);
            buttonsButton.setVisible(false);

            PauseTransition defeatPause = new PauseTransition(Duration.seconds(2));
            defeatPause.setOnFinished(e -> {
                if (Mainchar.getEnemyCount() > 0) {
                    Mainchar.setEnemyCount(Mainchar.getEnemyCount() - 1);
                }
                arrEnt.get(Mainchar.getEnemyCount()).setEntHP(
                        new bossEntityList().bossList().get(Mainchar.getEnemyCount()).getEntHP()
                );
                menuScene.setRoot(menuRoot);
            });
            defeatPause.play();
        }
    }

    @Override
    public void refreshCharStat() {
        statName.setText(Mainchar.getName());
        statHP.setText("HP: " + Mainchar.getCharHP());
        statATK.setText("WEAPON: " + weaponType());
        statCoin.setText("Coin: " + Mainchar.getCharCoin());
    }

    @Override
    public void refreshEntStat() {
        entity currentEnt = arrEnt.get(count());
        nameEnt.setText(currentEnt.getEntName());
        statEntHP.setText("Enemy HP : " + currentEnt.getEntHP());
        statEntATK.setText("Enemy ATK: " + arrEnt.get(count()).getEntAtk());
    }

    public void alertWeapon() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("SELECT WEAPON FIRST");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/font/styles.css").toExternalForm()
        );
        alert.showAndWait();
    }

    public String weaponType() {
        weaponReader wr = new weaponReader(Mainchar);
        if (wr.thereIsWeaponOnMyFileBLyat001niggaanjingmemek()) {
            return wr.getListWeapon(getDamageChar());
        }
        return "ERROR";
    }

    // ── Interface implementations ────────────────────────────────────────────
    @Override public int  getDamageChange()      { return damagePotion; }
    @Override public void setDamageChange(int b) { this.damagePotion = b; }
    @Override public int  getDamageChar()        { return damageChar; }
    @Override public void setDamageChar(int b)   { this.damageChar = b; }
}