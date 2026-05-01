CREATE TABLE IF NOT EXISTS `student_profile` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '关联系统用户ID',
    `student_no` VARCHAR(64) NOT NULL COMMENT '学号',
    `application_no` VARCHAR(64) DEFAULT NULL COMMENT '申请编号',
    `chinese_name` VARCHAR(64) DEFAULT NULL COMMENT '中文姓名',
    `english_name` VARCHAR(128) NOT NULL COMMENT '英文姓名',
    `gender` VARCHAR(16) NOT NULL DEFAULT 'UNKNOWN' COMMENT '性别：MALE男/FEMALE女/UNKNOWN未知',
    `birth_date` DATE DEFAULT NULL COMMENT '出生日期',
    `nationality` VARCHAR(64) NOT NULL COMMENT '国籍',
    `native_language` VARCHAR(64) DEFAULT NULL COMMENT '母语',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
    `wechat` VARCHAR(64) DEFAULT NULL COMMENT '微信号',
    `passport_no` VARCHAR(64) DEFAULT NULL COMMENT '护照号码',
    `passport_country` VARCHAR(64) DEFAULT NULL COMMENT '护照签发国家',
    `passport_issue_date` DATE DEFAULT NULL COMMENT '护照签发日期',
    `passport_expire_date` DATE DEFAULT NULL COMMENT '护照有效期',
    `college` VARCHAR(128) DEFAULT NULL COMMENT '学院',
    `major` VARCHAR(128) DEFAULT NULL COMMENT '专业',
    `degree_level` VARCHAR(64) DEFAULT NULL COMMENT '学历层次',
    `grade` VARCHAR(32) DEFAULT NULL COMMENT '年级',
    `class_name` VARCHAR(64) DEFAULT NULL COMMENT '班级',
    `enrollment_date` DATE DEFAULT NULL COMMENT '入学日期',
    `expected_graduation_date` DATE DEFAULT NULL COMMENT '预计毕业日期',
    `student_status` VARCHAR(32) NOT NULL DEFAULT 'PRE_ADMITTED' COMMENT '学生状态',
    `avatar_url` VARCHAR(512) DEFAULT NULL COMMENT '头像/证件照地址',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '启用状态：0禁用，1启用',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_profile_student_no` (`student_no`),
    UNIQUE KEY `uk_student_profile_user_id` (`user_id`),
    KEY `idx_student_profile_passport_no` (`passport_no`),
    KEY `idx_student_profile_student_status` (`student_status`),
    KEY `idx_student_profile_nationality` (`nationality`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生主档案表';

CREATE TABLE IF NOT EXISTS `student_contact` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `contact_type` VARCHAR(32) NOT NULL COMMENT '联系人类型',
    `contact_name` VARCHAR(64) NOT NULL COMMENT '联系人姓名',
    `relationship` VARCHAR(64) DEFAULT NULL COMMENT '关系',
    `phone` VARCHAR(32) DEFAULT NULL COMMENT '电话',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
    `is_primary` TINYINT NOT NULL DEFAULT 0 COMMENT '是否主联系人：0否，1是',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_contact_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生联系人表';

CREATE TABLE IF NOT EXISTS `student_education` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `school_name` VARCHAR(128) NOT NULL COMMENT '学校名称',
    `country` VARCHAR(64) DEFAULT NULL COMMENT '国家/地区',
    `degree_level` VARCHAR(64) DEFAULT NULL COMMENT '学历层次',
    `major` VARCHAR(128) DEFAULT NULL COMMENT '专业',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `certificate_url` VARCHAR(512) DEFAULT NULL COMMENT '证书附件地址',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_education_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生教育背景表';

CREATE TABLE IF NOT EXISTS `student_document` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `document_type` VARCHAR(32) NOT NULL COMMENT '材料类型',
    `document_name` VARCHAR(128) NOT NULL COMMENT '材料名称',
    `file_url` VARCHAR(512) NOT NULL COMMENT '文件地址',
    `file_size` BIGINT DEFAULT NULL COMMENT '文件大小',
    `mime_type` VARCHAR(128) DEFAULT NULL COMMENT '文件类型',
    `review_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '审核状态',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_document_student_id` (`student_id`),
    KEY `idx_student_document_type` (`document_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生附件材料表';

