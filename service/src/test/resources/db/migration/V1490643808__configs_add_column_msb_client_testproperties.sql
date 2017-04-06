/***********************************************************************************************************************
  File: V1490643808__configs_add_column_msb_client_testproperties.sql

  Desc: Adds the msb column to client_testproperties table

***********************************************************************************************************************/

USE configs;

/* Column already exists in V1480545468__configs_create_client_testproperties */
--ALTER TABLE client_testproperties
--  ADD COLUMN msb BIT(1) NOT NULL DEFAULT b'0';