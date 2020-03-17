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

/*Data for the table `auc_re_account_convert` */

insert  into `auc_re_account_convert`(`id`,`client_id`,`source_code`,`convert_login_name`,`create_time`,`update_time`,`create_user`,`update_user`) values (1189467511742214145,'DQ_EMS','DQ','*','2019-10-30 04:02:08','2019-10-30 20:33:47','964061','964061'),(1189467677601771522,'DQ_OA','DQ','*','2019-10-30 04:02:48','2019-10-30 20:33:53','964061','964061'),(1189467754667913217,'HLW_OA','HLW','zhanglili','2019-10-30 04:03:07','2019-11-06 01:02:28','964061','964061'),(1191606833899503617,'MEETING_UNION','DQ','zhangsongmei1','2019-11-05 00:43:03','2019-11-05 00:43:03','964061','964061'),(1191993950693621762,'HLW_OA','DQ','222','2019-11-06 02:21:19','2019-11-11 00:56:56','964061','888489'),(1191995817658023937,'HUMAN_CAPITAL','DQ','fenye_test12','2019-11-06 02:28:44','2019-11-06 20:59:32','888489','964061'),(1191995839527231490,'HUMAN_CAPITAL','DQ','fenye_test2','2019-11-06 02:28:49','2019-11-06 02:28:49','888489','888489'),(1191995857172680706,'HUMAN_CAPITAL','DQ','fenye_test3','2019-11-06 02:28:53','2019-11-06 02:28:53','888489','888489'),(1191995883617648642,'HUMAN_CAPITAL','DQ','fenye_test4','2019-11-06 02:29:00','2019-11-06 02:29:00','888489','888489'),(1191995897404432386,'HUMAN_CAPITAL','DQ','fenye_test5','2019-11-06 02:29:03','2019-11-06 02:29:03','888489','888489'),(1191995913313439746,'HUMAN_CAPITAL','DQ','fenye_test6','2019-11-06 02:29:07','2019-11-06 02:29:07','888489','888489'),(1191995931692761090,'HUMAN_CAPITAL','DQ','fenye_test7','2019-11-06 02:29:11','2019-11-06 02:29:11','888489','888489'),(1192252657138532354,'DQ_OA','HLW','huangli17','2019-11-06 19:29:19','2019-11-06 19:29:19','964061','964061'),(1192252790630645762,'DQ_OA','DQ','huangli17','2019-11-06 19:29:51','2019-11-06 19:29:51','964061','964061'),(1192256590187843586,'DQ_EMS','DQ','huangli17','2019-11-06 19:44:57','2019-11-06 19:44:57','964061','964061'),(1192257949175894017,'DQ_EMS','DQ','huanghui','2019-11-06 19:50:21','2019-11-06 19:50:21','964061','964061'),(1192275919730102273,'DQ_EMS','DQ','1','2019-11-06 21:01:45','2019-11-06 21:01:45','964061','964061'),(1193769392819863553,'DQ_EMS','DQ','1111','2019-11-10 23:56:17','2019-11-10 23:56:17','888489','888489'),(1193780131815161857,'HLW_OA','DQ','zhangqi73','2019-11-11 00:38:57','2019-11-11 00:38:57','888489','888489'),(1193780351235796994,'HLW_OA','DQ','zhangqi731','2019-11-11 14:39:52',NULL,'888489',NULL),(1194076180836405250,'DQ_EMS','DQ','huanghui1','2019-11-12 10:15:21',NULL,'964061',NULL);

/*Data for the table `auc_re_client` */

