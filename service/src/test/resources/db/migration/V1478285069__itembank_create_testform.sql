/***********************************************************************************************************************
  File: V1478285069__itembank_create_testform.sql

  Desc: Create the testform storing assessment form properties.

***********************************************************************************************************************/

use itembank;

DROP TABLE IF EXISTS testform;

CREATE TABLE `testform` (
  `_fk_adminsubject` varchar(250) NOT NULL,
  `_efk_itsbank` bigint(20) NOT NULL,
  `_efk_itskey` bigint(20) NOT NULL,
  `formid` varchar(150) DEFAULT NULL,
  `language` varchar(150) DEFAULT NULL,
  `_key` varchar(100) NOT NULL,
  `itsid` varchar(150) DEFAULT NULL,
  `iteration` int(11) NOT NULL DEFAULT '0',
  `loadconfig` bigint(20) DEFAULT NULL,
  `updateconfig` bigint(20) DEFAULT NULL,
  `cohort` varchar(20) NOT NULL DEFAULT 'default',
  PRIMARY KEY (`_key`),
  KEY `fk_testform_tblsetofadminsubjects` (`_fk_adminsubject`)
--   CONSTRAINT `fk_testform_tblsetofadminsubjects` FOREIGN KEY (`_fk_adminsubject`) REFERENCES `tblsetofadminsubjects` (`_key`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
