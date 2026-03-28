package DAO;

import Classes.Heros.Hero;
import Classes.User;
import Factory.*;
import Repository.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HeroDAO {
    private static volatile HeroDAO obj = null;
    private HeroDAO() {
        System.out.println("HeroDAO is instantiated.");
    }
    public static HeroDAO getInstance() {
        if (obj == null) {
            synchronized (HeroDAO.class) {
                if (obj == null) {
                    obj = new HeroDAO();
                }
            }
        }
        return obj;
    }

    public void saveHero(Hero hero, int userID) throws SQLException {
        String query = "INSERT INTO HEROES (class, level, maxHP, maxMana, attack, defense, userID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, hero.getHeroClassString());
            preparedStatement.setInt(2, hero.getLevel());
            preparedStatement.setInt(3, hero.getMaxHp());
            preparedStatement.setInt(4, hero.getMaxMana());
            preparedStatement.setInt(5, hero.getAttack());
            preparedStatement.setInt(6, hero.getDefense());
            preparedStatement.setInt(7, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Hero makeHeroWithProperties(int heroIDGotten, int userIDGotten, String heroClass, int level, int maxHp, int maxMana, int attack, int defense) throws SQLException {
        Hero heroMade = CentralHeroFactory.makeHeroBasedOnClass(heroClass);
        heroMade.setHeroIDInt(heroIDGotten);
        heroMade.setHeroUserID(userIDGotten);
        heroMade.setLevel(level);
        heroMade.setMaxHP(maxHp);
        heroMade.setMaxMana(maxMana);
        heroMade.setAttack(attack);
        heroMade.setDefense(defense);

        return heroMade;
    }

    public Hero getIndividualHero(int heroID) throws SQLException {
        String query = "SELECT * FROM HEROES WHERE idHEROES = ?";

        Hero heroMade = null;

        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, heroID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int heroIDGotten = resultSet.getInt("idHEROES");
                int userIDGotten = resultSet.getInt("userID");
                String heroClass = resultSet.getString("class");
                int level = resultSet.getInt("level");
                int maxHp = resultSet.getInt("maxHP");
                int maxMana = resultSet.getInt("maxMana");
                int attack = resultSet.getInt("attack");
                int defense = resultSet.getInt("defense");

                heroMade = makeHeroWithProperties(heroIDGotten, userIDGotten, heroClass, level, maxHp, maxMana, attack, defense);
            }
        }

        return heroMade;
    }

    public List<Hero> getHeroes(int userID) throws SQLException {
        List<Hero> heroes = new ArrayList<>();
        String query = "SELECT * FROM HEROES WHERE userID = ?";

        try  (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Hero heroMade;
                int heroIDGotten = resultSet.getInt("idHEROES");
                int userIDGotten = resultSet.getInt("userID");
                String heroClass = resultSet.getString("class");
                int level = resultSet.getInt("level");
                int maxHp = resultSet.getInt("maxHP");
                int maxMana = resultSet.getInt("maxMana");
                int attack = resultSet.getInt("attack");
                int defense = resultSet.getInt("defense");

                heroMade = makeHeroWithProperties(heroIDGotten, userIDGotten, heroClass, level, maxHp, maxMana, attack, defense);

                if (heroMade != null) {
                    heroes.add(heroMade);
                }
            }
        }
        return heroes;
    }
}
