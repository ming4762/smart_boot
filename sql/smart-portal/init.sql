-- 门户数据库

-- 图文素材管理
CREATE TABLE `smart_portal_material_gt` (
  `material_id` char(32) NOT NULL COMMENT 'ID',
  `title` varchar(50) NOT NULL COMMENT '标题',
  `subtitle` varchar(50) DEFAULT NULL COMMENT '副标题',
  `summary` varchar(255) DEFAULT NULL COMMENT '摘要',
  `cover_pic_id` varchar(50) DEFAULT NULL COMMENT '标题图片ID',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建人员ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` varchar(50) DEFAULT NULL COMMENT '更新人员ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `content` mediumtext COMMENT '内容',
  PRIMARY KEY (`material_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 图片分组
CREATE TABLE `smart_portal_pic_group` (
  `group_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分组ID',
  `group_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分组名称',
  `seq` int(255) NOT NULL DEFAULT '0' COMMENT '序号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 图片素材管理
CREATE TABLE `smart_portal_pic` (
  `pic_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片ID',
  `pic_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片名称',
  `file_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `group_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '分组ID',
  PRIMARY KEY (`pic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 模块管理页面
CREATE TABLE `smart_portal_module` (
  `module_id` varchar(50) NOT NULL COMMENT '模块ID',
  `module_name` varchar(50) NOT NULL COMMENT '模块名称',
  `module_icon` varchar(255) DEFAULT NULL COMMENT '模块图标',
  `cover_pic_id` varchar(50) DEFAULT NULL COMMENT '封面图片ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `parent_id` varchar(50) NOT NULL COMMENT '上级ID',
  `top_parent_id` varchar(50) NOT NULL COMMENT '顶级ID',
  `seq` int(11) NOT NULL COMMENT '序号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建人员ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` varchar(50) DEFAULT NULL COMMENT '更新人员ID',
  PRIMARY KEY (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块管理';