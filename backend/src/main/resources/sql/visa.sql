CREATE TABLE IF NOT EXISTS `visa_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `passport_no` VARCHAR(64) DEFAULT NULL COMMENT '护照号码',
    `visa_type` VARCHAR(32) NOT NULL COMMENT '签证类型',
    `visa_no` VARCHAR(64) DEFAULT NULL COMMENT '签证号码',
    `issue_place` VARCHAR(128) DEFAULT NULL COMMENT '签发地点',
    `issue_date` DATE DEFAULT NULL COMMENT '签发日期',
    `valid_from` DATE DEFAULT NULL COMMENT '有效期开始日期',
    `valid_until` DATE DEFAULT NULL COMMENT '有效期截止日期',
    `entry_count` VARCHAR(32) DEFAULT NULL COMMENT '入境次数',
    `status` VARCHAR(32) NOT NULL DEFAULT 'VALID' COMMENT '签证状态',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_visa_record_student` (`student_id`),
    KEY `idx_visa_record_valid_until` (`valid_until`),
    KEY `idx_visa_record_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签证信息表';

CREATE TABLE IF NOT EXISTS `residence_permit` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `permit_no` VARCHAR(64) DEFAULT NULL COMMENT '居留许可编号',
    `permit_type` VARCHAR(32) NOT NULL COMMENT '居留许可类型',
    `residence_address` VARCHAR(255) DEFAULT NULL COMMENT '居住地址',
    `issue_date` DATE DEFAULT NULL COMMENT '签发日期',
    `valid_from` DATE DEFAULT NULL COMMENT '有效期开始日期',
    `valid_until` DATE DEFAULT NULL COMMENT '有效期截止日期',
    `status` VARCHAR(32) NOT NULL DEFAULT 'VALID' COMMENT '居留许可状态',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_residence_permit_student` (`student_id`),
    KEY `idx_residence_permit_valid_until` (`valid_until`),
    KEY `idx_residence_permit_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='居留许可表';

CREATE TABLE IF NOT EXISTS `visa_renewal_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `renewal_type` VARCHAR(32) NOT NULL COMMENT '续签业务类型：VISA/RESIDENCE_PERMIT',
    `target_id` BIGINT DEFAULT NULL COMMENT '关联签证或居留许可ID',
    `application_date` DATE DEFAULT NULL COMMENT '申请日期',
    `acceptance_date` DATE DEFAULT NULL COMMENT '受理日期',
    `complete_date` DATE DEFAULT NULL COMMENT '办结日期',
    `renewal_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '续签状态',
    `result` VARCHAR(32) DEFAULT NULL COMMENT '办理结果',
    `new_valid_until` DATE DEFAULT NULL COMMENT '新有效期截止日期',
    `handler_id` BIGINT DEFAULT NULL COMMENT '经办人ID',
    `handler_name` VARCHAR(64) DEFAULT NULL COMMENT '经办人姓名',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_visa_renewal_student` (`student_id`),
    KEY `idx_visa_renewal_status` (`renewal_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签证居留续签记录表';

CREATE TABLE IF NOT EXISTS `visa_compliance_alert` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `alert_type` VARCHAR(32) NOT NULL COMMENT '预警类型',
    `alert_level` VARCHAR(32) NOT NULL COMMENT '预警等级',
    `target_type` VARCHAR(32) NOT NULL COMMENT '目标类型：VISA/PERMIT/STUDENT',
    `target_id` BIGINT DEFAULT NULL COMMENT '目标ID',
    `title` VARCHAR(128) NOT NULL COMMENT '预警标题',
    `content` VARCHAR(1000) DEFAULT NULL COMMENT '预警内容',
    `expire_date` DATE DEFAULT NULL COMMENT '到期日期',
    `remaining_days` INT DEFAULT NULL COMMENT '剩余天数',
    `alert_status` VARCHAR(32) NOT NULL DEFAULT 'OPEN' COMMENT '预警状态',
    `handler_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `handler_name` VARCHAR(64) DEFAULT NULL COMMENT '处理人姓名',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handle_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理说明',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_visa_alert_student` (`student_id`),
    KEY `idx_visa_alert_status` (`alert_status`),
    KEY `idx_visa_alert_target` (`target_type`, `target_id`, `alert_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签证居留合规预警表';

CREATE TABLE IF NOT EXISTS `visa_reminder_notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `alert_id` BIGINT DEFAULT NULL COMMENT '关联预警ID',
    `recipient_user_id` BIGINT DEFAULT NULL COMMENT '接收用户ID',
    `recipient_name` VARCHAR(64) DEFAULT NULL COMMENT '接收人姓名',
    `channel` VARCHAR(32) NOT NULL COMMENT '通知渠道',
    `send_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '发送状态',
    `title` VARCHAR(128) NOT NULL COMMENT '通知标题',
    `content` VARCHAR(1000) DEFAULT NULL COMMENT '通知内容',
    `error_message` VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
    `read_status` TINYINT NOT NULL DEFAULT 0 COMMENT '已读状态：0未读，1已读',
    `read_time` DATETIME DEFAULT NULL COMMENT '已读时间',
    `send_time` DATETIME DEFAULT NULL COMMENT '发送时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_visa_notification_alert` (`alert_id`),
    KEY `idx_visa_notification_user` (`recipient_user_id`),
    KEY `idx_visa_notification_read` (`read_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签证居留提醒通知表';

INSERT INTO `sys_dict_type` (`dict_name`, `dict_code`, `description`, `status`, `sort`)
SELECT t.dict_name, t.dict_code, t.description, 1, t.sort
FROM (
    SELECT '签证类型' dict_name, 'visa_type' dict_code, '签证类型' description, 30 sort UNION ALL
    SELECT '签证状态', 'visa_status', '签证状态', 31 UNION ALL
    SELECT '居留许可类型', 'residence_permit_type', '居留许可类型', 32 UNION ALL
    SELECT '居留许可状态', 'residence_permit_status', '居留许可状态', 33 UNION ALL
    SELECT '续签业务类型', 'visa_renewal_type', '签证居留续签业务类型', 34 UNION ALL
    SELECT '续签状态', 'visa_renewal_status', '签证居留续签状态', 35 UNION ALL
    SELECT '合规预警类型', 'visa_alert_type', '签证居留合规预警类型', 36 UNION ALL
    SELECT '合规预警等级', 'visa_alert_level', '签证居留合规预警等级', 37 UNION ALL
    SELECT '合规预警状态', 'visa_alert_status', '签证居留合规预警状态', 38 UNION ALL
    SELECT '提醒渠道', 'visa_notification_channel', '签证居留提醒渠道', 39 UNION ALL
    SELECT '提醒发送状态', 'visa_notification_status', '签证居留提醒发送状态', 40
) t
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` d WHERE d.`dict_code` = t.dict_code AND d.`deleted` = 0);

INSERT INTO `sys_dict_data` (`dict_code`, `dict_label`, `dict_value`, `tag_type`, `status`, `sort`)
SELECT t.dict_code, t.dict_label, t.dict_value, t.tag_type, 1, t.sort
FROM (
    SELECT 'visa_type' dict_code, '学习签证' dict_label, 'STUDY' dict_value, 'primary' tag_type, 1 sort UNION ALL
    SELECT 'visa_type', '访问签证', 'VISIT', 'success', 2 UNION ALL
    SELECT 'visa_type', '居留签证', 'RESIDENCE', 'warning', 3 UNION ALL
    SELECT 'visa_type', '其他', 'OTHER', 'info', 4 UNION ALL
    SELECT 'visa_status', '有效', 'VALID', 'success', 1 UNION ALL
    SELECT 'visa_status', '即将到期', 'EXPIRING', 'warning', 2 UNION ALL
    SELECT 'visa_status', '已过期', 'EXPIRED', 'danger', 3 UNION ALL
    SELECT 'visa_status', '已注销', 'CANCELLED', 'info', 4 UNION ALL
    SELECT 'visa_status', '异常', 'ABNORMAL', 'danger', 5 UNION ALL
    SELECT 'residence_permit_type', '学习类居留许可', 'STUDY', 'primary', 1 UNION ALL
    SELECT 'residence_permit_type', '团聚类居留许可', 'FAMILY', 'success', 2 UNION ALL
    SELECT 'residence_permit_type', '其他', 'OTHER', 'info', 3 UNION ALL
    SELECT 'residence_permit_status', '有效', 'VALID', 'success', 1 UNION ALL
    SELECT 'residence_permit_status', '即将到期', 'EXPIRING', 'warning', 2 UNION ALL
    SELECT 'residence_permit_status', '已过期', 'EXPIRED', 'danger', 3 UNION ALL
    SELECT 'residence_permit_status', '已注销', 'CANCELLED', 'info', 4 UNION ALL
    SELECT 'residence_permit_status', '异常', 'ABNORMAL', 'danger', 5 UNION ALL
    SELECT 'visa_renewal_type', '签证续签', 'VISA', 'primary', 1 UNION ALL
    SELECT 'visa_renewal_type', '居留许可续签', 'RESIDENCE_PERMIT', 'success', 2 UNION ALL
    SELECT 'visa_renewal_status', '待办理', 'PENDING', 'warning', 1 UNION ALL
    SELECT 'visa_renewal_status', '办理中', 'PROCESSING', 'primary', 2 UNION ALL
    SELECT 'visa_renewal_status', '已通过', 'APPROVED', 'success', 3 UNION ALL
    SELECT 'visa_renewal_status', '已拒绝', 'REJECTED', 'danger', 4 UNION ALL
    SELECT 'visa_alert_type', '即将到期', 'EXPIRING', 'warning', 1 UNION ALL
    SELECT 'visa_alert_type', '已逾期', 'EXPIRED', 'danger', 2 UNION ALL
    SELECT 'visa_alert_type', '资料缺失', 'DATA_MISSING', 'warning', 3 UNION ALL
    SELECT 'visa_alert_type', '状态异常', 'STATUS_ABNORMAL', 'danger', 4 UNION ALL
    SELECT 'visa_alert_level', '低', 'LOW', 'info', 1 UNION ALL
    SELECT 'visa_alert_level', '中', 'MEDIUM', 'warning', 2 UNION ALL
    SELECT 'visa_alert_level', '高', 'HIGH', 'danger', 3 UNION ALL
    SELECT 'visa_alert_status', '待处理', 'OPEN', 'danger', 1 UNION ALL
    SELECT 'visa_alert_status', '处理中', 'PROCESSING', 'warning', 2 UNION ALL
    SELECT 'visa_alert_status', '已处理', 'RESOLVED', 'success', 3 UNION ALL
    SELECT 'visa_alert_status', '已关闭', 'CLOSED', 'info', 4 UNION ALL
    SELECT 'visa_notification_channel', '站内通知', 'IN_APP', 'primary', 1 UNION ALL
    SELECT 'visa_notification_channel', '邮件', 'EMAIL', 'success', 2 UNION ALL
    SELECT 'visa_notification_status', '待发送', 'PENDING', 'warning', 1 UNION ALL
    SELECT 'visa_notification_status', '已发送', 'SENT', 'success', 2 UNION ALL
    SELECT 'visa_notification_status', '发送失败', 'FAILED', 'danger', 3
) t
WHERE NOT EXISTS (
    SELECT 1 FROM `sys_dict_data` d WHERE d.`dict_code` = t.dict_code AND d.`dict_value` = t.dict_value AND d.`deleted` = 0
);
