/***********************************************************************************************************************
  File: V1485113033__configs_assessment_window_config_tables.sql

  Desc: Adds the client_testtool and client_testtooltype tables leveraged during the accommodations

***********************************************************************************************************************/

USE configs;

DROP TABLE IF EXISTS client_testtool;
DROP TABLE IF EXISTS client_testtooltype;
DROP TABLE IF EXISTS client_segmentproperties;
DROP TABLE IF EXISTS `client_testformproperties`;

CREATE TABLE client_testtooltype (
  clientname              VARCHAR(100) NOT NULL,
  toolname                VARCHAR(255) NOT NULL,
  allowchange             BIT(1)       NOT NULL DEFAULT 1,
  tideselectable          BIT(1)                DEFAULT NULL,
  rtsfieldname            VARCHAR(100)          DEFAULT NULL,
  isrequired              BIT(1)       NOT NULL DEFAULT 0,
  tideselectablebysubject BIT(1)       NOT NULL DEFAULT 0,
  isselectable            BIT(1)       NOT NULL DEFAULT 1,
  isvisible               BIT(1)       NOT NULL DEFAULT 1,
  studentcontrol          BIT(1)       NOT NULL DEFAULT 1,
  tooldescription         VARCHAR(255)          DEFAULT NULL,
  sortorder               INT(11)      NOT NULL DEFAULT 0,
  dateentered             DATETIME(3)  NOT NULL,
  origin                  VARCHAR(50)           DEFAULT NULL,
  source                  VARCHAR(50)           DEFAULT NULL,
  contexttype             VARCHAR(50)  NOT NULL,
  context                 VARCHAR(255) NOT NULL,
  dependsontooltype       VARCHAR(50)           DEFAULT NULL,
  disableonguestsession   BIT(1)       NOT NULL DEFAULT 0,
  isfunctional            BIT(1)       NOT NULL DEFAULT 1,
  testmode                VARCHAR(25)  NOT NULL DEFAULT 'all',
  isentrycontrol          BIT(1)       NOT NULL DEFAULT 0,
  PRIMARY KEY (clientname, context, contexttype, toolname)
);

CREATE TABLE client_testtool (
  clientname           VARCHAR(100) NOT NULL,
  type                 VARCHAR(255) NOT NULL,
  code                 VARCHAR(255) NOT NULL,
  value                VARCHAR(255) NOT NULL,
  isdefault            BIT(1)       NOT NULL,
  allowcombine         BIT(1)       NOT NULL,
  valuedescription     VARCHAR(255)          DEFAULT NULL,
  context              VARCHAR(255) NOT NULL,
  sortorder            INT(11)      NOT NULL DEFAULT 0,
  origin               VARCHAR(50)           DEFAULT NULL,
  source               VARCHAR(50)           DEFAULT NULL,
  contexttype          VARCHAR(50)  NOT NULL,
  testmode             VARCHAR(25)  NOT NULL DEFAULT 'all',
  equivalentclientcode VARCHAR(255)          DEFAULT NULL,
  PRIMARY KEY (clientname, context, contexttype, type, code),
  KEY ix_clienttooltestid (context),
  CONSTRAINT fk_clienttool_tooltype FOREIGN KEY (clientname, context, contexttype, type) REFERENCES client_testtooltype (clientname, context, contexttype, toolname)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE client_segmentproperties (
  ispermeable        INT(11)      NOT NULL,
  clientname         VARCHAR(100) NOT NULL,
  entryapproval      INT(11)      NOT NULL,
  exitapproval       INT(11)      NOT NULL,
  itemreview         BIT(1)       NOT NULL DEFAULT 1,
  segmentid          VARCHAR(255) NOT NULL,
  segmentposition    INT(11)      NOT NULL,
  parenttest         VARCHAR(255) NOT NULL,
  ftstartdate        DATETIME(3)           DEFAULT NULL,
  ftenddate          DATETIME(3)           DEFAULT NULL,
  label              VARCHAR(255)          DEFAULT NULL,
  modekey            VARCHAR(250)          DEFAULT NULL,
  restart            INT(11)               DEFAULT NULL,
  graceperiodminutes INT(11)               DEFAULT NULL,
  PRIMARY KEY (clientname, parenttest, segmentid)
);

CREATE TABLE `client_testformproperties` (
  `clientname`     VARCHAR(100) NOT NULL,
  `_efk_testform`  VARCHAR(50)  NOT NULL,
  `startdate`      DATETIME(3)  DEFAULT NULL,
  `enddate`        DATETIME(3)  DEFAULT NULL,
  `language`       VARCHAR(25)  NOT NULL,
  `formid`         VARCHAR(200) DEFAULT NULL,
  `testid`         VARCHAR(150) NOT NULL,
  `testkey`        VARCHAR(250) DEFAULT NULL,
  `clientformid`   VARCHAR(25)  DEFAULT NULL,
  `accommodations` TEXT,
  PRIMARY KEY (`clientname`, `_efk_testform`),
  KEY `ix_form_testkey` (`testkey`)
);


