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
            List<Hero> aliveEnemies = new ArrayList<>();
            for (Hero enemy : enemyParty.getHeroes()) {
                if (!enemy.isDefeated()) {
                    aliveEnemies.add(enemy);
                }
            }

            if (!aliveEnemies.isEmpty()) {
                // Use the TARGET that was selected by the player
                int mainDamage = this.attack - target.getDefense();
                if (mainDamage < 0) mainDamage = 1;
                target.takeDamage(mainDamage);

                // Hit up to 2 MORE enemies (excluding the main target)
                int additionalHits = 0;
                for (Hero enemy : aliveEnemies) {
                    if (enemy != target && additionalHits < 2) {
                        int secondaryDamage = mainDamage / 4;
                        if (secondaryDamage < 1) secondaryDamage = 1;
                        enemy.takeDamage(secondaryDamage);
                        additionalHits++;
                    }
                }

                System.out.println(heroClass + " uses Berserker Attack on " +
                        target.getHeroClass() + ", hitting " + additionalHits + " additional enemies!");
            }
        }
    }
}