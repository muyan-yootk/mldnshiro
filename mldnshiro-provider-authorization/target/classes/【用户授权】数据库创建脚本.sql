DROP DATABASE IF EXISTS mldn_authorization;
CREATE DATABASE mldn_authorization CHARACTER SET UTF8 ;
USE mldn_authorization ;
CREATE TABLE role(
	rid					varchar(50) ,
	title				varchar(200) ,
	CONSTRAINT pk_rid PRIMARY KEY(rid)
) engine='innodb' ;
CREATE TABLE action(
	actid					varchar(50) ,
	title				varchar(200) ,
	rid					varchar(50) ,
	CONSTRAINT pk_actid PRIMARY KEY(actid) 
) engine='innodb' ;
CREATE TABLE member_role(
	mid				varchar(50) ,
	rid				varchar(50) 
) engine='innodb' ;
-- 定义角色信息
INSERT INTO role(rid,title) VALUES ('member','用户管理') ;
INSERT INTO role(rid,title) VALUES ('dept','部门管理') ;
INSERT INTO role(rid,title) VALUES ('news','新闻管理') ;
-- 定义权限信息
INSERT INTO action(actid,title,rid) VALUES ('member:add','用户追加','member') ;
INSERT INTO action(actid,title,rid) VALUES ('member:list','用户列表','member') ;
INSERT INTO action(actid,title,rid) VALUES ('member:edit','用户编辑','member') ;
INSERT INTO action(actid,title,rid) VALUES ('member:remove','用户删除','member') ;
INSERT INTO action(actid,title,rid) VALUES ('dept:list','部门列表','dept') ;
INSERT INTO action(actid,title,rid) VALUES ('dept:edit','部门编辑','dept') ;
INSERT INTO action(actid,title,rid) VALUES ('news:add','新闻追加','news') ;
INSERT INTO action(actid,title,rid) VALUES ('news:edit','新闻修改','news') ;
INSERT INTO action(actid,title,rid) VALUES ('news:audit','新闻审核','news') ;
INSERT INTO action(actid,title,rid) VALUES ('news:list','新闻列表','news') ;
-- 定义用户与角色的关系
INSERT INTO member_role(mid,rid) VALUES ('admin','member') ;
INSERT INTO member_role(mid,rid) VALUES ('admin','dept') ;
INSERT INTO member_role(mid,rid) VALUES ('admin','news') ;
INSERT INTO member_role(mid,rid) VALUES ('mldn','news') ;
INSERT INTO member_role(mid,rid) VALUES ('mermaid','dept') ;
