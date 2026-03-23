package Classes.Battle;

import java.time.LocalDateTime;

public class MatchResult {
    private final String player1;
    private final String player2;
    private final String winner;
    private final LocalDateTime timestamp;

    public MatchResult(String player1, String player2, String winner, LocalDateTime timestamp) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
        this.timestamp = timestamp;
    }

    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public String getWinner() { return winner; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
