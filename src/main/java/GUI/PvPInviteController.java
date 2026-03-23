package GUI;

import GlobalVariables.ApplicationStates;
import Repository.UserRepository;
import Repository.PartyRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.sql.SQLException;


public class PvPInviteController {

    @FXML private TextField opponentField;
    @FXML private Label statusLabel;

    UserRepository userRepo = UserRepository.getInstance();
    PartyRepository partyRepo = PartyRepository.getInstance();

    @FXML
    public void sendInvitation(ActionEvent event) {
        System.out.println("\n=== PvPInviteController.sendInvitation() START ===");

        String opponent = opponentField.getText().trim();
        System.out.println("Opponent entered: '" + opponent + "'");

        if (opponent.isEmpty()) {
            System.out.println("ERROR: Opponent field is empty");
            statusLabel.setText("Please enter a username.");
            return;
        }

        // Check current logged in user
        String currentUser = ApplicationStates.theUser.getUsername();
        System.out.println("Current logged in user: '" + currentUser + "'");

        // 1. Check if opponent exists
        try {
            System.out.println("Checking if opponent exists: '" + opponent + "'");
            if (!userRepo.checkIfUsernameExists(opponent)) {
                System.out.println("ERROR: Opponent does not exist: '" + opponent + "'");
                statusLabel.setText("User does not exist.");
                return;
            }
            System.out.println("✓ Opponent exists");
        } catch (SQLException e) {
            System.out.println("SQL ERROR checking opponent: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Database error.");
            return;
        }

        // 2. Check if YOU have at least one saved party
        System.out.println("Checking if current user has parties: '" + currentUser + "'");
        boolean currentUserHasParty = partyRepo.userHasParty(currentUser);
        System.out.println("Current user has parties? " + currentUserHasParty);

        if (!currentUserHasParty) {
            System.out.println("ERROR: Current user has no parties: '" + currentUser + "'");
            statusLabel.setText("You must have at least one saved party.");
            return;
        }
        System.out.println("✓ Current user has parties");

        // 3. Check if opponent has at least one saved party
        System.out.println("Checking if opponent has parties: '" + opponent + "'");
        boolean opponentHasParty = partyRepo.userHasParty(opponent);
        System.out.println("Opponent has parties? " + opponentHasParty);

        if (!opponentHasParty) {
            System.out.println("ERROR: Opponent has no parties: '" + opponent + "'");
            statusLabel.setText("Opponent has no saved parties.");
            return;
        }
        System.out.println("✓ Opponent has parties");

        // 4. Auto-accept invitation
        System.out.println("All checks passed! Accepting invitation...");
        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setText("Invitation accepted! Loading party selection...");

        // 5. Store opponent username globally
        ApplicationStates.opponentUsername = opponent;
        System.out.println("Stored opponent username in ApplicationStates: '" + ApplicationStates.opponentUsername + "'");

        // 6. Move to party selection screen
        try {
            System.out.println("Attempting to switch to PartySelection.fxml...");
            new ApplicationController().switchScene(event, "PartySelection");
            System.out.println("✓ Switch completed");
        } catch (Exception e) {
            System.out.println("ERROR switching scene: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== PvPInviteController.sendInvitation() END ===\n");
    }

    @FXML
    public void backToMainMenu(ActionEvent event) {
        System.out.println("=== backToMainMenu called from PvPInviteController ===");
        try {
            new ApplicationController().switchScene(event, "MainMenu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}