CREATE TABLE IF NOT EXISTS `student_status_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `old_status` VARCHAR(32) DEFAULT NULL COMMENT '原状态',
    `new_status` VARCHAR(32) NOT NULL COMMENT '新状态',
    `change_reason` VARCHAR(255) DEFAULT NULL COMMENT '变更原因',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(64) DEFAULT NULL COMMENT '操作人名称',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_status_log_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生状态变更日志表';

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_code`, `path`, `component`, `icon`, `visible`, `status`, `sort`)
SELECT 0, '留学生管理', 'CATALOG', 'student:manage', '/student', 'Layout', 'school', 1, 1, 20
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `permission_code` = 'student:manage' AND `deleted` = 0);

SET @student_parent_id := (SELECT `id` FROM `sys_menu` WHERE `permission_code` = 'student:manage' AND `deleted` = 0 LIMIT 1);

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_code`, `path`, `component`, `icon`, `visible`, `status`, `sort`)
SELECT @student_parent_id, '留学生档案', 'MENU', 'student:profile:list', '/student/profiles', 'student/profile/index', 'user', 1, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `permission_code` = 'student:profile:list' AND `deleted` = 0);

SET @student_profile_menu_id := (SELECT `id` FROM `sys_menu` WHERE `permission_code` = 'student:profile:list' AND `deleted` = 0 LIMIT 1);

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_code`, `path`, `component`, `icon`, `visible`, `status`, `sort`)
SELECT @student_profile_menu_id, t.menu_name, 'BUTTON', t.permission_code, NULL, NULL, NULL, 0, 1, t.sort
FROM (
    SELECT '新增档案' menu_name, 'student:profile:add' permission_code, 1 sort UNION ALL
    SELECT '编辑档案', 'student:profile:update', 2 UNION ALL
    SELECT '删除档案', 'student:profile:delete', 3 UNION ALL
    SELECT '档案详情', 'student:profile:detail', 4 UNION ALL
    SELECT '状态变更', 'student:profile:status', 5 UNION ALL
    SELECT '联系人列表', 'student:contact:list', 6 UNION ALL
    SELECT '新增联系人', 'student:contact:add', 7 UNION ALL
    SELECT '编辑联系人', 'student:contact:update', 8 UNION ALL
    SELECT '删除联系人', 'student:contact:delete', 9 UNION ALL
    SELECT '教育背景列表', 'student:education:list', 10 UNION ALL
    SELECT '新增教育背景', 'student:education:add', 11 UNION ALL
    SELECT '编辑教育背景', 'student:education:update', 12 UNION ALL
    SELECT '删除教育背景', 'student:education:delete', 13 UNION ALL
    SELECT '材料列表', 'student:document:list', 14 UNION ALL
    SELECT '新增材料', 'student:document:add', 15 UNION ALL
    SELECT '编辑材料', 'student:document:update', 16 UNION ALL
    SELECT '删除材料', 'student:document:delete', 17 UNION ALL
    SELECT '状态日志列表', 'student:status-log:list', 18
) t
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` m WHERE m.`permission_code` = t.permission_code AND m.`deleted` = 0);

SET @admin_user_id := (SELECT `id` FROM `sys_user` WHERE `username` = 'admin' AND `deleted` = 0 LIMIT 1);
SET @super_admin_role_id := (SELECT `id` FROM `sys_role` WHERE `role_code` = 'SUPER_ADMIN' AND `deleted` = 0 LIMIT 1);

INSERT INTO `sys_user_role` (`user_id`, `role_id`)
SELECT @admin_user_id, @super_admin_role_id
WHERE @admin_user_id IS NOT NULL
  AND @super_admin_role_id IS NOT NULL
ON DUPLICATE KEY UPDATE `deleted` = 0, `update_time` = NOW();

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT @super_admin_role_id, m.`id`
FROM `sys_menu` m
WHERE @super_admin_role_id IS NOT NULL
  AND m.`deleted` = 0
  AND (
    m.`permission_code` = 'student:manage'
    OR m.`permission_code` LIKE 'student:%'
  )
ON DUPLICATE KEY UPDATE `deleted` = 0, `update_time` = NOW();
