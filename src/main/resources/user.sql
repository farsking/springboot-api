CREATE TABLE `ba_user` (
  `id` bigint(11) NOT NULL,
  `tenant_id` bigint(11) NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL DEFAULT '',
  `status_id` int(4) NOT NULL DEFAULT '1' COMMENT '1启用,2禁用',
  `mobile` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `icon_path` varchar(200) DEFAULT NULL,
  `session` varchar(50) DEFAULT NULL,
  `login_date` datetime DEFAULT NULL,
  `login_client_type_id` int(4) DEFAULT NULL COMMENT '1Android,2IOS,3PC',
  `login_client` varchar(100) DEFAULT NULL COMMENT '手机设备码或PC IP地址',
  `note` varchar(200) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `failed_logins` int(11) NOT NULL DEFAULT '0',
  `password_expired_date` datetime DEFAULT NULL COMMENT '密码过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;