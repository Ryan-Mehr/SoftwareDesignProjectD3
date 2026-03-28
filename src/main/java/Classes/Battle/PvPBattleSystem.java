package Classes.Battle;

import Classes.Heros.Hero;
import Classes.Party;
import Repository.PvPRepository;
import Repository.UserRepository;

import java.util.*;

public class PvPBattleSystem {
    private Party party1;
    private Party party2;
    private List<Hero> turnOrder;
    private Queue<Hero> waitQueue = new LinkedList<>();
    private int currentTurnIndex = 0;
    private boolean battleInitialized = false;
    private boolean battleOver = false;
    private List<String> battleLog = new ArrayList<>();
    private Party winningParty;
    private Party losingParty;

    // Track which party each hero belongs to
    private Map<Hero, Party> heroPartyMap = new HashMap<>();

    public PvPBattleSystem(Party party1, Party party2) {
        this.party1 = party1;
        this.party2 = party2;

        // Map heroes to their parties
        for (Hero hero : party1.getHeroes()) {
            heroPartyMap.put(hero, party1);
        }
        for (Hero hero : party2.getHeroes()) {
            heroPartyMap.put(hero, party2);
        }
    }

    // Initialize the battle - call this before starting
    public void initializeBattle() {
        System.out.println("=== Initializing Battle ===");
        battleLog.clear();
        battleLog.add("=== PvP BATTLE STARTED ===");
        battleLog.add(party1.getPartyName() + " vs " + party2.getPartyName());

        determineTurnOrder();
        battleInitialized = true;
        battleOver = false;
        waitQueue.clear();

        battleLog.add("\nTurn order determined:");
        for (int i = 0; i < turnOrder.size(); i++) {
            battleLog.add((i + 1) + ". " + turnOrder.get(i).getHeroClass() +
                    " (Level " + turnOrder.get(i).getLevel() + ")");
        }
    }

    // Determine the turn order based on level and attack
    private void determineTurnOrder() {
        System.out.println("=== determineTurnOrder START ===");

        turnOrder = new ArrayList<>();

        System.out.println("Party1 heroes count: " + party1.getHeroes().size());
        for (Hero hero : party1.getHeroes()) {
            System.out.println("  Hero: " + hero.getHeroClass() + " - Defeated: " + hero.isDefeated());
        }

        System.out.println("Party2 heroes count: " + party2.getHeroes().size());
        for (Hero hero : party2.getHeroes()) {
            System.out.println("  Hero: " + hero.getHeroClass() + " - Defeated: " + hero.isDefeated());
        }

        turnOrder.addAll(party1.getHeroes());
        turnOrder.addAll(party2.getHeroes());
        System.out.println("Before removal - turnOrder size: " + turnOrder.size());

        // Remove defeated heroes
        int beforeRemove = turnOrder.size();
        turnOrder.removeIf(Hero::isDefeated);
        int afterRemove = turnOrder.size();
        System.out.println("Removed " + (beforeRemove - afterRemove) + " defeated heroes");
        System.out.println("After removal - turnOrder size: " + turnOrder.size());

        // Sort by highest level, then highest attack
        turnOrder.sort((h1, h2) -> {
            if (h1.getLevel() != h2.getLevel()) {
                return h2.getLevel() - h1.getLevel();
            }
            return h2.getAttack() - h1.getAttack();
        });

        currentTurnIndex = 0;

        System.out.println("Final turnOrder size: " + turnOrder.size());
        for (int i = 0; i < turnOrder.size(); i++) {
            System.out.println("  Turn " + i + ": " + turnOrder.get(i).getHeroClass());
        }

        System.out.println("=== determineTurnOrder END ===");
    }

    // Add this method to check if a hero is in the turn order
    private boolean isInTurnOrder(Hero hero) {
        return turnOrder.contains(hero);
    }

