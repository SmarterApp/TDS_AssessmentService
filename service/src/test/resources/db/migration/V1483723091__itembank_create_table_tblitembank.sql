USE itembank;

DROP TABLE IF EXISTS tblitembank;

CREATE TABLE tblitembank (
  _fk_client bigint(20) NOT NULL,
  homepath varchar(255) DEFAULT NULL,
  itempath varchar(50) DEFAULT NULL,
  stimulipath varchar(50) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  _efk_itembank bigint(20) NOT NULL,
  _key bigint(20) NOT NULL,
  contract varchar(255) DEFAULT NULL,
  PRIMARY KEY (_key),
  KEY fk_itembank_client (_fk_client),
  CONSTRAINT fk_itembank_client FOREIGN KEY (_fk_client) REFERENCES tblclient (_key) ON DELETE CASCADE ON UPDATE CASCADE
);