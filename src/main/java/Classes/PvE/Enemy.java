package Classes.PvE;

public class Enemy {
    private String name;
    private int health;
    private int attackPower;


    public Enemy(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
    }

    public void attack(Player player) {
        player.takeDamage(this.attackPower);
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public int attack() {
        return this.attackPower;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getAttackPower() { return attackPower; }

}