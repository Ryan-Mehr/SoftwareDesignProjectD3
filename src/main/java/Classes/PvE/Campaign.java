package Classes.PvE;

import Classes.Heros.Hero;

public class Campaign {
    private String campaignName;
    private int roomNumberIndex;
    private Hero playerHero;
    private int playerHeroID;
    private int userID;

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public int getRoomNumberIndex() {
        return roomNumberIndex;
    }

    public void setRoomNumberIndex(int roomNumberIndex) {
        this.roomNumberIndex = roomNumberIndex;
    }

    public Hero getPlayerHero() {
        return playerHero;
    }

    public void setPlayerHero(Hero playerHero) {
        this.playerHero = playerHero;
    }

    public int getPlayerHeroID() {
        return playerHeroID;
    }

    public void setPlayerHeroID(int playerHeroID) {
        this.playerHeroID = playerHeroID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
