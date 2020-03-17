/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.32-78.1-log : Database - auc
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`auc` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `auc`;

/*Table structure for table `auc_hi_log` */

DROP TABLE IF EXISTS `auc_hi_log`;

CREATE TABLE `auc_hi_log` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `log_type` smallint(2) DEFAULT NULL COMMENT '0正常1错误',
  `spend_time` int(11) DEFAULT NULL COMMENT '耗时,单位毫秒',
  `device` varchar(1024) DEFAULT NULL COMMENT '设备信息',
  `ip` varchar(64) DEFAULT NULL,
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `content` text,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1124 DEFAULT CHARSET=utf8 COMMENT='日志表历史表';

/*Table structure for table `auc_re_account_convert` */

DROP TABLE IF EXISTS `auc_re_account_convert`;

CREATE TABLE `auc_re_account_convert` (
  `id` bigint(19) NOT NULL,
  `client_id` varchar(124) DEFAULT NULL COMMENT '第三方系统ID',
  `source_code` varchar(64) DEFAULT NULL COMMENT '转换账号对应用户的数据源',
  `convert_login_name` varchar(32) DEFAULT NULL COMMENT '转换账号对应用户名',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_ac_uq_` (`client_id`,`source_code`,`convert_login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录名转换配置表';

/*Table structure for table `auc_re_client` */

DROP TABLE IF EXISTS `auc_re_client`;

CREATE TABLE `auc_re_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(124) NOT NULL COMMENT '客户端ID',
  `client_name` varchar(64) DEFAULT NULL COMMENT '客户端名称',
  `client_icon` varchar(64) DEFAULT NULL COMMENT '客户端图标',
  `client_login_url` varchar(128) DEFAULT NULL COMMENT '客户端登录URL',
  `client_secret` varchar(256) DEFAULT NULL COMMENT '客户端密钥',
  `authorized_types` varchar(256) DEFAULT NULL COMMENT '鉴权类型',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT '回调默认地址',
  `client_jwt_secret` varchar(256) DEFAULT NULL COMMENT '客户端自定义jtw密钥',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '0 显示， 1 不显示，',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 正常， 1 删除',
  `remarks` varchar(256) DEFAULT NULL,
  `login_type` int(2) DEFAULT '0' COMMENT '登录类型  0：域账号登录，1：系统账号登录',
  `custom_login` tinyint(1) DEFAULT '0' COMMENT '0非自定义,1自定义',
  `custom_login_info` varchar(256) DEFAULT NULL COMMENT '自定义登录范围json',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='第三方系统信息';

/*Table structure for table `auc_re_datasource` */

DROP TABLE IF EXISTS `auc_re_datasource`;

CREATE TABLE `auc_re_datasource` (
  `source_id` int(10) NOT NULL COMMENT '数据源ID',
  `source_name` varchar(60) DEFAULT NULL COMMENT '数据源名称',
  `source_code` varchar(60) DEFAULT NULL COMMENT '数据源编码',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`source_id`),
  UNIQUE KEY `Index_SourceCode` (`source_code`) USING BTREE,
  UNIQUE KEY `Index_SourceName` (`source_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录数据来源或国美各板块信息';

/*Table structure for table `auc_re_domain` */

DROP TABLE IF EXISTS `auc_re_domain`;

CREATE TABLE `auc_re_domain` (
  `domain_id` int(11) NOT NULL AUTO_INCREMENT,
  `domain_name` varchar(32) DEFAULT NULL,
  `domain_code` varchar(32) DEFAULT NULL,
  `status` smallint(1) DEFAULT NULL COMMENT '1激活,0未激活',
  `description` varchar(128) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`domain_id`),
  UNIQUE KEY `idx_uk_domaincode` (`domain_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='域信息';

/*Table structure for table `auc_re_domain_datasource` */

DROP TABLE IF EXISTS `auc_re_domain_datasource`;

CREATE TABLE `auc_re_domain_datasource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` int(11) DEFAULT NULL,
  `domain_id` int(11) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `auc_re_menu_info` */

DROP TABLE IF EXISTS `auc_re_menu_info`;

CREATE TABLE `auc_re_menu_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级菜单id',
  `path` varchar(255) DEFAULT NULL COMMENT 'ID全路径',
  `name` varchar(20) DEFAULT NULL COMMENT '菜单名称',
  `en_name` varchar(50) DEFAULT NULL COMMENT '英文名称标识',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单路径',
  `redirect` varchar(255) DEFAULT NULL COMMENT '重定向',
  `component` varchar(255) DEFAULT NULL COMMENT 'component',
  `hidden` tinyint(1) DEFAULT '0' COMMENT '是否隐藏',
  `type` varchar(10) DEFAULT NULL COMMENT '菜单类型',
  `flag` char(1) DEFAULT NULL COMMENT '菜单是否可用',
  `order_no` int(11) DEFAULT NULL COMMENT '菜单排序',
  `status` varchar(2) DEFAULT NULL COMMENT '状态 0 正常 1 删除',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='菜单表';

/*Table structure for table `auc_re_operational` */

DROP TABLE IF EXISTS `auc_re_operational`;

CREATE TABLE `auc_re_operational` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '操作父id',
  `oper_name` varchar(64) DEFAULT NULL COMMENT '操作名称',
  `oper_code` varchar(64) DEFAULT NULL COMMENT '操作编码',
  `stop_url_prefix` varchar(128) DEFAULT NULL COMMENT '拦截URL前缀',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='前端功能操作';

/*Table structure for table `auc_re_privilege` */

DROP TABLE IF EXISTS `auc_re_privilege`;

CREATE TABLE `auc_re_privilege` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pri_name` varchar(64) NOT NULL COMMENT '权限名称',
  `valid` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='权限';

/*Table structure for table `auc_re_privilege_oper` */

DROP TABLE IF EXISTS `auc_re_privilege_oper`;

CREATE TABLE `auc_re_privilege_oper` (
  `privilege_id` int(11) DEFAULT NULL COMMENT '权限ID',
  `operational_id` int(11) DEFAULT NULL COMMENT '操作ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  KEY `fk_po_001` (`privilege_id`),
  KEY `fk_po_002` (`operational_id`),
  CONSTRAINT `fk_po_001` FOREIGN KEY (`privilege_id`) REFERENCES `auc_re_privilege` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_po_002` FOREIGN KEY (`operational_id`) REFERENCES `auc_re_operational` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限和操作的关联表';

/*Table structure for table `auc_re_role` */

DROP TABLE IF EXISTS `auc_re_role`;

CREATE TABLE `auc_re_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) NOT NULL COMMENT '角色名称',
  `role_code` varchar(32) NOT NULL COMMENT '角色编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色类型表';

/*Table structure for table `auc_re_role_menu_relation` */

DROP TABLE IF EXISTS `auc_re_role_menu_relation`;

CREATE TABLE `auc_re_role_menu_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `role_id` bigint(20) NOT NULL COMMENT '权限id',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  `status` varchar(2) DEFAULT NULL COMMENT '状态 0 正常 1 删除',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='菜单和角色关系';

/*Table structure for table `auc_re_role_privilege` */

DROP TABLE IF EXISTS `auc_re_role_privilege`;

CREATE TABLE `auc_re_role_privilege` (
  `role_id` int(11) DEFAULT NULL,
  `privilege_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  KEY `fk_rp_01` (`role_id`),
  KEY `fk_rp_02` (`privilege_id`),
  CONSTRAINT `fk_rp_01` FOREIGN KEY (`role_id`) REFERENCES `auc_re_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rp_02` FOREIGN KEY (`privilege_id`) REFERENCES `auc_re_privilege` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';

/*Table structure for table `auc_re_user` */

DROP TABLE IF EXISTS `auc_re_user`;

CREATE TABLE `auc_re_user` (
  `id` int(10) NOT NULL COMMENT '用户id',
  `user_name` varchar(50) NOT NULL COMMENT '登录名',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `source_id` smallint(2) NOT NULL COMMENT '数据源ID',
  `personnel_code` varchar(24) DEFAULT NULL COMMENT '员工编码',
  `mobile_phone` varchar(64) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` smallint(1) DEFAULT NULL COMMENT '用户状态(1激活,0禁用)',
  `delete_flag` smallint(1) DEFAULT NULL COMMENT '在职状态(1离职,0在职)',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(5) DEFAULT NULL COMMENT '盐值',
  `ps_id` varchar(12) DEFAULT NULL COMMENT 'PSid',
  `app_account` varchar(64) DEFAULT NULL COMMENT '应用系统账号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ru_uname` (`user_name`,`source_id`),
  KEY `idx_ru_ureal` (`real_name`),
  KEY `idx_ru_ucode` (`personnel_code`,`source_id`),
  KEY `idx_ru_accpw` (`source_id`,`password`,`app_account`),
  KEY `idx_ru_ump` (`mobile_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

/*Table structure for table `auc_re_user_ext` */

DROP TABLE IF EXISTS `auc_re_user_ext`;

CREATE TABLE `auc_re_user_ext` (
  `id` int(10) NOT NULL,
  `u_key` varchar(64) DEFAULT NULL,
  `u_value` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`id`) REFERENCES `auc_re_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `auc_re_user_roles_relation` */

DROP TABLE IF EXISTS `auc_re_user_roles_relation`;

CREATE TABLE `auc_re_user_roles_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) DEFAULT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_ur_urid` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='用户角色配置表';

/*Table structure for table `auc_ru_log` */

DROP TABLE IF EXISTS `auc_ru_log`;

CREATE TABLE `auc_ru_log` (
  `id` bigint(19) NOT NULL,
  `log_type` smallint(2) DEFAULT NULL COMMENT '0正常1错误',
  `spend_time` int(11) DEFAULT NULL COMMENT '耗时,单位毫秒',
  `device` varchar(1024) DEFAULT NULL COMMENT '设备信息',
  `ip` varchar(64) DEFAULT NULL,
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `content` text,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_rl_ud` (`create_time`,`user_name`),
  KEY `idx_rl_ui` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志表';

/*Table structure for table `auc_ru_user_backup` */

DROP TABLE IF EXISTS `auc_ru_user_backup`;

CREATE TABLE `auc_ru_user_backup` (
  `id` int(10) NOT NULL COMMENT '和智慧办公保持一致',
  `status` smallint(1) NOT NULL COMMENT '用户状态(0激活,1禁用)',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `salt` varchar(5) NOT NULL COMMENT '盐值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息同步中间表,备份AUC信息';

/*Table structure for table `auc_ru_user_sync` */

DROP TABLE IF EXISTS `auc_ru_user_sync`;

CREATE TABLE `auc_ru_user_sync` (
  `id` int(10) NOT NULL COMMENT '和智慧办公保持一致',
  `user_name` varchar(50) NOT NULL COMMENT '登录名',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `source_id` smallint(2) NOT NULL COMMENT '数据源ID',
  `personnel_code` varchar(24) DEFAULT NULL COMMENT '员工编码',
  `mobile_phone` varchar(64) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` smallint(1) NOT NULL COMMENT '用户状态(0激活,1禁用)',
  `delete_flag` smallint(1) DEFAULT NULL COMMENT '在职状态(0离职,1在职)',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(5) DEFAULT NULL COMMENT '盐值',
  `ps_id` varchar(12) DEFAULT NULL COMMENT 'PSid',
  `app_account` varchar(64) DEFAULT NULL COMMENT '应用系统账号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息同步中间表';

/*Table structure for table `auc_ru_variable` */

DROP TABLE IF EXISTS `auc_ru_variable`;

CREATE TABLE `auc_ru_variable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` smallint(2) NOT NULL,
  `v_key` varchar(64) NOT NULL,
  `v_value` varchar(256) DEFAULT NULL,
  `ext` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `occur_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rv_key` (`type`,`v_key`)
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8 COMMENT='运行参数配置表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
