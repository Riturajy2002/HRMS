-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: leave_management
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `designations`
--

DROP TABLE IF EXISTS `designations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `designations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `designation_names` varchar(255) NOT NULL,
  `department_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `designations`
--

LOCK TABLES `designations` WRITE;
/*!40000 ALTER TABLE `designations` DISABLE KEYS */;
INSERT INTO `designations` VALUES (1,'Director','Management'),(2,'Sales Director','Sales'),(3,'Team Lead','Development'),(4,'Marketing Manager','Marketing'),(5,'Senior Sales Consultant','Sales'),(6,'Sr. Business Analyst','Product'),(7,'Head of Products','Product'),(8,'Software Engineer','Development'),(9,'Senior VP- Sales','Sales'),(10,'AVP- Product','Product'),(11,'Executive- Digital Marketing','Marketing'),(12,'Sr.Associate-Business Acquisition','Sales'),(13,'Senior Operations Associate','Management'),(14,'Client Success Executive','Support'),(15,'Sr. UI/UX Developer','Development'),(16,'Sr Data Scientist','Development'),(17,'AVP-Business Acquisition','Sales'),(18,'Sr. System Engineer','IT'),(19,'VP Infrastructure & Compliance','IT'),(20,'UI/UX Developer','Development'),(21,'HR Admin','Management'),(22,'Sales Associate','Sales'),(23,'Manager Finance & Account','Management'),(24,'Associate (Sales)','Sales'),(25,'Digital Marketing Executive','Marketing '),(26,'Trainee - Business Analyst','Sales'),(27,'Trainee -Software Engineer','Development');
/*!40000 ALTER TABLE `designations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `holiday_master`
--

DROP TABLE IF EXISTS `holiday_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `holiday_master` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `day` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday_master`
--

