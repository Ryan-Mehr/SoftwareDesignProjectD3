package Repository;

import Classes.Battle.MatchResult;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PvPRepository {

    private static PvPRepository instance;

    private PvPRepository() {}

    public static PvPRepository getInstance() {
        if (instance == null) {
            instance = new PvPRepository();
        }
        return instance;
    }

    public void saveMatchResult(String player1, String player2, String winner) {
        String sql = "INSERT INTO pvp_results (player1, player2, winner) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player1);
            stmt.setString(2, player2);
            stmt.setString(3, winner);

            stmt.executeUpdate();
            System.out.println("PvP match saved to database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MatchResult> getAllMatchResults() {
        List<MatchResult> results = new ArrayList<>();

        String sql = "SELECT player1, player2, winner, timestamp FROM pvp_results ORDER BY timestamp DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new MatchResult(
                        rs.getString("player1"),
                        rs.getString("player2"),
                        rs.getString("winner"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            System.err.println("Database error in getAllMatchResults: " + e.getMessage());
        }

        return results;
    }
}
