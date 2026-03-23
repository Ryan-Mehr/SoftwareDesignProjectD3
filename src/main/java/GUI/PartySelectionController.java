package GUI;

import GlobalVariables.ApplicationStates;
import Classes.Party;
import Repository.PartyRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PartySelectionController implements Initializable {

    @FXML
    private TableView<Party> partyTable;

    @FXML
    private TableColumn<Party, String> nameColumn;

    @FXML
    private TableColumn<Party, String> infoColumn;

    private PartyRepository partyRepo = PartyRepository.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("=== PartySelectionController.initialize() START ===");

        try {
            // Bind columns to Party fields
            System.out.println("Binding table columns...");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("partyName"));
            infoColumn.setCellValueFactory(new PropertyValueFactory<>("partyInfo"));
            System.out.println("Columns bound successfully");

            // Check if user is logged in
            if (ApplicationStates.theUser == null) {
                System.out.println("ERROR: No user logged in!");
                return;
            }

            // Load parties for the logged-in user
            String username = ApplicationStates.theUser.getUsername();
            System.out.println("Loading parties for user: '" + username + "'");  // Note the quotes to see spaces
            System.out.println("Opponent username: '" + ApplicationStates.opponentUsername + "'");

            List<Party> parties = partyRepo.getPartiesForUser(username);
            System.out.println("Repository returned " + parties.size() + " parties");

            // Print each party found
            if (parties.isEmpty()) {
                System.out.println("No parties found for user: " + username);
                System.out.println("Check if the username in database matches exactly: '" + username + "'");
            } else {
                System.out.println("Parties found:");
                for (int i = 0; i < parties.size(); i++) {
                    Party p = parties.get(i);
                    System.out.println("  Party " + (i+1) + ":");
                    System.out.println("    Name: '" + p.getPartyName() + "'");
                    System.out.println("    Info: '" + p.getPartyInfo() + "'");
                    System.out.println("    Username: '" + p.getUsername() + "'");
                }
            }

            // Populate the table
            partyTable.setItems(FXCollections.observableArrayList(parties));
            System.out.println("Table populated with " + parties.size() + " parties");

            // Force table refresh
            partyTable.refresh();

        } catch (Exception e) {
            System.out.println("ERROR in initialize: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== PartySelectionController.initialize() END ===");
    }

    @FXML
    private void chooseParty(ActionEvent event) {
        try {
            Party selected = partyTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                System.out.println("No party selected!");
                return;
            }

            System.out.println("Selected party: " + selected.getPartyName());

            // 🚨 FIX: Add test heroes to selected party if empty
            if (selected.getHeroes().isEmpty()) {
                System.out.println("⚠️ Your party has no heroes! Adding test heroes...");
                selected.addHero(new Classes.Heros.Warrior());
                selected.addHero(new Classes.Heros.Mage());
                selected.addHero(new Classes.Heros.Order());
                System.out.println("✅ Added 3 heroes to your party");
            }

            // Save selected party
            ApplicationStates.selectedParty = selected;

            // Check if opponent username is set
            if (ApplicationStates.opponentUsername == null) {
                System.out.println("No opponent username set!");
                return;
            }

            // Load opponent's parties
            List<Party> opponentParties = partyRepo.getPartiesForUser(ApplicationStates.opponentUsername);
            if (!opponentParties.isEmpty()) {
                ApplicationStates.opponentParty = opponentParties.get(0);
                System.out.println("Opponent party: " + opponentParties.get(0).getPartyName());

                // 🚨 FIX: Add test heroes to opponent party if empty
                if (ApplicationStates.opponentParty.getHeroes().isEmpty()) {
                    System.out.println("⚠️ Opponent party has no heroes! Adding test heroes...");
                    ApplicationStates.opponentParty.addHero(new Classes.Heros.Chaos());
                    ApplicationStates.opponentParty.addHero(new Classes.Heros.Order());
                    ApplicationStates.opponentParty.addHero(new Classes.Heros.Mage());
                    System.out.println("✅ Added 3 heroes to opponent party");
                }
            } else {
                System.out.println("Opponent has no parties!");
                return;
            }

            // Debug: Show heroes count
            System.out.println("Your party now has " + selected.getHeroes().size() + " heroes");
            System.out.println("Opponent party now has " + ApplicationStates.opponentParty.getHeroes().size() + " heroes");

            // Move to battle view
            new ApplicationController().switchScene(event, "BattleView");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error choosing party: " + e.getMessage());
        }
    }

    @FXML
    private void backToMainMenu(ActionEvent event) {
        System.out.println("=== backToMainMenu called ===");

        try {
            System.out.println("Returning to main menu from party selection");
            new ApplicationController().switchScene(event, "MainMenu");
            System.out.println("Return to main menu completed");
        } catch (Exception e) {
            System.out.println("ERROR in backToMainMenu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}