insert  into `auc_re_client`(`id`,`client_id`,`client_name`,`client_icon`,`client_login_url`,`client_secret`,`authorized_types`,`web_server_redirect_uri`,`client_jwt_secret`,`sort`,`status`,`del_flag`,`remarks`,`login_type`,`custom_login`,`custom_login_info`) values (1,'AUC','AUC','','1111122',NULL,NULL,NULL,NULL,2,0,0,'统一认证平台本身',0,0,NULL),(2,'MEETING_UNION','视频会议','//gfs14.atguat.net.cn/T1tWJ_BKET1RCvBVdK.jpg','https://gomeeting-pre.gomedc.com/webrtc-conference/index.html?token=',NULL,NULL,NULL,'gomeet123@',40,0,0,'视频会议联合登录密钥',0,0,NULL),(3,'HUMAN_CAPITAL','人资','//gfs16.atguat.net.cn/T1evYgB7hT1RCvBVdK.jpg','http://10.115.106.6:8030/psc/ps/EMPLOYEE/HRMS/c/GM_PT_MENU.GM_PT_LOAD_GBL.GBL?languageCd=ZHS&auth_key=',NULL,NULL,NULL,'mIFJzx9pyY24ri8A5dwy8uKw0nqIqWCC',0,0,1,'人资系统',0,0,NULL),(4,'DQ_EMS','电器费控','','http://10.112.65.107:3001/pages/main.jsf?to_page=main_list',NULL,NULL,NULL,NULL,10,0,0,'旧版电器费控',0,0,NULL),(5,'DQ_OA','电器OA','','http://10.0.222.12',NULL,NULL,NULL,'secret',20,0,0,'旧电器OA系统',0,0,NULL),(6,'HLW_OA','互联网OA','','http://10.115.2.76',NULL,NULL,NULL,'secret',60,0,0,'旧互联网OA系统',0,0,NULL),(7,'HLW_EMS','互联网费控','',NULL,NULL,NULL,NULL,NULL,NULL,1,0,'旧互联网费控系统',0,0,NULL),(8,'BIG_DATA','大数据应用平台','',NULL,NULL,NULL,NULL,NULL,NULL,1,0,NULL,0,0,NULL),(9,'CBOARD','BI数据分析系统',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,NULL,0,0,NULL),(10,'SENSORS','神策流量分析系统',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,NULL,0,0,NULL),(11,'DP','大屏系统',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,NULL,0,0,NULL),(12,'HUKING','虎鲸',NULL,NULL,NULL,NULL,NULL,NULL,0,1,1,NULL,0,0,NULL),(14,'OA','智慧办公','//gfs16.atguat.net.cn/T1NGVTBQhT1RCvBVdK.png','http://10.115.88.29/#/nologin',NULL,NULL,NULL,'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBxFOtU43iI0G9F0VaOt093G9qY2FaN8BEXfedwZsmxVRjZvp3Wc3wUgNj6Rvtw0cgIG2PhnYlHafWhIgZRSQckjwISzHdiR151CIHPP/lKtIBk4KPrvZRuadJoVTG+O0xgN4UOaVfu0vt/UTcZMjmbVJxWvcvan6hypbPf9d4wwIDAQAB',0,0,0,'智慧办公系统',0,0,NULL),(15,'TEST_SYSTEM','测试系统','//gfs14.atguat.net.cn/T1yGxvBCWv1RCvBVdK.png','http://baidu.com',NULL,NULL,'http://baidu.com','123456',999,0,0,'这是一个测试三方系统的数据，排序999',1,0,NULL),(16,'111','1111','','http://114.114.114.114:90/login',NULL,NULL,NULL,NULL,0,0,1,'1111',0,0,NULL),(17,'22','222','','222',NULL,NULL,NULL,NULL,999,1,1,'2222',0,0,NULL),(18,'HUKING','虎鲸','','',NULL,NULL,NULL,NULL,30,1,0,'虎鲸',0,0,NULL),(19,'444','444','http://gfs14.atguat.net.cn/T1EexTBgCT1RCvBVdK.jpg','',NULL,NULL,NULL,NULL,0,0,1,'444',0,0,NULL),(20,'5555','5555','','5555',NULL,NULL,NULL,NULL,0,1,1,'5555',0,0,NULL),(21,'666','6666','','666',NULL,NULL,NULL,NULL,0,1,1,'6666',0,0,NULL),(22,'7777','7777','','777',NULL,NULL,NULL,NULL,0,1,1,'7777',0,0,NULL),(23,'8888','888','//gfs16.atguat.net.cn/T1aD_vBgLv1RCvBVdK.jpg','888',NULL,NULL,NULL,NULL,0,1,1,'888',0,0,NULL),(24,'SMS','搜索管理系统',NULL,NULL,NULL,NULL,NULL,NULL,0,1,0,'Search management system(SMS)',0,0,NULL),(25,'TEST_SYSTEM2','测试系统','//gfs14.atguat.net.cn/T1yGxvBCWv1RCvBVdK.png','1111111111',NULL,NULL,'http://baidu.com','123456',999,1,0,'这是一个测试三方系统的数据，排序999',1,1,'{\"SYSTEM\":[\"DQ\"],\"MOBILE\":[\"DQ\",\"KG\",\"TX\",\"DC\",\"HLW\",\"JK\"],\"DOMAIN\":[\"GOMEDQ\",\"GOMEKG\"]}'),(26,'MEETING_UNIO','视频会议可删除','','https://gomeeting-pre.gomedc.com/webrtc-conference/index.htm?token=',NULL,NULL,NULL,NULL,0,0,1,'测试用',0,0,NULL);

