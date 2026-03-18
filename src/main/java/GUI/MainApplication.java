package GUI;

import GlobalVariables.ApplicationConstants;
import Repository.UserRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Stages are frames.
        // Scenes are panels.
        // As we are a modular GUI, it is one frame with many panels. Meaning one stage many scenes.
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/MainMenu.fxml")));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Legends of Sword and Wand");
        stage.show();
    }
}
