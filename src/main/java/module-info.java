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

    opens GUI to javafx.fxml;
    opens Classes to javafx.base, javafx.fxml;
    opens Classes.Heros to javafx.base, javafx.fxml;
    opens Classes.PvE to javafx.fxml;
    opens GlobalVariables to javafx.base, javafx.fxml;
    opens db.migration;

    exports GUI;
    exports Classes;
    exports Classes.Heros;
    exports Classes.PvE;
}