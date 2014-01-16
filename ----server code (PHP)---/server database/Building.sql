-- phpMyAdmin SQL Dump
-- version 3.3.8.1
-- http://www.phpmyadmin.net
--
-- Host: w.rdc.sae.sina.com.cn:3307
-- Generation Time: Dec 21, 2013 at 02:03 PM
-- Server version: 5.5.23
-- PHP Version: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `app_campusgps`
--

-- --------------------------------------------------------

--
-- Table structure for table `Building`
--

CREATE TABLE IF NOT EXISTS `Building` (
  `Bid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `BuildingName` varchar(50) NOT NULL,
  `BuildingAddress` varchar(100) NOT NULL,
  `LocationLat` double NOT NULL DEFAULT '0',
  `LocationLng` double NOT NULL DEFAULT '0',
  `QueryTime` int(10) NOT NULL DEFAULT '0',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdateTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`Bid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=51 ;

--
-- Dumping data for table `Building`
--

INSERT INTO `Building` (`Bid`, `BuildingName`, `BuildingAddress`, `LocationLat`, `LocationLng`, `QueryTime`, `CreateTime`, `UpdateTime`) VALUES
(1, 'Mclean Hall', '2 West Washington Street, Iowa City, IA 52242', 41.660715, -91.53652, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(2, 'Jessup Hall', '5 West Jefferson Street, Iowa City, IA 52242', 41.661885, -91.53652, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(3, 'Schaeffer hall', '20 East Washington Street, Iowa City, IA 52242', 41.660695, -91.535656, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(4, 'Macbride Hall', '17 North Clinton Street, Iowa City, IA 52242', 41.661889, -91.535667, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(5, 'Old Capitol Museum', '21 N Clinton St, Iowa City, IA 52242', 41.661284, -91.536155, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(6, 'Main Library', '125 West Washington Street, Iowa City, IA 52242', 41.659533, -91.53844, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(7, 'Halsey Hall', '28 West Jefferson Street, Iowa City, IA 52242', 41.662859, -91.537147, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(8, 'IMU Parking Ramp', '120 North Madison Street, Iowa City, IA 52242', 41.663106, -91.538247, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(9, 'Calvin Hall', '2 West Jefferson Street, Iowa City, IA 52242', 41.662799, -91.53645, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(10, 'Gilmore Hall', '112 North Capitol Street, Iowa City, IA 52242', 41.662817, -91.535801, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(11, 'Trowbridge Hall', '123 North Capitol Street, Iowa City, IA 52242', 41.663381, -91.536563, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(12, 'Tippie College of Business', '21 East Market Street, Iowa City, IA 52245', 41.663311, -91.534987, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(13, 'Chemistry Bldg', '251 North Capitol Street', 41.664113, -91.536632, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(14, 'Engineering Research Facility', '330 South Madison Street', 41.656966, -91.537271, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(15, 'Communications Center', '116 South Madison Street', 41.659309, -91.537291, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(16, 'Recreation Center', '309 South Madison', 41.657278, -91.538461, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(17, 'Iowa Memorial Union', '125 North Madison Street', 41.663106, -91.538247, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(18, 'Lindquist Center', '240 South Madison Street', 41.658529, -91.537228, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(19, 'Seaman Center', '103 South Capitol Street', 41.659683, -91.536616, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(20, 'Old Capitol Mall', '200 South Capitol Street', 41.659378, -91.535447, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(21, 'Pomerantz Center', '213 North Clinton Street', 41.663893, -91.53572, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(22, 'Black Honors Center', '221 North Clinton', 41.664354, -91.535656, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(23, 'Lutheran Campus Ministry', '109 E. Market Street, Iowa City, IA 52245', 41.663961, -91.534894, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(24, 'Duam Hall', '225 North Clinton Street', 41.664478, -91.535087, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(25, 'Burge Residence Hall', '301 North Clinton Street', 41.665244, -91.535216, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(26, 'Iowa Advanced Technology Labs', '205 North Madison Street', 41.664178, -91.538081, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(27, 'Womens Resource and Action Center', '130 North Madison', 41.663415, -91.537273, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(28, 'North Campus Parking', '339 North Madison Street', 41.665256, -91.536541, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(29, 'North Hall', '20 West Davenport Street', 41.666013, -91.536654, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(30, 'Stanley Residence Hall', '10 East Davenport Street', 41.666286, -91.535854, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(31, 'Currier Residence Hall', '413 North Clinton Street', 41.666358, -91.535044, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(32, 'Dey House', '507 North Clinton Street', 41.667245, -91.53506, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(33, 'Glenn Schaeffer Library', '507 North Clinton Street', 41.667429, -91.535105, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(34, 'Jefferson Bldg', '129 East Washington Street', 41.659895, -91.533344, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(35, 'Phillips Hall', '16 North Clinton Street', 41.661825, -91.534191, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(36, 'Biology Bldg', '129 East Jefferson Street', 41.661859, -91.533392, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(37, 'Biology Bldg East', '210 East Iowa Avenue', 41.661617, -91.532673, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(38, 'Sciences Library', '120 East Iowa Avenue', 41.662029, -91.533827, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(39, 'Van Allen Hall', '30 North Dubuque Street', 41.662166, -91.532148, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(40, 'Spence Laboratories of Psychology', '308 East Iowa Avenue', 41.661553, -91.531311, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(41, 'Seashore Hall', '301 East Jefferson Street', 41.661942, -91.531295, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(42, 'Stuit Hall', '335 East Jefferson Street', 41.662202, -91.530479, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(43, 'University Services Blgd', '1 West Prentiss', 41.654082, -91.536664, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(44, 'Hillcrest Hall', '25 Byington Road', 41.65926, -91.542399, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(45, 'Hillcrest Residence Hall', '25 Byington Road', 41.659268, -91.542844, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(46, 'English Philosophy Building', '308 English Philosophy Bldg, Iowa City, IA 52242', 41.660788103266725, -91.53988234698772, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(47, 'Adler Journalism and Mass Communication Building', '104 West Washington, Iowa City, IA 52242', 41.66060775558548, -91.53880879282951, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(48, 'Becker Communications Studies Bldg', '25 South Madison Street, Iowa City, IA 52242', 41.66049503802824, -91.53813119977713, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(49, 'Boyd Law Building', '220 Boyd Law Bldg, Iowa City, IA 52242', 41.65748189935511, -91.54279489070177, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00'),
(50, 'Field House', '225 South Grand Avenue, Iowa City, IA 52242', 41.65844078860639, -91.54661938548088, 0, '2013-12-01 10:44:13', '0000-00-00 00:00:00');
