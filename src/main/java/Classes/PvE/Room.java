package Classes.PvE;

public class Room {
    private int roomNumber;
    private Enemy enemy;

    public Room(int roomNumber, Enemy enemy) {
        this.roomNumber = roomNumber;
        this.enemy = enemy;
    }

    public Enemy getEnemy() { return enemy; }
    public int getRoomNumber() { return roomNumber;}
}