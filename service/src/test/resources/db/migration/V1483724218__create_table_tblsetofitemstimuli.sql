USE itembank;

DROP TABLE IF EXISTS tblsetofitemstimuli;

CREATE TABLE tblsetofitemstimuli (
  _fk_item varchar(150) NOT NULL,
  _fk_stimulus varchar(150) NOT NULL,
  _fk_adminsubject varchar(250) NOT NULL,
  loadconfig bigint(20) DEFAULT NULL,
  PRIMARY KEY (_fk_item,_fk_stimulus,_fk_adminsubject),
  KEY fk_tblsetofitemstimuli_tblstimulus (_fk_stimulus)
  -- CONSTRAINT fk_tblsetofitemstimuli_tblitem FOREIGN KEY (_fk_item) REFERENCES tblitem (_key) ON DELETE CASCADE ON UPDATE NO ACTION,
  -- CONSTRAINT fk_tblsetofitemstimuli_tblstimulus FOREIGN KEY (_fk_stimulus) REFERENCES tblstimulus (_key) ON DELETE CASCADE ON UPDATE NO ACTION
);