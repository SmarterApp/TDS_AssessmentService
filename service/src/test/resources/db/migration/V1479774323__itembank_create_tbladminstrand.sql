/***********************************************************************************************************************
  File: V1479774323__itembank_create_tbladminstrand.sql

  Desc: Creates the tbladminstrand table

***********************************************************************************************************************/

use itembank;

DROP TABLE IF EXISTS tbladminstrand;

CREATE TABLE `tbladminstrand` (
  `_fk_adminsubject` varchar(250) NOT NULL,
  `_fk_strand` varchar(150) NOT NULL,
  `minitems` int(11) DEFAULT NULL,
  `maxitems` int(11) DEFAULT NULL,
  `adaptivecut` float DEFAULT NULL,
  `startability` float DEFAULT NULL,
  `startinfo` float DEFAULT NULL,
  `scalar` float DEFAULT NULL,
  `_fk_proflevels` bigint(20) DEFAULT NULL,
  `loaderid` varchar(150) DEFAULT NULL,
  `synthmin` int(11) DEFAULT NULL,
  `synthmax` int(11) DEFAULT NULL,
  `inheritmin` int(11) DEFAULT NULL,
  `inheritmax` int(11) DEFAULT NULL,
  `inheritratio` float DEFAULT '-1',
  `numitems` int(11) DEFAULT NULL,
  `loadmin` int(11) DEFAULT NULL,
  `loadmax` int(11) DEFAULT NULL,
  `_key` varchar(255) NOT NULL,
  `isstrictmax` bit(1) NOT NULL DEFAULT b'0',
  `bpweight` float NOT NULL DEFAULT '1',
  `loadconfig` bigint(20) DEFAULT NULL,
  `updateconfig` bigint(20) DEFAULT NULL,
  `precisiontarget` float DEFAULT NULL,
  `precisiontargetmetweight` float DEFAULT NULL,
  `precisiontargetnotmetweight` float DEFAULT NULL,
  `abilityweight` float DEFAULT NULL,
  PRIMARY KEY (`_key`),
  KEY `ix_adminstrand_test` (`_fk_adminsubject`),
  KEY `ix_tbladminstrand` (`loaderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
