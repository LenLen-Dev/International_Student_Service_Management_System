CREATE TABLE IF NOT EXISTS `admission_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `application_no` VARCHAR(64) NOT NULL COMMENT '申请编号',
    `user_id` BIGINT NOT NULL COMMENT '申请人用户ID',
    `chinese_name` VARCHAR(64) DEFAULT NULL COMMENT '中文姓名',
    `english_name` VARCHAR(128) NOT NULL COMMENT '英文姓名',
    `gender` VARCHAR(16) NOT NULL DEFAULT 'UNKNOWN' COMMENT '性别',
    `birth_date` DATE DEFAULT NULL COMMENT '出生日期',
    `nationality` VARCHAR(64) NOT NULL COMMENT '国籍',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
    `passport_no` VARCHAR(64) DEFAULT NULL COMMENT '护照号码',
    `passport_country` VARCHAR(64) DEFAULT NULL COMMENT '护照签发国家',
    `passport_expire_date` DATE DEFAULT NULL COMMENT '护照有效期',
    `apply_college` VARCHAR(128) DEFAULT NULL COMMENT '申请学院',
    `apply_major` VARCHAR(128) DEFAULT NULL COMMENT '申请专业',
    `degree_level` VARCHAR(64) DEFAULT NULL COMMENT '申请学历层次',
    `program_type` VARCHAR(64) DEFAULT NULL COMMENT '项目类型',
    `education_background` VARCHAR(500) DEFAULT NULL COMMENT '教育背景摘要',
    `application_status` VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '申请状态',
    `admission_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '录取状态',
    `review_opinion` VARCHAR(1000) DEFAULT NULL COMMENT '审核意见',
    `student_profile_id` BIGINT DEFAULT NULL COMMENT '关联留学生档案ID',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_admission_application_no` (`application_no`),
    KEY `idx_admission_application_user` (`user_id`),
    KEY `idx_admission_application_status` (`application_status`),
    KEY `idx_admission_application_passport` (`passport_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='招生申请主表';

CREATE TABLE IF NOT EXISTS `admission_material` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `application_id` BIGINT NOT NULL COMMENT '申请ID',
    `material_type` VARCHAR(64) NOT NULL COMMENT '材料类型',
    `file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_url` VARCHAR(512) NOT NULL COMMENT '文件存储路径',
    `file_size` BIGINT DEFAULT NULL COMMENT '文件大小',
    `mime_type` VARCHAR(128) DEFAULT NULL COMMENT '文件类型',
    `review_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '审核状态',
    `review_opinion` VARCHAR(1000) DEFAULT NULL COMMENT '审核意见',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_admission_material_application` (`application_id`),
    KEY `idx_admission_material_type` (`material_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='招生申请材料表';

CREATE TABLE IF NOT EXISTS `admission_review_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `application_id` BIGINT NOT NULL COMMENT '申请ID',
    `action_type` VARCHAR(32) NOT NULL COMMENT '动作类型',
    `from_status` VARCHAR(32) DEFAULT NULL COMMENT '原状态',
    `to_status` VARCHAR(32) DEFAULT NULL COMMENT '新状态',
    `opinion` VARCHAR(1000) DEFAULT NULL COMMENT '意见',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(64) DEFAULT NULL COMMENT '操作人名称',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_admission_review_application` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='招生申请审核记录表';

CREATE TABLE IF NOT EXISTS `admission_notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `application_id` BIGINT NOT NULL COMMENT '申请ID',
    `notice_no` VARCHAR(64) NOT NULL COMMENT '通知书编号',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名称',
    `file_path` VARCHAR(512) NOT NULL COMMENT '文件路径',
    `issue_date` DATE DEFAULT NULL COMMENT '签发日期',
    `issuer_id` BIGINT DEFAULT NULL COMMENT '签发人ID',
    `issuer_name` VARCHAR(64) DEFAULT NULL COMMENT '签发人名称',
    `download_count` INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_admission_notice_no` (`notice_no`),
    KEY `idx_admission_notice_application` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='录取通知书表';

INSERT INTO `sys_dict_type` (`dict_name`, `dict_code`, `description`, `status`, `sort`)
SELECT t.dict_name, t.dict_code, t.description, 1, t.sort
FROM (
    SELECT '招生申请状态' dict_name, 'admission_application_status' dict_code, '招生申请流转状态' description, 20 sort UNION ALL
    SELECT '招生材料类型', 'admission_material_type', '招生申请材料类型', 21 UNION ALL
    SELECT '招生材料审核状态', 'admission_material_review_status', '招生申请材料审核状态', 22 UNION ALL
    SELECT '招生学历层次', 'admission_degree_level', '招生申请学历层次', 23 UNION ALL
    SELECT '招生项目类型', 'admission_program_type', '招生项目类型', 24
) t
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` d WHERE d.`dict_code` = t.dict_code AND d.`deleted` = 0);

