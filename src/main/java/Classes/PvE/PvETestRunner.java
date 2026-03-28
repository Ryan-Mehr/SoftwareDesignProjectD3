package Classes.PvE;
import Classes.Heros.Hero;
import Classes.Heros.Warrior;
import Classes.User;
import DAO.UserDAO;
import Classes.User;

import java.sql.SQLException;

public class PvETestRunner {
    public static void main(String[] args) {
        try {
            UserDAO userDAO = UserDAO.getInstance();
            User user = userDAO.login("ap", "pass");

            if (user == null) {
                System.out.println("Login failed!");
                return;
            }

            System.out.println("Logged in as: " + user.getUsername());

            Hero playerHero = new Warrior();
            Player player = new Player(user.getUsername(), 150, 25);

            Enemy zombie = new Enemy("Zombie", 20, 3);
            Enemy skeleton = new Enemy("Skeleton", 30, 4);
            Enemy spider = new Enemy("Spider", 25, 5);

            Room room1 = new Room(1, zombie);
            Room room2 = new Room(2, skeleton);
            Room room3 = new Room(3, spider);

            Room[] rooms = {room1, room2, room3};

            for (Room room : rooms) {
                System.out.println("\nEntering Room " + room.getRoomNumber());

                BattleEngine battle = new BattleEngine(player, room, playerHero);
                battle.startBattle();

                if (player.getHealth() <= 0) {
                    System.out.println("Game Over!");
                    return;
                }
            }

            System.out.println("\n Campaign Complete!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
