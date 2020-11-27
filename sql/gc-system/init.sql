-- 系统用户表
CREATE TABLE `sys_user` (
  `user_id` int(42) NOT NULL DEFAULT '3' COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `realname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机',
  `telephone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `sex` int(1) NOT NULL COMMENT '性别 ',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `deptId` int(42) DEFAULT NULL COMMENT '部门ID',
  `status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '10' COMMENT '状态（10：启用，20：禁用）',
  `userType` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户类型（10：系统用户，20：业务用户）',
  `create_user_id` int(42) NOT NULL COMMENT '创建人员ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` int(42) DEFAULT NULL COMMENT '更新人员ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `post_id` int(42) DEFAULT NULL COMMENT '职务ID',
  `work_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '工号',
  `seq` int(11) NOT NULL COMMENT '序号',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uni_user_name` (`username`) USING BTREE COMMENT '用户名唯一键',
  UNIQUE KEY `uni_work_no` (`work_no`) USING BTREE COMMENT '工号唯一键',
  KEY `idx_post_id` (`post_id`) COMMENT '部门ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户表';


create table sys_role
(
    role_id        bigint               not null
        primary key,
    role_name      varchar(50)          not null comment '角色名称',
    role_code      varchar(50)          not null comment '角色编码',
    remark         varchar(255)         null comment '备注',
    enable         tinyint(1) default 1 not null comment '是否启用',
    role_type      char(2)              null comment '角色类型',
    create_user_id bigint               null comment '创建人员ID',
    create_time    datetime             null comment '创建时间',
    update_user_id bigint               null comment '更新人员ID',
    update_time    datetime             null comment '更新时间',
    dept_id        bigint               null comment '部门id',
    seq            int                  null comment '序号'
)
    charset = utf8;

create table sys_user_group
(
    group_id       bigint               not null
        primary key,
    group_name     varchar(50)          not null comment '用户组名称',
    group_code     varchar(50)          not null comment '用户组编码',
    remark         varchar(255)         null comment '备注',
    seq            int                  null comment '序号',
    enable         tinyint(1) default 1 not null comment '是否启用',
    create_user_id bigint               null,
    create_time    datetime             null,
    update_user_id bigint               null,
    update_time    datetime             null,
    constraint uk_group_code
        unique (group_code)
)
    charset = utf8;

create table sys_user_group_role
(
    group_id       bigint               not null comment '用户组ID',
    role_id        bigint               not null comment '角色ID',
    enable         tinyint(1) default 1 not null comment '是否启用',
    create_time    datetime             not null comment '创建时间',
    create_user_id bigint               null comment '创建人员ID',
    primary key (group_id, role_id)
)
    charset = utf8;

create table sys_user_group_user
(
    user_id        bigint     not null comment '用户ID',
    user_group_id  bigint     not null comment '用户组ID',
    enable         tinyint(1) not null comment '是否启用',
    create_time    datetime   null,
    create_user_id bigint     null,
    primary key (user_id, user_group_id)
);

create table sys_user_role
(
    user_id        bigint               not null comment '用户ID',
    role_id        bigint               not null comment '角色ID',
    enable         tinyint(1) default 1 not null comment '是否启用',
    create_time    datetime             not null comment '创建时间',
    create_user_id bigint               null comment '创建人员Id',
    primary key (user_id, role_id)
);


-- auto-generated definition
create table sys_function
(
    function_id          bigint               not null comment '功能ID'
        primary key,
    parent_id            bigint               not null comment '上级ID',
    function_name        varchar(50)          not null comment '功能名称',
    function_type        char(2)              not null comment '功能类型（10：目录 20：菜单 30：功能）',
    icon                 varchar(50)          null comment '图标',
    seq                  int                  null comment '序号',
    create_time          datetime             not null comment '创建时间',
    create_user_id       bigint               null comment '创建人员ID',
    update_time          datetime             null comment '更新时间',
    update_user_id       bigint               null comment '更新人员ID',
    url                  varchar(500)         null comment 'url',
    permission           varchar(255)         null comment '权限',
    is_menu              tinyint(1) default 1 not null comment '是否菜单',
    internal_or_external tinyint(1) default 0 not null comment '外链菜单打开方式 0/内部打开 1/外部打开',
    data_rule            tinyint(1) default 0 not null comment '是否配置数据权限',
    http_method          varchar(10)          null
)
    comment '系统功能表';


-- auto-generated definition
create table sys_role_function
(
    role_id        bigint   not null,
    function_id    bigint   not null,
    create_time    datetime not null,
    create_user_id bigint   null,
    primary key (role_id, function_id)
)
    comment '角色菜单关系表' charset = utf8mb4;


    -- auto-generated definition
create table sys_log
(
    log_id         bigint       not null comment '日志ID'
        primary key,
    create_user_id bigint       null comment '用户名',
    operation      varchar(50)  not null comment '操作',
    use_time       bigint       not null comment '用时',
    method         text         not null comment '方法',
    params         longtext     null comment '参数',
    ip             varchar(64)  not null comment 'IP',
    create_time    datetime     not null comment '创建时间',
    request_path   varchar(255) not null comment '请求路径',
    status_code    int          not null comment '状态码',
    error_message  longtext     null comment '错误信息',
    type           varchar(50)  null comment '请求类型',
    Platform       varchar(50)  null comment '平台',
    ident          char(2)      null,
    result         text         null
);

create index idx_create_user_id
    on sys_log (create_user_id);
