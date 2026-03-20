package GUI;

import Classes.Battle.MatchResult;
import Repository.PvPRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class MatchHistoryController {

    @FXML private TableView<MatchResult> matchTable;
    @FXML private TableColumn<MatchResult, String> colPlayer1;
    @FXML private TableColumn<MatchResult, String> colPlayer2;
    @FXML private TableColumn<MatchResult, String> colWinner;
    @FXML private TableColumn<MatchResult, String> colTimestamp;

    @FXML
    public void initialize() {
        colPlayer1.setCellValueFactory(new PropertyValueFactory<>("player1"));
        colPlayer2.setCellValueFactory(new PropertyValueFactory<>("player2"));
        colWinner.setCellValueFactory(new PropertyValueFactory<>("winner"));
        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        matchTable.setItems(FXCollections.observableArrayList(
                PvPRepository.getInstance().getAllMatchResults()
        ));
    }

    // Back button method
    public void backToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
