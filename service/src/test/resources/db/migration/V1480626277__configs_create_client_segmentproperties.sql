/***********************************************************************************************************************
  File: V1480626277__configs_create_client_segmentproperties.sql

  Desc: Creates the client segment properties

***********************************************************************************************************************/

use configs;

DROP TABLE IF EXISTS client_segmentproperties;

CREATE TABLE `client_segmentproperties` (
  `ispermeable` int(11) NOT NULL,
  `clientname` varchar(100) NOT NULL,
  `entryapproval` int(11) NOT NULL,
  `exitapproval` int(11) NOT NULL,
  `itemreview` bit(1) NOT NULL DEFAULT b'1',
  `segmentid` varchar(255) NOT NULL,
  `segmentposition` int(11) NOT NULL,
  `parenttest` varchar(255) NOT NULL,
  `ftstartdate` datetime(3) DEFAULT NULL,
  `ftenddate` datetime(3) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `modekey` varchar(250) DEFAULT NULL,
  `restart` int(11) DEFAULT NULL,
  `graceperiodminutes` int(11) DEFAULT NULL,
  PRIMARY KEY (`clientname`,`parenttest`,`segmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