INSERT INTO `sys_dict_data` (`dict_code`, `dict_label`, `dict_value`, `tag_type`, `status`, `sort`)
SELECT t.dict_code, t.dict_label, t.dict_value, t.tag_type, 1, t.sort
FROM (
    SELECT 'admission_application_status' dict_code, '草稿' dict_label, 'DRAFT' dict_value, 'info' tag_type, 1 sort UNION ALL
    SELECT 'admission_application_status', '已提交', 'SUBMITTED', 'primary', 2 UNION ALL
    SELECT 'admission_application_status', '审核中', 'UNDER_REVIEW', 'warning', 3 UNION ALL
    SELECT 'admission_application_status', '已退回', 'RETURNED', 'warning', 4 UNION ALL
    SELECT 'admission_application_status', '已拒绝', 'REJECTED', 'danger', 5 UNION ALL
    SELECT 'admission_application_status', '已录取', 'ADMITTED', 'success', 6 UNION ALL
    SELECT 'admission_application_status', '已发通知书', 'NOTICE_ISSUED', 'success', 7 UNION ALL
    SELECT 'admission_material_type', '护照', 'PASSPORT', 'primary', 1 UNION ALL
    SELECT 'admission_material_type', '证件照', 'PHOTO', 'success', 2 UNION ALL
    SELECT 'admission_material_type', '学历证明', 'DEGREE_CERTIFICATE', 'primary', 3 UNION ALL
    SELECT 'admission_material_type', '语言成绩', 'LANGUAGE_SCORE', 'success', 4 UNION ALL
    SELECT 'admission_material_type', '体检证明', 'PHYSICAL_EXAM', 'warning', 5 UNION ALL
    SELECT 'admission_material_type', '其他材料', 'OTHER', 'info', 6 UNION ALL
    SELECT 'admission_material_review_status', '待审核', 'PENDING', 'warning', 1 UNION ALL
    SELECT 'admission_material_review_status', '已通过', 'APPROVED', 'success', 2 UNION ALL
    SELECT 'admission_material_review_status', '已拒绝', 'REJECTED', 'danger', 3 UNION ALL
    SELECT 'admission_degree_level', '本科', 'BACHELOR', 'primary', 1 UNION ALL
    SELECT 'admission_degree_level', '硕士', 'MASTER', 'success', 2 UNION ALL
    SELECT 'admission_degree_level', '博士', 'DOCTOR', 'warning', 3 UNION ALL
    SELECT 'admission_program_type', '学历生', 'DEGREE', 'primary', 1 UNION ALL
    SELECT 'admission_program_type', '交换生', 'EXCHANGE', 'success', 2 UNION ALL
    SELECT 'admission_program_type', '语言生', 'LANGUAGE', 'warning', 3
) t
WHERE NOT EXISTS (
    SELECT 1 FROM `sys_dict_data` d WHERE d.`dict_code` = t.dict_code AND d.`dict_value` = t.dict_value AND d.`deleted` = 0
);
