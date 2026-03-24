package Classes.Battle;

import Classes.Heros.Hero;
import Classes.Party;
import Repository.PvPRepository;
import Repository.UserRepository;
import java.util.*;

public class BattleManager {
    private final Party party1;
    private final Party party2;
    private List<Hero> turnOrder;
    private Queue<Hero> waitQueue = new LinkedList<>();
    private int currentTurnIndex = 0;
    private boolean battleActive = true;
    private Party winningParty;
    private List<String> battleLog = new ArrayList<>();

    public BattleManager(Party party1, Party party2) {
        this.party1 = party1;
        this.party2 = party2;
    }

    public void startBattle() {
        determineTurnOrder();
        battleActive = true;

        log("=== BATTLE STARTED ===");
        log(party1.getPartyName() + " vs " + party2.getPartyName());

        while (battleActive && !isBattleOver()) {
            Hero currentHero = turnOrder.get(currentTurnIndex);

            if (!currentHero.isDefeated() && !currentHero.isStunned()) {
                processHeroTurn(currentHero);
            } else if (currentHero.isStunned()) {
                log(currentHero.getHeroClass() + " is stunned and skips turn!");
                currentHero.processEndOfTurn();
            }

            currentTurnIndex = (currentTurnIndex + 1) % turnOrder.size();

            // At end of round, process waiting heroes
            if (currentTurnIndex == 0) {
                processWaitQueue();
            }
        }

        determineWinner();
    }

    private void determineTurnOrder() {
        turnOrder = new ArrayList<>();
        turnOrder.addAll(party1.getHeroes());
        turnOrder.addAll(party2.getHeroes());

        turnOrder.removeIf(Hero::isDefeated);

        // Sort by highest level, then highest attack
        turnOrder.sort((h1, h2) -> {
            if (h1.getLevel() != h2.getLevel()) {
                return h2.getLevel() - h1.getLevel();
            }
            return h2.getAttack() - h1.getAttack();
        });

        log("Turn order determined:");
        for (int i = 0; i < turnOrder.size(); i++) {
            log((i+1) + ". " + turnOrder.get(i).getHeroClass() + " (Lvl " + turnOrder.get(i).getLevel() + ")");
        }
    }

    private void processHeroTurn(Hero hero) {
        // Determine which party this hero belongs to
        Party friendlyParty = party1.getHeroes().contains(hero) ? party1 : party2;
        Party enemyParty = friendlyParty == party1 ? party2 : party1;

        // Simple AI for demo - you can replace with player input
        List<Hero> aliveEnemies = getAliveHeroes(enemyParty);
        if (aliveEnemies.isEmpty()) return;

        Hero target = aliveEnemies.get(0);

        // 30% chance to use special ability
        Random rand = new Random();
        if (hero.canUseSpecialAbility() && rand.nextInt(100) < 30) {
            log(hero.getHeroClass() + " uses " + hero.getSpecialAbilityName() + "!");
            hero.useSpecialAbility(target, friendlyParty, enemyParty);
        } else {
            // Basic attack
            int damage = hero.getAttack() - target.getDefense();
            if (damage < 0) damage = 1;
            target.takeDamage(damage);
            log(hero.getHeroClass() + " attacks " + target.getHeroClass() + " for " + damage + " damage!");
        }

        // Apply defend end-of-turn effect
        hero.applyDefendEndOfTurn();

        // Check for defeated enemies
        if (target.isDefeated()) {
            log(target.getHeroClass() + " has been defeated!");
        }
    }

    private void processWaitQueue() {
        while (!waitQueue.isEmpty()) {
            Hero waitingHero = waitQueue.poll();
            if (!waitingHero.isDefeated()) {
                log(waitingHero.getHeroClass() + " acts from wait!");
                processHeroTurn(waitingHero);
            }
        }
    }

    private List<Hero> getAliveHeroes(Party party) {
        List<Hero> alive = new ArrayList<>();
        for (Hero hero : party.getHeroes()) {
            if (!hero.isDefeated()) {
                alive.add(hero);
            }
        }
        return alive;
    }

    private boolean isBattleOver() {
        boolean party1Alive = !getAliveHeroes(party1).isEmpty();
        boolean party2Alive = !getAliveHeroes(party2).isEmpty();
        return !party1Alive || !party2Alive;
    }

    private void determineWinner() {
        boolean party1Alive = !getAliveHeroes(party1).isEmpty();
        winningParty = party1Alive ? party1 : party2;

        log("\n=== BATTLE OVER ===");
        log(winningParty.getPartyName() + " wins!");

        // Display final stats
        log("\nFinal Stats:");
        for (Hero hero : party1.getHeroes()) {
            log(party1.getPartyName() + " - " + hero.getHeroClass() + ": " +
                    (hero.isDefeated() ? "DEFEATED" : "HP: " + hero.getHp() + "/" + hero.getMaxHp()));
        }
        for (Hero hero : party2.getHeroes()) {
            log(party2.getPartyName() + " - " + hero.getHeroClass() + ": " +
                    (hero.isDefeated() ? "DEFEATED" : "HP: " + hero.getHp() + "/" + hero.getMaxHp()));
        }
    }

    public void saveResults() {
        if (winningParty == null) {
            determineWinner();
        }

        PvPRepository.getInstance().saveMatchResult(
                party1.getUsername(),
                party2.getUsername(),
                winningParty.getUsername()
        );

        UserRepository.getInstance().addWinToUser(winningParty.getUsername());
        log("\n✓ Match saved to database!");
    }

    public List<String> getBattleLog() {
        return battleLog;
    }

    private void log(String message) {
        System.out.println(message);
        battleLog.add(message);
    }
}