    // Update your nextTurn() method
    public void nextTurn() {
        if (!battleInitialized) {
            initializeBattle();
        }

        // Remove any defeated heroes from turnOrder
        turnOrder.removeIf(Hero::isDefeated);

        // Process waiting heroes at end of round
        if (currentTurnIndex == 0 && !waitQueue.isEmpty()) {
            processWaitQueue();
        }

        // If turnOrder is empty, battle is over
        if (turnOrder.isEmpty()) {
            return;
        }

        // Move to next hero
        currentTurnIndex = (currentTurnIndex + 1) % turnOrder.size();
    }

    // Add a hero to the wait queue
    public void addToWaitQueue(Hero hero) {
        waitQueue.add(hero);
        battleLog.add(hero.getHeroClass() + " chose to wait and will act at the end of the round.");
    }

    // Process all waiting heroes
    private void processWaitQueue() {
        battleLog.add("\n--- Processing waiting heroes ---");
        while (!waitQueue.isEmpty()) {
            Hero waitingHero = waitQueue.poll();
            if (!waitingHero.isDefeated()) {
                // Process waiting hero's turn (simplified - they just attack)
                Party enemyParty = getEnemyParty(waitingHero);
                List<Hero> aliveEnemies = getAliveHeroes(enemyParty);

                if (!aliveEnemies.isEmpty()) {
                    Hero target = aliveEnemies.get(new Random().nextInt(aliveEnemies.size()));
                    int damage = waitingHero.getAttack() - target.getDefense();
                    if (damage < 0) damage = 1;

                    battleLog.add(waitingHero.getHeroClass() + " acts from wait and attacks " +
                            target.getHeroClass() + " for " + damage + " damage!");
                    target.takeDamage(damage);
                }
            }
        }
    }

    // Get the current hero whose turn it is
    // Update your getCurrentHero() method
    public Hero getCurrentHero() {
        if (!battleInitialized) {
            initializeBattle();
        }

        // Remove any defeated heroes
        turnOrder.removeIf(Hero::isDefeated);

        if (turnOrder.isEmpty()) {
            return null;
        }

        // Make sure index is valid
        if (currentTurnIndex >= turnOrder.size()) {
            currentTurnIndex = 0;
        }

        return turnOrder.get(currentTurnIndex);
    }

    // Get alive heroes from a party
    private List<Hero> getAliveHeroes(Party party) {
        List<Hero> alive = new ArrayList<>();
        for (Hero hero : party.getHeroes()) {
            if (!hero.isDefeated()) {
                alive.add(hero);
            }
        }
        return alive;
    }

    // Get the enemy party for a hero
    public Party getEnemyParty(Hero hero) {
        Party heroParty = heroPartyMap.get(hero);
        return heroParty == party1 ? party2 : party1;
    }

    // Get the friendly party for a hero
    public Party getFriendlyParty(Hero hero) {
        return heroPartyMap.get(hero);
    }

    // Process an attack action
    public String processAttack(Hero attacker, Hero target) {
        System.out.println(">>> PvPBattleSystem.processAttack START");
        System.out.println("attacker: " + attacker.getHeroClass() + " (ATK: " + attacker.getAttack() + ")");
        System.out.println("target: " + target.getHeroClass() + " (DEF: " + target.getDefense() + ", HP: " + target.getHp() + ")");

        StringBuilder result = new StringBuilder();

        // Calculate damage
        int damage = attacker.getAttack() - target.getDefense();
        if (damage < 0) damage = 1;

        System.out.println("Calculated damage: " + damage);

        result.append(attacker.getHeroClass()).append(" attacks ").append(target.getHeroClass()).append(" for ").append(damage).append(" damage!\n");

        // Apply damage
        int oldHp = target.getHp();
        target.takeDamage(damage);
        System.out.println("HP changed from " + oldHp + " to " + target.getHp());

        // Check if target was defeated
        if (target.isDefeated()) {
            result.append(target.getHeroClass()).append(" has been defeated!\n");
            System.out.println(target.getHeroClass() + " DEFEATED!");
        }

        battleLog.add(result.toString());
        System.out.println(">>> PvPBattleSystem.processAttack END");
        return result.toString();
    }

