/***********************************************************************************************************************
  File: V1486594639__create_configs_client_tooldependencies.sql

  Desc: Adds the client_tooldependencies table for accommodation/tool dependencies

***********************************************************************************************************************/

USE configs;

DROP TABLE IF EXISTS client_tooldependencies;

CREATE TABLE `client_tooldependencies` (
  `context` varchar(250) NOT NULL,
  `contexttype` varchar(50) NOT NULL,
  `iftype` varchar(50) NOT NULL,
  `ifvalue` varchar(255) NOT NULL,
  `isdefault` bit(1) NOT NULL DEFAULT b'0',
  `thentype` varchar(50) NOT NULL,
  `thenvalue` varchar(255) NOT NULL,
  `clientname` varchar(100) NOT NULL,
  `_key` varbinary(16) NOT NULL,
  `testmode` varchar(25) NOT NULL DEFAULT 'all',
  PRIMARY KEY (`_key`),
  KEY `ix_clienttooldependencies` (`clientname`,`contexttype`,`context`)
)