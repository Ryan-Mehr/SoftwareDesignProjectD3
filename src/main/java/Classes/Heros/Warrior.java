package Classes.Heros;

import Classes.Party;
import java.util.ArrayList;
import java.util.List;

public class Warrior extends Hero {
    public Warrior() {
        this.heroClass = HeroClass.WARRIOR;
        // Warrior gets +2 attack, +3 defense per level
        this.attackLevelUpIncrement += 2;  // Base 1 + 2 = 3 total
        this.defenseLevelUpIncrement += 3; // Base 1 + 3 = 4 total
        recalculateStats();
    }

    @Override
    public boolean canUseSpecialAbility() {
        return mana >= 60; // Berserker attack costs 60 mana
    }

    @Override
    public String getSpecialAbilityName() {
        return "Berserker Attack";
    }

    @Override
    public int getSpecialAbilityCost() {
        return 60;
    }

    @Override
    public void useSpecialAbility(Hero target, Party friendlyParty, Party enemyParty) {
        if (spendMana(60)) {
            // When attacking a unit, damage 2 more units for 25% of the original damage
            List<Hero> aliveEnemies = new ArrayList<>();
            for (Hero enemy : enemyParty.getHeroes()) {
                if (!enemy.isDefeated()) {
                    aliveEnemies.add(enemy);
                }
            }

            if (!aliveEnemies.isEmpty()) {
                // Main target (first alive enemy)
                Hero mainTarget = aliveEnemies.get(0);
                int mainDamage = this.attack - mainTarget.getDefense();
                if (mainDamage < 0) mainDamage = 1;
                mainTarget.takeDamage(mainDamage);

                // Hit up to 2 more enemies for 25% damage
                for (int i = 1; i < Math.min(3, aliveEnemies.size()); i++) {
                    Hero secondaryTarget = aliveEnemies.get(i);
                    int secondaryDamage = mainDamage / 4;
                    if (secondaryDamage < 1) secondaryDamage = 1;
                    secondaryTarget.takeDamage(secondaryDamage);
                }

                System.out.println(heroClass + " uses Berserker Attack, hitting multiple enemies!");
            }
        }
    }
}