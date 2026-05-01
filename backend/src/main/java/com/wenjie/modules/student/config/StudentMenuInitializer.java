/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.config
 * @className: StudentMenuInitializer
 * @description: 留学生档案菜单权限初始化组件
 * @author: Wenjie
 * @createDate: 2026-05-01 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-05-01 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentMenuInitializer implements ApplicationRunner {

    private static final String SUPER_ADMIN = "SUPER_ADMIN";
    private static final String ADMIN_USERNAME = "admin";

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) {
        Long studentRootId = ensureMenu(0L, "留学生管理", "CATALOG", "student:manage",
                "/student", "Layout", "school", 1, 1, 20);
        Long profileMenuId = ensureMenu(studentRootId, "留学生档案", "MENU", "student:profile:list",
                "/student/profiles", "student/profile/index", "user", 1, 1, 1);

        List<Long> studentMenuIds = new ArrayList<>();
        studentMenuIds.add(studentRootId);
        studentMenuIds.add(profileMenuId);
        studentMenuIds.add(ensureMenu(profileMenuId, "新增档案", "BUTTON", "student:profile:add", null, null, null, 0, 1, 1));
        studentMenuIds.add(ensureMenu(profileMenuId, "编辑档案", "BUTTON", "student:profile:update", null, null, null, 0, 1, 2));
        studentMenuIds.add(ensureMenu(profileMenuId, "删除档案", "BUTTON", "student:profile:delete", null, null, null, 0, 1, 3));
        studentMenuIds.add(ensureMenu(profileMenuId, "档案详情", "BUTTON", "student:profile:detail", null, null, null, 0, 1, 4));
        studentMenuIds.add(ensureMenu(profileMenuId, "状态变更", "BUTTON", "student:profile:status", null, null, null, 0, 1, 5));
        studentMenuIds.add(ensureMenu(profileMenuId, "联系人列表", "BUTTON", "student:contact:list", null, null, null, 0, 1, 6));
        studentMenuIds.add(ensureMenu(profileMenuId, "新增联系人", "BUTTON", "student:contact:add", null, null, null, 0, 1, 7));
        studentMenuIds.add(ensureMenu(profileMenuId, "编辑联系人", "BUTTON", "student:contact:update", null, null, null, 0, 1, 8));
        studentMenuIds.add(ensureMenu(profileMenuId, "删除联系人", "BUTTON", "student:contact:delete", null, null, null, 0, 1, 9));
        studentMenuIds.add(ensureMenu(profileMenuId, "教育背景列表", "BUTTON", "student:education:list", null, null, null, 0, 1, 10));
        studentMenuIds.add(ensureMenu(profileMenuId, "新增教育背景", "BUTTON", "student:education:add", null, null, null, 0, 1, 11));
        studentMenuIds.add(ensureMenu(profileMenuId, "编辑教育背景", "BUTTON", "student:education:update", null, null, null, 0, 1, 12));
        studentMenuIds.add(ensureMenu(profileMenuId, "删除教育背景", "BUTTON", "student:education:delete", null, null, null, 0, 1, 13));
        studentMenuIds.add(ensureMenu(profileMenuId, "材料列表", "BUTTON", "student:document:list", null, null, null, 0, 1, 14));
        studentMenuIds.add(ensureMenu(profileMenuId, "新增材料", "BUTTON", "student:document:add", null, null, null, 0, 1, 15));
        studentMenuIds.add(ensureMenu(profileMenuId, "编辑材料", "BUTTON", "student:document:update", null, null, null, 0, 1, 16));
        studentMenuIds.add(ensureMenu(profileMenuId, "删除材料", "BUTTON", "student:document:delete", null, null, null, 0, 1, 17));
        studentMenuIds.add(ensureMenu(profileMenuId, "状态日志列表", "BUTTON", "student:status-log:list", null, null, null, 0, 1, 18));

        grantAdminPermissions(studentMenuIds);
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
        Long adminUserId = findUserId(ADMIN_USERNAME);
        Long superAdminRoleId = findRoleId(SUPER_ADMIN);
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

    private Long findUserId(String username) {
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM sys_user WHERE username = ? AND deleted = 0 ORDER BY id ASC LIMIT 1",
                Long.class,
                username
        );
        return ids.isEmpty() ? null : ids.get(0);
    }

    private Long findRoleId(String roleCode) {
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM sys_role WHERE role_code = ? AND deleted = 0 ORDER BY id ASC LIMIT 1",
                Long.class,
                roleCode
        );
        return ids.isEmpty() ? null : ids.get(0);
    }
}
