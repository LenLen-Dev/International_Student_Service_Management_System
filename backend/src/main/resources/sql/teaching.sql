CREATE TABLE IF NOT EXISTS `teaching_course` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `course_code` VARCHAR(64) NOT NULL COMMENT '课程编码',
    `course_name` VARCHAR(128) NOT NULL COMMENT '课程名称',
    `credits` DECIMAL(4,1) NOT NULL DEFAULT 0 COMMENT '学分',
    `total_hours` INT DEFAULT NULL COMMENT '总学时',
    `course_type` VARCHAR(32) NOT NULL COMMENT '课程类型',
    `college` VARCHAR(128) DEFAULT NULL COMMENT '开课学院',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_teaching_course_code` (`course_code`),
    KEY `idx_teaching_course_type` (`course_type`),
    KEY `idx_teaching_course_college` (`college`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程基础信息表';

CREATE TABLE IF NOT EXISTS `teaching_course_offering` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `course_id` BIGINT NOT NULL COMMENT '课程ID',
    `teacher_id` BIGINT NOT NULL COMMENT '任课教师用户ID',
    `teacher_name` VARCHAR(64) NOT NULL COMMENT '任课教师姓名',
    `academic_year` VARCHAR(20) NOT NULL COMMENT '学年',
    `semester` VARCHAR(20) NOT NULL COMMENT '学期',
    `capacity` INT NOT NULL DEFAULT 0 COMMENT '容量',
    `selected_count` INT NOT NULL DEFAULT 0 COMMENT '已选人数',
    `selection_start_time` DATETIME DEFAULT NULL COMMENT '选课开始时间',
    `selection_end_time` DATETIME DEFAULT NULL COMMENT '选课结束时间',
    `class_time` VARCHAR(128) DEFAULT NULL COMMENT '上课时间',
    `classroom` VARCHAR(128) DEFAULT NULL COMMENT '上课地点',
    `offering_status` VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '开课状态',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_teaching_offering_course` (`course_id`),
    KEY `idx_teaching_offering_teacher` (`teacher_id`),
    KEY `idx_teaching_offering_term` (`academic_year`, `semester`),
    KEY `idx_teaching_offering_status` (`offering_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教学班开课信息表';

CREATE TABLE IF NOT EXISTS `teaching_course_enrollment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `offering_id` BIGINT NOT NULL COMMENT '教学班ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `enrollment_status` VARCHAR(32) NOT NULL DEFAULT 'SELECTED' COMMENT '选课状态',
    `select_time` DATETIME DEFAULT NULL COMMENT '选课时间',
    `drop_time` DATETIME DEFAULT NULL COMMENT '退课时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_teaching_enrollment_student_offering` (`offering_id`, `student_id`),
    KEY `idx_teaching_enrollment_student` (`student_id`),
    KEY `idx_teaching_enrollment_status` (`enrollment_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生选课记录表';

CREATE TABLE IF NOT EXISTS `teaching_grade` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `enrollment_id` BIGINT NOT NULL COMMENT '选课记录ID',
    `offering_id` BIGINT NOT NULL COMMENT '教学班ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `usual_score` DECIMAL(5,2) DEFAULT NULL COMMENT '平时成绩',
    `final_score` DECIMAL(5,2) DEFAULT NULL COMMENT '期末成绩',
    `total_score` DECIMAL(5,2) DEFAULT NULL COMMENT '总评成绩',
    `grade_point` DECIMAL(3,2) DEFAULT NULL COMMENT '绩点',
    `passed` TINYINT DEFAULT NULL COMMENT '是否通过：0否，1是',
    `grade_status` VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '成绩状态',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_teaching_grade_enrollment` (`enrollment_id`),
    KEY `idx_teaching_grade_offering` (`offering_id`),
    KEY `idx_teaching_grade_student` (`student_id`),
    KEY `idx_teaching_grade_status` (`grade_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生成绩记录表';

CREATE TABLE IF NOT EXISTS `teaching_attendance` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `offering_id` BIGINT NOT NULL COMMENT '教学班ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `attendance_date` DATE NOT NULL COMMENT '上课日期',
    `attendance_status` VARCHAR(32) NOT NULL COMMENT '出勤状态',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_teaching_attendance_once` (`offering_id`, `student_id`, `attendance_date`),
    KEY `idx_teaching_attendance_student` (`student_id`),
    KEY `idx_teaching_attendance_status` (`attendance_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生出勤记录表';

CREATE TABLE IF NOT EXISTS `teaching_academic_alert` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `student_id` BIGINT NOT NULL COMMENT '留学生档案ID',
    `alert_type` VARCHAR(32) NOT NULL COMMENT '预警类型',
    `alert_level` VARCHAR(32) NOT NULL COMMENT '预警等级',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源业务ID',
    `title` VARCHAR(128) NOT NULL COMMENT '预警标题',
    `content` VARCHAR(1000) DEFAULT NULL COMMENT '预警内容',
    `alert_status` VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '预警状态',
    `handler_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `handler_name` VARCHAR(64) DEFAULT NULL COMMENT '处理人姓名',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handle_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理说明',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_teaching_alert_student` (`student_id`),
    KEY `idx_teaching_alert_type` (`alert_type`),
    KEY `idx_teaching_alert_status` (`alert_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学业预警表';

INSERT INTO `sys_dict_type` (`dict_name`, `dict_code`, `description`, `status`, `sort`)
SELECT t.dict_name, t.dict_code, t.description, 1, t.sort
FROM (
    SELECT '课程类型' dict_name, 'teaching_course_type' dict_code, '教务课程类型' description, 60 sort UNION ALL
    SELECT '课程状态', 'teaching_course_status', '课程启用状态', 61 UNION ALL
    SELECT '开课状态', 'teaching_offering_status', '教学班开课状态', 62 UNION ALL
    SELECT '选课状态', 'teaching_enrollment_status', '学生选课状态', 63 UNION ALL
    SELECT '出勤状态', 'teaching_attendance_status', '学生出勤状态', 64 UNION ALL
    SELECT '成绩状态', 'teaching_grade_status', '成绩发布状态', 65 UNION ALL
    SELECT '学业预警类型', 'teaching_alert_type', '学业预警类型', 66 UNION ALL
    SELECT '学业预警等级', 'teaching_alert_level', '学业预警等级', 67 UNION ALL
    SELECT '学业预警状态', 'teaching_alert_status', '学业预警处理状态', 68
) t
WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_type` d WHERE d.`dict_code` = t.dict_code AND d.`deleted` = 0);

INSERT INTO `sys_dict_data` (`dict_code`, `dict_label`, `dict_value`, `tag_type`, `status`, `sort`)
SELECT t.dict_code, t.dict_label, t.dict_value, t.tag_type, 1, t.sort
FROM (
    SELECT 'teaching_course_type' dict_code, '必修课' dict_label, 'REQUIRED' dict_value, 'primary' tag_type, 1 sort UNION ALL
    SELECT 'teaching_course_type', '选修课', 'ELECTIVE', 'success', 2 UNION ALL
    SELECT 'teaching_course_type', '语言课', 'LANGUAGE', 'warning', 3 UNION ALL
    SELECT 'teaching_course_type', '实践课', 'PRACTICE', 'info', 4 UNION ALL
    SELECT 'teaching_offering_status', '草稿', 'DRAFT', 'info', 1 UNION ALL
    SELECT 'teaching_offering_status', '开放选课', 'OPEN', 'success', 2 UNION ALL
    SELECT 'teaching_offering_status', '已关闭', 'CLOSED', 'warning', 3 UNION ALL
    SELECT 'teaching_offering_status', '已结课', 'FINISHED', 'info', 4 UNION ALL
    SELECT 'teaching_enrollment_status', '已选', 'SELECTED', 'success', 1 UNION ALL
    SELECT 'teaching_enrollment_status', '已退课', 'DROPPED', 'info', 2 UNION ALL
    SELECT 'teaching_attendance_status', '出勤', 'PRESENT', 'success', 1 UNION ALL
    SELECT 'teaching_attendance_status', '迟到', 'LATE', 'warning', 2 UNION ALL
    SELECT 'teaching_attendance_status', '请假', 'LEAVE', 'primary', 3 UNION ALL
    SELECT 'teaching_attendance_status', '缺勤', 'ABSENT', 'danger', 4 UNION ALL
    SELECT 'teaching_grade_status', '草稿', 'DRAFT', 'info', 1 UNION ALL
    SELECT 'teaching_grade_status', '已发布', 'PUBLISHED', 'success', 2 UNION ALL
    SELECT 'teaching_alert_type', '挂科预警', 'FAILED_COURSE', 'danger', 1 UNION ALL
    SELECT 'teaching_alert_type', '低分预警', 'LOW_SCORE', 'warning', 2 UNION ALL
    SELECT 'teaching_alert_type', '缺勤预警', 'ABSENCE', 'danger', 3 UNION ALL
    SELECT 'teaching_alert_type', '未选课预警', 'NO_SELECTION', 'warning', 4 UNION ALL
    SELECT 'teaching_alert_level', '低', 'LOW', 'info', 1 UNION ALL
    SELECT 'teaching_alert_level', '中', 'MEDIUM', 'warning', 2 UNION ALL
    SELECT 'teaching_alert_level', '高', 'HIGH', 'danger', 3 UNION ALL
    SELECT 'teaching_alert_status', '待处理', 'PENDING', 'warning', 1 UNION ALL
    SELECT 'teaching_alert_status', '已处理', 'HANDLED', 'success', 2 UNION ALL
    SELECT 'teaching_alert_status', '已关闭', 'CLOSED', 'info', 3
) t
WHERE NOT EXISTS (
    SELECT 1 FROM `sys_dict_data` d WHERE d.`dict_code` = t.dict_code AND d.`dict_value` = t.dict_value AND d.`deleted` = 0
);
