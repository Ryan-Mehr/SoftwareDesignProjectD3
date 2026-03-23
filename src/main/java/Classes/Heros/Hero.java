package Classes.Heros;

import Classes.Party;

public abstract class Hero {
    protected String heroID = "";
    protected HeroClass heroClass;
    protected int level = 1;
    protected int maxLevel = 20;

    // Battle stats
    protected int hp;
    protected int maxHp = 100;
    protected int mana;
    protected int maxMana = 50;
    protected int attack = 5;
    protected int defense = 5;
    protected int shield = 0; // For Protect ability

    // Level up increments
    protected int attackLevelUpIncrement = 1;
    protected int defenseLevelUpIncrement = 1;
    protected int healthPointsLevelUpIncrement = 5;
    protected int manaLevelUpIncrement = 2;

    // Battle status
    protected boolean defeated = false;
    protected boolean stunned = false;
    protected boolean defending = false;
    protected int stunTurns = 0;

    // Constructor
    public Hero() {
        this.hp = maxHp;
        this.mana = maxMana;
    }

    // Getters
    public String getHeroID() { return heroID; }
    public HeroClass getHeroClass() { return heroClass; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getShield() { return shield; }
    public boolean isDefeated() { return defeated; }
    public boolean isStunned() { return stunned; }
    public boolean isDefending() { return defending; }

    // Setters
    public void setLevel(int level) {
        this.level = level;
        recalculateStats();
    }

    public void setHp(int hp) {
        this.hp = hp;
        if (this.hp > maxHp) this.hp = maxHp;
        if (this.hp <= 0) {
            this.hp = 0;
            this.defeated = true;
        }
    }

    public void setMana(int mana) {
        this.mana = mana;
        if (this.mana > maxMana) this.mana = maxMana;
    }

    protected void recalculateStats() {
        // Reset to base stats
        attack = 5;
        defense = 5;
        maxHp = 100;
        maxMana = 50;

        // Add per-level bonuses
        for (int i = 2; i <= level; i++) {
            attack += attackLevelUpIncrement;
            defense += defenseLevelUpIncrement;
            maxHp += healthPointsLevelUpIncrement;
            maxMana += manaLevelUpIncrement;
        }

        // Ensure current HP/Mana don't exceed max
        if (hp == 0) hp = maxHp; // New hero starts with full HP
        if (mana == 0) mana = maxMana; // New hero starts with full mana
    }

    public void takeDamage(int damage) {
        System.out.println(">>> Hero.takeDamage called for " + this.heroClass);
        System.out.println("  Before - HP: " + hp + ", Defending: " + defending);

        System.out.println("\t*** takeDamage::damage=" + damage);
        System.out.println("\t*** takeDamage::shield=" + shield);
        // Check shield first (if you have shield implemented)
        if (shield > 0) {
            int shieldDamage = Math.min(shield, damage);
            shield -= shieldDamage;
            damage -= shieldDamage;
            System.out.println("  Shield absorbed " + shieldDamage + " damage, remaining damage: " + damage);
        }

        System.out.println("\t*** takeDamage::defending=" + defending + " defense=" + defense);
        // Check defending
        if (defending) {
            damage -= defense;
            if (damage < 0) damage = 0;
            defending = false;
            System.out.println("  Was defending, reduced damage to: " + damage);
        }

        int damageToTake = damage - defense;
        if (damageToTake < 0) damageToTake = 0;

        System.out.println("  Final damage to take: " + damageToTake);

        hp -= damageToTake;
        System.out.println("  After - HP: " + hp + "/" + maxHp);

        if (hp <= 0) {
            hp = 0;
            defeated = true;
            System.out.println("  " + heroClass + " has been defeated!");
        }

        System.out.println("<<< Hero.takeDamage END");
    }

    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
        System.out.println(heroClass + " heals for " + amount + ". HP: " + hp + "/" + maxHp);
    }

    public void restoreMana(int amount) {
        mana += amount;
        if (mana > maxMana) mana = maxMana;
        System.out.println(heroClass + " restores " + amount + " mana. Mana: " + mana + "/" + maxMana);
    }

    public boolean spendMana(int amount) {
        if (mana >= amount) {
            mana -= amount;
            return true;
        }
        System.out.println(heroClass + " doesn't have enough mana! Need " + amount + ", have " + mana);
        return false;
    }

    public void defend() {
        defending = true;
        System.out.println(heroClass + " is defending!");
    }

    public void waitAction() {
        System.out.println(heroClass + " is waiting...");
    }

    public void addShield(int amount) {
        shield += amount;
        System.out.println(heroClass + " gains a shield for " + amount + " damage!");
    }

    public void stun() {
        stunned = true;
        stunTurns = 1;
        System.out.println(heroClass + " is stunned!");
    }

    public void processEndOfTurn() {
        if (stunned && stunTurns > 0) {
            stunTurns--;
            if (stunTurns == 0) {
                stunned = false;
                System.out.println(heroClass + " is no longer stunned!");
            }
        }
    }

    // Special abilities to be overridden by subclasses
    public boolean canUseSpecialAbility() {
        return false;
    }

    public void useSpecialAbility(Hero target, Party friendlyParty, Party enemyParty) {
        // Override in subclasses
    }

    public String getSpecialAbilityName() {
        return "None";
    }

    public int getSpecialAbilityCost() {
        return 0;
    }

    @Override
    public String toString() {
        return heroClass + " (Lvl " + level + ") HP: " + hp + "/" + maxHp + " MP: " + mana + "/" + maxMana;
    }
}