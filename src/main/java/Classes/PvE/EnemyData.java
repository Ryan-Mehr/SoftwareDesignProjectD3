package Classes.PvE;

public class EnemyData {
    private final String name;
    private final int health;

    public EnemyData(String name, int health) {
        this.name = name;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }
}