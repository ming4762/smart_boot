-- 用户表
CREATE TABLE `sys_user` (
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `depte_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门ID',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机',
  `telephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '固话',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '状态（0：禁用 1、启用 2、锁定）',
  `user_type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '10' COMMENT '用户类型（10:普通用户,11：系统用户，12：业务用户）',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '性别（0：未知，1：男，2：女）',
  `birth` datetime DEFAULT NULL COMMENT '生日',
  `create_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建用户ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人员ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `avatar_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像ID',
  `seq` int(11) DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`user_id`),
  KEY `username_index` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户表';

INSERT INTO sys_user (user_id, username, name, password, user_type, create_user_id, create_time, seq) VALUES ('1', 'admin', 'admin', 'd68da2cf398e005ab7413f619af3c676', '11', '1', NOW(), 1);