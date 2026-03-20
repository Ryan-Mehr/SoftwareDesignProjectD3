package Classes;

import Classes.Heros.Hero;
import java.util.List;
import java.util.ArrayList;

public class Party {
    private String partyId;
    private String username;
    private String partyName;
    private String partyInfo;
    private boolean isDefeated;
    private List<Hero> heroes = new ArrayList<>();

    // Constructor
    public Party(String partyName, String partyInfo, String username) {
        this.partyName = partyName;
        this.partyInfo = partyInfo;
        this.username = username;
        this.isDefeated = false;
    }

    // Getters and Setters
    public String getPartyId() { return partyId; }
    public void setPartyId(String partyId) { this.partyId = partyId; }

    public String getPartyName() { return partyName; }
    public void setPartyName(String partyName) { this.partyName = partyName; }

    public String getPartyInfo() { return partyInfo; }
    public void setPartyInfo(String partyInfo) { this.partyInfo = partyInfo; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isDefeated() { return isDefeated; }
    public void setDefeated(boolean defeated) { isDefeated = defeated; }

    public List<Hero> getHeroes() { return heroes; }
    public void setHeroes(List<Hero> heroes) { this.heroes = heroes; }

    public void addHero(Hero hero) {
        heroes.add(hero);
    }

    public void removeHero(Hero hero) {
        heroes.remove(hero);
    }

    public boolean hasAliveHeroes() {
        for (Hero hero : heroes) {
            if (!hero.isDefeated()) {
                return true;
            }
        }
        return false;
    }

    public int getAliveCount() {
        int count = 0;
        for (Hero hero : heroes) {
            if (!hero.isDefeated()) {
                count++;
            }
        }
        return count;
    }

    // For JavaFX TableView
    public javafx.beans.property.StringProperty partyNameProperty() {
        return new javafx.beans.property.SimpleStringProperty(partyName);
    }

    public javafx.beans.property.StringProperty partyInfoProperty() {
        return new javafx.beans.property.SimpleStringProperty(partyInfo);
    }
}