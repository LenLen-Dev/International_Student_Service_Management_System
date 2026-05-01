package com.wenjie.modules.config.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(10)
@RequiredArgsConstructor
public class ConfigAuditInitializer implements ApplicationRunner {

    private static final String SUPER_ADMIN = "SUPER_ADMIN";
    private static final String ADMIN_USERNAME = "admin";

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("sql/config_audit.sql"));
        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(dataSource);

        Long rootId = ensureMenu(0L, "系统配置与审计", "CATALOG", "config:audit:manage",
                "/config", "Layout", "setting", 1, 1, 30);
        List<Long> menuIds = new ArrayList<>();
        menuIds.add(rootId);

        Long dictId = ensureMenu(rootId, "字典配置", "MENU", "config:dict:list",
                "/config/dict", "config/dict/index", "menu", 1, 1, 1);
        menuIds.add(dictId);
        menuIds.add(ensureButton(dictId, "新增字典", "config:dict:add", 1));
        menuIds.add(ensureButton(dictId, "编辑字典", "config:dict:update", 2));
        menuIds.add(ensureButton(dictId, "删除字典", "config:dict:delete", 3));
        menuIds.add(ensureButton(dictId, "字典状态", "config:dict:status", 4));

        Long flowId = ensureMenu(rootId, "流程配置", "MENU", "config:flow:list",
                "/config/flow", "config/flow/index", "share", 1, 1, 2);
        menuIds.add(flowId);
        menuIds.add(ensureButton(flowId, "新增流程", "config:flow:add", 1));
        menuIds.add(ensureButton(flowId, "编辑流程", "config:flow:update", 2));
        menuIds.add(ensureButton(flowId, "删除流程", "config:flow:delete", 3));
        menuIds.add(ensureButton(flowId, "流程状态", "config:flow:status", 4));
        menuIds.add(ensureButton(flowId, "节点配置", "config:flow:nodes", 5));

        Long logId = ensureMenu(rootId, "操作日志", "MENU", "audit:operation-log:list",
                "/config/log", "config/log/index", "document", 1, 1, 3);
        menuIds.add(logId);
        menuIds.add(ensureButton(logId, "日志详情", "audit:operation-log:detail", 1));
        menuIds.add(ensureButton(logId, "导出日志", "audit:operation-log:export", 2));
        menuIds.add(ensureButton(logId, "删除日志", "audit:operation-log:delete", 3));

        Long backupId = ensureMenu(rootId, "数据备份", "MENU", "config:backup:list",
                "/config/backup", "config/backup/index", "folder", 1, 1, 4);
        menuIds.add(backupId);
        menuIds.add(ensureButton(backupId, "创建备份", "config:backup:create", 1));
        menuIds.add(ensureButton(backupId, "下载备份", "config:backup:download", 2));
        menuIds.add(ensureButton(backupId, "删除备份", "config:backup:delete", 3));

        grantAdminPermissions(menuIds);
    }

    private Long ensureButton(Long parentId, String menuName, String permissionCode, Integer sort) {
        return ensureMenu(parentId, menuName, "BUTTON", permissionCode, null, null, null, 0, 1, sort);
    }

    private Long ensureMenu(Long parentId, String menuName, String menuType, String permissionCode, String path,
                            String component, String icon, Integer visible, Integer status, Integer sort) {
        Long existingId = findMenuId(permissionCode);
        if (existingId != null) {
            jdbcTemplate.update("""
                    UPDATE sys_menu
                    SET parent_id = ?, menu_name = ?, menu_type = ?, path = ?, component = ?, icon = ?,
                        visible = ?, status = ?, sort = ?, update_time = NOW()
                    WHERE id = ? AND deleted = 0
                    """, parentId, menuName, menuType, path, component, icon, visible, status, sort, existingId);
            return existingId;
        }
        jdbcTemplate.update("""
                INSERT INTO sys_menu
                    (parent_id, menu_name, menu_type, permission_code, path, component, icon, visible, status, sort)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, parentId, menuName, menuType, permissionCode, path, component, icon, visible, status, sort);
        return findMenuId(permissionCode);
    }

    private Long findMenuId(String permissionCode) {
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM sys_menu WHERE permission_code = ? AND deleted = 0 ORDER BY id ASC LIMIT 1",
                Long.class,
                permissionCode
        );
        return ids.isEmpty() ? null : ids.get(0);
    }

    private void grantAdminPermissions(List<Long> menuIds) {
        Long adminUserId = findUserId();
        Long superAdminRoleId = findRoleId();
        if (adminUserId == null || superAdminRoleId == null) {
            return;
        }
        jdbcTemplate.update("""
                INSERT INTO sys_user_role (user_id, role_id)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE deleted = 0, update_time = NOW()
                """, adminUserId, superAdminRoleId);
        for (Long menuId : menuIds) {
            jdbcTemplate.update("""
                    INSERT INTO sys_role_menu (role_id, menu_id)
                    VALUES (?, ?)
                    ON DUPLICATE KEY UPDATE deleted = 0, update_time = NOW()
                    """, superAdminRoleId, menuId);
        }
    }

    private Long findUserId() {
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM sys_user WHERE username = ? AND deleted = 0 ORDER BY id ASC LIMIT 1",
                Long.class,
                ADMIN_USERNAME
        );
        return ids.isEmpty() ? null : ids.get(0);
    }

    private Long findRoleId() {
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM sys_role WHERE role_code = ? AND deleted = 0 ORDER BY id ASC LIMIT 1",
                Long.class,
                SUPER_ADMIN
        );
        return ids.isEmpty() ? null : ids.get(0);
    }
}
