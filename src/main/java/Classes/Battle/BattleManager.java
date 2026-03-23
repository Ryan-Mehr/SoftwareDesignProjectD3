package Classes.Battle;

import Repository.PvPRepository;
import java.util.Scanner;
import Repository.UserRepository;

public class BattleManager {
    private final Player player1;
    private final Player player2;
    private final Scanner scanner = new Scanner(System.in);

    private final String label1 = "Player 1";
    private final String label2 = "Player 2";

    public BattleManager(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }

    public void startBattle(){
        System.out.println("----- PvP Battle Started -----");

        Player current = player1;
        Player opponent = player2;

        while (player1.isAlive() && player2.isAlive()){
            System.out.println("\n" + (current == player1 ? label1 : label2) + "'s turn!");
            System.out.println("1. Attack");
            System.out.println("2. Defend");
            System.out.println("Choose your action: ");

            int choice = scanner.nextInt();
            Action action;
            if (choice == 1){
                action = new AttackAction();
            } else {
                action = new DefendAction();
            }
            action.execute(current, opponent);
            System.out.println(opponent.getName() + " health: " + opponent.getHealth());

            // Switch turns
            Player temp = current;
            current = opponent;
            opponent = temp;
        }

        System.out.println("\n ----- Battle Over -----");

        Player winner;
        if (player1.isAlive()) {
            winner = player1;
            System.out.println(label1 + " (" + player1.getName() + ") wins!");
        } else {
            winner = player2;
            System.out.println(label2 + " (" + player2.getName() + ") wins!");
        }

// SAVE RESULT TO DATABASE
        PvPRepository.getInstance().saveMatchResult(
                player1.getName(),
                player2.getName(),
                winner.getName()
        );

        System.out.println("Match saved to database.");
        // UPDATED WINNER'S TOTAL WINS
        UserRepository.getInstance().addWinToUser(winner.getName());
        System.out.println("Win count updated for: " + winner.getName());
    }
}
