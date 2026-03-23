package Classes.Battle;

public class AttackAction implements Action {

    @Override
    public void execute(Player attacker, Player defender) {
        int damage = attacker.getAttackPower();
        defender.takeDamage(damage);
        System.out.println(attacker.getName() + "attacks for" + damage + " damage!");
    }
}
