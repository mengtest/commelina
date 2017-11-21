CREATE DATABASE IF NOT EXISTS `server_passport` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `server_passport`;

CREATE TABLE `passport_main_member` (
  `uid` bigint(20) UNSIGNED NOT NULL COMMENT '用户uid',
  `pwd` varchar(60) NOT NULL COMMENT '账号密码',
  `nickname` varchar(80) CHARACTER SET utf8mb4 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

ALTER TABLE `passport_main_member`
  ADD PRIMARY KEY (`uid`);

ALTER TABLE `passport_main_member`
  MODIFY `uid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户uid', AUTO_INCREMENT=1;

CREATE TABLE `passport_account` (
  `uid` bigint(20) UNSIGNED NOT NULL COMMENT '用户uid',
  `account` varchar(80) NOT NULL COMMENT '用户帐户',
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户帐户类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户帐户表';

ALTER TABLE `passport_account`
  ADD PRIMARY KEY (`uid`),
  ADD UNIQUE KEY `account_type_idx` (`account`,`type`);

