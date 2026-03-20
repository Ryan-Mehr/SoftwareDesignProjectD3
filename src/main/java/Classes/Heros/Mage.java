package Classes.Heros;

import Classes.Party;

public class Mage extends Hero {
    public Mage() {
        this.heroClass = HeroClass.MAGE;
        // Mage gets +5 mana, +1 attack per level
        this.manaLevelUpIncrement += 5;  // Base 2 + 5 = 7 total
        this.attackLevelUpIncrement += 1; // Base 1 + 1 = 2 total
        recalculateStats();
    }

    @Override
    public boolean canUseSpecialAbility() {
        return mana >= 80; // Replenish costs 80 mana
    }

    @Override
    public String getSpecialAbilityName() {
        return "Replenish";
    }

    @Override
    public int getSpecialAbilityCost() {
        return 80;
    }

    @Override
    public void useSpecialAbility(Hero target, Party friendlyParty, Party enemyParty) {
        if (spendMana(80)) {
            // Replenish 30 mana points to all friendly units and 60 to self
            for (Hero hero : friendlyParty.getHeroes()) {
                if (!hero.isDefeated()) {
                    if (hero == this) {
                        hero.restoreMana(60);
                    } else {
                        hero.restoreMana(30);
                    }
                }
            }
            System.out.println(heroClass + " casts Replenish, restoring mana to the entire party!");
        }
    }
}