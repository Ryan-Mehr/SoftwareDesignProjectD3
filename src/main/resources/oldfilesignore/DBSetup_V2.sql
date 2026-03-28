CREATE DATABASE  IF NOT EXISTS `my_application_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `my_application_db`;
-- MySQL dump 10.13  Distrib 8.0.45, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: my_application_db
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `USERS`
--

DROP TABLE IF EXISTS `USERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERS` (
  `idUSERS` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` char(60) NOT NULL,
  `wins` int DEFAULT '0',
  PRIMARY KEY (`idUSERS`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `idUSERS_UNIQUE` (`idUSERS`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USERS`
--

LOCK TABLES `USERS` WRITE;
/*!40000 ALTER TABLE `USERS` DISABLE KEYS */;
INSERT INTO `USERS` VALUES (1,'testuser','$2a$10$GIaHENxLpohMXBbGvtPyC.5BGs8tDugJI9UAiYL1aSusF.kvwrdCi',1),(2,'tester','$2a$10$vddjL3hV656X46KKowgt3O.Jt/YZ/OGKo/u7V/4AqPQsDN6Bhe36e',18),(3,'anafil','$2a$10$uDLfDU66hwQ56IICFpcq0eLeMUcSlPRU8CilL4eMoS4IwQfLwoSDe',0);
/*!40000 ALTER TABLE `USERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parties`
--

DROP TABLE IF EXISTS `parties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parties` (
  `party_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `party_name` varchar(100) NOT NULL,
  `party_info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`party_id`),
  KEY `username` (`username`),
  CONSTRAINT `parties_ibfk_1` FOREIGN KEY (`username`) REFERENCES `USERS` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parties`
--

LOCK TABLES `parties` WRITE;
/*!40000 ALTER TABLE `parties` DISABLE KEYS */;
INSERT INTO `parties` VALUES (4,'tester','Starter Team','Pikachu, Bulbasaur, Charmander'),(5,'tester','Order Keepers','Level 1 Order hero: +5 mana, +2 defense per level. Spells: Protect (shield 10% HP), Heal (25% HP).'),(6,'tester','Starter Team','Pikachu, Bulbasaur, Charmander'),(20,'anafil','Anafil\'s Dream Team','Warrior and Mage combo for balanced attacks'),(21,'anafil','Defensive Masters','Order heroes with strong protection spells'),(22,'anafil','Chaos Legion','High damage chaos heroes for quick victories');
/*!40000 ALTER TABLE `parties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pvp_results`
--

DROP TABLE IF EXISTS `pvp_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pvp_results` (
  `id` int NOT NULL AUTO_INCREMENT,
  `player1` varchar(50) DEFAULT NULL,
  `player2` varchar(50) DEFAULT NULL,
  `winner` varchar(50) DEFAULT NULL,
  `battle_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pvp_results`
--

LOCK TABLES `pvp_results` WRITE;
/*!40000 ALTER TABLE `pvp_results` DISABLE KEYS */;
INSERT INTO `pvp_results` VALUES (1,'Player 1','Player 2','Player 1','2026-03-18 19:40:28'),(2,'Player 1','Player 2','Player 1','2026-03-19 15:49:52'),(3,'Player 1','Player 2','Player 1','2026-03-19 15:52:12'),(4,'testuser','tester','testuser','2026-03-19 16:22:54'),(5,'tester','tester','tester','2026-03-19 19:06:06'),(6,'tester','CPU_Opponent','tester','2026-03-19 19:33:36'),(7,'tester','CPU_Opponent','tester','2026-03-19 19:39:23'),(8,'tester','CPU_Opponent','tester','2026-03-19 19:42:17'),(9,'tester','CPU_Opponent','tester','2026-03-19 19:51:33'),(10,'anafil','tester','tester','2026-03-19 22:17:33'),(11,'anafil','tester','tester','2026-03-19 22:18:51'),(12,'anafil','tester','tester','2026-03-19 22:33:31'),(13,'anafil','tester','tester','2026-03-19 22:37:55'),(14,'anafil','tester','tester','2026-03-19 22:43:42'),(15,'anafil','tester','tester','2026-03-19 22:45:18'),(16,'anafil','tester','tester','2026-03-19 22:51:30'),(17,'anafil','tester','tester','2026-03-19 22:52:13'),(18,'anafil','tester','tester','2026-03-19 22:58:02'),(19,'anafil','tester','tester','2026-03-20 00:22:11'),(20,'anafil','tester','tester','2026-03-20 00:27:08'),(21,'anafil','tester','tester','2026-03-23 18:42:24'),(22,'anafil','tester','tester','2026-03-23 20:42:59');
/*!40000 ALTER TABLE `pvp_results` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-24 14:08:59
