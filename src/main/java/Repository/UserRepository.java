package Repository;

import Classes.User;
import DAO.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Classes.Battle.UserWins;


public class UserRepository {
    // Make it singleton.
    // https://www.geeksforgeeks.org/system-design/singleton-design-pattern/
    private static volatile UserRepository obj = null;
    private UserRepository() {
        System.out.println("UserRepository is instantiated.");
    }
    public static UserRepository getInstance() {
        if (obj == null) {
            synchronized (UserRepository.class) {
                if (obj == null) {
                    obj = new UserRepository();
                }
            }
        }
        return obj;
    }

    // Make sure DAOs are singletons as well.
    private final UserDAO userDAO = UserDAO.getInstance();

    public boolean validateInput(String username, String password) {
        // True if valid, false if not.
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean checkIfUsernameExists(String username) throws SQLException {
        return userDAO.checkIfUsernameExists(username);
    }

    public boolean registerUser(String username, String password) throws SQLException {
        if (validateInput(username, password)) {
            return userDAO.save(username, password);
        }
        return false;
    }

    public User loginUser(String username,String password) throws SQLException {
        // Any business logic here like input validation if needed.
        return userDAO.login(username, password);
    }
    public void addWinToUser(String username) {
        String sql = "UPDATE USERS SET wins = wins + 1 WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();

            System.out.println("Win added to user: " + username);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT username FROM users";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }

        } catch (SQLException e) {
            System.err.println("Database error in getAllUsernames: " + e.getMessage());
        }

        return usernames;
    }

    public List<UserWins> getAllUserWins() {
        List<UserWins> list = new ArrayList<>();

        String sql = "SELECT username, wins FROM users ORDER BY wins DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new UserWins(
                        rs.getString("username"),
                        rs.getInt("wins")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int findUserID(String username) throws SQLException {
        return userDAO.findUserID(username);
    }

}
