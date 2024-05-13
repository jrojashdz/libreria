CREATE DATABASE  IF NOT EXISTS `libreria` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `libreria`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: libreria
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `libros`
--

DROP TABLE IF EXISTS `libros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libros` (
  `codigo` int NOT NULL AUTO_INCREMENT,
  `nombre` text,
  `editorial` text,
  `autor` text,
  `genero` text,
  `paisautor` text,
  `numeropaginas` int DEFAULT NULL,
  `anio` int DEFAULT NULL,
  `precio` int DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libros`
--

LOCK TABLES `libros` WRITE;
/*!40000 ALTER TABLE `libros` DISABLE KEYS */;
INSERT INTO `libros` VALUES (1,'Don Quijote de La Mancha I','Anaya','Miguel de Cervantes','Caballeresco','España',517,1991,2750),(3,'Historias de Nueva Orleans','Alfaguara','William Faulkner','Novela','Estados Unidos',186,1985,675),(4,'El principito','Andina','Antoine Saint-Exupery','Aventura','Francia',120,1996,750),(5,'El príncipe','S.M.','Maquiavelo','Político','Italia',210,1995,1125),(6,'Diplomacia','S.M.','Henry Kissinger','Político','Alemania',825,1997,1750),(8,'El Último Emperador','Caralt','Pu-Yi','Autobiografías','China',353,1989,995),(9,'Fortunata y Jacinta','Plaza & Janes','Pérez Galdós','Novela','España',625,1984,725);
/*!40000 ALTER TABLE `libros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libros_has_prestamo`
--

DROP TABLE IF EXISTS `libros_has_prestamo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libros_has_prestamo` (
  `libros_codigo` int NOT NULL,
  `prestamo_idprestamo` int NOT NULL,
  PRIMARY KEY (`libros_codigo`,`prestamo_idprestamo`),
  KEY `fk_libros_has_prestamo_prestamo1_idx` (`prestamo_idprestamo`),
  KEY `fk_libros_has_prestamo_libros1_idx` (`libros_codigo`),
  CONSTRAINT `fk_libros_has_prestamo_libros1` FOREIGN KEY (`libros_codigo`) REFERENCES `libros` (`codigo`),
  CONSTRAINT `fk_libros_has_prestamo_prestamo1` FOREIGN KEY (`prestamo_idprestamo`) REFERENCES `prestamo` (`idprestamo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libros_has_prestamo`
--

LOCK TABLES `libros_has_prestamo` WRITE;
/*!40000 ALTER TABLE `libros_has_prestamo` DISABLE KEYS */;
/*!40000 ALTER TABLE `libros_has_prestamo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prestamo`
--

DROP TABLE IF EXISTS `prestamo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prestamo` (
  `idprestamo` int NOT NULL AUTO_INCREMENT,
  `fecha` date DEFAULT NULL,
  `fechadevolucion` date DEFAULT NULL,
  `usuarios_idusuario` int NOT NULL,
  `estatus` enum('vencido','devuelto','prestamo') DEFAULT NULL,
  PRIMARY KEY (`idprestamo`),
  KEY `fk_prestamo_usuarios_idx` (`usuarios_idusuario`),
  CONSTRAINT `fk_prestamo_usuarios` FOREIGN KEY (`usuarios_idusuario`) REFERENCES `usuarios` (`idusuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamo`
--

LOCK TABLES `prestamo` WRITE;
/*!40000 ALTER TABLE `prestamo` DISABLE KEYS */;
/*!40000 ALTER TABLE `prestamo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `idusuario` int NOT NULL AUTO_INCREMENT,
  `nombre` text,
  `calle_numero` text,
  `codigo_postal` int DEFAULT NULL,
  `ciudad` text,
  `pais` text,
  `correo_electronico` text,
  PRIMARY KEY (`idusuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Juan Gutierrez','Moliner, 50',46100,'Valencia','España','Juan.Gutierrez@uv.es'),(2,'Esther de Ves','Menendez Pidal,9',46009,'Valencia','España','Esther.Deves@uv.es'),(3,'Elena Diaz','Moliner,50',46100,'Burjassot','España','Elena.Diaz@uv.es'),(4,'Miguel Martinez','Altea, 5',56789,'Madrid','España','Mima@hotmail.com'),(5,'Juan Gonzalez','Cuenca,50',45320,'Barcelona','España','Jugo@hotmail.com'),(6,'Juan Jose López','Pérez Galdós',26040,'Almansa','España','julo@retevision.com'),(7,'Rosario Alcocer','Rambla de la Mancha',26100,'Albacete','España','Roal@upv.es');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-12 21:14:57
