package GlobalVariables;

import Classes.PvE.Campaign;
import Classes.User;
import Classes.Party;

public class ApplicationStates {

    private ApplicationStates() {
        throw new AssertionError();
    }

    // Logged-in user
    public static User theUser = null;

    // PvP opponent username
    public static String opponentUsername = null;

    // Selected parties for PvP
    public static Party selectedParty = null;
    public static Party opponentParty = null;

    public static Campaign PvECampaign = null;
}
