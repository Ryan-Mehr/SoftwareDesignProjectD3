package Classes.Heros;

import Classes.Party;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Chaos extends Hero {
    private Random random = new Random();

    public Chaos() {
        this.heroClass = HeroClass.CHAOS;
        // Chaos gets +3 attack, +5 health per level
        this.attackLevelUpIncrement += 3;  // Base 1 + 3 = 4 total
        this.healthPointsLevelUpIncrement += 5; // Base 5 + 5 = 10 total
        recalculateStats();
    }

    @Override
    public boolean canUseSpecialAbility() {
        return mana >= 30; // Fireball costs 30 mana
    }

    @Override
    public String getSpecialAbilityName() {
        return "Fireball";
    }

    @Override
    public int getSpecialAbilityCost() {
        return 30;
    }

    @Override
    public void useSpecialAbility(Hero target, Party friendlyParty, Party enemyParty) {
        // Fireball affects up to 3 enemy units
        List<Hero> aliveEnemies = new ArrayList<>();
        for (Hero enemy : enemyParty.getHeroes()) {
            if (!enemy.isDefeated()) {
                aliveEnemies.add(enemy);
            }
        }

        int targetsHit = Math.min(3, aliveEnemies.size());
        for (int i = 0; i < targetsHit; i++) {
            Hero enemy = aliveEnemies.get(i);
            int damage = this.attack * 2; // Fireball does double damage
            enemy.takeDamage(damage);
        }
        System.out.println(heroClass + " casts Fireball hitting " + targetsHit + " enemies!");
    }

    // Chain Lightning - separate method
    public void chainLightning(Hero firstTarget, Party enemyParty){
        if(!spendMana(40)) return;
        List<Hero> aliveEnemies = new ArrayList<>();
        for (Hero enemy : enemyParty.getHeroes()) {
            if(!enemy.isDefeated()) {
                aliveEnemies.add(enemy);
            }
        }
        if(aliveEnemies.isEmpty())return;

        // Randomize order starting with the chosen target
        Collections.shuffle(aliveEnemies);
        // Now move chosen target to the front
        aliveEnemies.remove(firstTarget);
        aliveEnemies.add(0, firstTarget);

        int damage = this.attack;
        for(Hero enemy : aliveEnemies){
            enemy.takeDamage(damage);
            damage = damage /4; // Each subsequent gets 25% of previous
            if (damage < 1) damage = 1;
        }
        System.out.println(heroClass + " casts Chain Lightning!");
    }
}