USE itembank;

DROP TABLE IF EXISTS testformitem;

CREATE TABLE testformitem (
  _fk_item varchar(150) NOT NULL,
  _efk_itsformkey bigint(20) NOT NULL,
  formposition int(11) NOT NULL,
  _fk_adminsubject varchar(250) NOT NULL,
  _fk_testform varchar(100) NOT NULL DEFAULT '',
  isactive bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (_fk_item,formposition,_fk_adminsubject,_fk_testform),
  UNIQUE KEY ix_formitemposition (_fk_testform,formposition),
  UNIQUE KEY ix_formitemunique (_fk_testform,_fk_item),
  KEY ix_formitem_test (_fk_adminsubject),
  CONSTRAINT fk_formitemform FOREIGN KEY (_fk_testform) REFERENCES testform (_key) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;