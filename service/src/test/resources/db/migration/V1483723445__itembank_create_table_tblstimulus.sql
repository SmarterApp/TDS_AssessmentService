USE itembank;

DROP TABLE IF EXISTS tblstimulus;

CREATE TABLE tblstimulus (
  _efk_itembank bigint(20) NOT NULL,
  _efk_itskey bigint(20) NOT NULL AUTO_INCREMENT,
  clientid varchar(100) DEFAULT NULL,
  filepath varchar(50) DEFAULT NULL,
  filename varchar(50) DEFAULT NULL,
  version varchar(150) DEFAULT NULL,
  datelastupdated datetime(3) DEFAULT NULL,
  _key varchar(150) NOT NULL,
  contentsize int(11) DEFAULT NULL,
  loadconfig bigint(20) DEFAULT NULL,
  updateconfig bigint(20) DEFAULT NULL,
  PRIMARY KEY (_key),
  KEY ix_tblstimulus_efk_itskey (_efk_itskey),
  KEY ix_tblstimulus (_efk_itembank,_efk_itskey)
);