LOCK TABLES `holiday_master` WRITE;
/*!40000 ALTER TABLE `holiday_master` DISABLE KEYS */;
INSERT INTO `holiday_master` VALUES (1,'Republic Day','2024-01-26','Friday','Fixed'),(2,'Holi','2024-03-25','Monday','Fixed'),(3,'Id-Ul-Fitr','2024-04-11','Thursday','Fixed'),(4,'Ram Navmi','2024-04-17','Wednesday','Fixed'),(5,'Independence Day','2024-08-15','Thursday','Fixed'),(6,'Gandhi Jyanti','2024-10-02','Wednesday','Fixed'),(7,'Diwali','2024-10-31','Thursday','Fixed'),(8,'Guru Nanak Jayanti','2024-11-15','Friday','Fixed'),(9,'Maha Shivratri','2024-03-08','Friday','Flexi'),(10,'Gudi Padva','2024-04-09','Tuesday','Flexi'),(21,'Ramzan','2024-04-11','Thursday','Flexi'),(22,'Buddha Purnima','2024-05-23','Thursday','Flexi'),(23,'Muharram','2024-07-17','Wednesday','Flexi'),(24,'Raksha Bandhan','2024-08-19','Monday','Flexi'),(25,'Janmasthami','2024-08-26','Monday','Flexi'),(26,'Milad-Un-Nabi','2024-09-16','Monday','Flexi'),(27,'Maha Asthami','2024-10-10','Thursday','Flexi'),(28,'Maha Navmi','2024-10-11','Friday','Flexi'),(29,'Dhanteres','2024-11-29','Tuesday','Flexi'),(30,'Naraka Chaturdashi','2024-11-30','Wednesday','Flexi'),(31,'Chatt Puja','2024-11-07','Thursday','Flexi');
/*!40000 ALTER TABLE `holiday_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_config`
--

DROP TABLE IF EXISTS `leave_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `value` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_config`
--

LOCK TABLES `leave_config` WRITE;
/*!40000 ALTER TABLE `leave_config` DISABLE KEYS */;
INSERT INTO `leave_config` VALUES (1,'monthlyLeaveIncrementCount',1),(2,'flexiLimitPerYear',2);
/*!40000 ALTER TABLE `leave_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_master`
--

DROP TABLE IF EXISTS `leave_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_master` (
  `leave_type_id` bigint NOT NULL AUTO_INCREMENT,
  `leave_name` varchar(255) NOT NULL,
  `applicable_for` varchar(255) NOT NULL,
  `no_of_days` int DEFAULT NULL,
  `sandwich_included` bit(1) NOT NULL,
  `types_of_leaves` varchar(255) NOT NULL,
  `max_applicable` int DEFAULT NULL,
  PRIMARY KEY (`leave_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_master`
--

LOCK TABLES `leave_master` WRITE;
/*!40000 ALTER TABLE `leave_master` DISABLE KEYS */;
INSERT INTO `leave_master` VALUES (1,'Maternity','Female',60,_binary '','fixed',1),(2,'Paternity','Male',10,_binary '','fixed',1),(3,'Componsatory','All',1,_binary '\0','fixed',1),(4,'Medical','All',NULL,_binary '\0','flexible',NULL),(5,'Casual','All',NULL,_binary '\0','flexible',NULL),(6,'Flexi','All',2,_binary '\0','flexible',2);
/*!40000 ALTER TABLE `leave_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_request`
--

DROP TABLE IF EXISTS `leave_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `manager` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `leave_operation_type` varchar(255) DEFAULT NULL,
  `applied_date` datetime(6) DEFAULT NULL,
  `from_date` datetime(6) DEFAULT NULL,
  `to_date` datetime(6) DEFAULT NULL,
  `number_of_days` int NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK28ykte0n73edocnb1phrnqo3s` (`user_id`),
  CONSTRAINT `FK28ykte0n73edocnb1phrnqo3s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_request`
--

LOCK TABLES `leave_request` WRITE;
/*!40000 ALTER TABLE `leave_request` DISABLE KEYS */;
INSERT INTO `leave_request` VALUES (2,'NPPL025','Abhinav Tiwari','Medical','Debit','2024-07-18 12:02:29.358000','2024-07-02 00:00:00.000000','2024-07-09 00:00:00.000000',2,'jhgfdbgn','Approved',NULL),(4,'NPPL025','Abhinav Tiwari','Flexi','Debit','2024-07-18 16:08:34.836000','2024-07-17 05:30:00.000000','2024-07-17 05:30:00.000000',1,'sadfv','Approved',NULL),(5,'NPPL025','Abhinav Tiwari','Casual','Debit','2024-07-18 16:11:04.126000','2024-07-04 00:00:00.000000','2024-07-10 00:00:00.000000',7,'asddf','Approved',NULL),(6,'NPPL027','Abhinav Tiwari','Maternity','Credit','2024-07-18 17:26:10.647000','2024-07-09 00:00:00.000000','2024-07-16 00:00:00.000000',5,'hjypoikjjh','Approved',NULL),(7,'NPPL025','Abhinav Tiwari','Maternity','Debit','2024-07-23 12:50:50.154000','2024-07-17 00:00:00.000000','2024-07-17 00:00:00.000000',16,'For the Medical Check UP','Approved',NULL),(8,'NPPL027','Abhinav Tiwari','Casual','Debit ','2024-07-23 12:55:25.303000','2024-07-02 00:00:00.000000','2024-07-17 00:00:00.000000',16,'thyfgd','Approved',NULL),(9,'NPPL027','Abhinav Tiwari','Flexi','Debit','2024-07-31 15:26:55.358000','2024-07-17 05:30:00.000000','2024-07-17 05:30:00.000000',1,'jyuthr rfbkjm','Approved',NULL),(10,'NPPL004',NULL,'Casual','Credit','2024-07-31 17:51:00.037000','2024-07-31 17:51:00.037000','2024-07-31 17:51:00.037000',1,NULL,'Approved',NULL),(11,'NPPL004',NULL,'Medical','Credit','2024-07-31 17:51:00.048000','2024-07-31 17:51:00.048000','2024-07-31 17:51:00.048000',1,NULL,'Approved',NULL),(12,'NPPL022',NULL,'Casual','Credit','2024-07-31 17:51:00.052000','2024-07-31 17:51:00.052000','2024-07-31 17:51:00.052000',1,NULL,'Approved',NULL),(13,'NPPL022',NULL,'Medical','Credit','2024-07-31 17:51:00.057000','2024-07-31 17:51:00.057000','2024-07-31 17:51:00.057000',1,NULL,'Approved',NULL),(14,'NPPL025',NULL,'Casual','Credit','2024-07-31 17:51:00.061000','2024-07-31 17:51:00.061000','2024-07-31 17:51:00.061000',1,NULL,'Approved',NULL),(15,'NPPL025',NULL,'Medical','Credit','2024-07-31 17:51:00.065000','2024-07-31 17:51:00.065000','2024-07-31 17:51:00.065000',1,NULL,'Approved',NULL),(16,'NPPL027',NULL,'Casual','Credit','2024-07-31 17:51:00.070000','2024-07-31 17:51:00.070000','2024-07-31 17:51:00.070000',1,NULL,'Approved',NULL),(17,'NPPL027',NULL,'Medical','Credit','2024-07-31 17:51:00.074000','2024-07-31 17:51:00.074000','2024-07-31 17:51:00.074000',1,NULL,'Approved',NULL),(18,'NPPL040',NULL,'Casual','Credit','2024-07-31 17:51:00.079000','2024-07-31 17:51:00.079000','2024-07-31 17:51:00.079000',1,NULL,'Approved',NULL),(19,'NPPL040',NULL,'Medical','Credit','2024-07-31 17:51:00.083000','2024-07-31 17:51:00.083000','2024-07-31 17:51:00.083000',1,NULL,'Approved',NULL),(20,'NPPL004',NULL,'Casual','Credit','2024-07-31 17:52:00.026000','2024-07-31 17:52:00.026000','2024-07-31 17:52:00.026000',1,NULL,'Approved',NULL),(21,'NPPL004',NULL,'Medical','Credit','2024-07-31 17:52:00.036000','2024-07-31 17:52:00.036000','2024-07-31 17:52:00.036000',1,NULL,'Approved',NULL),(22,'NPPL022',NULL,'Casual','Credit','2024-07-31 17:52:00.040000','2024-07-31 17:52:00.040000','2024-07-31 17:52:00.040000',1,NULL,'Approved',NULL),(23,'NPPL022',NULL,'Medical','Credit','2024-07-31 17:52:00.045000','2024-07-31 17:52:00.045000','2024-07-31 17:52:00.045000',1,NULL,'Approved',NULL),(24,'NPPL025',NULL,'Casual','Credit','2024-07-31 17:52:00.050000','2024-07-31 17:52:00.050000','2024-07-31 17:52:00.050000',1,NULL,'Approved',NULL),(25,'NPPL025',NULL,'Medical','Credit','2024-07-31 17:52:00.055000','2024-07-31 17:52:00.055000','2024-07-31 17:52:00.055000',1,NULL,'Approved',NULL),(26,'NPPL027',NULL,'Casual','Credit','2024-07-31 17:52:00.059000','2024-07-31 17:52:00.059000','2024-07-31 17:52:00.059000',1,NULL,'Approved',NULL),(27,'NPPL027',NULL,'Medical','Credit','2024-07-31 17:52:00.063000','2024-07-31 17:52:00.063000','2024-07-31 17:52:00.063000',1,NULL,'Approved',NULL),(28,'NPPL040',NULL,'Casual','Credit','2024-07-31 17:52:00.067000','2024-07-31 17:52:00.067000','2024-07-31 17:52:00.067000',1,NULL,'Approved',NULL),(29,'NPPL040',NULL,'Medical','Credit','2024-07-31 17:52:00.072000','2024-07-31 17:52:00.072000','2024-07-31 17:52:00.072000',1,NULL,'Approved',NULL),(30,'NPPL004',NULL,'Casual','Credit','2024-07-31 17:53:00.019000','2024-07-31 17:53:00.019000','2024-07-31 17:53:00.019000',1,NULL,'Approved',NULL),(31,'NPPL004',NULL,'Medical','Credit','2024-07-31 17:53:00.029000','2024-07-31 17:53:00.029000','2024-07-31 17:53:00.029000',1,NULL,'Approved',NULL),(32,'NPPL022',NULL,'Casual','Credit','2024-07-31 17:53:00.033000','2024-07-31 17:53:00.033000','2024-07-31 17:53:00.033000',1,NULL,'Approved',NULL),(33,'NPPL022',NULL,'Medical','Credit','2024-07-31 17:53:00.037000','2024-07-31 17:53:00.037000','2024-07-31 17:53:00.037000',1,NULL,'Approved',NULL),(34,'NPPL025',NULL,'Casual','Credit','2024-07-31 17:53:00.041000','2024-07-31 17:53:00.041000','2024-07-31 17:53:00.041000',1,NULL,'Approved',NULL),(35,'NPPL025',NULL,'Medical','Credit','2024-07-31 17:53:00.046000','2024-07-31 17:53:00.046000','2024-07-31 17:53:00.046000',1,NULL,'Approved',NULL),(36,'NPPL027',NULL,'Casual','Credit','2024-07-31 17:53:00.050000','2024-07-31 17:53:00.050000','2024-07-31 17:53:00.050000',1,NULL,'Approved',NULL),(37,'NPPL027',NULL,'Medical','Credit','2024-07-31 17:53:00.055000','2024-07-31 17:53:00.055000','2024-07-31 17:53:00.055000',1,NULL,'Approved',NULL),(38,'NPPL040',NULL,'Casual','Credit','2024-07-31 17:53:00.059000','2024-07-31 17:53:00.059000','2024-07-31 17:53:00.059000',1,NULL,'Approved',NULL),(39,'NPPL040',NULL,'Medical','Credit','2024-07-31 17:53:00.063000','2024-07-31 17:53:00.063000','2024-07-31 17:53:00.063000',1,NULL,'Approved',NULL),(40,'NPPL004',NULL,'Casual','Credit','2024-07-31 17:54:00.011000','2024-07-31 17:54:00.011000','2024-07-31 17:54:00.011000',1,NULL,'Approved',NULL),(41,'NPPL004',NULL,'Medical','Credit','2024-07-31 17:54:00.021000','2024-07-31 17:54:00.021000','2024-07-31 17:54:00.021000',1,NULL,'Approved',NULL),(42,'NPPL022',NULL,'Casual','Credit','2024-07-31 17:54:00.025000','2024-07-31 17:54:00.025000','2024-07-31 17:54:00.025000',1,NULL,'Approved',NULL),(43,'NPPL022',NULL,'Medical','Credit','2024-07-31 17:54:00.030000','2024-07-31 17:54:00.030000','2024-07-31 17:54:00.030000',1,NULL,'Approved',NULL),(44,'NPPL025',NULL,'Casual','Credit','2024-07-31 17:54:00.034000','2024-07-31 17:54:00.034000','2024-07-31 17:54:00.034000',1,NULL,'Approved',NULL),(45,'NPPL025',NULL,'Medical','Credit','2024-07-31 17:54:00.039000','2024-07-31 17:54:00.039000','2024-07-31 17:54:00.039000',1,NULL,'Approved',NULL),(46,'NPPL027',NULL,'Casual','Credit','2024-07-31 17:54:00.044000','2024-07-31 17:54:00.044000','2024-07-31 17:54:00.044000',1,NULL,'Approved',NULL),(47,'NPPL027',NULL,'Medical','Credit','2024-07-31 17:54:00.048000','2024-07-31 17:54:00.048000','2024-07-31 17:54:00.048000',1,NULL,'Approved',NULL),(48,'NPPL040',NULL,'Casual','Credit','2024-07-31 17:54:00.053000','2024-07-31 17:54:00.053000','2024-07-31 17:54:00.053000',1,NULL,'Approved',NULL),(49,'NPPL040',NULL,'Medical','Credit','2024-07-31 17:54:00.057000','2024-07-31 17:54:00.057000','2024-07-31 17:54:00.057000',1,NULL,'Approved',NULL),(50,'NPPL004','Abhisheak Singh','Casual','Credit','2024-07-31 17:56:00.038000','2024-07-31 17:56:00.038000','2024-07-31 17:56:00.038000',1,NULL,'Approved',NULL),(51,'NPPL004','Abhisheak Singh','Medical','Debit','2024-07-31 17:56:00.049000','2024-07-31 17:56:00.049000','2024-07-31 17:56:00.049000',1,NULL,'Approved',NULL),(52,'NPPL022','Abhinav Tiwari','Casual','Credit','2024-07-31 17:56:00.053000','2024-07-31 17:56:00.053000','2024-07-31 17:56:00.053000',1,NULL,'Approved',NULL),(53,'NPPL022','Abhinav Tiwari','Medical','Credit','2024-07-31 17:56:00.057000','2024-07-31 17:56:00.057000','2024-07-31 17:56:00.057000',1,NULL,'Approved',NULL),(54,'NPPL025','Abhinav Tiwari','Casual','Credit','2024-07-31 17:56:00.062000','2024-07-31 17:56:00.062000','2024-07-31 17:56:00.062000',1,NULL,'Approved',NULL),(55,'NPPL025','Abhinav Tiwari','Medical','Credit','2024-07-31 17:56:00.066000','2024-07-31 17:56:00.066000','2024-07-31 17:56:00.066000',1,NULL,'Approved',NULL),(56,'NPPL027','Abhinav Tiwari','Casual','Credit','2024-07-31 17:56:00.070000','2024-07-31 17:56:00.070000','2024-07-31 17:56:00.070000',1,NULL,'Approved',NULL),(57,'NPPL027','Abhinav Tiwari','Medical','Credit','2024-07-31 17:56:00.074000','2024-07-31 17:56:00.074000','2024-07-31 17:56:00.074000',1,NULL,'Approved',NULL),(58,'NPPL040','Abhinav Tiwari','Casual','Credit','2024-07-31 17:56:00.078000','2024-07-31 17:56:00.078000','2024-07-31 17:56:00.078000',1,NULL,'Approved',NULL),(59,'NPPL040','Abhinav Tiwari','Medical','Credit','2024-07-31 17:56:00.082000','2024-07-31 17:56:00.082000','2024-07-31 17:56:00.082000',1,NULL,'Approved',NULL),(60,'NPPL004','Abhisheak Singh','Casual','Credit','2024-07-31 17:57:00.022000','2024-07-31 17:57:00.022000','2024-07-31 17:57:00.022000',1,NULL,'Approved',NULL),(61,'NPPL004','Abhisheak Singh','Medical','Debit','2024-07-31 17:57:00.031000','2024-07-31 17:57:00.031000','2024-07-31 17:57:00.031000',1,NULL,'Approved',NULL),(62,'NPPL022','Abhinav Tiwari','Casual','Credit','2024-07-31 17:57:00.035000','2024-07-31 17:57:00.035000','2024-07-31 17:57:00.035000',1,NULL,'Approved',NULL),(63,'NPPL022','Abhinav Tiwari','Medical','Credit','2024-07-31 17:57:00.039000','2024-07-31 17:57:00.039000','2024-07-31 17:57:00.039000',1,NULL,'Approved',NULL),(64,'NPPL025','Abhinav Tiwari','Casual','Credit','2024-07-31 17:57:00.044000','2024-07-31 17:57:00.044000','2024-07-31 17:57:00.044000',1,NULL,'Approved',NULL),(65,'NPPL025','Abhinav Tiwari','Medical','Credit','2024-07-31 17:57:00.048000','2024-07-31 17:57:00.048000','2024-07-31 17:57:00.048000',1,NULL,'Approved',NULL),(66,'NPPL027','Abhinav Tiwari','Casual','Credit','2024-07-31 17:57:00.052000','2024-07-31 17:57:00.052000','2024-07-31 17:57:00.052000',1,NULL,'Approved',NULL),(67,'NPPL027','Abhinav Tiwari','Medical','Credit','2024-07-31 17:57:00.056000','2024-07-31 17:57:00.056000','2024-07-31 17:57:00.056000',1,NULL,'Approved',NULL),(68,'NPPL040','Abhinav Tiwari','Casual','Credit','2024-07-31 17:57:00.060000','2024-07-31 17:57:00.060000','2024-07-31 17:57:00.060000',1,NULL,'Approved',NULL),(69,'NPPL040','Abhinav Tiwari','Medical','Credit','2024-07-31 17:57:00.064000','2024-07-31 17:57:00.064000','2024-07-31 17:57:00.064000',1,NULL,'Approved',NULL),(70,'NPPL004','Abhisheak Singh','Casual','Credit','2024-07-31 18:01:00.033000','2024-07-31 18:01:00.033000','2024-07-31 18:01:00.033000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(71,'NPPL004','Abhisheak Singh','Medical','Credit','2024-07-31 18:01:00.043000','2024-07-31 18:01:00.043000','2024-07-31 18:01:00.043000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(72,'NPPL022','Abhinav Tiwari','Casual','Credit','2024-07-31 18:01:00.047000','2024-07-31 18:01:00.047000','2024-07-31 18:01:00.047000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(73,'NPPL022','Abhinav Tiwari','Medical','Credit','2024-07-31 18:01:00.052000','2024-07-31 18:01:00.052000','2024-07-31 18:01:00.052000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(74,'NPPL025','Abhinav Tiwari','Casual','Credit','2024-07-31 18:01:00.056000','2024-07-31 18:01:00.056000','2024-07-31 18:01:00.056000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(75,'NPPL025','Abhinav Tiwari','Medical','Credit','2024-07-31 18:01:00.060000','2024-07-31 18:01:00.060000','2024-07-31 18:01:00.060000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(76,'NPPL027','Abhinav Tiwari','Casual','Credit','2024-07-31 18:01:00.065000','2024-07-31 18:01:00.065000','2024-07-31 18:01:00.065000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(77,'NPPL027','Abhinav Tiwari','Medical','Credit','2024-07-31 18:01:00.069000','2024-07-31 18:01:00.069000','2024-07-31 18:01:00.069000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(78,'NPPL040','Abhinav Tiwari','Casual','Credit','2024-07-31 18:01:00.073000','2024-07-31 18:01:00.073000','2024-07-31 18:01:00.073000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(79,'NPPL040','Abhinav Tiwari','Medical','Credit','2024-07-31 18:01:00.077000','2024-07-31 18:01:00.077000','2024-07-31 18:01:00.077000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(80,'NPPL004','Abhisheak Singh','Casual','Credit','2024-07-31 18:02:00.017000','2024-07-31 18:02:00.017000','2024-07-31 18:02:00.017000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(81,'NPPL004','Abhisheak Singh','Medical','Credit','2024-07-31 18:02:00.026000','2024-07-31 18:02:00.026000','2024-07-31 18:02:00.026000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(82,'NPPL022','Abhinav Tiwari','Casual','Credit','2024-07-31 18:02:00.031000','2024-07-31 18:02:00.031000','2024-07-31 18:02:00.031000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(83,'NPPL022','Abhinav Tiwari','Medical','Credit','2024-07-31 18:02:00.035000','2024-07-31 18:02:00.035000','2024-07-31 18:02:00.035000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(84,'NPPL025','Abhinav Tiwari','Casual','Credit','2024-07-31 18:02:00.039000','2024-07-31 18:02:00.039000','2024-07-31 18:02:00.039000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(85,'NPPL025','Abhinav Tiwari','Medical','Credit','2024-07-31 18:02:00.043000','2024-07-31 18:02:00.043000','2024-07-31 18:02:00.043000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(86,'NPPL027','Abhinav Tiwari','Casual','Credit','2024-07-31 18:02:00.046000','2024-07-31 18:02:00.046000','2024-07-31 18:02:00.046000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(87,'NPPL027','Abhinav Tiwari','Medical','Credit','2024-07-31 18:02:00.051000','2024-07-31 18:02:00.051000','2024-07-31 18:02:00.051000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(88,'NPPL040','Abhinav Tiwari','Casual','Credit','2024-07-31 18:02:00.056000','2024-07-31 18:02:00.056000','2024-07-31 18:02:00.056000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(89,'NPPL040','Abhinav Tiwari','Medical','Credit','2024-07-31 18:02:00.060000','2024-07-31 18:02:00.060000','2024-07-31 18:02:00.060000',1,'Monthly Credited Leave','Approved','This Leave is Credited By the Schedular Automatically.'),(90,'NPPL004','Abhisheak Singh','Flexi','Debit','2024-08-01 13:24:08.888000','2024-07-17 05:30:00.000000','2024-07-17 05:30:00.000000',1,'ikiuyujyg','Pending',NULL),(92,'NPPL025','Abhinav Tiwari','Medical','Debit','2024-08-01 19:08:20.489000','2024-08-06 00:00:00.000000','2024-08-07 00:00:00.000000',2,'euyhytrg','Declined','No Not available leaves for You.');
/*!40000 ALTER TABLE `leave_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monthly_report`
--

DROP TABLE IF EXISTS `monthly_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `monthly_report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `designation` varchar(45) DEFAULT NULL,
  `reporting_manager` varchar(45) DEFAULT NULL,
  `report_year_month` date DEFAULT NULL,
  `available_leaves` int NOT NULL,
  `approved_leaves` int NOT NULL,
  `lwp_count` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monthly_report`
--

LOCK TABLES `monthly_report` WRITE;
/*!40000 ALTER TABLE `monthly_report` DISABLE KEYS */;
INSERT INTO `monthly_report` VALUES (1,'NPPL004','Abhinav Tiwari',NULL,NULL,'2024-07-01',12,0,1),(2,'NPPL027','rajesh',NULL,NULL,'2024-07-01',7,8,0),(3,'NPPL004','Abhinav Tiwari',NULL,NULL,'2024-07-01',12,0,1),(4,'NPPL027','rajesh',NULL,NULL,'2024-07-01',7,8,0),(5,'NPPL004','Abhinav Tiwari','Team Lead','Abhisheak Singh','2024-08-01',12,2,1),(6,'NPPL027','rajesh','Software Engineer','Abhinav Tiwari','2024-08-01',21,0,0),(7,'NPPL020','xyaz','Senior VP- Sales','Abhinav Tiwari','2024-08-01',0,0,0),(8,'NPPL038','jons','Software Engineer','Abhinav Tiwari','2024-08-01',0,0,4);
/*!40000 ALTER TABLE `monthly_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `report_manager` varchar(255) DEFAULT NULL,
  `email_id` varchar(255) DEFAULT NULL,
  `contact_no` bigint DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `aniversary_date` date DEFAULT NULL,
  `user_key` varchar(255) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `password` blob,
  `profile_pic_url` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('NPPL004','Abhinav@NP','Abhinav Tiwari','Male','Manager,User,Admin','Team Lead','Development','Abhisheak Singh','abhinav@novelpatterns.com',7854123456,'1997-01-02','2018-05-09','37p9DfS0',_binary '',_binary '\ÌWßÛ\’\0\ﬂK\"t9b\È\‚∏','62595f84-c5ca-45c4-ad8a-27865b410e65_Noida_img.jpg'),('NPPL020','xyz@NP','xyaz','Male','User,Admin','Senior VP- Sales','Sales','Abhinav Tiwari','xyz@gmail.com',6789546234,'2024-08-06','2024-08-24','EMyy0h7',_binary '',_binary '\Ì¶\‚ügº†gØ•ïT\Œh',NULL),('NPPL022','lalit@123','lalit lodhi','Male','User','Software Engineer','Development','Abhinav Tiwari','lalit@gmail.com',7890675643,'2024-07-05','2024-07-11','LbFvhhY',_binary '',_binary '˚\œ*™\„\·`WyZXT\Êπ',NULL),('NPPL025','Honey@NP','Honey Verma','Male','User','Software Engineer','Development','Abhinav Tiwari','honey@gmail.com',9465276823,'2024-07-01','2024-07-03','zEIGzCL',_binary '',_binary '8!\⁄¡\¬1î\ﬁ\ÁÖ}=≠',NULL),('NPPL027','rajesh@NP','rajesh','Male','User','Software Engineer','Development','Abhinav Tiwari','rajesh.kc@novelpatterns.com',5643123456,'2024-07-03','2024-07-04','QmhhV0yF',_binary '',_binary ']É\\Sït\€˛s\Ã◊èŸ£F',NULL),('NPPL037','rojesh@NP','rojesh Singh','Male','User','Senior Operations Associate','Management','Abhinav Tiwari','royesh@gmail.com',8978653427,'2024-08-15','2024-08-08','MdbGnW',_binary '',_binary 'VVO6qWª\ZH\”}`\'',NULL),('NPPL038','jon@NP','jons','Male','User,Admin','Software Engineer','Development','Abhinav Tiwari','jon@gmail.com',5678965434,'2024-08-08','2024-08-09','OgO0yIGG',_binary '',_binary '\ŒÕ∂\Ô\Ì\'6∂gìI¶é\Ô',NULL),('NPPL040','july@123','July Singh','Female','User,Admin','HR Admin','Management','Abhinav Tiwari','july@gmail.com',1234567890,'2024-07-04','2024-07-16','cnMX7N',_binary '',_binary 'uïµ∂\·U\·G\Àj7`¸ö',NULL),('NPPL046','nuto@NP','nuto Singh','Female','User','Senior Sales Consultant','Sales','Abhinav Tiwari','nuto@gmail.com',8976543456,'2024-08-07','2024-08-14','aqMgPAl',_binary '',_binary '\«¡H+Üz™Fõ\'‘¢¨%',NULL),('NPPL067','jakson@NP','jackSon','Male','User','Software Engineer','Development','Abhinav Tiwari','jackson@gmail.com',6543123456,'2024-08-08','2024-08-16','Sl0ecah',_binary '',_binary '-¡\Ê\ÊØLÆ5Ú•L§õ0A',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-02 18:26:23
