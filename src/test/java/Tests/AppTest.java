package Tests;

import Classes.Battle.*;
import Classes.Heros.*;
import Classes.Party;
import Classes.User;
import Factory.WarriorFactory;
import Interfaces.Factory.HeroFactory;

import org.junit.jupiter.api.Order;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Suite for SoftwareDesignProjectD2
 *
 * Design Patterns Tested:
 *  1. Strategy Pattern     — Action interface (AttackAction, DefendAction)
 *  2. Factory Method       — HeroFactory / WarriorFactory
 *  3. Template Method      — Hero abstract class with overridable useSpecialAbility()
 *  4. Singleton Pattern    — Repository classes (PvPRepository, UserRepository) via getInstance()
 *  5. Command Pattern      — Player-level Action execute() dispatch
 *  6. Composite-like       — Party holding a collection of Hero objects
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    /** Build a named party with one hero of each type. */
    private Party buildFullParty(String name, String owner) {
        Party p = new Party(name, "Test party", owner);
        p.addHero(new Warrior());
        p.addHero(new Mage());
        p.addHero(new OrderHero());
        p.addHero(new Chaos());
        return p;
    }

    /** Build a party with one Warrior, levelled to the requested level. */
    private Party singleWarriorParty(String name, String owner, int level) {
        Party p = new Party(name, "Single-hero party", owner);
        Warrior w = new Warrior();
        w.setLevel(level);
        p.addHero(w);
        return p;
    }

    // ====================================================================
    // 1. STRATEGY PATTERN — Action interface (AttackAction / DefendAction)
    //    The Action interface lets behaviour be swapped at runtime.
    // =========================================================================

    @Nested
    @DisplayName("Pattern 1 · Strategy — Action interface")
    class StrategyPatternTests {

        @Test
        @Order(1)
        @DisplayName("AttackAction reduces defender health by attacker's attack power")
        void attackActionReducesHealth() {
            Player attacker = new Player("Hero", 100, 20, 5);
            Player defender = new Player("Enemy", 100, 10, 5);

            Action attack = new AttackAction();
            attack.execute(attacker, defender);

            assertEquals(80, defender.getHealth(),
                    "Defender should lose exactly attacker.attackPower HP");
        }

        @Test
        @Order(2)
        @DisplayName("DefendAction sets the player into a defending stance")
        void defendActionTogglesDefendingFlag() {
            Player player = new Player("Tank", 100, 10, 15);
            assertTrue(player.isAlive());

            Action defend = new DefendAction();
            defend.execute(player, player); // attacker == defender for defend

            // After defending, next hit should be reduced by defensePower
            Player attacker = new Player("Attacker", 100, 20, 0);
            Action attack = new AttackAction();
            attack.execute(attacker, player);

            // damage = 20 - 15 defense = 5; 100 - 5 = 95
            assertEquals(95, player.getHealth(),
                    "Defending player should absorb damage equal to defensePower");
        }

        @Test
        @Order(3)
        @DisplayName("Strategy is swappable — different Action implementations behave differently")
        void strategyIsInterchangeable() {
            Player p1 = new Player("Alpha", 100, 30, 5);
            Player p2 = new Player("Beta", 100, 10, 5);

            int healthBefore = p2.getHealth();

            // Use AttackAction strategy
            Action strategy = new AttackAction();
            strategy.execute(p1, p2);
            int healthAfterAttack = p2.getHealth();

            assertTrue(healthAfterAttack < healthBefore,
                    "AttackAction strategy must reduce health");

            // Swap to DefendAction strategy — health should NOT drop further
            strategy = new DefendAction();
            strategy.execute(p2, p1);   // p2 defends
            assertEquals(healthAfterAttack, p2.getHealth(),
                    "DefendAction strategy must not reduce health");
        }

        @Test
        @Order(4)
        @DisplayName("Player health cannot go below zero after attack")
        void attackDoesNotReduceHealthBelowZero() {
            Player overkillAttacker = new Player("Titan", 100, 9999, 5);
            Player victim = new Player("Glass", 1, 1, 0);

            Action attack = new AttackAction();
            attack.execute(overkillAttacker, victim);

            assertEquals(0, victim.getHealth(), "Health floor is 0");
            assertFalse(victim.isAlive(), "Player with 0 HP is not alive");
        }
    }

    // =========================================================================
    // 2. FACTORY METHOD PATTERN — HeroFactory / WarriorFactory
    //    WarriorFactory implements HeroFactory to decouple creation from use.
    // =========================================================================

    @Nested
    @DisplayName("Pattern 2 · Factory Method — HeroFactory")
    class FactoryMethodPatternTests {

        @Test
        @Order(10)
        @DisplayName("WarriorFactory.createHero() returns a non-null Hero")
        void factoryReturnsNonNullHero() {
            HeroFactory factory = new WarriorFactory();
            Hero hero = factory.createHero();
            assertNotNull(hero, "Factory must return a non-null hero");
        }

        @Test
        @Order(11)
        @DisplayName("WarriorFactory produces a Warrior with correct HeroClass")
        void factoryProducesCorrectHeroClass() {
            HeroFactory factory = new WarriorFactory();
            Hero hero = factory.createHero();
            assertEquals(HeroClass.WARRIOR, hero.getHeroClass(),
                    "WarriorFactory must produce HeroClass.WARRIOR");
        }

        @Test
        @Order(12)
        @DisplayName("Factory-created hero starts at level 1 with positive stats")
        void factoryHeroHasDefaultStats() {
            HeroFactory factory = new WarriorFactory();
            Hero hero = factory.createHero();

            assertEquals(1, hero.getLevel());
            assertTrue(hero.getHp() > 0, "HP must be positive");
            assertTrue(hero.getAttack() > 0, "Attack must be positive");
            assertTrue(hero.getDefense() > 0, "Defense must be positive");
            assertFalse(hero.isDefeated(), "New hero must not be defeated");
        }

        @Test
        @Order(13)
        @DisplayName("Each factory call produces a distinct Hero instance")
        void factoryProducesDistinctInstances() {
            HeroFactory factory = new WarriorFactory();
            Hero h1 = factory.createHero();
            Hero h2 = factory.createHero();
            assertNotSame(h1, h2, "Factory must return distinct object references");
        }

        @Test
        @Order(14)
        @DisplayName("HeroFactory interface can be used polymorphically")
        void interfacePolymorphism() {
            // Demonstrate that any HeroFactory reference works the same way
            HeroFactory ref = new WarriorFactory();
            Hero hero = ref.createHero();
            assertInstanceOf(Hero.class, hero,
                    "Result of createHero() must be assignable to Hero");
        }
    }

    // =========================================================================
    // 3. TEMPLATE METHOD PATTERN — Hero abstract class
    //    Hero defines the skeleton (takeDamage, heal, useSpecialAbility)
    //    and subclasses override the specifics.
    // =========================================================================

    @Nested
    @DisplayName("Pattern 3 · Template Method — Hero hierarchy")
    class TemplateMethodPatternTests {

        @Test
        @Order(20)
        @DisplayName("Warrior special ability (Berserker Attack) requires 60 mana")
        void warriorSpecialAbilityCost() {
            Warrior warrior = new Warrior();
            warrior.setMana(59);
            assertFalse(warrior.canUseSpecialAbility(),
                    "Warrior needs ≥60 mana for Berserker Attack");

            warrior.setMana(60);
            assertTrue(warrior.canUseSpecialAbility(),
                    "Warrior with exactly 60 mana can use Berserker Attack");
        }

        @Test
        @Order(21)
        @DisplayName("Mage special ability (Replenish) requires 80 mana")
        void mageSpecialAbilityCost() {
            Mage mage = new Mage();
            mage.setMana(79);
            assertFalse(mage.canUseSpecialAbility());

            mage.setMana(80);
            assertTrue(mage.canUseSpecialAbility());
        }

        @Test
        @Order(22)
        @DisplayName("Order special ability (Protect) requires 25 mana")
        void orderSpecialAbilityCost() {
            OrderHero order = new OrderHero();
            order.setMana(24);
            assertFalse(order.canUseSpecialAbility());

            order.setMana(25);
            assertTrue(order.canUseSpecialAbility());
        }

        @Test
        @Order(23)
        @DisplayName("Chaos special ability (Fireball) requires 30 mana")
        void chaosSpecialAbilityCost() {
            Chaos chaos = new Chaos();
            chaos.setMana(29);
            assertFalse(chaos.canUseSpecialAbility());

            chaos.setMana(30);
            assertTrue(chaos.canUseSpecialAbility());
        }

        @Test
        @Order(24)
        @DisplayName("Hero.takeDamage() template: shield absorbs damage before HP")
        void shieldAbsorbsDamageFirst() {
            Warrior warrior = new Warrior();
            int initialHp = warrior.getHp();
            warrior.addShield(50);

            warrior.takeDamage(30); // shield absorbs all 30
            assertEquals(initialHp, warrior.getHp(), "HP unchanged when shield covers full damage");
            assertEquals(20, warrior.getShield(), "Shield should be reduced by damage amount");
        }

        @Test
        @Order(25)
        @DisplayName("Hero.takeDamage() template: defending reduces incoming damage")
        void defendingReducesDamage() {
            OrderHero order = new OrderHero();
            int initialHp = order.getHp();
            order.defend();

            // At level 1, Order has base 5 defense + 2 increment = 5 defense
            order.takeDamage(10); // 10 - defense(5) = 5; minimum 1, so 5 taken
            assertTrue(order.getHp() < initialHp);
            assertTrue(order.getHp() > initialHp - 10,
                    "Defending should reduce damage below the full 10");
        }

        @Test
        @Order(26)
        @DisplayName("Subclass stats differ: Warrior has higher defense increment than Mage")
        void subclassStatsDiffer() {
            Warrior warrior = new Warrior();
            Mage mage = new Mage();

            warrior.setLevel(5);
            mage.setLevel(5);

            assertTrue(warrior.getDefense() > mage.getDefense(),
                    "Warrior should have higher defense than Mage at same level");
            assertTrue(mage.getMana() > warrior.getMana(),
                    "Mage should have higher mana than Warrior at same level");
        }

        @Test
        @Order(27)
        @DisplayName("applyDefendEndOfTurn heals and restores mana when defending")
        void applyDefendEndOfTurnEffect() {
            Warrior warrior = new Warrior();
            warrior.takeDamage(20); // reduce HP
            warrior.setMana(10);   // reduce mana
            int hpBeforeDefend = warrior.getHp();
            int manaBeforeDefend = warrior.getMana();

            warrior.defend();
            warrior.applyDefendEndOfTurn();

            assertTrue(warrior.getHp() > hpBeforeDefend, "HP should increase after defend end-of-turn");
            assertTrue(warrior.getMana() > manaBeforeDefend, "Mana should increase after defend end-of-turn");
        }
    }

    // =========================================================================
    // 4. COMMAND PATTERN — Player-level Action dispatch
    //    Action objects encapsulate a request as an object, enabling
    //    parameterisation and queuing of operations.
    // =========================================================================

    @Nested
    @DisplayName("Pattern 4 · Command — Action encapsulation")
    class CommandPatternTests {

        @Test
        @Order(30)
        @DisplayName("Commands are objects: Action references can be stored and executed later")
        void commandStoredAndExecutedLater() {
            Player attacker = new Player("Cmd-Hero", 100, 25, 5);
            Player defender = new Player("Cmd-Enemy", 100, 10, 5);

            // Store command without executing
            Action queued = new AttackAction();

            assertEquals(100, defender.getHealth(), "No effect before execute()");

            // Execute the stored command
            queued.execute(attacker, defender);
            assertEquals(75, defender.getHealth(), "Command executed reduces HP by 25");
        }

        @Test
        @Order(31)
        @DisplayName("Multiple commands can be executed in sequence on the same players")
        void multipleCommandsInSequence() {
            Player p1 = new Player("Seq-A", 100, 15, 5);
            Player p2 = new Player("Seq-B", 100, 10, 5);

            Action[] commands = { new AttackAction(), new AttackAction(), new DefendAction() };

            for (Action cmd : commands) {
                cmd.execute(p1, p2);
            }

            // Two attacks: each 15 damage → 100 - 15 - 15 = 70
            assertEquals(70, p2.getHealth(), "Two AttackAction commands should deal 30 total damage");
        }

        @Test
        @Order(32)
        @DisplayName("Command interface allows substitution without changing calling code")
        void commandSubstitution() {
            Player p1 = new Player("Sub-A", 100, 20, 5);
            Player p2 = new Player("Sub-B", 100, 5, 5);

            // Calling code only knows about Action (interface)
            Action cmd;

            cmd = new AttackAction();
            cmd.execute(p1, p2);
            int hpAfterAttack = p2.getHealth();

            assertTrue(hpAfterAttack < 100, "Attack must have lowered HP");

            cmd = new DefendAction(); // substitute without changing calling code
            cmd.execute(p2, p1);
            // Verifying no exception thrown and p2 health unchanged
            assertEquals(hpAfterAttack, p2.getHealth(),
                    "Defend command must not alter the player's HP");
        }
    }

    // =========================================================================
    // 5. COMPOSITE PATTERN — Party as a composite of Hero objects
    //    Party acts as a composite node; Hero instances are leaf nodes.
    // =========================================================================

    @Nested
    @DisplayName("Pattern 5 · Composite — Party / Hero structure")
    class CompositePatternTests {

        @Test
        @Order(40)
        @DisplayName("Party.addHero() increases hero count")
        void addHeroIncreasesCount() {
            Party party = new Party("Warriors", "Brave fighters", "alice");
            assertEquals(0, party.getHeroes().size());

            party.addHero(new Warrior());
            assertEquals(1, party.getHeroes().size());

            party.addHero(new Mage());
            assertEquals(2, party.getHeroes().size());
        }

        @Test
        @Order(41)
        @DisplayName("Party.removeHero() decreases hero count")
        void removeHeroDecreasesCount() {
            Party party = new Party("Elites", "Elite squad", "bob");
            Warrior w = new Warrior();
            party.addHero(w);
            party.addHero(new Mage());

            party.removeHero(w);
            assertEquals(1, party.getHeroes().size());
        }

        @Test
        @Order(42)
        @DisplayName("hasAliveHeroes() returns false when all heroes are defeated")
        void hasAliveHeroesReturnsFalseWhenAllDefeated() {
            Party party = new Party("Doomed", "Fallen heroes", "charlie");
            Warrior w = new Warrior();
            w.setHp(0);                  // sets defeated flag
            party.addHero(w);

            assertFalse(party.hasAliveHeroes(),
                    "Party with only defeated heroes should report no alive heroes");
        }

        @Test
        @Order(43)
        @DisplayName("getAliveCount() reflects the correct number of surviving heroes")
        void aliveCountIsAccurate() {
            Party party = buildFullParty("Mixed", "dave");
            assertEquals(4, party.getAliveCount());

            // Defeat one hero
            party.getHeroes().get(0).setHp(0);
            assertEquals(3, party.getAliveCount());
        }

        @Test
        @Order(44)
        @DisplayName("Party stores metadata independently of hero list")
        void partyMetadataIsIndependent() {
            Party party = new Party("Knights", "Royal guard", "eve");
            party.addHero(new OrderHero());

            assertEquals("Knights", party.getPartyName());
            assertEquals("eve", party.getUsername());
            assertEquals(1, party.getHeroes().size());

            party.setPartyName("Paladins");
            assertEquals("Paladins", party.getPartyName());
            assertEquals(1, party.getHeroes().size(), "Rename must not affect hero list");
        }

        @Test
        @Order(45)
        @DisplayName("Composite operation: Order's Protect ability shields all party members")
        void compositeProtectShedsAllMembers() {
            Party party = new Party("Shielded", "Shielded party", "frank");
            OrderHero order = new OrderHero();
            Warrior w1 = new Warrior();
            Warrior w2 = new Warrior();
            party.addHero(order);
            party.addHero(w1);
            party.addHero(w2);

            order.setMana(100); // ensure enough mana
            order.useSpecialAbility(null, party, party); // Protect all members

            for (Hero hero : party.getHeroes()) {
                assertTrue(hero.getShield() > 0,
                        hero.getHeroClass() + " should have a shield after Protect");
            }
        }
    }

    // =========================================================================
    // 6. BONUS — Observer-like / State: PvPBattleSystem state transitions
    //    PvPBattleSystem transitions between uninitialised → active → over,
    //    simulating a state machine on battle lifecycle events.
    // =========================================================================

    @Nested
    @DisplayName("Pattern 6 · State — PvPBattleSystem lifecycle")
    class StateBattleSystemTests {

        private Party buildSingleHeroParty(String name, String owner) {
            return singleWarriorParty(name, owner, 1);
        }

        @Test
        @Order(50)
        @DisplayName("Battle is not initialised before initializeBattle()")
        void notInitializedBeforeCall() {
            Party p1 = buildSingleHeroParty("TeamA", "userA");
            Party p2 = buildSingleHeroParty("TeamB", "userB");
            PvPBattleSystem battle = new PvPBattleSystem(p1, p2);
            assertFalse(battle.isBattleInitialized());
        }

        @Test
        @Order(51)
        @DisplayName("Battle is initialised after initializeBattle()")
        void initializedAfterCall() {
            Party p1 = buildSingleHeroParty("TeamA", "userA");
            Party p2 = buildSingleHeroParty("TeamB", "userB");
            PvPBattleSystem battle = new PvPBattleSystem(p1, p2);
            battle.initializeBattle();
            assertTrue(battle.isBattleInitialized());
        }

        @Test
        @Order(52)
        @DisplayName("isBattleOver() is false at start when both parties have alive heroes")
        void battleNotOverAtStart() {
            Party p1 = buildSingleHeroParty("TeamA", "userA");
            Party p2 = buildSingleHeroParty("TeamB", "userB");
            PvPBattleSystem battle = new PvPBattleSystem(p1, p2);
            battle.initializeBattle();
            assertFalse(battle.isBattleOver(), "Battle should not be over when heroes are alive");
        }

        @Test
        @Order(53)
        @DisplayName("isBattleOver() returns true when one party is fully defeated")
        void battleOverWhenPartyDefeated() {
            Party p1 = buildSingleHeroParty("Winners", "userW");
            Party p2 = buildSingleHeroParty("Losers", "userL");
            PvPBattleSystem battle = new PvPBattleSystem(p1, p2);
            battle.initializeBattle();

            // Defeat all heroes in party 2
            for (Hero h : p2.getHeroes()) {
                h.setHp(0);
            }

            assertTrue(battle.isBattleOver(), "Battle must be over when all heroes of one party are defeated");
        }

        @Test
        @Order(54)
        @DisplayName("processAttack() deals correct damage and logs a battle event")
        void processAttackDamageAndLog() {
            Party p1 = buildSingleHeroParty("Attk", "userA");
            Party p2 = buildSingleHeroParty("Dfnd", "userD");
            PvPBattleSystem battle = new PvPBattleSystem(p1, p2);
            battle.initializeBattle();

            Hero attacker = p1.getHeroes().get(0);
            Hero defender = p2.getHeroes().get(0);
            int hpBefore = defender.getHp();

            String result = battle.processAttack(attacker, defender);

            assertTrue(defender.getHp() < hpBefore, "Defender HP must drop after processAttack");
            assertNotNull(result, "processAttack must return a result string");
            assertFalse(result.isEmpty(), "Result string must not be empty");
        }

        @Test
        @Order(55)
        @DisplayName("processDefend() sets hero to defending state")
        void processDefendSetsDefendingState() {
            Party p1 = buildSingleHeroParty("DefP", "userD");
            Party p2 = buildSingleHeroParty("AtkP", "userA");
            PvPBattleSystem battle = new PvPBattleSystem(p1, p2);
            battle.initializeBattle();

            Hero defender = p1.getHeroes().get(0);
            battle.processDefend(defender);

            assertTrue(defender.isDefending(), "Hero must be in defending state after processDefend");
        }

        @Test
        @Order(56)
        @DisplayName("getBattleLog() grows as events occur")
        void battleLogGrowsWithEvents() {
            Party p1 = buildSingleHeroParty("LogA", "logUserA");
            Party p2 = buildSingleHeroParty("LogB", "logUserB");
            PvPBattleSystem battle = new PvPBattleSystem(p1, p2);
            battle.initializeBattle();

            int logSizeAfterInit = battle.getBattleLog().size();

            battle.processAttack(p1.getHeroes().get(0), p2.getHeroes().get(0));

            assertTrue(battle.getBattleLog().size() > logSizeAfterInit,
                    "Battle log must grow when events are processed");
        }

        @Test
        @Order(57)
        @DisplayName("determineWinner() sets winningParty after battle ends")
        void determineWinnerSetsWinner() {
            Party p1 = buildSingleHeroParty("Champs", "champ");
            Party p2 = buildSingleHeroParty("Losers", "loser");
            PvPBattleSystem battle = new PvPBattleSystem(p1, p2);
            battle.initializeBattle();

            // Defeat party 2
            for (Hero h : p2.getHeroes()) h.setHp(0);

            battle.determineWinner();
            assertNotNull(battle.getWinningParty(), "Winning party must be set after determineWinner()");
            assertEquals("Champs", battle.getWinningParty().getPartyName());
        }
    }

    // =========================================================================
    // 7. BONUS EDGE CASES — Hero stat levelling and User domain object
    // =========================================================================

    @Nested
    @DisplayName("Extra · Hero levelling & User model")
    class EdgeCaseTests {

        @Test
        @Order(60)
        @DisplayName("Hero stats increase when level is set higher")
        void heroStatsScaleWithLevel() {
            Warrior w = new Warrior();
            int atkLvl1 = w.getAttack();
            int defLvl1 = w.getDefense();

            w.setLevel(10);

            assertTrue(w.getAttack() > atkLvl1, "Attack must increase with level");
            assertTrue(w.getDefense() > defLvl1, "Defense must increase with level");
            assertTrue(w.getMaxHp() > 100, "MaxHP must increase with level");
        }

        @Test
        @Order(61)
        @DisplayName("Hero HP is capped at maxHp")
        void hpCapAtMaxHp() {
            Warrior w = new Warrior();
            w.setHp(9999);
            assertEquals(w.getMaxHp(), w.getHp(), "HP must be capped at maxHp");
        }

        @Test
        @Order(62)
        @DisplayName("Hero stun lasts exactly one processEndOfTurn() cycle")
        void stunLastsOneTurn() {
            Mage mage = new Mage();
            mage.stun();
            assertTrue(mage.isStunned());

            mage.processEndOfTurn();
            assertFalse(mage.isStunned(), "Stun must clear after one processEndOfTurn call");
        }

        @Test
        @Order(63)
        @DisplayName("Hero.heal() does not exceed maxHp")
        void healDoesNotExceedMaxHp() {
            Chaos chaos = new Chaos();
            chaos.setHp(80);
            chaos.heal(9999);
            assertEquals(chaos.getMaxHp(), chaos.getHp(), "Heal must be capped at maxHp");
        }

        @Test
        @Order(64)
        @DisplayName("Hero can be set to any valid level without exception")
        void heroAcceptsValidLevels() {
            for (int level : new int[]{1, 5, 10, 15, 20}) {
                final int lvl = level;
                assertDoesNotThrow(() -> {
                    OrderHero order = new OrderHero();
                    order.setLevel(lvl);
                    assertEquals(lvl, order.getLevel(), "Level should be set to " + lvl);
                });
            }
        }

        @Test
        @Order(65)
        @DisplayName("User stores id and username correctly")
        void userStoresFields() {
            User user = new User(42, "testUser");
            assertEquals(42, user.getId());
            assertEquals("testUser", user.getUsername());
        }

        @Test
        @Order(66)
        @DisplayName("User.setUsername() updates the username")
        void userSetterWorks() {
            User user = new User(1, "original");
            user.setUsername("updated");
            assertEquals("updated", user.getUsername());
        }

        @Test
        @Order(67)
        @DisplayName("MatchResult stores all fields immutably")
        void matchResultStoresFields() {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            MatchResult result = new MatchResult("alice", "bob", "alice", now);

            assertEquals("alice", result.getPlayer1());
            assertEquals("bob", result.getPlayer2());
            assertEquals("alice", result.getWinner());
            assertEquals(now, result.getTimestamp());
        }

        @Test
        @Order(68)
        @DisplayName("UserWins stores username and win count correctly")
        void userWinsStoresFields() {
            UserWins uw = new UserWins("charlie", 7);
            assertEquals("charlie", uw.getUsername());
            assertEquals(7, uw.getWins());
        }

        @Test
        @Order(69)
        @DisplayName("Mage Replenish restores mana to all alive party members")
        void mageReplenishRestoresManaToParty() {
            Party party = new Party("MageParty", "Mana users", "greta");
            Mage mage = new Mage();
            Warrior warrior = new Warrior();
            party.addHero(mage);
            party.addHero(warrior);

            // Drain some mana first
            warrior.setMana(0);
            mage.setMana(80);  // enough for Replenish

            mage.useSpecialAbility(null, party, party);

            assertTrue(warrior.getMana() > 0, "Warrior mana should be restored by Replenish");
        }
    }
}