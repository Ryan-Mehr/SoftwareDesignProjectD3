package Classes.PvE;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class PvEController {

    @FXML private Button attackButton;
    @FXML private Button defendButton;
    @FXML private Button startButton;
    @FXML private Button nextRoomButton;

    @FXML private Label playerHealthLabel;
    @FXML private Label enemyHealthLabel;
    @FXML private Label roomLabel;
    @FXML private TextArea battleLog;

    private Player player;
    private Enemy currentEnemy;
    private Room[] rooms;
    private int currentRoomIndex = 0;

    @FXML
    private void handleStartBattle(ActionEvent event) {

        attackButton.setVisible(true);
        defendButton.setVisible(true);

        startButton.setDisable(true);

        player = new Player("Hero", 100, 20);

        Enemy zombie = new Enemy("Zombie", 50, 10);
        Enemy skeleton = new Enemy("Skeleton", 60, 12);
        Enemy spider = new Enemy("Spider", 40, 8);

        rooms = new Room[] {
                new Room(1, zombie),
                new Room(2, skeleton),
                new Room(3, spider)
        };

        currentRoomIndex = 0;

        battleLog.clear();

        loadRoom();

        attackButton.setDisable(false);
        defendButton.setDisable(false);
        nextRoomButton.setDisable(true);

        battleLog.appendText("Battle started!\n");
    }

    @FXML
    private void handleRestart(ActionEvent event) {
        currentRoomIndex = 0;

        player.setHealth(100);
        loadRoom();

        attackButton.setDisable(false);
        defendButton.setDisable(false);
        nextRoomButton.setDisable(true);

        nextRoomButton.setText("Next Room");

        battleLog.clear();
        battleLog.appendText("Campaign Restarted!\n");
    }

    @FXML
    private void handleAttack(ActionEvent event) {
        int damage = player.attack();
        currentEnemy.takeDamage(damage);
        battleLog.appendText("Player attacks " + currentEnemy.getName() +
                " for " + damage + " damage.\n");

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

    private void updateLabels() {
        playerHealthLabel.setText(String.valueOf(player.getHealth()));
        enemyHealthLabel.setText(String.valueOf(currentEnemy.getHealth()));
        roomLabel.setText("Room " + rooms[currentRoomIndex].getRoomNumber() +
                ": " + currentEnemy.getName());
    }

    private void checkBattleEnd() {
        if (player.getHealth() <= 0) {
            battleLog.appendText("Player defeated!\n");
            disableButtons();
            startButton.setDisable(false);
        } else if (currentEnemy.getHealth() <= 0) {
            battleLog.appendText("Enemy defeated!\n");
            disableButtons();
            nextRoomButton.setDisable(false);
        }
    }

    @FXML
    private void handleNextRoom(ActionEvent event) {

        currentRoomIndex++;
        if (currentRoomIndex < rooms.length) {
            loadRoom();
            attackButton.setVisible(true);
            defendButton.setVisible(true);
            attackButton.setDisable(false);
            defendButton.setDisable(false);

            nextRoomButton.setDisable(true);
        } else {
            battleLog.appendText("\nCampaign Complete!\n");
            attackButton.setVisible(false);
            defendButton.setVisible(false);
            nextRoomButton.setDisable(true);
            startButton.setDisable(false);
        }
    }

    private void loadRoom() {
        currentEnemy = rooms[currentRoomIndex].getEnemy();
        battleLog.appendText("\nEntering Room " + rooms[currentRoomIndex].getRoomNumber() +
                " - Enemy: " + currentEnemy.getName() + "\n");
        updateLabels();
    }

    private void disableButtons() {
        attackButton.setDisable(true);
        defendButton.setDisable(true);

    }

    @FXML
    private void initialize() {
        attackButton.setVisible(false);
        defendButton.setVisible(false);
        nextRoomButton.setDisable(true);
    }

}