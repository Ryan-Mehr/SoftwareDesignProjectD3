package Classes.Heros;

import Classes.Party;
import java.util.*;

public class Order extends Hero {
    public Order() {
        this.heroClass = HeroClass.ORDER;
        // Order gets +5 mana, +2 defense per level
        this.manaLevelUpIncrement += 5;  // Base 2 + 5 = 7 total
        this.defenseLevelUpIncrement += 2; // Base 1 + 2 = 3 total
        recalculateStats();
    }

    @Override
    public boolean canUseSpecialAbility() {
        return mana >= 25; // Protect costs 25 mana
    }

    @Override
    public String getSpecialAbilityName() {
        return "Protect";
    }

    @Override
    public int getSpecialAbilityCost() {
        return 25;
    }

    @Override
    public void useSpecialAbility(Hero target, Party friendlyParty, Party enemyParty) {
        if (spendMana(25)) {
            // Cast shield on all party members for 10% of each hero's health
            for (Hero hero : friendlyParty.getHeroes()) {
                if (!hero.isDefeated()) {
                    int shieldAmount = hero.getMaxHp() / 10;
                    hero.addShield(shieldAmount);
                }
            }
            System.out.println(heroClass + " casts Protect on the entire party!");
        }
    }

    // Heal - separate method from the heal ability
    public void healLowestAlly(Party friendlyParty) {
        if(!spendMana(35)) return;

        //find the hero with the lowest current health
        Hero lowestHero = null;
        int lowestHp = Integer.MAX_VALUE;

        for (Hero hero : friendlyParty.getHeroes()) {
            if(!hero.isDefeated() && hero.getHp() < lowestHp) {
                lowestHp = hero.getHp();
                lowestHero = hero;
            }
        }
        if (lowestHero != null) {
            int healAmount = lowestHero.getMaxHp() / 4; // 25% of original health
            lowestHero.heal(healAmount);
            System.out.println(heroClass +  " heals " + lowestHero.getHeroClass() + " for " + healAmount + " HP!");

        }
    }

}