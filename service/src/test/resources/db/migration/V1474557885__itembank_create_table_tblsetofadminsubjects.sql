/***********************************************************************************************************************
  File: V1474557885__itembank_create_table_tblsetofadminsubjects.sql

  Desc: Create the tblsetofadminsubjects table the assessment service relies upon and populate it with a single record.

***********************************************************************************************************************/
use itembank;

DROP TABLE IF EXISTS tblsetofadminsubjects;

CREATE TABLE tblsetofadminsubjects (
  _key varchar(250) NOT NULL,
  _fk_testadmin varchar(150) NOT NULL,
  _fk_subject varchar(150) NOT NULL,
  testid varchar(255) DEFAULT NULL,
  startability float DEFAULT NULL,
  startinfo float DEFAULT NULL,
  minitems int(11) DEFAULT NULL,
  maxitems int(11) DEFAULT NULL,
  slope float DEFAULT NULL,
  intercept float DEFAULT NULL,
  ftstartpos int(11) DEFAULT NULL,
  ftendpos int(11) DEFAULT NULL,
  ftminitems int(11) DEFAULT NULL,
  ftmaxitems int(11) DEFAULT NULL,
  sampletarget float DEFAULT NULL,
  selectionalgorithm varchar(50) DEFAULT NULL,
  formselection varchar(100) DEFAULT NULL,
  blueprintweight float NOT NULL DEFAULT 5,
  abilityweight float NOT NULL DEFAULT 1,
  cset1size int(11) NOT NULL DEFAULT 20,
  cset2random int(11) NOT NULL DEFAULT 1,
  cset2initialrandom int(11) NOT NULL DEFAULT 5,
  virtualtest varchar(200) DEFAULT NULL,
  testposition int(11) DEFAULT NULL,
  issegmented bit(1) NOT NULL DEFAULT 0,
  computeabilityestimates bit(1) NOT NULL DEFAULT 1,
  loadconfig bigint(20) DEFAULT NULL,
  updateconfig bigint(20) DEFAULT NULL,
  itemweight float DEFAULT 5,
  abilityoffset float DEFAULT 0,
  contract varchar(100) DEFAULT NULL,
  its_id varchar(200) DEFAULT NULL,
  cset1order varchar(50) NOT NULL DEFAULT 'ability',
  refreshminutes int(11) DEFAULT NULL,
  rcabilityweight float NOT NULL DEFAULT 1,
  precisiontarget float DEFAULT NULL,
  precisiontargetmetweight float NOT NULL DEFAULT 1,
  precisiontargetnotmetweight float NOT NULL DEFAULT 1,
  adaptivecut float DEFAULT NULL,
  toocloseses float DEFAULT NULL,
  terminationoverallinfo bit(1) NOT NULL DEFAULT 0,
  terminationrcinfo bit(1) NOT NULL DEFAULT 0,
  terminationmincount bit(1) NOT NULL DEFAULT 0,
  terminationtooclose bit(1) NOT NULL DEFAULT 0,
  terminationflagsand bit(1) NOT NULL DEFAULT 0,
  bpmetricfunction varchar(25) NOT NULL DEFAULT 'bp2',
  _efk_blueprint bigint(20) DEFAULT NULL,
  initialabilitytestID varchar(100) DEFAULT NULL,
  testtype varchar(30) DEFAULT NULL,
  PRIMARY KEY (_key)
);