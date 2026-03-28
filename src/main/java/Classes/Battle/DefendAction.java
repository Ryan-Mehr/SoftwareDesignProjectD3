package Classes.Battle;

public class DefendAction implements Action {
    @Override
    public void execute(Player attacker, Player defender) {
        attacker.defend();
        System.out.println(attacker.getName() + " is defending and will take reduced damage next turn!");
    }
}
