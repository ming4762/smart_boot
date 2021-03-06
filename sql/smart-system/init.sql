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

-- 角色表
CREATE TABLE `sys_role` (
  `role_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `role_type` int(1) NOT NULL DEFAULT '1' COMMENT '角色类型(1:业务角色;2:管理角色 ;3:系统内置角色)',
  `enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建用户ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新用户ID',
  `seq` int(11) DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

INSERT into sys_role (role_id, role_name, remark, create_time, create_user_id, seq) VALUES ('1', '超级管理员', '最高权限', now(), '1', 1);

-- 创建用户角色表
CREATE TABLE `sys_user_role` (
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `role_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`),
  UNIQUE KEY `user_id_role_id` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO sys_user_role VALUES ('1', '1');


--组织机构表
CREATE TABLE `sys_organ` (
  `organ_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门ID',
  `organ_code` varchar(50) NOT NULL COMMENT '组织代码',
  `organ_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `short_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门简称',
  `leader_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '负责人ID',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `parent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '上级ID',
  `create_time` datetime NOT NULL,
  `create_user_id` varchar(50) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user_id` varchar(50) DEFAULT NULL,
  `top_parent_id` varchar(50) NOT NULL COMMENT '顶级ID',
  PRIMARY KEY (`organ_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 组织机构角色表
CREATE TABLE `sys_organ_role` (
  `organ_id` varchar(50) NOT NULL,
  `role_id` varchar(50) NOT NULL,
  PRIMARY KEY (`organ_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 日志表
CREATE TABLE `sys_log` (
  `log_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志ID',
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作',
  `use_time` int(11) NOT NULL COMMENT '用时',
  `method` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '方法',
  `params` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '参数',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'IP',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `request_path` varchar(255) NOT NULL COMMENT '请求路径',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 字典表
CREATE TABLE `sys_dict` (
  `dict_code` varchar(50) NOT NULL COMMENT '字典ID',
  `dict_name` varchar(50) NOT NULL COMMENT '字典名称',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建人员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `in_use` tinyint(1) NOT NULL COMMENT '是否启用',
  `update_user_id` varchar(50) DEFAULT NULL COMMENT '更新人员',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `seq` int(255) DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 字典项表
CREATE TABLE `sys_dict_item` (
  `id` varchar(50) NOT NULL,
  `item_code` varchar(50) NOT NULL,
  `item_value` varchar(50) NOT NULL,
  `seq` int(11) DEFAULT NULL,
  `parent_code` varchar(50) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `in_use` tinyint(1) NOT NULL,
  `dict_code` varchar(50) NOT NULL,
  `create_user_id` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_user_id` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;