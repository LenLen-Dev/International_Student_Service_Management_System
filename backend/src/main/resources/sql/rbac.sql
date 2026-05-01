CREATE DATABASE IF NOT EXISTS intl_student
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE intl_student;

DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(64) NOT NULL COMMENT '登录账号',
    `password` VARCHAR(128) NOT NULL COMMENT '登录密码（当前明文存储）',
    `real_name` VARCHAR(64) NOT NULL COMMENT '真实姓名',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
    `user_type` VARCHAR(32) NOT NULL COMMENT '用户类型：STUDENT/TEACHER/ADMIN/SERVICE_STAFF',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_username` (`username`),
    KEY `idx_sys_user_type` (`user_type`),
    KEY `idx_sys_user_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码',
    `role_name` VARCHAR(64) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级菜单ID，0表示根节点',
    `menu_name` VARCHAR(64) NOT NULL COMMENT '菜单名称',
    `menu_type` VARCHAR(16) NOT NULL COMMENT '菜单类型：CATALOG目录/MENU菜单/BUTTON按钮',
    `permission_code` VARCHAR(128) DEFAULT NULL COMMENT '权限标识，如system:user:add',
    `path` VARCHAR(255) DEFAULT NULL COMMENT '前端路由路径',
    `component` VARCHAR(255) DEFAULT NULL COMMENT '前端组件路径',
    `icon` VARCHAR(64) DEFAULT NULL COMMENT '菜单图标',
    `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见：0隐藏，1显示',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_sys_menu_parent` (`parent_id`),
    KEY `idx_sys_menu_permission` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

CREATE TABLE `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_role` (`user_id`, `role_id`),
    KEY `idx_sys_user_role_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关系表';

CREATE TABLE `sys_role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_role_menu` (`role_id`, `menu_id`),
    KEY `idx_sys_role_menu_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关系表';

INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `email`, `phone`, `user_type`, `status`) VALUES
(1, 'admin', 'admin123', '系统管理员', 'admin@example.com', '13800000000', 'ADMIN', 1);

INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `description`, `status`, `sort`) VALUES
(1, 'SUPER_ADMIN', '系统管理员', '拥有系统全部权限', 1, 1),
(2, 'FOREIGN_ADMIN', '外事管理员', '负责外事管理业务', 1, 2),
(3, 'ADMISSION_ADMIN', '招生管理员', '负责招生管理业务', 1, 3),
(4, 'TEACHER', '教师', '教师用户角色', 1, 4),
(5, 'STUDENT', '留学生', '留学生用户角色', 1, 5),
(6, 'SERVICE_STAFF', '服务人员', '服务人员角色', 1, 6);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `permission_code`, `path`, `component`, `icon`, `visible`, `status`, `sort`) VALUES
(1, 0, '系统管理', 'CATALOG', 'system:manage', '/system', 'Layout', 'system', 1, 1, 1),
(2, 1, '用户管理', 'MENU', 'system:user:list', '/system/users', 'system/user/index', 'user', 1, 1, 1),
(3, 2, '新增用户', 'BUTTON', 'system:user:add', NULL, NULL, NULL, 0, 1, 1),
(4, 2, '编辑用户', 'BUTTON', 'system:user:edit', NULL, NULL, NULL, 0, 1, 2),
(5, 2, '删除用户', 'BUTTON', 'system:user:delete', NULL, NULL, NULL, 0, 1, 3),
(6, 2, '用户状态', 'BUTTON', 'system:user:status', NULL, NULL, NULL, 0, 1, 4),
(7, 2, '分配角色', 'BUTTON', 'system:user:roles', NULL, NULL, NULL, 0, 1, 5),
(8, 1, '角色管理', 'MENU', 'system:role:list', '/system/roles', 'system/role/index', 'role', 1, 1, 2),
(9, 8, '新增角色', 'BUTTON', 'system:role:add', NULL, NULL, NULL, 0, 1, 1),
(10, 8, '编辑角色', 'BUTTON', 'system:role:edit', NULL, NULL, NULL, 0, 1, 2),
(11, 8, '删除角色', 'BUTTON', 'system:role:delete', NULL, NULL, NULL, 0, 1, 3),
(12, 8, '分配菜单', 'BUTTON', 'system:role:menus', NULL, NULL, NULL, 0, 1, 4),
(13, 1, '菜单权限', 'MENU', 'system:menu:list', '/system/menus', 'system/menu/index', 'menu', 1, 1, 3),
(14, 13, '新增菜单', 'BUTTON', 'system:menu:add', NULL, NULL, NULL, 0, 1, 1),
(15, 13, '编辑菜单', 'BUTTON', 'system:menu:edit', NULL, NULL, NULL, 0, 1, 2),
(16, 13, '删除菜单', 'BUTTON', 'system:menu:delete', NULL, NULL, NULL, 0, 1, 3);

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, `id` FROM `sys_menu` WHERE `deleted` = 0;
