package Classes.PvE;

import GlobalVariables.ApplicationStates;
import Repository.CampaignRepository;
import Repository.HeroRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class PvEController {

    @FXML private Button attackButton;
    @FXML private Button defendButton;
    @FXML private Button startButton;
    @FXML private Button nextRoomButton;
    @FXML private Button returnButton;

    @FXML private Label playerHealthLabel;
    @FXML private Label enemyHealthLabel;
    @FXML private Label roomLabel;
    @FXML private TextArea battleLog;

    private Player player;
    private Enemy currentEnemy;
    private Room[] rooms;
    private int currentRoomIndex = 0;
    private BattleUIManager uiManager;

    @FXML
    private void initialize() {
        // Single constructor, assign buttons and labels
        uiManager = new BattleUIManager(
                attackButton, defendButton, nextRoomButton,
                playerHealthLabel, enemyHealthLabel, roomLabel
        );

        uiManager.disableAllButtons();
        uiManager.hideAllButtons();
        returnButton.setVisible(true);
    }

    @FXML
    private void handleStartBattle(ActionEvent event) {
        initializePlayer();
        initializeRooms();
        startRoom();
        battleLog.appendText("Battle started!\n");
        startButton.setDisable(true);
    }

    private void initializePlayer() {
        player = new Player("Hero", 100, 20);
    }

    private void initializeRooms() {
        Enemy zombie = new Enemy("Zombie", 50, 10);
        Enemy skeleton = new Enemy("Skeleton", 60, 12);
        Enemy spider = new Enemy("Spider", 40, 8);

        rooms = new Room[] {
                new Room(1, zombie),
                new Room(2, skeleton),
                new Room(3, spider)
        };
        currentRoomIndex = ApplicationStates.PvECampaign.getRoomNumberIndex();
    }

    private void startRoom() {
        battleLog.clear();
        loadRoom();
        uiManager.showAllButtons();
        uiManager.enableAllButtons();
    }

    private void updateLabels() {
        PlayerData playerData = player.getData();
        EnemyData enemyData = currentEnemy.getData();
        RoomData roomData = rooms[currentRoomIndex].getData();

        uiManager.updateLabels(playerData, enemyData, roomData);
    }

    @FXML
    private void handleAttack(ActionEvent event) {
        int damage = player.attack();
        currentEnemy.takeDamage(damage);
        battleLog.appendText("Player attacks " + currentEnemy.getName() + " for " + damage + " damage.\n");

        enemyTurn();
        updateLabels();
        checkBattleEnd();
    }

    @FXML
    private void handleDefend(ActionEvent event) {
        player.defend();
        battleLog.appendText("Player defends!\n");

        enemyTurn();
        updateLabels();
        checkBattleEnd();
    }

    private void enemyTurn() {
        if (currentEnemy.getHealth() > 0) {
            int damage = currentEnemy.attack();
            player.takeDamage(damage);
            battleLog.appendText("Enemy attacks for " + damage + " damage.\n");
        }
    }

    private void checkBattleEnd() {
        if (player.getHealth() <= 0) {
            battleLog.appendText("Player defeated!\n");
            uiManager.disableAllButtons();
            startButton.setDisable(false);
        } else if (currentEnemy.getHealth() <= 0) {
            battleLog.appendText("Enemy defeated!\n");
            uiManager.disableAllButtons();
            nextRoomButton.setDisable(false); // enable next room only when enemy dead
        }
    }

    @FXML
    private void handleNextRoom(ActionEvent event) {
        currentRoomIndex++;
        if (currentRoomIndex < rooms.length) {
            loadRoom();
            uiManager.showAllButtons();
            uiManager.enableAllButtons();
            nextRoomButton.setDisable(true); // wait for enemy defeat
        } else {
            battleLog.appendText("\nCampaign Complete!\n");
            uiManager.hideAllButtons();
            nextRoomButton.setDisable(true);
            startButton.setDisable(false);
            // returnButton.setVisible(true);
        }
        ApplicationStates.PvECampaign.getPlayerHero().setLevel(ApplicationStates.PvECampaign.getPlayerHero().getLevel() + 1);
    }

    private void loadRoom() {
        currentEnemy = rooms[currentRoomIndex].getEnemy();
        battleLog.appendText("\nEntering Room " + rooms[currentRoomIndex].getRoomNumber() +
                " - Enemy: " + currentEnemy.getName() + "\n");
        updateLabels();
    }

    @FXML
    private void handleReturnToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/MainMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading Main Menu: " + e.getMessage());
        }
    }

    // --- BattleUIManager ---
    private class BattleUIManager {
        private Button attackButton;
        private Button defendButton;
        private Button nextRoomButton;
        private Label playerHealthLabel;
        private Label enemyHealthLabel;
        private Label roomLabel;

        public BattleUIManager(Button attack, Button defend, Button nextRoom,
                               Label playerLabel, Label enemyLabel, Label roomLabel) {
            this.attackButton = attack;
            this.defendButton = defend;
            this.nextRoomButton = nextRoom;

            this.playerHealthLabel = playerLabel;
            this.enemyHealthLabel = enemyLabel;
            this.roomLabel = roomLabel;
        }

        public void disableAllButtons() {
            attackButton.setDisable(true);
            defendButton.setDisable(true);
            nextRoomButton.setDisable(true);
        }

        public void enableAllButtons() {
            attackButton.setDisable(false);
            defendButton.setDisable(false);
        }

        public void showAllButtons() {
            attackButton.setVisible(true);
            defendButton.setVisible(true);
            nextRoomButton.setVisible(true);
        }

        public void hideAllButtons() {
            attackButton.setVisible(false);
            defendButton.setVisible(false);
            nextRoomButton.setVisible(false);
        }

        public void updateLabels(PlayerData playerData, EnemyData enemyData, RoomData roomData) {
            playerHealthLabel.setText(String.valueOf(playerData.getHealth()));
            enemyHealthLabel.setText(String.valueOf(enemyData.getHealth()));
            roomLabel.setText("Room " + roomData.getRoomNumber() + ": " + enemyData.getName());
        }
    }

    public void saveTheCampaign(ActionEvent event) throws IOException, SQLException {
        // CampaignRepository.getInstance().createCampaign(ApplicationStates.PvECampaign);
        System.out.println("Saving campaign, " + ApplicationStates.PvECampaign.getRoomNumberIndex() + " " + currentRoomIndex);
        ApplicationStates.PvECampaign.setRoomNumberIndex(currentRoomIndex);
        System.out.println("Saving campaign, " + ApplicationStates.PvECampaign.getRoomNumberIndex() + " " + currentRoomIndex);
        CampaignRepository.getInstance().updateCampaign(ApplicationStates.PvECampaign, ApplicationStates.PvECampaign.getCampaignName(), ApplicationStates.theUser.getId());
        HeroRepository.getInstance().updateHero(ApplicationStates.PvECampaign.getPlayerHero(), ApplicationStates.PvECampaign.getUserID());
    }
}