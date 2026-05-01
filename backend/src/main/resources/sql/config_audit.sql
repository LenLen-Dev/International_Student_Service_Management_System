CREATE TABLE IF NOT EXISTS `sys_dict_type` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dict_name` VARCHAR(64) NOT NULL COMMENT '字典名称',
    `dict_code` VARCHAR(64) NOT NULL COMMENT '字典编码',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_dict_type_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统字典类型表';

CREATE TABLE IF NOT EXISTS `sys_dict_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dict_code` VARCHAR(64) NOT NULL COMMENT '字典编码',
    `dict_label` VARCHAR(64) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(64) NOT NULL COMMENT '字典值',
    `tag_type` VARCHAR(32) DEFAULT NULL COMMENT '标签样式',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_dict_data_value` (`dict_code`, `dict_value`),
    KEY `idx_sys_dict_data_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统字典数据表';

CREATE TABLE IF NOT EXISTS `sys_flow_template` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `flow_name` VARCHAR(64) NOT NULL COMMENT '流程名称',
    `flow_code` VARCHAR(64) NOT NULL COMMENT '流程编码',
    `business_type` VARCHAR(64) DEFAULT NULL COMMENT '业务类型',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_flow_template_code` (`flow_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程模板表';

CREATE TABLE IF NOT EXISTS `sys_flow_node` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `flow_id` BIGINT NOT NULL COMMENT '流程模板ID',
    `node_name` VARCHAR(64) NOT NULL COMMENT '节点名称',
    `node_code` VARCHAR(64) DEFAULT NULL COMMENT '节点编码',
    `node_type` VARCHAR(32) NOT NULL COMMENT '节点类型：START/APPROVAL/END',
    `approver_role_code` VARCHAR(64) DEFAULT NULL COMMENT '审批角色编码',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_sys_flow_node_flow` (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程节点表';

CREATE TABLE IF NOT EXISTS `sys_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `module_name` VARCHAR(64) NOT NULL COMMENT '模块名称',
    `operation_type` VARCHAR(32) NOT NULL COMMENT '操作类型',
    `operation_name` VARCHAR(128) NOT NULL COMMENT '操作名称',
    `request_method` VARCHAR(16) DEFAULT NULL COMMENT '请求方法',
    `request_uri` VARCHAR(255) DEFAULT NULL COMMENT '请求地址',
    `controller_method` VARCHAR(255) DEFAULT NULL COMMENT '控制器方法',
    `request_params` TEXT DEFAULT NULL COMMENT '请求参数',
    `response_result` TEXT DEFAULT NULL COMMENT '响应结果',
    `success` TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功：0失败，1成功',
    `error_message` TEXT DEFAULT NULL COMMENT '异常信息',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(64) DEFAULT NULL COMMENT '操作人名称',
    `ip_address` VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(512) DEFAULT NULL COMMENT 'User-Agent',
    `cost_time` BIGINT DEFAULT NULL COMMENT '耗时毫秒',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_sys_operation_log_module` (`module_name`),
    KEY `idx_sys_operation_log_operator` (`operator_id`),
    KEY `idx_sys_operation_log_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统操作日志表';

CREATE TABLE IF NOT EXISTS `sys_data_backup` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `backup_name` VARCHAR(128) NOT NULL COMMENT '备份名称',
    `file_name` VARCHAR(255) DEFAULT NULL COMMENT '文件名称',
    `file_path` VARCHAR(512) DEFAULT NULL COMMENT '文件路径',
    `file_size` BIGINT DEFAULT NULL COMMENT '文件大小',
    `backup_status` VARCHAR(32) NOT NULL DEFAULT 'RUNNING' COMMENT '备份状态：RUNNING/SUCCESS/FAILED',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `cost_time` BIGINT DEFAULT NULL COMMENT '耗时毫秒',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(64) DEFAULT NULL COMMENT '操作人名称',
    `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_sys_data_backup_status` (`backup_status`),
    KEY `idx_sys_data_backup_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统数据备份表';

INSERT INTO `sys_dict_type` (`dict_name`, `dict_code`, `description`, `status`, `sort`)
SELECT t.dict_name, t.dict_code, t.description, 1, t.sort
FROM (
    SELECT '学生状态' dict_name, 'student_status' dict_code, '留学生档案学生状态' description, 1 sort UNION ALL
    SELECT '性别', 'gender', '人员性别', 2 UNION ALL
    SELECT '联系人类型', 'contact_type', '留学生联系人类型', 3 UNION ALL
    SELECT '材料类型', 'document_type', '留学生附件材料类型', 4 UNION ALL
    SELECT '材料审核状态', 'document_review_status', '附件材料审核状态', 5
) t
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` d WHERE d.`dict_code` = t.dict_code AND d.`deleted` = 0);

INSERT INTO `sys_dict_data` (`dict_code`, `dict_label`, `dict_value`, `tag_type`, `status`, `sort`)
SELECT t.dict_code, t.dict_label, t.dict_value, t.tag_type, 1, t.sort
FROM (
    SELECT 'student_status' dict_code, '预录取' dict_label, 'PRE_ADMITTED' dict_value, 'primary' tag_type, 1 sort UNION ALL
    SELECT 'student_status', '在读', 'ENROLLED', 'success', 2 UNION ALL
    SELECT 'student_status', '休学', 'SUSPENDED', 'warning', 3 UNION ALL
    SELECT 'student_status', '已毕业', 'GRADUATED', 'info', 4 UNION ALL
    SELECT 'student_status', '退学', 'DROPPED', 'danger', 5 UNION ALL
    SELECT 'student_status', '已离校', 'LEFT', 'info', 6 UNION ALL
    SELECT 'gender', '男', 'MALE', 'primary', 1 UNION ALL
    SELECT 'gender', '女', 'FEMALE', 'danger', 2 UNION ALL
    SELECT 'gender', '未知', 'UNKNOWN', 'info', 3 UNION ALL
    SELECT 'contact_type', '紧急联系人', 'EMERGENCY', 'danger', 1 UNION ALL
    SELECT 'contact_type', '家庭联系人', 'FAMILY', 'success', 2 UNION ALL
    SELECT 'contact_type', '监护人', 'GUARDIAN', 'warning', 3 UNION ALL
    SELECT 'contact_type', '其他', 'OTHER', 'info', 4 UNION ALL
    SELECT 'document_type', '护照', 'PASSPORT', 'primary', 1 UNION ALL
    SELECT 'document_type', '照片', 'PHOTO', 'success', 2 UNION ALL
    SELECT 'document_type', '录取通知书', 'ADMISSION_NOTICE', 'warning', 3 UNION ALL
    SELECT 'document_type', '学历证明', 'DEGREE_CERTIFICATE', 'primary', 4 UNION ALL
    SELECT 'document_type', '语言成绩', 'LANGUAGE_SCORE', 'success', 5 UNION ALL
    SELECT 'document_type', '体检证明', 'PHYSICAL_EXAM', 'warning', 6 UNION ALL
    SELECT 'document_type', '保险材料', 'INSURANCE', 'primary', 7 UNION ALL
    SELECT 'document_type', '其他', 'OTHER', 'info', 8 UNION ALL
    SELECT 'document_review_status', '待审核', 'PENDING', 'warning', 1 UNION ALL
    SELECT 'document_review_status', '已通过', 'APPROVED', 'success', 2 UNION ALL
    SELECT 'document_review_status', '已拒绝', 'REJECTED', 'danger', 3
) t
WHERE NOT EXISTS (
    SELECT 1 FROM `sys_dict_data` d WHERE d.`dict_code` = t.dict_code AND d.`dict_value` = t.dict_value AND d.`deleted` = 0
);
