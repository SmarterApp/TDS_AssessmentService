/***********************************************************************************************************************
  File: V1478729030__configs_create_table_itemconstraints.sql

  Desc: Create the client_test_itemconstraint table containing item constraint rules

***********************************************************************************************************************/

use configs;

DROP TABLE IF EXISTS client_test_itemconstraint;

CREATE TABLE `client_test_itemconstraint` (
  `clientname` varchar(100) NOT NULL,
  `testid` varchar(255) NOT NULL,
  `propname` varchar(100) NOT NULL,
  `propvalue` varchar(100) NOT NULL,
  `tooltype` varchar(255) NOT NULL,
  `toolvalue` varchar(255) NOT NULL,
  `item_in` bit(1) NOT NULL,
  PRIMARY KEY (`clientname`,`testid`,`propname`,`propvalue`,`item_in`)
);
