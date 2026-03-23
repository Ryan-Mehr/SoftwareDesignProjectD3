package GUI;

import GlobalVariables.ApplicationStates;
import Repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import Classes.Battle.Player;
import Classes.Battle.BattleManager;


public class ApplicationController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Label usernameLabel;

    // Make sure Repositories are singletons.
    UserRepository userRepository = UserRepository.getInstance();

    // Controller methods.
    public void switchScene(ActionEvent event, String fileName) throws IOException {
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/" + fileName + ".fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(newRoot);
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

        // Player 1 = logged-in user
        Player p1 = new Player(ApplicationStates.theUser.getUsername(), 100, 20, 5);

        // Player 2 = another real user (temporary hard-coded)
        Player p2 = new Player("CPU_Opponent", 100, 20, 5);

        new Thread(() -> {
            BattleManager battle = new BattleManager(p1, p2);
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

}
