package DAO;

import Classes.User;
import Repository.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDAO {
    private static volatile UserDAO obj = null;
    private UserDAO() {
        System.out.println("UserDAO is instantiated.");
    }
    public static UserDAO getInstance() {
        if (obj == null) {
            synchronized (UserDAO.class) {
                if (obj == null) {
                    obj = new UserDAO();
                }
            }
        }
        return obj;
    }

    public void registrationHelper(String usernameGiven) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO parties (username, party_name, party_info) VALUES (?, 'My Awesome New Party', 'This is my party description.')";

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, usernameGiven);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean save(String username, String password) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "INSERT INTO USERS (username, password) VALUES (?, ?)";
        boolean ifSo = false;

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            ifSo = preparedStatement.executeUpdate() > 0;
            registrationHelper(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ifSo;
    }

    public boolean checkIfUsernameExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) AS total_amount FROM USERS WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int total_amount = rs.getInt("total_amount");
                if (total_amount > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public User login(String username, String password) throws SQLException {
        // String query = "SELECT idUSERS, username FROM USERS WHERE username = ? AND password = ?";
        String query = "SELECT idUSERS, username, password FROM USERS WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            // preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

//            To print out the results of an executeQuery.
//            ResultSetMetaData metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();
//            for  (int i = 1; i <= columnCount; i++) {
//                System.out.print(metaData.getColumnName(i) + "\t");
//                if (i < columnCount) {
//                    System.out.print(", ");
//                }
//            }
//            System.out.println();
//            while (resultSet.next()) {
//                for (int i = 1; i <= columnCount; i++) {
//                    System.out.print(resultSet.getString(i) + "\t");
//                    if (i < columnCount) {
//                        System.out.print(", ");
//                    }
//                }
//                System.out.println();
//            }

            if (resultSet.next()) {
                //System.out.println("-=-=-=We got here.");
                String storedHash = resultSet.getString("password");
                if (BCrypt.checkpw(password, storedHash)) {
                    return new User(resultSet.getInt("idUSERS"), resultSet.getString("username"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int findUserID(String username) throws SQLException {
        String query = "SELECT idUSERS FROM USERS WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("idUSERS");
            }
        }
        return -1;
    }
}
