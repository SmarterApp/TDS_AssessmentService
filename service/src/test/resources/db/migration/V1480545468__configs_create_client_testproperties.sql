/***********************************************************************************************************************
  File: V1480545468__configs_create_client_testproperties.sql

  Desc: Creates the client test properties which is included when fetching assessments

***********************************************************************************************************************/

use configs;

DROP TABLE IF EXISTS client_testproperties;

CREATE TABLE client_testproperties (
  clientname varchar(100) NOT NULL,
  testid varchar(255) NOT NULL,
  maxopportunities int(11) DEFAULT NULL,
  handscoreproject int(11) DEFAULT NULL,
  prefetch int(11) NOT NULL DEFAULT '2',
  datechanged datetime(3) DEFAULT NULL,
  isprintable bit(1) NOT NULL DEFAULT b'0',
  isselectable bit(1) NOT NULL DEFAULT b'1',
  label varchar(255) DEFAULT NULL,
  printitemtypes varchar(1000) DEFAULT '',
  scorebytds bit(1) NOT NULL DEFAULT b'1',
  batchmodereport bit(1) NOT NULL DEFAULT b'0',
  subjectname varchar(100) DEFAULT NULL,
  origin varchar(50) DEFAULT NULL,
  source varchar(50) DEFAULT NULL,
  maskitemsbysubject bit(1) NOT NULL DEFAULT b'1',
  initialabilitybysubject bit(1) NOT NULL DEFAULT b'1',
  startdate datetime(3) DEFAULT NULL,
  enddate datetime(3) DEFAULT NULL,
  ftstartdate datetime(3) DEFAULT NULL,
  ftenddate datetime(3) DEFAULT NULL,
  accommodationfamily varchar(50) DEFAULT NULL,
  sortorder int(11) DEFAULT NULL,
  rtsformfield varchar(50) NOT NULL DEFAULT 'tds-testform',
  rtswindowfield varchar(50) NOT NULL DEFAULT 'tds-testwindow',
  windowtideselectable bit(1) NOT NULL DEFAULT b'0',
  requirertswindow bit(1) NOT NULL DEFAULT b'0',
  reportinginstrument varchar(50) DEFAULT NULL,
  tide_id varchar(100) DEFAULT NULL,
  forcecomplete bit(1) NOT NULL DEFAULT b'1',
  rtsmodefield varchar(50) NOT NULL DEFAULT 'tds-testmode',
  modetideselectable bit(1) NOT NULL DEFAULT b'0',
  requirertsmode bit(1) NOT NULL DEFAULT b'0',
  requirertsmodewindow bit(1) NOT NULL DEFAULT b'0',
  deleteunanswereditems bit(1) NOT NULL DEFAULT b'0',
  abilityslope float NOT NULL DEFAULT '1',
  abilityintercept float NOT NULL DEFAULT '0',
  validatecompleteness bit(1) NOT NULL DEFAULT b'0',
  gradetext varchar(50) DEFAULT NULL,
  initialabilitytestid varchar(100) DEFAULT NULL,
  proctoreligibility int(11) NOT NULL DEFAULT '0',
  category varchar(50) DEFAULT NULL,
  msb bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (clientname,testid),
  KEY ix_testprops_test (testid)
);