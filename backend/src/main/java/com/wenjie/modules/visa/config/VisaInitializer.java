package com.wenjie.modules.visa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(40)
@RequiredArgsConstructor
public class VisaInitializer implements ApplicationRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("sql/visa.sql"));
        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(dataSource);

        ensureRole("FOREIGN_ADMIN", "外事管理员", "外事与签证居留业务管理员", 8);
        ensureRole("STUDENT", "留学生", "留学生角色", 20);

        Long rootId = ensureMenu(0L, "签证与居留管理", "CATALOG", "visa:manage",
                "/visa", "Layout", "office", 1, 1, 30);
        Long recordId = ensureMenu(rootId, "签证信息", "MENU", "visa:record:list",
                "/visa/record", "visa/record/index", "document", 1, 1, 1);
        Long permitId = ensureMenu(rootId, "居留许可", "MENU", "visa:permit:list",
                "/visa/permit", "visa/permit/index", "folder", 1, 1, 2);
        Long renewalId = ensureMenu(rootId, "续签记录", "MENU", "visa:renewal:list",
                "/visa/renewal", "visa/renewal/index", "edit", 1, 1, 3);
        Long alertId = ensureMenu(rootId, "合规预警", "MENU", "visa:alert:list",
                "/visa/alert", "visa/alert/index", "warning", 1, 1, 4);
        Long myId = ensureMenu(rootId, "我的签证居留", "MENU", "visa:my:view",
                "/visa/my", "visa/my/index", "user", 1, 1, 5);

        List<Long> manageMenus = new ArrayList<>();
        manageMenus.add(rootId);
        manageMenus.add(recordId);
        manageMenus.add(permitId);
        manageMenus.add(renewalId);
        manageMenus.add(alertId);
        manageMenus.add(ensureButton(recordId, "签证详情", "visa:record:detail", 1));
        manageMenus.add(ensureButton(recordId, "新增签证", "visa:record:add", 2));
        manageMenus.add(ensureButton(recordId, "编辑签证", "visa:record:update", 3));
        manageMenus.add(ensureButton(recordId, "删除签证", "visa:record:delete", 4));
        manageMenus.add(ensureButton(permitId, "居留许可详情", "visa:permit:detail", 1));
        manageMenus.add(ensureButton(permitId, "新增居留许可", "visa:permit:add", 2));
        manageMenus.add(ensureButton(permitId, "编辑居留许可", "visa:permit:update", 3));
        manageMenus.add(ensureButton(permitId, "删除居留许可", "visa:permit:delete", 4));
        manageMenus.add(ensureButton(renewalId, "续签详情", "visa:renewal:detail", 1));
        manageMenus.add(ensureButton(renewalId, "新增续签", "visa:renewal:add", 2));
        manageMenus.add(ensureButton(renewalId, "编辑续签", "visa:renewal:update", 3));
        manageMenus.add(ensureButton(renewalId, "删除续签", "visa:renewal:delete", 4));
        manageMenus.add(ensureButton(renewalId, "登记结果", "visa:renewal:result", 5));
        manageMenus.add(ensureButton(alertId, "预警详情", "visa:alert:detail", 1));
        manageMenus.add(ensureButton(alertId, "处理预警", "visa:alert:resolve", 2));
        manageMenus.add(ensureButton(alertId, "生成预警", "visa:alert:generate", 3));
        manageMenus.add(ensureButton(alertId, "发送提醒", "visa:alert:notify", 4));
        manageMenus.add(ensureButton(alertId, "通知列表", "visa:notification:list", 5));
        manageMenus.add(ensureButton(alertId, "标记已读", "visa:notification:read", 6));

        List<Long> studentMenus = new ArrayList<>();
        studentMenus.add(rootId);
        studentMenus.add(myId);
        studentMenus.add(ensureButton(myId, "我的通知", "visa:my:notification", 1));
        studentMenus.add(findMenuId("visa:notification:read"));

        grantRoleMenus("SUPER_ADMIN", merge(manageMenus, studentMenus));
        grantRoleMenus("FOREIGN_ADMIN", manageMenus);
        grantRoleMenus("STUDENT", studentMenus);
    }

    private void ensureRole(String roleCode, String roleName, String description, Integer sort) {
        jdbcTemplate.update("""
                INSERT INTO sys_role (role_code, role_name, description, status, sort)
                SELECT ?, ?, ?, 1, ?
                WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_code = ? AND deleted = 0)
                """, roleCode, roleName, description, sort, roleCode);
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
                INSERT INTO sys_menu (parent_id, menu_name, menu_type, permission_code, path, component, icon, visible, status, sort)
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

    private void grantRoleMenus(String roleCode, List<Long> menuIds) {
        Long roleId = findRoleId(roleCode);
        if (roleId == null) {
            return;
        }
        for (Long menuId : menuIds) {
            if (menuId == null) {
                continue;
            }
            jdbcTemplate.update("""
                    INSERT INTO sys_role_menu (role_id, menu_id)
                    VALUES (?, ?)
                    ON DUPLICATE KEY UPDATE deleted = 0, update_time = NOW()
                    """, roleId, menuId);
        }
    }

    private Long findRoleId(String roleCode) {
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM sys_role WHERE role_code = ? AND deleted = 0 ORDER BY id ASC LIMIT 1",
                Long.class,
                roleCode
        );
        return ids.isEmpty() ? null : ids.get(0);
    }

    private List<Long> merge(List<Long> first, List<Long> second) {
        List<Long> merged = new ArrayList<>(first);
        second.stream().filter(id -> !merged.contains(id)).forEach(merged::add);
        return merged;
    }
}
