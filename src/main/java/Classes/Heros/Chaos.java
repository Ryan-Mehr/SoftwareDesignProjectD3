package Classes.Heros;

import Classes.Party;
import java.util.ArrayList;
import java.util.List;

public class Chaos extends Hero {
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
        if (spendMana(30)) {
            // Fireball affects up to 3 enemy units
            int targetsHit = 0;
            List<Hero> aliveEnemies = new ArrayList<>();

            // Get all alive enemies
            for (Hero enemy : enemyParty.getHeroes()) {
                if (!enemy.isDefeated()) {
                    aliveEnemies.add(enemy);
                }
            }

            // Hit up to 3 enemies
            for (int i = 0; i < Math.min(3, aliveEnemies.size()); i++) {
                Hero enemy = aliveEnemies.get(i);
                int damage = this.attack * 2; // Fireball does double damage
                enemy.takeDamage(damage);
                targetsHit++;
            }

            System.out.println(heroClass + " casts Fireball hitting " + targetsHit + " enemies!");
        }
    }
}