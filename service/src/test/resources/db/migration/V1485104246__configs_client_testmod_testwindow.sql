/***********************************************************************************************************************
  File: V1485104246__configs_client_testmod_testwindow.sql

  Desc: Creates the client test mode and client test window tables

***********************************************************************************************************************/

USE configs;

DROP TABLE IF EXISTS client_testmode;
CREATE TABLE client_testmode (
  clientname             VARCHAR(100)  NOT NULL,
  testid                 VARCHAR(200)  NOT NULL,
  mode                   VARCHAR(50)   NOT NULL DEFAULT 'online',
  algorithm              VARCHAR(50)            DEFAULT NULL,
  formtideselectable     BIT(1)        NOT NULL DEFAULT 0,
  issegmented            BIT(1)        NOT NULL DEFAULT 0,
  maxopps                INT(11)       NOT NULL DEFAULT 50,
  requirertsform         BIT(1)        NOT NULL DEFAULT 0,
  requirertsformwindow   BIT(1)        NOT NULL DEFAULT 0,
  requirertsformifexists BIT(1)        NOT NULL DEFAULT 1,
  sessiontype            INT(11)       NOT NULL DEFAULT '-1',
  testkey                VARCHAR(250)           DEFAULT NULL,
  _key                   VARBINARY(16) NOT NULL,
  PRIMARY KEY (_key),
  UNIQUE KEY ix_clienttestmode (testkey, sessiontype),
  KEY ix_testmode (clientname, testid, sessiontype),
  KEY ix_testmodekey (testkey),
  KEY ix_testmode_test (testid)
);

DROP TABLE IF EXISTS client_testwindow;
CREATE TABLE client_testwindow (
  clientname  VARCHAR(100)  NOT NULL,
  testid      VARCHAR(200)  NOT NULL,
  window      INT(11)       NOT NULL DEFAULT 1,
  numopps     INT(11)       NOT NULL,
  startdate   DATETIME(3)            DEFAULT NULL,
  enddate     DATETIME(3)            DEFAULT NULL,
  origin      VARCHAR(100)           DEFAULT NULL,
  source      VARCHAR(100)           DEFAULT NULL,
  windowid    VARCHAR(50)            DEFAULT NULL,
  _key        VARBINARY(16) NOT NULL,
  sessiontype INT(11)       NOT NULL DEFAULT -1,
  sortorder   INT(11)                DEFAULT 1,
  PRIMARY KEY (_key),
  KEY fk_timewindow (clientname, windowid),
  KEY ix_clienttestwindow (clientname, testid),
  KEY ix_testwindow_test (testid)
);