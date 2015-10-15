-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 12, 2014 at 05:57 PM
-- Server version: 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `BANK-VCT`
--

-- --------------------------------------------------------

--
-- Table structure for table `Account`
--

CREATE TABLE IF NOT EXISTS `Account` (
  `KundenID` int(11) NOT NULL,
  `Kundenname` varchar(256) NOT NULL,
  `Strassenname` varchar(256) NOT NULL,
  `PLZ` varchar(10) NOT NULL,
  `Stadt` varchar(64) NOT NULL,
  `Land` varchar(64) NOT NULL,
  `Kundenart` varchar(10) NOT NULL,
  `Kontonummer` decimal(11,0) NOT NULL,
  `Saldo` float NOT NULL,
  `Clearing` decimal(64,0) NOT NULL,
  PRIMARY KEY (`KundenID`),
  UNIQUE KEY `KundenID` (`KundenID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Account`
--

INSERT INTO `Account` (`KundenID`, `Kundenname`, `Strassenname`, `PLZ`, `Stadt`, `Land`, `Kundenart`, `Kontonummer`, `Saldo`, `Clearing`) VALUES
(1, 'Roger Müller', 'Riggenbachstrasse 12', '4600', 'Olten', 'Switzerland', 'Privat', '14556445884', 1156.14, '261'),
(2, 'FHNW', 'Riggenbachstrasse 16', '4600', 'Olten', 'Switzerland', 'Firma', '66866954523', 2.36, '261'),
(3, 'Aldo von Wangenheim', 'Rue de la Chanson 3a', '1205', 'Genf', 'Switzerland', 'Privat', '55688912563', 500154, '651'),
(4, 'F. van Harmelen', 'Amstraat 12', '1089HE', 'Amsterdam', 'The Netherlands', 'Privat', '66944755425', 65875, '261'),
(5, 'M. M. Richter', 'Universitätsgasse 2', '67655', 'Kaiserslautern', 'Germany', 'Privat', '66566545425', 13658.2, '261'),
(6, 'Otthein Herzog', 'Hinter dem Deich 2', '18119', 'Warnemünde', 'Germany', 'Privat', '99899699623', 5211.36, '261'),
(7, 'Encoway', 'Startupweg 1', '28195', 'Bremen', 'Deutschland', 'Firma', '66566525253', 100898, '261'),
(8, 'Dr. Oliver Bendel', 'Kastelgasse 31', '8001', 'Zürich', 'Switzerland', 'Privat', '65454522136', 1002.23, '261'),
(9, 'Stella Gatziu Grivas', 'Blocherweg 3', '8704', 'Herrliberg-Feldmeilen', 'Switzerland', 'Privat', '11233232236', 200.56, '261'),
(10, 'Knut HINKELMANN', 'Am Wald 3078', '4616', 'Kappel', 'Schweiz', 'Private', '99899855612', 1523.32, '261');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
