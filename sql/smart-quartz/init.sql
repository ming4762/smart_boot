-- 定时任务表
CREATE TABLE `smart_timed_task` (
  `task_id` varchar(50) NOT NULL COMMENT '任务ID',
  `task_name` varchar(50) NOT NULL COMMENT '任务名称',
  `class` varchar(255) NOT NULL COMMENT '类',
  `cron` varchar(20) NOT NULL COMMENT 'cron表达式',
  `enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `seq` int(11) NOT NULL DEFAULT 1 COMMENT '序号',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务表';