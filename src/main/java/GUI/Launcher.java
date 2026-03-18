package GUI;

import GlobalVariables.ApplicationConstants;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        // Make sure DB is up to date with changes.
        // Whenever you make a change to the DB using MySQL Workbench, add the .sql file to resources/db.migration. Following similar titles.
        // If you made a new SQL DB structure, make sure to call it V2. Use MySQL workbench to work with the DB.

        Application.launch(MainApplication.class, args);
    }
}