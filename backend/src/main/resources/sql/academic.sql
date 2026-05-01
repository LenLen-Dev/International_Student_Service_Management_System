CREATE TABLE IF NOT EXISTS `academic_major` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `college` VARCHAR(128) NOT NULL COMMENT '学院',
    `major_code` VARCHAR(64) NOT NULL COMMENT '专业编码',
    `major_name` VARCHAR(128) NOT NULL COMMENT '专业名称',
    `degree_level` VARCHAR(32) DEFAULT NULL COMMENT '学历层次',
    `study_duration` INT DEFAULT NULL COMMENT '学制年限',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_academic_major_code` (`major_code`),
    KEY `idx_academic_major_college` (`college`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专业基础信息表';

CREATE TABLE IF NOT EXISTS `academic_grade` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `grade_code` VARCHAR(64) NOT NULL COMMENT '年级编码',
    `grade_name` VARCHAR(64) NOT NULL COMMENT '年级名称',
    `enrollment_year` INT NOT NULL COMMENT '入学年份',
    `graduation_year` INT DEFAULT NULL COMMENT '预计毕业年份',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_academic_grade_code` (`grade_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='年级基础信息表';

CREATE TABLE IF NOT EXISTS `academic_class` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `major_id` BIGINT NOT NULL COMMENT '专业ID',
    `grade_id` BIGINT NOT NULL COMMENT '年级ID',
    `class_code` VARCHAR(64) NOT NULL COMMENT '班级编码',
    `class_name` VARCHAR(128) NOT NULL COMMENT '班级名称',
    `advisor_id` BIGINT DEFAULT NULL COMMENT '班主任/辅导员用户ID',
    `advisor_name` VARCHAR(64) DEFAULT NULL COMMENT '班主任/辅导员姓名',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_academic_class_code` (`class_code`),
    KEY `idx_academic_class_major` (`major_id`),
    KEY `idx_academic_class_grade` (`grade_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级基础信息表';

CREATE TABLE IF NOT EXISTS `academic_student_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `major_id` BIGINT NOT NULL COMMENT '专业ID',
    `grade_id` BIGINT NOT NULL COMMENT '年级ID',
    `class_id` BIGINT DEFAULT NULL COMMENT '班级ID',
    `student_status` VARCHAR(32) NOT NULL COMMENT '学籍状态',
    `enrollment_date` DATE DEFAULT NULL COMMENT '入学日期',
    `expected_graduation_date` DATE DEFAULT NULL COMMENT '预计毕业日期',
    `actual_leave_date` DATE DEFAULT NULL COMMENT '实际离校日期',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_academic_record_student` (`student_id`),
    KEY `idx_academic_record_major` (`major_id`),
    KEY `idx_academic_record_grade` (`grade_id`),
    KEY `idx_academic_record_class` (`class_id`),
    KEY `idx_academic_record_status` (`student_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生学籍记录表';

CREATE TABLE IF NOT EXISTS `academic_leave_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `leave_type` VARCHAR(32) NOT NULL COMMENT '请假类型',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `reason` VARCHAR(1000) NOT NULL COMMENT '请假原因',
    `leave_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '请假状态',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approver_name` VARCHAR(64) DEFAULT NULL COMMENT '审批人姓名',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approve_opinion` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_academic_leave_student` (`student_id`),
    KEY `idx_academic_leave_status` (`leave_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生请假申请表';

CREATE TABLE IF NOT EXISTS `academic_status_change` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `change_type` VARCHAR(32) NOT NULL COMMENT '异动类型',
    `old_status` VARCHAR(32) DEFAULT NULL COMMENT '原学籍状态',
    `new_status` VARCHAR(32) DEFAULT NULL COMMENT '新学籍状态',
    `old_major_id` BIGINT DEFAULT NULL COMMENT '原专业ID',
    `new_major_id` BIGINT DEFAULT NULL COMMENT '新专业ID',
    `old_grade_id` BIGINT DEFAULT NULL COMMENT '原年级ID',
    `new_grade_id` BIGINT DEFAULT NULL COMMENT '新年级ID',
    `old_class_id` BIGINT DEFAULT NULL COMMENT '原班级ID',
    `new_class_id` BIGINT DEFAULT NULL COMMENT '新班级ID',
    `effective_date` DATE DEFAULT NULL COMMENT '生效日期',
    `reason` VARCHAR(1000) NOT NULL COMMENT '异动原因',
    `change_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '异动审批状态',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approver_name` VARCHAR(64) DEFAULT NULL COMMENT '审批人姓名',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approve_opinion` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_academic_change_student` (`student_id`),
    KEY `idx_academic_change_status` (`change_status`),
    KEY `idx_academic_change_type` (`change_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学籍异动申请表';

INSERT INTO `sys_dict_type` (`dict_name`, `dict_code`, `description`, `status`, `sort`)
SELECT t.dict_name, t.dict_code, t.description, 1, t.sort
FROM (
    SELECT '学籍状态' dict_name, 'academic_student_status' dict_code, '学籍与在校管理学籍状态' description, 50 sort UNION ALL
    SELECT '请假类型', 'academic_leave_type', '学生请假类型', 51 UNION ALL
    SELECT '请假状态', 'academic_leave_status', '学生请假审批状态', 52 UNION ALL
    SELECT '异动类型', 'academic_change_type', '学籍异动类型', 53 UNION ALL
    SELECT '异动状态', 'academic_change_status', '学籍异动审批状态', 54 UNION ALL
    SELECT '学籍学历层次', 'academic_degree_level', '学籍管理学历层次', 55
) t
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` d WHERE d.`dict_code` = t.dict_code AND d.`deleted` = 0);

INSERT INTO `sys_dict_data` (`dict_code`, `dict_label`, `dict_value`, `tag_type`, `status`, `sort`)
SELECT t.dict_code, t.dict_label, t.dict_value, t.tag_type, 1, t.sort
FROM (
    SELECT 'academic_student_status' dict_code, '预录取' dict_label, 'PRE_ADMITTED' dict_value, 'primary' tag_type, 1 sort UNION ALL
    SELECT 'academic_student_status', '在读', 'ENROLLED', 'success', 2 UNION ALL
    SELECT 'academic_student_status', '休学', 'SUSPENDED', 'warning', 3 UNION ALL
    SELECT 'academic_student_status', '已毕业', 'GRADUATED', 'info', 4 UNION ALL
    SELECT 'academic_student_status', '退学', 'DROPPED', 'danger', 5 UNION ALL
    SELECT 'academic_student_status', '已离校', 'LEFT', 'info', 6 UNION ALL
    SELECT 'academic_leave_type', '事假', 'PERSONAL', 'primary', 1 UNION ALL
    SELECT 'academic_leave_type', '病假', 'SICK', 'warning', 2 UNION ALL
    SELECT 'academic_leave_type', '公假', 'OFFICIAL', 'success', 3 UNION ALL
    SELECT 'academic_leave_type', '其他', 'OTHER', 'info', 4 UNION ALL
    SELECT 'academic_leave_status', '待审批', 'PENDING', 'warning', 1 UNION ALL
    SELECT 'academic_leave_status', '已通过', 'APPROVED', 'success', 2 UNION ALL
    SELECT 'academic_leave_status', '已拒绝', 'REJECTED', 'danger', 3 UNION ALL
    SELECT 'academic_leave_status', '已撤回', 'CANCELLED', 'info', 4 UNION ALL
    SELECT 'academic_change_type', '休学', 'SUSPEND', 'warning', 1 UNION ALL
    SELECT 'academic_change_type', '复学', 'RESUME', 'success', 2 UNION ALL
    SELECT 'academic_change_type', '转专业', 'TRANSFER_MAJOR', 'primary', 3 UNION ALL
    SELECT 'academic_change_type', '转班', 'TRANSFER_CLASS', 'primary', 4 UNION ALL
    SELECT 'academic_change_type', '毕业', 'GRADUATE', 'success', 5 UNION ALL
    SELECT 'academic_change_type', '退学', 'DROP_OUT', 'danger', 6 UNION ALL
    SELECT 'academic_change_type', '离校', 'LEAVE_SCHOOL', 'info', 7 UNION ALL
    SELECT 'academic_change_status', '待审批', 'PENDING', 'warning', 1 UNION ALL
    SELECT 'academic_change_status', '已通过', 'APPROVED', 'success', 2 UNION ALL
    SELECT 'academic_change_status', '已拒绝', 'REJECTED', 'danger', 3 UNION ALL
    SELECT 'academic_degree_level', '本科', 'BACHELOR', 'primary', 1 UNION ALL
    SELECT 'academic_degree_level', '硕士', 'MASTER', 'success', 2 UNION ALL
    SELECT 'academic_degree_level', '博士', 'DOCTOR', 'warning', 3
) t
WHERE NOT EXISTS (
    SELECT 1 FROM `sys_dict_data` d WHERE d.`dict_code` = t.dict_code AND d.`dict_value` = t.dict_value AND d.`deleted` = 0
);
