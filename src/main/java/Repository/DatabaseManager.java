package Repository;

import java.io.FileInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import GlobalVariables.ApplicationConstants;
//
//import java.sql.*;

public class DatabaseManager {
//    private static Properties props = new Properties();
//
//    static {
//        try {
//            props.load(new FileInputStream("config.properties"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Connection getConnection() throws SQLException {
//        String url = props.getProperty("db_link");
//        String user = props.getProperty("db_user");
//        String password = props.getProperty("db_password");
//
//        return DriverManager.getConnection(url, user, password);
//    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                ApplicationConstants.db_link,
                ApplicationConstants.db_user,
                ApplicationConstants.db_password
        );
    }
}
