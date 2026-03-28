package Factory;

import Classes.PvE.Campaign;
import DAO.CampaignDAO;

public class CampaignFactory implements Interfaces.Campaign.CampaignFactory {
    private static volatile CampaignFactory obj = null;
    private CampaignFactory() {
        System.out.println("CampaignFactory is instantiated.");
    }
    public static CampaignFactory getInstance() {
        if (obj == null) {
            synchronized (CampaignFactory.class) {
                if (obj == null) {
                    obj = new CampaignFactory();
                }
            }
        }
        return obj;
    }
    @Override
    public Campaign createCampaign() {
        return new Campaign();
    }
}
