/***********************************************************************************************************************
  File: V1476978944__itembank_create_table_tblSubject.sql

  Desc: Create the tblsubject table the assessment service relies upon.

***********************************************************************************************************************/

use itembank;

DROP TABLE IF EXISTS tblitemprops;

CREATE TABLE tblitemprops (
  _fk_item varchar(150) NOT NULL,
  propname varchar(50) NOT NULL,
  propvalue varchar(128) NOT NULL,
  propdescription varchar(150) DEFAULT NULL,
  _fk_adminsubject varchar(250) NOT NULL,
  isactive bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (_fk_adminsubject,_fk_item,propname,propvalue)
);