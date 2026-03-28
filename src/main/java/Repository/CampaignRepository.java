package Repository;

import Classes.PvE.Campaign;
import DAO.CampaignDAO;

import java.sql.SQLException;
import java.util.List;

public class CampaignRepository {
    private static volatile CampaignRepository obj = null;
    private CampaignRepository() {
        System.out.println("CampaignRepository is instantiated.");
    }
    public static CampaignRepository getInstance() {
        if (obj == null) {
            synchronized (CampaignRepository.class) {
                if (obj == null) {
                    obj = new CampaignRepository();
                }
            }
        }
        return obj;
    }

    public void createCampaign(Campaign campaign) throws SQLException {
        CampaignDAO.getInstance().createCampaign(campaign);
    }

    public void updateCampaign(Campaign campaign, String campaignName, int userID) throws SQLException {
        CampaignDAO.getInstance().updateCampaign(campaign, campaignName, userID);
    }

    public List<Campaign> getAllCampaigns(int userID) throws SQLException {
        return CampaignDAO.getInstance().getCampaigns(userID);
    }

    public Campaign getCampaign(String campaignName, int userID) throws SQLException {
        return CampaignDAO.getInstance().loadIndividualCampaign(campaignName, userID);
    }

}
