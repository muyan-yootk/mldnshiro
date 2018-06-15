DROP DATABASE IF EXISTS mldn_dept;
CREATE DATABASE mldn_dept CHARACTER SET UTF8 ;
USE mldn_dept ;
CREATE TABLE dept (
	deptno     BIGINT ,
	dname  VARCHAR(200) ,
	CONSTRAINT pk_deptno PRIMARY KEY(deptno)
) ;
-- 编写测试数据
INSERT INTO dept(deptno,dname) VALUES (10,'开发部') ;
INSERT INTO dept(deptno,dname) VALUES (20,'财务部') ;
INSERT INTO dept(deptno,dname) VALUES (30,'市场部') ;