/*Data for the table `auc_re_datasource` */

insert  into `auc_re_datasource`(`source_id`,`source_name`,`source_code`,`description`,`create_time`,`update_time`) values (28,'美信','MX','            ','2016-12-08 16:16:02',NULL),(29,'电器','DQ','            ','2016-12-08 16:16:40',NULL),(30,'跨境','KJ','            ','2016-12-08 16:17:10',NULL),(41,'在线','ZX','在线','2016-12-09 11:39:25',NULL),(42,'成都大数据','CDDSJ','成都大数据','2016-12-11 18:26:10',NULL),(47,'转售','ZS','            ','2016-12-12 16:31:06',NULL),(48,'投资','TZ','            ','2016-12-14 17:17:12',NULL),(50,'极信','JX','啊       	','2017-02-07 16:26:36',NULL),(56,'国美集团','GMJT','国美集团','2017-03-29 17:24:22',NULL),(59,'跨境电商','KJDS','','2017-05-02 11:34:26',NULL),(60,'售后','SH','','2017-05-02 14:25:51',NULL),(61,'安迅','AX','','2017-05-02 14:27:31',NULL),(62,'家居','JJ','','2017-05-02 14:33:09',NULL),(63,'蜘蛛侠公司','ZZX','蜘蛛侠公司','2017-05-02 15:06:27',NULL),(64,'地产','DC','','2017-05-09 11:39:05',NULL),(66,'集团','JT','演示专用其他人请勿使用','2017-05-16 11:02:53',NULL),(70,'yk数据源测试','YKTEST','','2017-08-08 14:41:56',NULL),(72,'09191测试数据源','09191CSSJY','测试，待删','2017-09-19 16:00:03',NULL),(74,'控股集团','KG','','2017-10-24 17:53:03',NULL),(75,'互联网','HLW','','2017-10-26 14:29:11',NULL),(78,'巨鼎医疗','JD01','','2017-12-19 16:49:45',NULL),(82,'数据','SHUJU1','','2018-01-04 17:29:28',NULL),(84,'智慧企业','ZH10001','','2018-03-17 12:22:59',NULL),(85,'金控','JK','金控OA数据源','2018-07-03 17:05:33',NULL),(87,'通讯','TX','','2019-01-29 14:19:09',NULL),(88,'yktest111','YKTEST111','','2019-09-06 17:24:20',NULL),(95,'yktestshu','YKTESTSHU','','2019-09-09 17:39:40',NULL),(900,'第三方系统2','SYS2',NULL,'2020-03-06 15:00:04',NULL),(901,'第三方系统1','SYS1',NULL,'2020-03-06 14:59:59',NULL);

/*Data for the table `auc_re_domain` */

insert  into `auc_re_domain`(`domain_id`,`domain_name`,`domain_code`,`status`,`description`,`create_time`,`update_time`) values (1,'国美电器','GOMEDQ',1,NULL,'2018-04-22 10:35:14',NULL),(2,'国美互联网','GOMEHLW',0,NULL,'2018-04-22 10:35:14',NULL),(3,'国美控股','GOMEKG',1,NULL,'2019-09-16 16:39:14',NULL);

/*Data for the table `auc_re_domain_datasource` */

insert  into `auc_re_domain_datasource`(`id`,`source_id`,`domain_id`,`create_time`,`update_time`) values (1,29,1,'2018-04-22 10:35:46',NULL),(2,75,2,'2018-04-22 10:35:46',NULL),(3,74,3,'2019-09-19 22:10:16',NULL);

/*Data for the table `auc_re_menu_info` */

