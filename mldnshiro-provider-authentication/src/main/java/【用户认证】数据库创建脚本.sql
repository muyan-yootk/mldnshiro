DROP DATABASE IF EXISTS mldn_authentication;
CREATE DATABASE mldn_authentication CHARACTER SET UTF8 ;
USE mldn_authentication ;
CREATE TABLE member(
   mid                  varchar(50) not null,
   name                 varchar(30),
   password             varchar(32),
   locked               int,
   CONSTRAINT pk_mid PRIMARY KEY (mid)
) engine='innodb';
-- 0表示活跃、1表示锁定
INSERT INTO member(mid,name,password,locked) VALUES ('admin','管理员','hello',0) ;
INSERT INTO member(mid,name,password,locked) VALUES ('mldn','普通人','java',0) ;
INSERT INTO member(mid,name,password,locked) VALUES ('mermaid','美人鱼','hello',1) ;
