module org.example.softwaredesignprojectd2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;

    // Open ALL your packages to JavaFX
    opens GUI to javafx.fxml;
    opens Classes to javafx.base, javafx.fxml;        // ADD THIS
    opens Classes.Heros to javafx.base, javafx.fxml;  // ADD THIS
    opens GlobalVariables to javafx.base, javafx.fxml; // ADD THIS
    opens db.migration;

    exports GUI;
    exports Classes;
    exports Classes.Heros;
}