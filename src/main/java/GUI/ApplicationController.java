package GUI;

import Classes.Heros.Hero;
import Classes.PvE.Campaign;
import DAO.CampaignDAO;
import Factory.*;
import GlobalVariables.ApplicationConstants;
import GlobalVariables.ApplicationStates;
import Repository.CampaignRepository;
import Repository.HeroRepository;
import Repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Classes.Party;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import Classes.Battle.BattleManager;


public class ApplicationController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Label usernameLabel;

    @FXML private ChoiceBox<String> chooseHeroChoicebox;
    @FXML private ChoiceBox<String> chooseHeroClassChoicebox;
    @FXML private ChoiceBox<String> loadSavedCampaignChoiceBox;
//    @FXML private Label heroNameLabel;
    @FXML private Label heroClassLabel;
    @FXML private Label heroLevelLabel;
    @FXML private Label heroAttackLabel;
    @FXML private Label heroDefenseLabel;
    @FXML private Label heroMaxHPLabel;
    @FXML private Label heroMaxManaLabel;


    @FXML private TextField campaignNameField;

    String[] heroIDs;
    String[] heroNames;
    String[] heroClasses = {"ORDER", "CHAOS", "WARRIOR", "MAGE"};

    OrderFactory orderFactory = new OrderFactory();
    ChaosFactory chaosFactory = new ChaosFactory();
    WarriorFactory warriorFactory = new WarriorFactory();
    MageFactory mageFactory = new MageFactory();


    // Make sure Repositories are singletons.
    UserRepository userRepository = UserRepository.getInstance();
    HeroRepository heroRepository = HeroRepository.getInstance();
    CampaignRepository campaignRepository = CampaignRepository.getInstance();

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

    // Main menu methods.
    public void playGame(ActionEvent actionEvent) throws IOException {
        System.out.println("playGame");
        if (checkLoggedIn()) {
            // Switch to playGame scene.
            switchScene(actionEvent, "GameModeMenu");
        }
    }
    public void account(ActionEvent actionEvent) throws IOException {
        switchScene(actionEvent, "AccountMenu");
        System.out.println("account");
    }
    public void about(ActionEvent actionEvent) throws IOException {
        System.out.println("about");
        switchScene(actionEvent, "AboutMenu");
    }
    public void exit() {
        System.out.println("exit");
        System.exit(0);
    }
    public void openMatchHistory(ActionEvent event) throws IOException {
        System.out.println("Opening Match History...");
        switchScene(event, "MatchHistory");
    }
    public void startPvP(ActionEvent actionEvent) {
        System.out.println("Starting PvP Battle...");

        // Get the parties that were selected during party selection
        Party player1Party = ApplicationStates.selectedParty;
        Party player2Party = ApplicationStates.opponentParty;

        // Check if parties were properly selected
        if (player1Party == null) {
            System.out.println("ERROR: Player 1 party not selected!");
            return;
        }

        if (player2Party == null) {
            System.out.println("ERROR: Player 2 party not selected!");
            return;
        }

        System.out.println("Player 1: " + player1Party.getPartyName() +
                " (" + player1Party.getHeroes().size() + " heroes)");
        System.out.println("Player 2: " + player2Party.getPartyName() +
                " (" + player2Party.getHeroes().size() + " heroes)");

        // Start the battle in a new thread
        new Thread(() -> {
            BattleManager battle = new BattleManager(player1Party, player2Party);
            battle.startBattle();
        }).start();
    }


    // Account menu methods.
    public void loginMenu(ActionEvent actionEvent) throws IOException {
        System.out.println("loginMenu");
        switchScene(actionEvent, "LoginMenu");
    }
    public void registerAccountMenu(ActionEvent actionEvent) throws IOException {
        System.out.println("registerAccountMenu");
        switchScene(actionEvent, "RegisterMenu");
    }
    public void userProfile(ActionEvent actionEvent) throws IOException {
        if (checkLoggedIn()) {
            System.out.println("userProfileMenu");
            switchScene(actionEvent, "UserprofileMenu");
        }
    }
    public void updateUserProfile(ActionEvent actionEvent) throws IOException {
        System.out.println("updateUserProfileMenu");
        usernameLabel.setText(ApplicationStates.theUser.getUsername());
    }

    // Login menu methods.
    public void loginAccount(ActionEvent actionEvent) throws IOException, SQLException {
        System.out.println("loginAccount");

        String usernameGotten = usernameField.getText();
        String passwordGotten = passwordField.getText();

        if  (usernameGotten.isEmpty() || passwordGotten.isEmpty()) {
            System.out.println("Please enter your username and password");
            updateStatus("Please enter your username and password.", Color.RED);
            return;
        }

        // System.out.println(usernameGotten + " : " +  passwordGotten);
        ApplicationStates.theUser = userRepository.loginUser(usernameGotten, passwordGotten);
        // System.out.println(ApplicationStates.theUser.getId() + " : " + ApplicationStates.theUser.getUsername());

        if (ApplicationStates.theUser == null) {
            updateStatus("User not found.", Color.RED);
        }
        else {
            updateStatus("Logged in successfully.", Color.GREEN);
        }
    }

    // Register menu methods.
    public void registerAccount(ActionEvent actionEvent) throws IOException, SQLException {
        System.out.println("registerAccount");

        String usernameGotten = usernameField.getText();
        String passwordGotten = passwordField.getText();

        if  (usernameGotten.isEmpty() || passwordGotten.isEmpty()) {
            System.out.println("Please enter your username and password");
            updateStatus("Please enter your username and password.", Color.RED);
            return;
        }

        if (userRepository.checkIfUsernameExists(usernameGotten)) {
            System.out.println("Username already exists.");
            updateStatus("Username already exists.", Color.RED);
            return;
        }

        boolean success = userRepository.registerUser(usernameGotten, passwordGotten);

        if (success) {
            System.out.println("Account successfully registered");
            updateStatus("Account successfully registered", Color.GREEN);
            clearFields();
        }
        else {
            System.out.println("Account failed to register");
            updateStatus("Account failed to register", Color.RED);
        }
    }

    // Status update.
    public void updateStatus(String message, Color color) {
        System.out.println("updateStatus");
        statusLabel.setText(message);
        statusLabel.setTextFill(color);
    }

    // Clear fields.
    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    // Universal methods.
    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        System.out.println("backToMainMenu");
        switchScene(actionEvent, "MainMenu");
    }
    public boolean checkLoggedIn() throws IOException {
        System.out.println("checkLoggedIn");
        if (ApplicationStates.theUser != null) {
            System.out.println("checkLoggedIn : User is logged in.");
            return true;
        }
        else {
            System.out.println("checkLoggedIn : User is not logged in.");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("LOG IN INFORMATION");
            alert.setHeaderText(null);
            alert.setContentText("You are not logged in. Please log in or register account if none.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("User clicked OK");
            }
        }

        return false;
    }
    public void switchToPvPInvite(ActionEvent event) throws IOException {
        if (checkLoggedIn()) {
            switchScene(event, "PvPInvite");
        }
    }
    public void switchToPvEMenu(ActionEvent event) throws IOException {
        if (checkLoggedIn()) {
            switchScene(event, "PvEMenu");
        }
    }
    @FXML
    public void switchToPvENewCampaign(ActionEvent event) throws IOException {
        if (checkLoggedIn()) {
            switchScene(event, "NewCampaignMenu");
        }
    }
    public void beginPvENewCampaign(ActionEvent event) throws IOException, SQLException {
        if (checkLoggedIn()) {
            if (campaignNameField.getText().isEmpty()) {
                System.out.println("Please enter your campaign name");
                return;
            }

            Hero heroChosen;

            if (chooseHeroChoicebox.getValue() == null) {
                System.out.println("Please choose a hero");
                return;
            }

            heroChosen = getHeroSelected(event);

            ApplicationStates.PvECampaign = CampaignFactory.getInstance().createCampaign();
            ApplicationStates.PvECampaign.setCampaignName(campaignNameField.getText());
            ApplicationStates.PvECampaign.setRoomNumberIndex(0);
            ApplicationStates.PvECampaign.setPlayerHero(heroChosen);
            ApplicationStates.PvECampaign.setPlayerHeroID(heroChosen.getHeroIDInt());
            ApplicationStates.PvECampaign.setUserID(ApplicationStates.theUser.getId());

            CampaignRepository.getInstance().createCampaign(ApplicationStates.PvECampaign);

            switchScene(event, "PvE");
        }
    }
    public void switchToPvELoadCampaign(ActionEvent event) throws IOException {
        if (checkLoggedIn()) {
            // System.out.println("UNAVAILABLE AT THE MOMENT");
            switchScene(event, "LoadCampaignMenu");
        }
    }
    public void beginPvELoadCampaign(ActionEvent event) throws IOException, SQLException {
        if (checkLoggedIn()) {
            if (loadSavedCampaignChoiceBox.getValue() == null) {
                System.out.println("Please choose a campaign to load.");
                return;
            }

            ApplicationStates.PvECampaign = CampaignRepository.getInstance().getCampaign(loadSavedCampaignChoiceBox.getValue(), ApplicationStates.theUser.getId());

            if (ApplicationStates.PvECampaign.getRoomNumberIndex() < ApplicationConstants.totalCampaignRooms) {
                switchScene(event, "PvE");
            } else {
                System.out.println("PvE campaign has been completed.");
            }
        }
    }

    public void goToHeroesMenu(ActionEvent event) throws IOException {
        if (checkLoggedIn()) {
            switchScene(event, "HeroesMenu");
        }
    }

    public void goToHeroesListInfoMenu(ActionEvent event) throws IOException {
        if (checkLoggedIn()) {
            switchScene(event, "HeroesListInfoMenu");
        }
    }

    public void goToHeroMakerMenu(ActionEvent event) throws IOException {
        if (checkLoggedIn()) {
            switchScene(event, "HeroMakerMenu");
        }
    }

    public Hero getHeroSelected(ActionEvent event) throws IOException, SQLException {
        if (checkLoggedIn()) {
            String stringGotten =  (String) chooseHeroChoicebox.getValue();

            String heroClass = stringGotten.split("#")[0];
            String heroId =  stringGotten.split("#")[1];

            System.out.println(stringGotten + "#" + heroClass + "#" + heroId);

            return HeroRepository.getInstance().getIndividualHero(Integer.parseInt(heroId));
        }
        return null;
    }

    public void listOutHeroInformation(ActionEvent event) throws IOException, SQLException {
        // A
        // Ignore.
        Hero heroGotten = getHeroSelected(event);
        if (heroGotten != null) {
            heroClassLabel.setText(heroGotten.getHeroClassString());
            heroLevelLabel.setText("" + heroGotten.getLevel());
            heroAttackLabel.setText("" + heroGotten.getAttack());
            heroDefenseLabel.setText("" + heroGotten.getDefense());
            heroMaxHPLabel.setText("" + heroGotten.getMaxHp());
            heroMaxManaLabel.setText("" + heroGotten.getMaxMana());
        }
    }

    public void makeHero(ActionEvent event) throws IOException, SQLException {
        if (checkLoggedIn()) {
            // A
            String choiceBoxValue = (String) chooseHeroClassChoicebox.getValue();
            Hero heroChosen = orderFactory.createHero();

            if (chooseHeroClassChoicebox.getValue() == null) {
                System.out.println("Please choose a hero");
                return;
            }

            heroChosen = CentralHeroFactory.makeHeroBasedOnClass(choiceBoxValue);
            heroRepository.saveHero(heroChosen, ApplicationStates.theUser.getId());

        }
    }


    // Initialize to make things preload.
    @FXML
    public void initialize() throws SQLException {
        if (chooseHeroChoicebox != null) {
            // chooseHeroChoicebox.getItems().addAll(heroClasses);
            // You would get the userID and add all Hero IDs here.
            // chooseHeroChoicebox.getItems().add("You should see this.");

            List<Hero> heroesList = HeroRepository.getInstance().getHeroes(ApplicationStates.theUser.getId());

            for (Hero hero : heroesList) {
                System.out.println("FROM LISTING OUT HERO INFORMATION: " + hero.getHeroClassString() + "#" + hero.getHeroIDInt());
                chooseHeroChoicebox.getItems().add(hero.getHeroClassString() + "#" + hero.getHeroIDInt());
            }
        }

        if (chooseHeroClassChoicebox != null) {
            chooseHeroClassChoicebox.getItems().addAll(heroClasses);
        }

        if (loadSavedCampaignChoiceBox != null) {
            System.out.println("You see this?");
            List<Campaign> campaigns = CampaignDAO.getInstance().getCampaigns(ApplicationStates.theUser.getId());

            for (Campaign campaign : campaigns) {
                System.out.println("FROM LISTING OUT CAMPAIGNS: " + campaign.getCampaignName() + "#" + campaign.getUserID());
                loadSavedCampaignChoiceBox.getItems().addAll(campaign.getCampaignName());
            }
        }
    }

}
