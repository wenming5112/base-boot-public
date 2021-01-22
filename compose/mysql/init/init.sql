GRANT All privileges ON *.* TO 'root'@'%';

CREATE USER 'ming'@'%' IDENTIFIED BY '123456';
GRANT All privileges ON *.* TO 'ming'@'%';

-- navicat 编码问题
-- ALTER USER root@'%' IDENTIFIED WITH mysql_native_password BY 'tpwallet123456';

-- 开放远程访问权限
-- grant all privileges on *.* to 'root' @'%' identified by 'Mblog112031' with grant option;

-- 刷新权限
-- flush privileges;
