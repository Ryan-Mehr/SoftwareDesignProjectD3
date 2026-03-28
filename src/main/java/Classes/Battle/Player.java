package Classes.Battle;

public class Player {
    private final String name;
    private int health;
    private final int attackPower;
    private final int defensePower;
    private boolean defending = false;

    public Player (String name, int health, int attackPower, int defensePower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
    }
    public String getName() {
        return name;
    }
    public int getHealth() {
        return health;
    }
    public boolean isAlive() {
        return health > 0;
    }
    public void takeDamage(int amount) {
        if (defending) {
            amount -= defensePower;
            if(amount < 0) amount = 0;
            defending = false;
        }
        health -= amount;
        if (health <= 0) health = 0;
    }
    public int getAttackPower(){
        return attackPower;
    }
    public void defend(){
        defending = true;
    }
}