    // Process a defend action
    public String processDefend(Hero defender) {
        String result = defender.getHeroClass() + " takes a defensive stance!\n";
        defender.defend();
        battleLog.add(result);
        return result;
    }

    // Process a wait action
    public String processWait(Hero hero) {
        String result = hero.getHeroClass() + " prepares to wait.\n";
        addToWaitQueue(hero);
        battleLog.add(result);
        return result;
    }

    // Process a cast action
    public String processCast(Hero caster, Hero target) {
        StringBuilder result = new StringBuilder();

        if (!caster.canUseSpecialAbility()) {
            result.append("Not enough mana to cast ").append(caster.getSpecialAbilityName()).append("!\n");
            return result.toString();
        }

        result.append(caster.getHeroClass()).append(" casts ").append(caster.getSpecialAbilityName())
                .append(" on ").append(target.getHeroClass()).append("!\n");

        // Get parties for the caster
        Party friendlyParty = getFriendlyParty(caster);
        Party enemyParty = getEnemyParty(caster);

        // Use the special ability
        caster.useSpecialAbility(target, friendlyParty, enemyParty);

        battleLog.add(result.toString());
        return result.toString();
    }

    // Check if battle is over
    public boolean isBattleOver() {
        boolean party1Alive = false;
        boolean party2Alive = false;

        for (Hero hero : party1.getHeroes()) {
            if (!hero.isDefeated()) {
                party1Alive = true;
                break;
            }
        }

        for (Hero hero : party2.getHeroes()) {
            if (!hero.isDefeated()) {
                party2Alive = true;
                break;
            }
        }

        battleOver = !party1Alive || !party2Alive;
        return battleOver;
    }

    // Determine the winner when battle ends
    public void determineWinner() {
        boolean party1Alive = !getAliveHeroes(party1).isEmpty();

        if (party1Alive) {
            winningParty = party1;
            losingParty = party2;
        } else {
            winningParty = party2;
            losingParty = party1;
        }

        battleLog.add("\n=== BATTLE OVER ===");
        battleLog.add(winningParty.getPartyName() + " wins!");

        // Display final stats
        battleLog.add("\nFinal Party Status:");
        battleLog.add(party1.getPartyName() + ":");
        for (Hero hero : party1.getHeroes()) {
            battleLog.add("  " + hero.getHeroClass() + " - " +
                    (hero.isDefeated() ? "DEFEATED" : "HP: " + hero.getHp() + "/" + hero.getMaxHp()));
        }

        battleLog.add(party2.getPartyName() + ":");
        for (Hero hero : party2.getHeroes()) {
            battleLog.add("  " + hero.getHeroClass() + " - " +
                    (hero.isDefeated() ? "DEFEATED" : "HP: " + hero.getHp() + "/" + hero.getMaxHp()));
        }
    }

    // Save results to database
    public void saveResults() {
        if (winningParty == null) {
            determineWinner();
        }

        // Save match result
        PvPRepository.getInstance().saveMatchResult(
                party1.getUsername(),
                party2.getUsername(),
                winningParty.getUsername()
        );

        // Update winner's win count
        UserRepository.getInstance().addWinToUser(winningParty.getUsername());

        battleLog.add("\n✓ Match saved to database. Winner: " + winningParty.getUsername());
    }

    // Getters
    public Party getWinningParty() {
        return winningParty;
    }

    public Party getLosingParty() {
        return losingParty;
    }

    public List<String> getBattleLog() {
        return battleLog;
    }

    public boolean isBattleInitialized() {
        return battleInitialized;
    }

    public List<Hero> getAliveEnemies(Hero hero) {
        Party enemyParty = getEnemyParty(hero);
        return getAliveHeroes(enemyParty);
    }
}