insert  into `auc_re_menu_info`(`id`,`parent_id`,`path`,`name`,`en_name`,`url`,`redirect`,`component`,`hidden`,`type`,`flag`,`order_no`,`status`,`remark`,`create_user`,`create_time`,`update_user`,`update_time`,`version`) values (1,0,'/1','首页',NULL,'/system',NULL,NULL,0,NULL,'1',10,'0',NULL,NULL,'2019-10-28 11:00:52',NULL,'2019-10-28 11:00:58',0),(2,0,'/2','系统管理',NULL,'/config','/configPage','Home',0,NULL,'1',20,'0',NULL,NULL,'2019-10-28 13:56:53',NULL,'2019-10-28 13:56:56',0),(3,2,'/2/3','系统配置','config','/configPage',NULL,'pages/config/config',0,NULL,'1',10,'0',NULL,NULL,'2019-11-11 18:08:15',NULL,'2019-11-11 18:08:20',0),(4,2,'/2/4','系统日志','systemLog','/systemLog',NULL,'pages/systemLog/systemLog',0,NULL,'1',20,'0',NULL,NULL,'2019-11-11 18:09:27',NULL,'2019-11-11 18:09:30',0),(5,0,'/5','用户管理',NULL,'/user','/userMange','Home',0,NULL,'1',30,'0',NULL,NULL,'2019-12-09 15:18:09',NULL,'2019-12-09 15:18:12',0),(6,5,'/5/6','用户管理','userMange','/userMange',NULL,'pages/userMange/userMangeList',0,NULL,'1',10,'0',NULL,NULL,'2019-12-09 15:20:58',NULL,'2019-12-09 15:21:03',0),(7,2,'/2/7','外部系统管理','thirdSystemConfig','/thirdSystemConfig',NULL,'pages/thirdSystemConfig/thirdSystemConfig',0,NULL,'1',30,'0',NULL,NULL,'2020-01-03 14:26:54',NULL,'2020-01-03 14:26:57',0);

/*Data for the table `auc_re_operational` */

insert  into `auc_re_operational`(`id`,`parent_id`,`oper_name`,`oper_code`,`stop_url_prefix`,`create_time`) values (1,0,'账号转换操作',NULL,NULL,'2019-12-12 15:16:50'),(2,1,'保存','auc:accountconvert:save',NULL,'2019-12-12 15:17:10'),(3,1,'删除','auc:accountconvert:delete',NULL,'2019-12-12 15:17:25'),(4,1,'导入','auc:accountconvert:import',NULL,'2019-12-12 16:00:17'),(5,1,'查询','auc:accountconvert:query',NULL,'2019-12-12 16:09:23'),(6,0,'日志操作',NULL,NULL,'2019-12-16 14:46:07'),(7,6,'查询','system:log:query',NULL,'2019-12-16 14:50:38'),(8,0,'第三方系统模块操作',NULL,NULL,'2019-12-16 15:09:10'),(9,8,'查询','auc:client:query',NULL,'2019-12-16 15:11:54'),(10,8,'保存','auc:client:save',NULL,'2019-12-16 15:12:01'),(11,8,'删除','auc:client:delete',NULL,'2019-12-16 15:12:08'),(12,0,'用户模块操作',NULL,NULL,'2019-12-16 15:20:47'),(13,12,'禁用','system:user:disable',NULL,'2019-12-16 15:21:23'),(14,12,'启用','system:user:enable',NULL,'2019-12-16 15:21:36'),(15,0,'权限模块操作',NULL,NULL,'2019-12-16 15:55:24'),(16,15,'查询','system:privilege:query',NULL,'2019-12-16 15:56:11'),(17,15,'保存','system:privilege:save',NULL,'2019-12-16 15:56:20'),(18,15,'删除','system:privilege:delete',NULL,'2019-12-16 15:56:31'),(19,0,'操作模块',NULL,NULL,'2019-12-16 15:57:11'),(20,19,'查询','system:operational:query',NULL,'2019-12-16 15:57:42'),(21,19,'保存','system:operational:save',NULL,'2019-12-16 15:58:00'),(22,19,'删除','system:operational:delete',NULL,'2019-12-16 15:58:09'),(23,12,'编辑','system:user:edit',NULL,'2019-12-18 10:06:16'),(24,12,'修改密码','system:user:changePassword',NULL,'2019-12-18 11:23:29');

/*Data for the table `auc_re_privilege` */

