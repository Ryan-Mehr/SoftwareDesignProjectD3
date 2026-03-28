package DAO;

import Classes.PvE.Campaign;
import Factory.CampaignFactory;
import Repository.DatabaseManager;
import Repository.HeroRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CampaignDAO {
    private static volatile CampaignDAO obj = null;
    private CampaignDAO() {
        System.out.println("CampaignDAO is instantiated.");
    }
    public static CampaignDAO getInstance() {
        if (obj == null) {
            synchronized (CampaignDAO.class) {
                if (obj == null) {
                    obj = new CampaignDAO();
                }
            }
        }
        return obj;
    }

    public void createCampaign(Campaign campaign) throws SQLException {
        String query = "INSERT INTO CAMPAIGNS (campaignName, roomNumber, playerHeroID, userID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, campaign.getCampaignName());
            preparedStatement.setInt(2, campaign.getRoomNumberIndex());
            preparedStatement.setInt(3, campaign.getPlayerHeroID());
            preparedStatement.setInt(4, campaign.getUserID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateCampaign(Campaign campaign, String campaignName, int userID) throws SQLException {
        String query = "UPDATE CAMPAIGNS SET campaignName = ?, roomNumber = ?, playerHeroID = ?, userID = ? " +
                "WHERE campaignName = ? AND userID = ?";
        //String query = "UPDATE CAMPAIGNS SET (campaignName, roomNumber, playerHeroID, userID) VALUES (?, ?, ?, ?) WHERE (campaignName = ? AND userID = ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, campaign.getCampaignName());
            preparedStatement.setInt(2, campaign.getRoomNumberIndex());
            preparedStatement.setInt(3, campaign.getPlayerHeroID());
            preparedStatement.setInt(4, campaign.getUserID());
            preparedStatement.setString(5, campaign.getCampaignName());
            preparedStatement.setInt(6, campaign.getUserID());
            preparedStatement.executeUpdate();
        }
    }

    public Campaign giveCampaignProperties(String campaignName, int roomNumber, int playerHeroID, int userID) throws SQLException {
        Campaign campaign = CampaignFactory.getInstance().createCampaign();

        campaign.setCampaignName(campaignName);
        campaign.setRoomNumberIndex(roomNumber);
        campaign.setPlayerHeroID(playerHeroID);
        campaign.setPlayerHero(HeroRepository.getInstance().getIndividualHero(playerHeroID));
        campaign.setUserID(userID);

        return campaign;
    }

    public List<Campaign> getCampaigns(int userID) throws SQLException {
        List<Campaign> campaigns = new ArrayList<>();
        String query = "SELECT * FROM CAMPAIGNS WHERE userID = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Campaign campaign;
                String campaignName = resultSet.getString("campaignName");
                int roomNumber = resultSet.getInt("roomNumber");
                int playerHeroID = resultSet.getInt("playerHeroID");
                int userIDGotten = resultSet.getInt("userID");
                campaign = giveCampaignProperties(campaignName, roomNumber, playerHeroID, userIDGotten);

                if (campaign != null) {
                    campaigns.add(campaign);
                }
            }
        }
        return campaigns;
    }

    public Campaign loadIndividualCampaign(String campaignName, int userID) throws SQLException {
        Campaign campaignToReturn = null;
        String query = "SELECT * FROM CAMPAIGNS WHERE campaignName = ? AND userID = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, campaignName);
            preparedStatement.setInt(2, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String campaignNameGotten = resultSet.getString("campaignName");
                int roomNumber = resultSet.getInt("roomNumber");
                int playerHeroID = resultSet.getInt("playerHeroID");
                int userIDGotten = resultSet.getInt("userID");

                campaignToReturn = giveCampaignProperties(campaignNameGotten, roomNumber, playerHeroID, userIDGotten);
            }
        }
        return campaignToReturn;
    }
}
