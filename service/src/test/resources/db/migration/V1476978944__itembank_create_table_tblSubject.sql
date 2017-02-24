/***********************************************************************************************************************
  File: V1476978944__itembank_create_table_tblSubject.sql

  Desc: Create the tblsubject table the assessment service relies upon.

***********************************************************************************************************************/
use itembank;

DROP TABLE IF EXISTS tblclient;
DROP TABLE IF EXISTS tblsubject;

CREATE TABLE tblclient (
  _key bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  description varchar(255) DEFAULT NULL,
  homepath varchar(250) DEFAULT NULL,
  PRIMARY KEY (_key)
);

CREATE TABLE tblsubject (
  name varchar(100) NOT NULL,
  grade varchar(64) DEFAULT NULL,
  _key varchar(150) NOT NULL,
  _fk_client bigint(20) DEFAULT NULL,
  loadconfig bigint(20) DEFAULT NULL,
  updateconfig bigint(20) DEFAULT NULL,
  PRIMARY KEY (_key),
  KEY fk_subject_client (_fk_client),
  CONSTRAINT fk_subject_client FOREIGN KEY (_fk_client) REFERENCES tblclient (_key) ON DELETE CASCADE ON UPDATE CASCADE
);