insert  into `auc_re_privilege`(`id`,`pri_name`,`valid`,`create_time`) values (1,'账号转换权限','Y','2019-12-12 15:18:01'),(2,'日志列表权限','Y','2019-12-12 16:28:03'),(4,'第三方系统模块权限','Y','2019-12-16 15:51:52'),(5,'用户模块权限','Y','2019-12-16 15:52:35'),(6,'权限模块的权限','Y','2019-12-16 15:59:19'),(7,'操作项模块的权限','Y','2019-12-16 15:59:29'),(8,'分级管理员账号权限','Y','2019-12-19 09:41:30'),(9,'一般用户权限项','Y','2019-12-19 14:01:54');

/*Data for the table `auc_re_privilege_oper` */

insert  into `auc_re_privilege_oper`(`privilege_id`,`operational_id`,`create_time`) values (1,2,'2019-12-16 16:34:25'),(1,3,'2019-12-16 16:34:25'),(1,4,'2019-12-16 16:34:25'),(1,5,'2019-12-16 16:34:25'),(8,5,'2019-12-19 09:42:34'),(9,7,'2019-12-19 14:02:23'),(8,24,'2019-12-19 14:09:48'),(9,24,'2019-12-19 14:44:51'),(4,9,'2020-01-13 14:21:56'),(4,10,'2020-01-13 14:22:01'),(4,11,'2020-01-13 14:22:07');

/*Data for the table `auc_re_role` */

insert  into `auc_re_role`(`id`,`role_name`,`role_code`) values (1,'ADMIN','ADMIN'),(2,'USER','USER'),(3,'SUPER','SUPER');

/*Data for the table `auc_re_role_menu_relation` */

insert  into `auc_re_role_menu_relation`(`id`,`role_id`,`menu_id`,`status`,`remark`,`create_user`,`create_time`,`update_user`,`update_time`,`version`) values (1,1,1,'',NULL,NULL,NULL,NULL,NULL,0),(3,1,2,NULL,NULL,NULL,NULL,NULL,NULL,0),(4,1,3,NULL,NULL,NULL,NULL,NULL,NULL,0),(5,2,1,NULL,NULL,NULL,NULL,NULL,NULL,0),(6,3,1,NULL,NULL,NULL,NULL,NULL,NULL,0),(7,3,2,NULL,NULL,NULL,NULL,NULL,NULL,0),(8,3,3,NULL,NULL,NULL,NULL,NULL,NULL,0),(9,3,4,NULL,NULL,NULL,NULL,NULL,NULL,0),(10,3,5,NULL,NULL,NULL,NULL,NULL,NULL,0),(11,3,6,NULL,NULL,NULL,NULL,NULL,NULL,0),(12,1,5,NULL,NULL,NULL,NULL,NULL,NULL,0),(13,1,6,NULL,NULL,NULL,NULL,NULL,NULL,0),(14,2,5,NULL,NULL,NULL,NULL,NULL,NULL,0),(15,2,6,NULL,NULL,NULL,NULL,NULL,NULL,0),(16,2,2,NULL,NULL,NULL,NULL,NULL,NULL,0),(17,2,4,NULL,NULL,NULL,NULL,NULL,NULL,0),(18,3,7,NULL,NULL,NULL,NULL,NULL,NULL,0),(19,1,7,NULL,NULL,NULL,NULL,NULL,NULL,0);

/*Data for the table `auc_re_role_privilege` */

insert  into `auc_re_role_privilege`(`role_id`,`privilege_id`,`create_time`) values (1,8,'2019-12-12 15:19:59'),(2,9,'2019-12-19 14:14:55'),(1,4,'2020-01-13 14:22:45');

/*Data for the table `auc_re_user_roles_relation` */

insert  into `auc_re_user_roles_relation`(`id`,`user_id`,`user_name`,`role_id`) values (1,'888489','zhangqi73',3),(6,'935687','wangzhibo',3),(7,'935699','handong10',3),(8,'916635','lujia8',3),(9,'536970','huangli17',1),(10,'522674','dumingxu',3),(18,'964061','yeqin4',3),(20,'115922','huanghui',1),(22,'115940','liangweicheng',1),(23,'666','xuxiaoli10',1),(24,'115919','lizekun1',1),(25,'7','zhaobin',1),(27,'541180','zhangsongmei',3),(29,'1029186','testers001',1),(32,'916628','luoqiong1',1),(33,'1','admin',3),(34,'541180','zhangsongmei',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
