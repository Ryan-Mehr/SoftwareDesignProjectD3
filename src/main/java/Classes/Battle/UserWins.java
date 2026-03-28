package Classes.Battle;

public class UserWins {
    private final String username;
    private final int wins;

    public UserWins(String username, int wins) {
        this.username = username;
        this.wins = wins;
    }

    public String getUsername() { return username; }
    public int getWins() { return wins; }
}
