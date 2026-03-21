package GUI;

import Classes.Battle.PvPBattleSystem;
import Classes.Heros.Hero;
import Classes.Party;
import GlobalVariables.ApplicationStates;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BattleController implements Initializable {

    @FXML private Label yourPartyNameLabel;
    @FXML private Label opponentPartyNameLabel;
    @FXML private Label yourPartyStatus;
    @FXML private Label opponentPartyStatus;
    @FXML private ListView<String> yourPartyList;
    @FXML private ListView<String> opponentPartyList;
    @FXML private TextArea battleLogArea;
    @FXML private Button startBattleButton;
    @FXML private Button nextTurnButton;
    @FXML private Button backButton;
    @FXML private Label turnIndicator;
    @FXML private VBox actionButtonsVBox;
    @FXML private Button attackButton;
    @FXML private Button defendButton;
    @FXML private Button castButton;
    @FXML private ComboBox<String> targetSelector;

    private PvPBattleSystem battleSystem;
    private Party yourParty;
    private Party opponentParty;
    private boolean battleStarted = false;
    private Hero currentActingHero;
    private List<Hero> aliveEnemies;
    private String player1Name;
    private String player2Name;
    private boolean isPlayer1Turn = true; // true = Player 1's turn, false = Player 2's turn


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("=== BattleController.initialize() START ===");

        // Get parties from ApplicationStates
        yourParty = ApplicationStates.selectedParty;
        opponentParty = ApplicationStates.opponentParty;

        if (yourParty == null || opponentParty == null) {
            System.out.println("ERROR: Parties not set in ApplicationStates");
            return;
        }
        player1Name = ApplicationStates.theUser.getUsername();
        player2Name = ApplicationStates.opponentUsername;

        System.out.println("Player 1: " + player1Name);
        System.out.println("Player 2: " + player2Name);

        System.out.println("Your party: " + yourParty.getPartyName());
        System.out.println("Opponent party: " + opponentParty.getPartyName());

        // Set party names
        yourPartyNameLabel.setText(yourParty.getPartyName());
        opponentPartyNameLabel.setText(opponentParty.getPartyName());

        // DEBUG: Check how many heroes are in each party
        System.out.println("=== PARTY HERO COUNT DEBUG ===");
        System.out.println("Your party heroes count: " + yourParty.getHeroes().size());
        if (yourParty.getHeroes().isEmpty()) {
            System.out.println("⚠️ WARNING: Your party has NO heroes!");
        } else {
            for (Hero hero : yourParty.getHeroes()) {
                System.out.println("  Your hero: " + hero.getHeroClass() + " (Lvl " + hero.getLevel() + ")");
            }
        }

        System.out.println("Opponent party heroes count: " + opponentParty.getHeroes().size());
        if (opponentParty.getHeroes().isEmpty()) {
            System.out.println("⚠️ WARNING: Opponent party has NO heroes!");
        } else {
            for (Hero hero : opponentParty.getHeroes()) {
                System.out.println("  Opponent hero: " + hero.getHeroClass() + " (Lvl " + hero.getLevel() + ")");
            }
        }
        System.out.println("=== END PARTY HERO COUNT DEBUG ===");

        // Create battle system
        battleSystem = new PvPBattleSystem(yourParty, opponentParty);

        // Display heroes in lists
        updatePartyDisplays();

        // Hide action buttons initially
        hideActionButtons();
        targetSelector.setVisible(false);

        // Disable next turn button until battle starts
        nextTurnButton.setDisable(true);
        turnIndicator.setText("Click 'Start Battle' to begin!");

        System.out.println("=== BattleController.initialize() END ===");
    }

    private void updatePartyDisplays() {
        // Clear existing items
        yourPartyList.getItems().clear();
        opponentPartyList.getItems().clear();

        // Add your heroes
        for (Hero hero : yourParty.getHeroes()) {
            String status = getHeroStatusString(hero);
            yourPartyList.getItems().add(hero.getHeroClass() + " (Lvl " + hero.getLevel() + ") " + status);
        }

        // Add opponent heroes
        for (Hero hero : opponentParty.getHeroes()) {
            String status = getHeroStatusString(hero);
            opponentPartyList.getItems().add(hero.getHeroClass() + " (Lvl " + hero.getLevel() + ") " + status);
        }

        // Update status labels
        long yourAlive = yourParty.getHeroes().stream().filter(h -> !h.isDefeated()).count();
        long opponentAlive = opponentParty.getHeroes().stream().filter(h -> !h.isDefeated()).count();

        yourPartyStatus.setText(yourAlive + "/" + yourParty.getHeroes().size() + " heroes standing");
        opponentPartyStatus.setText(opponentAlive + "/" + opponentParty.getHeroes().size() + " heroes standing");
    }

    private String getHeroStatusString(Hero hero) {
        if (hero.isDefeated()) {
            return "💀 DEFEATED";
        }

        String hpBar = getHealthBar(hero.getHp(), hero.getMaxHp());
        String manaBar = getManaBar(hero.getMana(), hero.getMaxMana());

        String status = hero.isStunned() ? "⚡ STUNNED" : "✓ READY";
        if (hero.isDefending()) {
            status = "🛡️ DEFENDING";
        }

        return String.format("%s HP: %d/%d %s | MP: %d/%d %s",
                status, hero.getHp(), hero.getMaxHp(), hpBar,
                hero.getMana(), hero.getMaxMana(), manaBar);
    }

    private String getHealthBar(int current, int max) {
        int percent = (current * 10) / max;
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < percent) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }

    private String getManaBar(int current, int max) {
        int percent = (current * 10) / max;
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < percent) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }

    private void hideActionButtons() {
        actionButtonsVBox.setVisible(false);
        targetSelector.setVisible(false);
    }

    private void showActionButtonsForHero(Hero hero) {
        System.out.println("showActionButtonsForHero called with hero: " + (hero == null ? "null" : hero.getHeroClass()));

        System.out.println("=== showActionButtonsForHero START ===");
        System.out.println("Hero: " + (hero == null ? "null" : hero.getHeroClass()));
        System.out.println("actionButtonsVBox is null? " + (actionButtonsVBox == null));

        if (actionButtonsVBox == null) {
            System.out.println("ERROR: actionButtonsVBox is NULL! Cannot show buttons.");
            return;
        }

        currentActingHero = hero;

        // Make the action buttons VBox visible
        System.out.println("Setting actionButtonsVBox visible to true");
        actionButtonsVBox.setVisible(true);
        actionButtonsVBox.setManaged(true); // Add this line - important!
        System.out.println("actionButtonsVBox visible: " + actionButtonsVBox.isVisible());

        // Check targetSelector
        System.out.println("targetSelector is null? " + (targetSelector == null));

        // Update target selector with alive enemies
        targetSelector.getItems().clear();
        aliveEnemies = battleSystem.getAliveEnemies(hero);
        System.out.println("Alive enemies found: " + (aliveEnemies == null ? 0 : aliveEnemies.size()));

        if (aliveEnemies != null) {
            for (Hero enemy : aliveEnemies) {
                targetSelector.getItems().add(enemy.getHeroClass() + " (Lvl " + enemy.getLevel() + ")");
            }
        }

        if (!aliveEnemies.isEmpty()) {
            targetSelector.getSelectionModel().selectFirst();
            System.out.println("Selected first enemy: " + aliveEnemies.get(0).getHeroClass());
        }
        targetSelector.setVisible(true);
        System.out.println("targetSelector visible: " + targetSelector.isVisible());

        // Check individual buttons
        System.out.println("attackButton is null? " + (attackButton == null));
        System.out.println("defendButton is null? " + (defendButton == null));
        System.out.println("castButton is null? " + (castButton == null));

        // Disable cast button if not enough mana
        if (!hero.canUseSpecialAbility()) {
            castButton.setDisable(true);
            castButton.setText("💫 Cast (" + hero.getSpecialAbilityCost() + " mana) - NOT ENOUGH MANA");
            System.out.println("Cast button disabled - not enough mana");
        } else {
            castButton.setDisable(false);
            castButton.setText("💫 Cast " + hero.getSpecialAbilityName() + " (" + hero.getSpecialAbilityCost() + " mana)");
            System.out.println("Cast button enabled");
        }

        // Enable all action buttons
        attackButton.setDisable(false);
        defendButton.setDisable(false);

        System.out.println("Attack button enabled: " + !attackButton.isDisable());
        System.out.println("Defend button enabled: " + !defendButton.isDisable());

        System.out.println("=== showActionButtonsForHero END ===");
    }

    @FXML
    private void startBattle(ActionEvent event) {
        System.out.println("=== Battle Started ===");

        battleStarted = true;
        startBattleButton.setDisable(true);
        nextTurnButton.setDisable(false);
        battleLogArea.clear();

        // Initialize the battle
        battleSystem.initializeBattle();

        // Player 1 starts
        isPlayer1Turn = true;
        currentActingHero = yourParty.getHeroes().get(0); // Player 1's first hero

        turnIndicator.setText("📢 " + player1Name + "'s turn! Choose an action:");
        showActionButtonsForHero(currentActingHero);

        battleLogArea.appendText("⚔️ BATTLE STARTED!\n");
        battleLogArea.appendText(player1Name + " vs " + player2Name + "\n\n");
        battleLogArea.appendText(player1Name + "'s turn. Choose an action below.\n");

        updatePartyDisplays();
    }

    @FXML
    private void attackAction(ActionEvent event) {
        System.out.println("\n=== ATTACK ACTION ===");

        System.out.println("\t*** currentActingHero=" + currentActingHero);
        // Check if we have a hero
        if (currentActingHero == null) {
            battleLogArea.appendText("Error: No active hero found!\n");
            return;
        }

        System.out.println("\t*** currentActingHero.isDefeated=" + currentActingHero.isDefeated());
        // Check if hero is defeated
        if (currentActingHero.isDefeated()) {
            battleLogArea.appendText(currentActingHero.getHeroClass() + " is defeated and cannot act!\n");
            return;
        }

        // Check if target is selected
        int selectedIndex = targetSelector.getSelectionModel().getSelectedIndex();
        System.out.println("\t*** selectedIndex=" + selectedIndex);
        System.out.println("\t*** aliveEnemies.size=" + aliveEnemies.size());
        if (selectedIndex < 0 || selectedIndex >= aliveEnemies.size()) {
            battleLogArea.appendText("Please select a target!\n");
            return;
        }

        // Get the target
        Hero target = aliveEnemies.get(selectedIndex);

        System.out.println("\t*** currentActingHero.getAttack()=" + currentActingHero.getAttack() +
                " target.getDefense()=" + target.getDefense());
        // Calculate damage
        int damage = currentActingHero.getAttack() - target.getDefense();
        System.out.println("\t*** damage=" + damage);
        if (damage < 0) damage = 1;

        // Apply damage
        target.takeDamage(damage);

        // Show what happened
        String attacker = isPlayer1Turn ? player1Name : player2Name;
        String defender = isPlayer1Turn ? player2Name : player1Name;
        battleLogArea.appendText("\n" + attacker + "'s " + currentActingHero.getHeroClass() + " attacks " + defender + "'s " + target.getHeroClass() + "!\n");
        battleLogArea.appendText("Deals " + damage + " damage!\n");
        battleLogArea.appendText(target.getHeroClass() + " HP: " + target.getHp() + "/" + target.getMaxHp() + "\n");

        // Check if target died
        if (target.isDefeated()) {
            battleLogArea.appendText("💀 " + target.getHeroClass() + " has been defeated! 💀\n");
        }

        // End the turn
        endTurn();
    }


    @FXML
    private void defendAction(ActionEvent event) {
        if (currentActingHero == null || currentActingHero.isDefeated()) {
            battleLogArea.appendText("This hero cannot act!\n");
            return;
        }

        String result = battleSystem.processDefend(currentActingHero);
        battleLogArea.appendText("\n" + result);

        // End turn
        endTurn();
    }

    @FXML
    private void castAction(ActionEvent event) {
        if (currentActingHero == null || currentActingHero.isDefeated()) {
            battleLogArea.appendText("This hero cannot act!\n");
            return;
        }

        int selectedIndex = targetSelector.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= aliveEnemies.size()) {
            battleLogArea.appendText("Please select a target!\n");
            return;
        }

        Hero target = aliveEnemies.get(selectedIndex);

        String result = battleSystem.processCast(currentActingHero, target);
        battleLogArea.appendText("\n" + result);

        // End turn
        endTurn();
    }

    private void endTurn() {
        System.out.println("=== endTurn START ===");
        System.out.println("Current player: " + (isPlayer1Turn ? player1Name : player2Name));

        // Update displays
        updatePartyDisplays();

        // Check if battle is over
        if (battleSystem.isBattleOver()) {
            endBattle();
            return;
        }

        // SWITCH PLAYERS
        isPlayer1Turn = !isPlayer1Turn;

        if (isPlayer1Turn) {
            // Player 1's turn
            currentActingHero = yourParty.getHeroes().get(0);
            turnIndicator.setText("📢 " + player1Name + "'s turn! Choose an action:");
            battleLogArea.appendText("\n--- " + player1Name + "'s turn ---\n");
        } else {
            // Player 2's turn
            currentActingHero = opponentParty.getHeroes().get(0);
            turnIndicator.setText("📢 " + player2Name + "'s turn! Choose an action:");
            battleLogArea.appendText("\n--- " + player2Name + "'s turn ---\n");
        }

        // Show action buttons for the current player
        showActionButtonsForHero(currentActingHero);

        System.out.println("Now it's " + (isPlayer1Turn ? player1Name : player2Name) + "'s turn");
        System.out.println("=== endTurn END ===");
    }

    private int turnOrderSize() {
        return yourParty.getHeroes().size() + opponentParty.getHeroes().size();
    }

    private void endBattle() {
        battleStarted = false;
        nextTurnButton.setDisable(true);
        hideActionButtons();

        battleSystem.determineWinner();
        Party winner = battleSystem.getWinningParty();

        turnIndicator.setText("🏆 " + winner.getPartyName() + " WINS! 🏆");
        battleLogArea.appendText("\n=== BATTLE OVER ===\n");
        battleLogArea.appendText(winner.getPartyName() + " defeats " +
                (winner == yourParty ? opponentParty.getPartyName() : yourParty.getPartyName()) + "!\n");

        // Save results
        battleSystem.saveResults();
        battleLogArea.appendText("✓ Match saved to database!\n");

        updatePartyDisplays();
    }

    @FXML
    private void nextTurn(ActionEvent event) {
        // This button is now just a backup - the main flow uses action buttons
        if (currentActingHero != null && !currentActingHero.isDefeated() && !currentActingHero.isStunned()) {
            battleLogArea.appendText("Please choose an action for " + currentActingHero.getHeroClass() + "!\n");
        } else {
            endTurn();
        }
    }

    @FXML
    private void backToMainMenu(ActionEvent event) {
        System.out.println("=== Returning to Main Menu ===");

        // Clear battle state
        ApplicationStates.selectedParty = null;
        ApplicationStates.opponentParty = null;
        ApplicationStates.opponentUsername = null;

        try {
            new ApplicationController().switchScene(event, "MainMenu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void testShowButtons(ActionEvent event) {
        System.out.println("=== TEST BUTTON CLICKED ===");
        System.out.println("actionButtonsVBox exists: " + (actionButtonsVBox != null));

        if (actionButtonsVBox != null) {
            actionButtonsVBox.setVisible(true);
            actionButtonsVBox.setManaged(true);
            System.out.println("Set actionButtonsVBox visible to: " + actionButtonsVBox.isVisible());
        }

        System.out.println("attackButton exists: " + (attackButton != null));
        if (attackButton != null) {
            attackButton.setDisable(false);
        }
    }
}