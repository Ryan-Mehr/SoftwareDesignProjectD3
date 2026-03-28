package Repository;

import Classes.Party;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartyRepository {

    private static PartyRepository instance;

    private PartyRepository() {}

    public static PartyRepository getInstance() {
        if (instance == null) {
            instance = new PartyRepository();
        }
        return instance;
    }

    public boolean userHasParty(String username) {
        System.out.println(">>> PartyRepository.userHasParty called for: '" + username + "'");
        String sql = "SELECT COUNT(*) FROM parties WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(">>> Found " + count + " parties for user: '" + username + "'");
                return count > 0;
            }

        } catch (Exception e) {
            System.out.println(">>> ERROR in userHasParty: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println(">>> No parties found for user: '" + username + "'");
        return false;
    }

    public List<Party> getPartiesForUser(String username) {
        List<Party> list = new ArrayList<>();

        String sql = "SELECT * FROM parties WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Party(
                        rs.getString("party_name"),
                        rs.getString("party_info"),
                        username
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
