DROP DATABASE IF EXISTS mldn_client;
CREATE DATABASE mldn_client CHARACTER SET UTF8 ;
USE mldn_client ;
CREATE TABLE client (
	clid     BIGINT  AUTO_INCREMENT ,
	client_id  VARCHAR(200) ,
	client_secret VARCHAR(200) ,
	CONSTRAINT pk_clid PRIMARY KEY(clid)
) ;
-- 编写测试数据
INSERT INTO client(client_id,client_secret) VALUES ('mldn_client','mldnjava') ;