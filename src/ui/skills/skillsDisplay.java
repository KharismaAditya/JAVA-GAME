package ui.skills;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import method.ActivePane;
import method.GamePopup;
import method.Refreshable;
import method.playerSkills;
import model.entity;
import model.player;

public class skillsDisplay {
    private final Refreshable refresh;
    private final ActivePane activePane;
    private final skillsComp comp = new skillsComp();
    private final playerSkills skillsLogic = new playerSkills();

    Label statCoin;
    Label statHP;

    public skillsDisplay(Refreshable refresh, ActivePane activePane) {
        this.refresh = refresh;
        this.activePane = activePane;
    }

    public VBox SKILLS(player mainChar, entity enemy, Scene mainscene, Parent mainroot) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.setMinSize(520, 360);

        Button exit = comp.ExitButton();
        HBox upsection = new HBox();
        upsection.setMinSize(520, 50);
        upsection.setAlignment(Pos.CENTER_RIGHT);
        upsection.setPadding(new Insets(10, 14, 10, 14));
        upsection.getChildren().add(exit);

        // Container scroll skill card
        HBox skillsContainer = new HBox(25);
        skillsContainer.setAlignment(Pos.CENTER_LEFT);
        skillsContainer.setPadding(new Insets(16, 25, 16, 25));

        // Buat kartu untuk setiap skill yang ada di playerSkills
        int skillCount = skillsLogic.getSkillCount();
        for (int i = 0; i < skillCount; i++) {
            final int skillId = i;
            String name = skillsLogic.getSkillName(skillId);
            String category = skillsLogic.getSkillCategory(skillId);
            int coinCost = skillsLogic.getCoinCost(skillId);

            VBox card = comp.skillCard(category, name, coinCost);
            Button useBtn = comp.useButton();

            useBtn.setOnAction(e -> {
                int hpCost = skillsLogic.getHpCost(skillId, mainChar);
                String effectDesc = skillsLogic.getSkillEffectDescription(skillId);

                // Tampilkan Popup Konfirmasi Penggunaan Skill JavaFX
                comp.showSkillConfirmationPopup(
                        null,
                        name,
                        category,
                        coinCost,
                        hpCost,
                        effectDesc,
                        mainChar.getCharCoin(),
                        mainChar.getCharHP(),
                        () -> handleSkillExecution(skillId, mainChar, enemy)
                );
            });

            VBox skillColumn = new VBox(8);
            skillColumn.setAlignment(Pos.CENTER);
            skillColumn.getChildren().addAll(card, useBtn);

            skillsContainer.getChildren().add(skillColumn);
        }

        VBox scrollContent = new VBox(skillsContainer);
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

        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(0));

        HBox downSection = new HBox();
        downSection.setMinSize(520, 50);
        downSection.setAlignment(Pos.CENTER);
        downSection.setSpacing(10);

        statHP = new Label("HP: " + mainChar.getCharHP());
        statCoin = new Label("Coin: " + mainChar.getCharCoin());
        downSection.getChildren().addAll(statHP,statCoin);
        root.getChildren().addAll(upsection, scrollPane, downSection);

        exit.setOnMouseClicked(e -> {
            mainscene.setRoot(mainroot);
            activePane.setActivePane(false);
        });

        try {
            root.getStylesheets().add(getClass().getResource("/font/styles.css").toExternalForm());
        } catch (Exception ignored) {}

        return root;
    }

    private void handleSkillExecution(int skillId, player mainChar, entity enemy) {
        if (!skillsLogic.canUseSkill(skillId, mainChar)) {
            String reason = skillsLogic.getCannotUseReason(skillId, mainChar);
            GamePopup.showMessage("FAILED TO USE SKILL", reason);
            return;
        }

        boolean success = skillsLogic.executeSkill(skillId, mainChar, enemy);
        if (success) {
            refresh.refreshCharStat();
            refresh.refreshEntStat();
            statHP.setText("HP: " + mainChar.getCharHP()); statCoin.setText("Coin: " + mainChar.getCharCoin() );
            GamePopup.showMessage("SKILL ACTIVATED!", skillsLogic.getSkillName(skillId) + " was used successfully!");
        }
    }
}
