package Classes.PvE;

import Classes.Heros.Hero;
import GlobalVariables.ApplicationStates;
import Repository.CampaignRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class PvEController {

    @FXML private Button attackButton;
    @FXML private Button defendButton;
    @FXML private Button startButton;
    @FXML private Button nextRoomButton;

    @FXML private Label playerHealthLabel;
    @FXML private Label enemyHealthLabel;
    @FXML private Label roomLabel;
    @FXML private TextArea battleLog;

    private Campaign currentCampaign;

    private Player player;
    private Enemy currentEnemy;
    private Room[] rooms;
    private int currentRoomIndex = 0;
    private boolean nowStarted = false;

    public void loadCurrentCampaign(ActionEvent actionEvent) {
        // A
        currentRoomIndex = ApplicationStates.PvECampaign.getRoomNumberIndex();

    }

    @FXML
    private void handleStartBattle(ActionEvent event) {

        attackButton.setVisible(true);
        defendButton.setVisible(true);

        startButton.setDisable(true);

        // ApplicationStates.inPvEBattle being false means that PvE battle is being loaded. If true it means already in PvE battle.
        if (!nowStarted) {
            System.out.println("Loading in the CAMPAIGN.");
            loadCurrentCampaign(event);
        } else {
            System.out.println("Not loading in the campaign again.");
        }

         player = new Player(ApplicationStates.PvECampaign.getPlayerHero().getHeroClassString(), ApplicationStates.PvECampaign.getPlayerHero().getMaxHp(), ApplicationStates.PvECampaign.getPlayerHero().getAttack());

        Enemy zombie = new Enemy("Zombie", 50, 5);
        Enemy skeleton = new Enemy("Skeleton", 60, 6);
        Enemy spider = new Enemy("Spider", 40, 4);

        rooms = new Room[] {
                new Room(1, zombie),
                new Room(2, skeleton),
                new Room(3, spider)
        };

        // currentRoomIndex = 0;

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
    private void handleAttack(ActionEvent event) throws SQLException {
        int damage = player.attack();
        currentEnemy.takeDamage(damage);
        battleLog.appendText("Player attacks " + currentEnemy.getName() +
                " for " + damage + " damage.\n");

        enemyTurn();
        updateLabels();
        checkBattleEnd();
    }

    @FXML
    private void handleDefend(ActionEvent event) throws SQLException {
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

    private void checkBattleEnd() throws SQLException {
        if (player.getHealth() <= 0) {
            battleLog.appendText("Player defeated!\n");
            disableButtons();
            startButton.setDisable(false);
        } else if (currentEnemy.getHealth() <= 0) {
            battleLog.appendText("Enemy defeated!\n");
            disableButtons();
            nextRoomButton.setDisable(false);
            ApplicationStates.PvECampaign.getPlayerHero().setLevel(ApplicationStates.PvECampaign.getPlayerHero().getLevel() + 1);
        }
    }

    @FXML
    private void handleNextRoom(ActionEvent event) throws SQLException, IOException {

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
            saveTheCampaign(event);
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

    public void switchScene(ActionEvent event, String fileName) throws IOException {
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/" + fileName + ".fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(newRoot);
        if (Objects.equals(fileName, "BattleView")) {
            stage.setWidth(1100);
            stage.setHeight(900);
        }
        else {
            stage.setWidth(700);
            stage.setHeight(700);
        }
        stage.setResizable(false);
        stage.show();
    }

    public void backToMainMenu(ActionEvent event) throws IOException {
        switchScene(event, "MainMenu");
    }

    public void saveTheCampaign(ActionEvent event) throws IOException, SQLException {
        // CampaignRepository.getInstance().createCampaign(ApplicationStates.PvECampaign);
        System.out.println("Saving campaign, " + ApplicationStates.PvECampaign.getRoomNumberIndex() + " " + currentRoomIndex);
        ApplicationStates.PvECampaign.setRoomNumberIndex(currentRoomIndex);
        System.out.println("Saving campaign, " + ApplicationStates.PvECampaign.getRoomNumberIndex() + " " + currentRoomIndex);
        CampaignRepository.getInstance().updateCampaign(ApplicationStates.PvECampaign, ApplicationStates.PvECampaign.getCampaignName(), ApplicationStates.theUser.getId());
    }

}