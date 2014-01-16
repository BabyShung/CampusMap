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
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `UserID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Email` varchar(100) NOT NULL,
  `UserName` varchar(50) NOT NULL,
  `UserPWD` varchar(50) NOT NULL,
  `dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `lasttime` datetime NOT NULL COMMENT '最近一次登录时间',
  PRIMARY KEY (`UserID`),
  KEY `UserName` (`UserName`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=40 ;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`UserID`, `Email`, `UserName`, `UserPWD`, `dt`, `lasttime`) VALUES
(39, 'hao-zheng@uiowa.edu', 'Hao Zheng', '123123zh', '2013-11-28 14:59:05', '2013-12-20 04:19:12');
