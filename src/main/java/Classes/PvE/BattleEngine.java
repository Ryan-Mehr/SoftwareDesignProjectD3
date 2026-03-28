package Classes.PvE;

import Classes.Heros.Hero;
import Classes.PvE.Enemy;
import DAO.UserDAO;

public class BattleEngine {
    private Hero playerHero;
    private Player player;
    private Room room;

    public BattleEngine(Player player, Room room, Hero playerHero) {
        this.playerHero = playerHero;
        this.player = player;
        this.room = room;
    }

    public void startBattle() {
        Enemy enemy = room.getEnemy();
        System.out.println("Battle started with " + enemy.getName());

        while(player.getHealth() > 0 && enemy.getHealth() > 0) {
            player.attack(enemy);
            System.out.println("Enemy health: " + enemy.getHealth());

            if(enemy.getHealth() <= 0) break;

            enemy.attack(player);
            System.out.println("Player health: " + player.getHealth());
        }

        if(player.getHealth() > 0) {
            System.out.println("Player won room " + room.getRoomNumber());

        } else {
            System.out.println("Player was defeated in room " + room.getRoomNumber());
        }
    }
}