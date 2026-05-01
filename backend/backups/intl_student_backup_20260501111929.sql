-- International Student Service Management System SQL Backup
-- Database: `intl_student`

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `student_contact`
-- ----------------------------
DROP TABLE IF EXISTS `student_contact`;
CREATE TABLE `student_contact` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '留学生档案ID',
  `contact_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系人类型',
  `contact_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系人姓名',
  `relationship` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关系',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `is_primary` tinyint NOT NULL DEFAULT '0' COMMENT '是否主联系人：0否，1是',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_contact_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生联系人表';


-- ----------------------------
-- Table structure for `student_document`
-- ----------------------------
DROP TABLE IF EXISTS `student_document`;
CREATE TABLE `student_document` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '留学生档案ID',
  `document_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '材料类型',
  `document_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '材料名称',
  `file_url` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件地址',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `mime_type` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件类型',
  `review_status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT '审核状态',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_document_student_id` (`student_id`),
  KEY `idx_student_document_type` (`document_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生附件材料表';


-- ----------------------------
-- Table structure for `student_education`
-- ----------------------------
DROP TABLE IF EXISTS `student_education`;
CREATE TABLE `student_education` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '留学生档案ID',
  `school_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学校名称',
  `country` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家/地区',
  `degree_level` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学历层次',
  `major` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专业',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `certificate_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '证书附件地址',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_education_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生教育背景表';


-- ----------------------------
-- Table structure for `student_profile`
-- ----------------------------
DROP TABLE IF EXISTS `student_profile`;
CREATE TABLE `student_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint DEFAULT NULL COMMENT '关联系统用户ID',
  `student_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学号',
  `application_no` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '申请编号',
  `chinese_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '中文姓名',
  `english_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '英文姓名',
  `gender` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'UNKNOWN' COMMENT '性别：MALE男/FEMALE女/UNKNOWN未知',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `nationality` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国籍',
  `native_language` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '母语',
  `email` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `wechat` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信号',
  `passport_no` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '护照号码',
  `passport_country` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '护照签发国家',
  `passport_issue_date` date DEFAULT NULL COMMENT '护照签发日期',
  `passport_expire_date` date DEFAULT NULL COMMENT '护照有效期',
  `college` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学院',
  `major` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '专业',
  `degree_level` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学历层次',
  `grade` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '年级',
  `class_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '班级',
  `enrollment_date` date DEFAULT NULL COMMENT '入学日期',
  `expected_graduation_date` date DEFAULT NULL COMMENT '预计毕业日期',
  `student_status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PRE_ADMITTED' COMMENT '学生状态',
  `avatar_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像/证件照地址',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '启用状态：0禁用，1启用',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_profile_student_no` (`student_no`),
  UNIQUE KEY `uk_student_profile_user_id` (`user_id`),
  KEY `idx_student_profile_passport_no` (`passport_no`),
  KEY `idx_student_profile_student_status` (`student_status`),
  KEY `idx_student_profile_nationality` (`nationality`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生主档案表';


-- ----------------------------
-- Table structure for `student_status_log`
-- ----------------------------
DROP TABLE IF EXISTS `student_status_log`;
CREATE TABLE `student_status_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '留学生档案ID',
  `old_status` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原状态',
  `new_status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '新状态',
  `change_reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更原因',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人名称',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_status_log_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留学生状态变更日志表';


-- ----------------------------
-- Table structure for `sys_data_backup`
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_backup`;
CREATE TABLE `sys_data_backup` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `backup_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '备份名称',
  `file_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件名称',
  `file_path` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件路径',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `backup_status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'RUNNING' COMMENT '备份状态：RUNNING/SUCCESS/FAILED',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `cost_time` bigint DEFAULT NULL COMMENT '耗时毫秒',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人名称',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_sys_data_backup_status` (`backup_status`),
  KEY `idx_sys_data_backup_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统数据备份表';


-- ----------------------------
-- Table structure for `sys_dict_data`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典编码',
  `dict_label` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典值',
  `tag_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签样式',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_dict_data_value` (`dict_code`,`dict_value`),
  KEY `idx_sys_dict_data_code` (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统字典数据表';

INSERT INTO `sys_dict_data` VALUES (1, 'student_status', '预录取', 'PRE_ADMITTED', 'primary', NULL, 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (2, 'student_status', '在读', 'ENROLLED', 'success', NULL, 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (3, 'student_status', '休学', 'SUSPENDED', 'warning', NULL, 1, 3, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (4, 'student_status', '已毕业', 'GRADUATED', 'info', NULL, 1, 4, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (5, 'student_status', '退学', 'DROPPED', 'danger', NULL, 1, 5, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (6, 'student_status', '已离校', 'LEFT', 'info', NULL, 1, 6, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (7, 'gender', '男', 'MALE', 'primary', NULL, 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (8, 'gender', '女', 'FEMALE', 'danger', NULL, 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (9, 'gender', '未知', 'UNKNOWN', 'info', NULL, 1, 3, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (10, 'contact_type', '紧急联系人', 'EMERGENCY', 'danger', NULL, 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (11, 'contact_type', '家庭联系人', 'FAMILY', 'success', NULL, 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (12, 'contact_type', '监护人', 'GUARDIAN', 'warning', NULL, 1, 3, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (13, 'contact_type', '其他', 'OTHER', 'info', NULL, 1, 4, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (14, 'document_type', '护照', 'PASSPORT', 'primary', NULL, 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (15, 'document_type', '照片', 'PHOTO', 'success', NULL, 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (16, 'document_type', '录取通知书', 'ADMISSION_NOTICE', 'warning', NULL, 1, 3, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (17, 'document_type', '学历证明', 'DEGREE_CERTIFICATE', 'primary', NULL, 1, 4, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (18, 'document_type', '语言成绩', 'LANGUAGE_SCORE', 'success', NULL, 1, 5, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (19, 'document_type', '体检证明', 'PHYSICAL_EXAM', 'warning', NULL, 1, 6, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (20, 'document_type', '保险材料', 'INSURANCE', 'primary', NULL, 1, 7, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (21, 'document_type', '其他', 'OTHER', 'info', NULL, 1, 8, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (22, 'document_review_status', '待审核', 'PENDING', 'warning', NULL, 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (23, 'document_review_status', '已通过', 'APPROVED', 'success', NULL, 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_data` VALUES (24, 'document_review_status', '已拒绝', 'REJECTED', 'danger', NULL, 1, 3, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);

-- ----------------------------
-- Table structure for `sys_dict_type`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典名称',
  `dict_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典编码',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_dict_type_code` (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统字典类型表';

INSERT INTO `sys_dict_type` VALUES (1, '学生状态', 'student_status', '留学生档案学生状态', 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_type` VALUES (2, '性别', 'gender', '人员性别', 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_type` VALUES (3, '联系人类型', 'contact_type', '留学生联系人类型', 1, 3, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_type` VALUES (4, '材料类型', 'document_type', '留学生附件材料类型', 1, 4, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_dict_type` VALUES (5, '材料审核状态', 'document_review_status', '附件材料审核状态', 1, 5, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);

-- ----------------------------
-- Table structure for `sys_flow_node`
-- ----------------------------
DROP TABLE IF EXISTS `sys_flow_node`;
CREATE TABLE `sys_flow_node` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_id` bigint NOT NULL COMMENT '流程模板ID',
  `node_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点名称',
  `node_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点编码',
  `node_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '节点类型：START/APPROVAL/END',
  `approver_role_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审批角色编码',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_sys_flow_node_flow` (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程节点表';


-- ----------------------------
-- Table structure for `sys_flow_template`
-- ----------------------------
DROP TABLE IF EXISTS `sys_flow_template`;
CREATE TABLE `sys_flow_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '流程名称',
  `flow_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '流程编码',
  `business_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务类型',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_flow_template_code` (`flow_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程模板表';


-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父级菜单ID，0表示根节点',
  `menu_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `menu_type` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单类型：CATALOG目录/MENU菜单/BUTTON按钮',
  `permission_code` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识，如system:user:add',
  `path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '前端路由路径',
  `component` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '前端组件路径',
  `icon` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单图标',
  `visible` tinyint NOT NULL DEFAULT '1' COMMENT '是否可见：0隐藏，1显示',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_sys_menu_parent` (`parent_id`),
  KEY `idx_sys_menu_permission` (`permission_code`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', 'CATALOG', 'system:manage', '/system', 'Layout', 'system', 1, 1, 1, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (2, 1, '用户管理', 'MENU', 'system:user:list', '/system/users', 'system/user/index', 'user', 1, 1, 1, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (3, 2, '新增用户', 'BUTTON', 'system:user:add', NULL, NULL, NULL, 0, 1, 1, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (4, 2, '编辑用户', 'BUTTON', 'system:user:edit', NULL, NULL, NULL, 0, 1, 2, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (5, 2, '删除用户', 'BUTTON', 'system:user:delete', NULL, NULL, NULL, 0, 1, 3, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (6, 2, '用户状态', 'BUTTON', 'system:user:status', NULL, NULL, NULL, 0, 1, 4, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (7, 2, '分配角色', 'BUTTON', 'system:user:roles', NULL, NULL, NULL, 0, 1, 5, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (8, 1, '角色管理', 'MENU', 'system:role:list', '/system/roles', 'system/role/index', 'role', 1, 1, 2, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (9, 8, '新增角色', 'BUTTON', 'system:role:add', NULL, NULL, NULL, 0, 1, 1, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (10, 8, '编辑角色', 'BUTTON', 'system:role:edit', NULL, NULL, NULL, 0, 1, 2, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (11, 8, '删除角色', 'BUTTON', 'system:role:delete', NULL, NULL, NULL, 0, 1, 3, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (12, 8, '分配菜单', 'BUTTON', 'system:role:menus', NULL, NULL, NULL, 0, 1, 4, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (13, 1, '菜单权限', 'MENU', 'system:menu:list', '/system/menus', 'system/menu/index', 'menu', 1, 1, 3, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (14, 13, '新增菜单', 'BUTTON', 'system:menu:add', NULL, NULL, NULL, 0, 1, 1, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (15, 13, '编辑菜单', 'BUTTON', 'system:menu:edit', NULL, NULL, NULL, 0, 1, 2, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (16, 13, '删除菜单', 'BUTTON', 'system:menu:delete', NULL, NULL, NULL, 0, 1, 3, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_menu` VALUES (17, 0, '留学生管理', 'CATALOG', 'student:manage', '/student', 'Layout', 'school', 1, 1, 20, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (18, 17, '留学生档案', 'MENU', 'student:profile:list', '/student/profiles', 'student/profile/index', 'user', 1, 1, 1, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (19, 18, '新增档案', 'BUTTON', 'student:profile:add', NULL, NULL, NULL, 0, 1, 1, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (20, 18, '编辑档案', 'BUTTON', 'student:profile:update', NULL, NULL, NULL, 0, 1, 2, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (21, 18, '删除档案', 'BUTTON', 'student:profile:delete', NULL, NULL, NULL, 0, 1, 3, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (22, 18, '档案详情', 'BUTTON', 'student:profile:detail', NULL, NULL, NULL, 0, 1, 4, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (23, 18, '状态变更', 'BUTTON', 'student:profile:status', NULL, NULL, NULL, 0, 1, 5, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (24, 18, '联系人列表', 'BUTTON', 'student:contact:list', NULL, NULL, NULL, 0, 1, 6, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (25, 18, '新增联系人', 'BUTTON', 'student:contact:add', NULL, NULL, NULL, 0, 1, 7, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (26, 18, '编辑联系人', 'BUTTON', 'student:contact:update', NULL, NULL, NULL, 0, 1, 8, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (27, 18, '删除联系人', 'BUTTON', 'student:contact:delete', NULL, NULL, NULL, 0, 1, 9, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (28, 18, '教育背景列表', 'BUTTON', 'student:education:list', NULL, NULL, NULL, 0, 1, 10, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (29, 18, '新增教育背景', 'BUTTON', 'student:education:add', NULL, NULL, NULL, 0, 1, 11, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (30, 18, '编辑教育背景', 'BUTTON', 'student:education:update', NULL, NULL, NULL, 0, 1, 12, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (31, 18, '删除教育背景', 'BUTTON', 'student:education:delete', NULL, NULL, NULL, 0, 1, 13, '2026-05-01T00:14:51', '2026-05-01T11:18:35', 0);
INSERT INTO `sys_menu` VALUES (32, 18, '材料列表', 'BUTTON', 'student:document:list', NULL, NULL, NULL, 0, 1, 14, '2026-05-01T00:14:51', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_menu` VALUES (33, 18, '新增材料', 'BUTTON', 'student:document:add', NULL, NULL, NULL, 0, 1, 15, '2026-05-01T00:14:51', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_menu` VALUES (34, 18, '编辑材料', 'BUTTON', 'student:document:update', NULL, NULL, NULL, 0, 1, 16, '2026-05-01T00:14:51', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_menu` VALUES (35, 18, '删除材料', 'BUTTON', 'student:document:delete', NULL, NULL, NULL, 0, 1, 17, '2026-05-01T00:14:51', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_menu` VALUES (36, 18, '状态日志列表', 'BUTTON', 'student:status-log:list', NULL, NULL, NULL, 0, 1, 18, '2026-05-01T00:14:51', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_menu` VALUES (50, 0, '系统配置与审计', 'CATALOG', 'config:audit:manage', '/config', 'Layout', 'setting', 1, 1, 30, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (51, 50, '字典配置', 'MENU', 'config:dict:list', '/config/dict', 'config/dict/index', 'menu', 1, 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (52, 51, '新增字典', 'BUTTON', 'config:dict:add', NULL, NULL, NULL, 0, 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (53, 51, '编辑字典', 'BUTTON', 'config:dict:update', NULL, NULL, NULL, 0, 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (54, 51, '删除字典', 'BUTTON', 'config:dict:delete', NULL, NULL, NULL, 0, 1, 3, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (55, 51, '字典状态', 'BUTTON', 'config:dict:status', NULL, NULL, NULL, 0, 1, 4, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (56, 50, '流程配置', 'MENU', 'config:flow:list', '/config/flow', 'config/flow/index', 'share', 1, 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (57, 56, '新增流程', 'BUTTON', 'config:flow:add', NULL, NULL, NULL, 0, 1, 1, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (58, 56, '编辑流程', 'BUTTON', 'config:flow:update', NULL, NULL, NULL, 0, 1, 2, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (59, 56, '删除流程', 'BUTTON', 'config:flow:delete', NULL, NULL, NULL, 0, 1, 3, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (60, 56, '流程状态', 'BUTTON', 'config:flow:status', NULL, NULL, NULL, 0, 1, 4, '2026-05-01T11:18:37', '2026-05-01T11:18:37', 0);
INSERT INTO `sys_menu` VALUES (61, 56, '节点配置', 'BUTTON', 'config:flow:nodes', NULL, NULL, NULL, 0, 1, 5, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_menu` VALUES (62, 50, '操作日志', 'MENU', 'audit:operation-log:list', '/config/log', 'config/log/index', 'document', 1, 1, 3, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_menu` VALUES (63, 62, '日志详情', 'BUTTON', 'audit:operation-log:detail', NULL, NULL, NULL, 0, 1, 1, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_menu` VALUES (64, 62, '导出日志', 'BUTTON', 'audit:operation-log:export', NULL, NULL, NULL, 0, 1, 2, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_menu` VALUES (65, 62, '删除日志', 'BUTTON', 'audit:operation-log:delete', NULL, NULL, NULL, 0, 1, 3, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_menu` VALUES (66, 50, '数据备份', 'MENU', 'config:backup:list', '/config/backup', 'config/backup/index', 'folder', 1, 1, 4, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_menu` VALUES (67, 66, '创建备份', 'BUTTON', 'config:backup:create', NULL, NULL, NULL, 0, 1, 1, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_menu` VALUES (68, 66, '下载备份', 'BUTTON', 'config:backup:download', NULL, NULL, NULL, 0, 1, 2, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_menu` VALUES (69, 66, '删除备份', 'BUTTON', 'config:backup:delete', NULL, NULL, NULL, 0, 1, 3, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);

-- ----------------------------
-- Table structure for `sys_operation_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模块名称',
  `operation_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型',
  `operation_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作名称',
  `request_method` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求方法',
  `request_uri` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求地址',
  `controller_method` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '控制器方法',
  `request_params` text COLLATE utf8mb4_unicode_ci COMMENT '请求参数',
  `response_result` text COLLATE utf8mb4_unicode_ci COMMENT '响应结果',
  `success` tinyint NOT NULL DEFAULT '1' COMMENT '是否成功：0失败，1成功',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '异常信息',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人名称',
  `ip_address` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'User-Agent',
  `cost_time` bigint DEFAULT NULL COMMENT '耗时毫秒',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_sys_operation_log_module` (`module_name`),
  KEY `idx_sys_operation_log_operator` (`operator_id`),
  KEY `idx_sys_operation_log_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统操作日志表';


-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

INSERT INTO `sys_role` VALUES (1, 'SUPER_ADMIN', '系统管理员', '拥有系统全部权限', 1, 1, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role` VALUES (2, 'FOREIGN_ADMIN', '外事管理员', '负责外事管理业务', 1, 2, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role` VALUES (3, 'ADMISSION_ADMIN', '招生管理员', '负责招生管理业务', 1, 3, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role` VALUES (4, 'TEACHER', '教师', '教师用户角色', 1, 4, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role` VALUES (5, 'STUDENT', '留学生', '留学生用户角色', 1, 5, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role` VALUES (6, 'SERVICE_STAFF', '服务人员', '服务人员角色', 1, 6, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_role_menu` (`role_id`,`menu_id`),
  KEY `idx_sys_role_menu_menu` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关系表';

INSERT INTO `sys_role_menu` VALUES (1, 1, 1, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (2, 1, 2, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (3, 1, 3, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (4, 1, 4, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (5, 1, 5, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (6, 1, 6, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (7, 1, 7, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (8, 1, 8, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (9, 1, 9, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (10, 1, 10, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (11, 1, 11, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (12, 1, 12, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (13, 1, 13, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (14, 1, 14, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (15, 1, 15, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (16, 1, 16, '2026-04-30T17:02:37', '2026-04-30T17:02:37', 0);
INSERT INTO `sys_role_menu` VALUES (32, 1, 17, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (33, 1, 18, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (34, 1, 19, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (35, 1, 20, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (36, 1, 21, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (37, 1, 22, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (38, 1, 23, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (39, 1, 24, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (40, 1, 25, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (41, 1, 26, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (42, 1, 27, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (43, 1, 28, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (44, 1, 29, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (45, 1, 30, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (46, 1, 31, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (47, 1, 32, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (48, 1, 33, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (49, 1, 34, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (50, 1, 35, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (51, 1, 36, '2026-05-01T00:35:11', '2026-05-01T11:18:36', 0);
INSERT INTO `sys_role_menu` VALUES (92, 1, 50, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (93, 1, 51, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (94, 1, 52, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (95, 1, 53, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (96, 1, 54, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (97, 1, 55, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (98, 1, 56, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (99, 1, 57, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (100, 1, 58, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (101, 1, 59, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (102, 1, 60, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (103, 1, 61, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (104, 1, 62, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (105, 1, 63, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (106, 1, 64, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (107, 1, 65, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (108, 1, 66, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (109, 1, 67, '2026-05-01T11:18:38', '2026-05-01T11:18:38', 0);
INSERT INTO `sys_role_menu` VALUES (110, 1, 68, '2026-05-01T11:18:39', '2026-05-01T11:18:39', 0);
INSERT INTO `sys_role_menu` VALUES (111, 1, 69, '2026-05-01T11:18:39', '2026-05-01T11:18:39', 0);

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录账号',
  `password` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录密码（当前明文存储）',
  `real_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '真实姓名',
  `email` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `user_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户类型：STUDENT/TEACHER/ADMIN/SERVICE_STAFF',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_username` (`username`),
  KEY `idx_sys_user_type` (`user_type`),
  KEY `idx_sys_user_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

INSERT INTO `sys_user` VALUES (1, 'admin', 'admin123', '系统管理员', 'admin@example.com', '13800000000', 'SUPER_ADMIN', 1, '2026-04-30T17:02:36', '2026-04-30T22:51:55', 0);

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_role` (`user_id`,`role_id`),
  KEY `idx_sys_user_role_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关系表';

INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2026-04-30T17:02:37', '2026-05-01T11:18:38', 0);

SET FOREIGN_KEY_CHECKS=1;
