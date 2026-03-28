package Classes.PvE;

public class Player {
    private String name;
    private int health;
    private int attackPower;

    public Player(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int attack() {
        return this.attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void attack(Enemy enemy) {
        System.out.println(name + " attacks " + enemy.getName() + " for " + this.attackPower + " damage!");
        enemy.setHealth(enemy.getHealth() - this.attackPower);
    }
    public void defend() {
        this.health += 5;
        if (this.health > 100) this.health = 100;
        System.out.println(this.name + " defends and recovers 5 health!");
    }
    public void setHealth(int health) {
        this.health = Math.max(health, 0);
    }

    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
        System.out.println(this.name + " takes " + damage + " damage, remaining health: " + this.health);
    }


}