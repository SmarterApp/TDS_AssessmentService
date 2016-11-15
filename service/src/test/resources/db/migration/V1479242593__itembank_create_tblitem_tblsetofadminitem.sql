/***********************************************************************************************************************
  File: V1479242593__itembank_create_tblitem_tblsetofadminitem.sql

  Desc: Creates the tblitem and tblsetofadminitem tables containing item data

***********************************************************************************************************************/

use itembank;

DROP TABLE IF EXISTS tblitem;
DROP TABLE IF EXISTS tblsetofadminitems;

CREATE TABLE `tblitem` (
  `_efk_itembank` bigint(20) NOT NULL,
  `_efk_item` bigint(20) NOT NULL AUTO_INCREMENT,
  `itemtype` varchar(50) DEFAULT NULL,
  `answer` varchar(50) DEFAULT NULL,
  `scorepoint` int(11) DEFAULT NULL,
  `filepath` varchar(50) DEFAULT NULL,
  `filename` varchar(50) DEFAULT NULL,
  `version` varchar(150) DEFAULT NULL,
  `datelastupdated` datetime(3) DEFAULT NULL,
  `itemid` varchar(80) DEFAULT NULL,
  `_key` varchar(150) NOT NULL,
  `contentsize` int(11) DEFAULT NULL,
  `loadconfig` bigint(20) DEFAULT NULL,
  `updateconfig` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`_key`),
  UNIQUE KEY `ix_tblitem` (`_efk_itembank`,`_efk_item`),
  KEY `ix_efk_item` (`_efk_item`)
) ENGINE=InnoDB AUTO_INCREMENT=3642 DEFAULT CHARSET=utf8;

CREATE TABLE `tblsetofadminitems` (
  `_fk_item` varchar(150) NOT NULL,
  `_fk_adminsubject` varchar(250) NOT NULL,
  `groupid` varchar(100) DEFAULT NULL,
  `itemposition` int(11) DEFAULT NULL,
  `isfieldtest` bit(1) DEFAULT NULL,
  `isactive` bit(1) DEFAULT NULL,
  `irt_b` varchar(150) DEFAULT NULL,
  `blockid` varchar(10) DEFAULT NULL,
  `ftstartdate` datetime(3) DEFAULT NULL,
  `ftenddate` datetime(3) DEFAULT NULL,
  `isrequired` bit(1) NOT NULL DEFAULT b'0',
  `isprintable` bit(1) DEFAULT b'0',
  `_fk_testadmin` varchar(150) DEFAULT NULL,
  `responsemimetype` varchar(255) NOT NULL DEFAULT 'text/plain',
  `testcohort` int(11) NOT NULL DEFAULT '1',
  `_fk_strand` varchar(150) DEFAULT NULL,
  `loadconfig` bigint(20) DEFAULT NULL,
  `updateconfig` bigint(20) DEFAULT NULL,
  `groupkey` varchar(100) DEFAULT NULL,
  `strandname` varchar(150) DEFAULT NULL,
  `irt_a` float DEFAULT NULL,
  `irt_c` float DEFAULT NULL,
  `irt_model` varchar(100) DEFAULT NULL,
  `bvector` varchar(200) DEFAULT NULL,
  `notforscoring` bit(1) NOT NULL DEFAULT b'0',
  `clstring` text,
  `irt2_a` varchar(400) DEFAULT NULL,
  `irt2_b` varchar(800) DEFAULT NULL,
  `irt2_c` varchar(400) DEFAULT NULL,
  `irt2_model` varchar(800) DEFAULT NULL,
  `ftweight` float NOT NULL DEFAULT '1',
  PRIMARY KEY (`_fk_adminsubject`,`_fk_item`),
  KEY `fk_tblsetofadminitems_tblitem` (`_fk_item`),
  KEY `ix_adminitemgroup2` (`_fk_adminsubject`,`groupid`,`blockid`),
  KEY `ix_adminitem_ftitemgroup` (`_fk_adminsubject`,`isfieldtest`,`groupkey`),
  CONSTRAINT `fk_tblsetofadminitems_tblitem` FOREIGN KEY (`_fk_item`) REFERENCES `tblitem` (`_key`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_tblsetofadminitems_tblsetofadminsubjects` FOREIGN KEY (`_fk_adminsubject`) REFERENCES `tblsetofadminsubjects` (`_key`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
