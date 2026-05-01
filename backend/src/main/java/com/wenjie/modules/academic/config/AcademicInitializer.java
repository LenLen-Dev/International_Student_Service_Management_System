package com.wenjie.modules.academic.config;

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
@Order(50)
@RequiredArgsConstructor
public class AcademicInitializer implements ApplicationRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("sql/academic.sql"));
        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(dataSource);

        ensureRole("ACADEMIC_ADMIN", "教务管理员", "学籍与在校管理业务管理员", 9);
        ensureRole("TEACHER", "教师", "教师角色", 15);
        ensureRole("STUDENT", "留学生", "留学生角色", 20);

        Long rootId = ensureMenu(0L, "学籍与在校管理", "CATALOG", "academic:manage",
                "/academic", "Layout", "school", 1, 1, 40);
        Long majorId = ensureMenu(rootId, "专业管理", "MENU", "academic:major:list",
                "/academic/major", "academic/major/index", "collection", 1, 1, 1);
        Long classId = ensureMenu(rootId, "年级班级", "MENU", "academic:class:list",
                "/academic/class", "academic/class/index", "grid", 1, 1, 2);
        Long recordId = ensureMenu(rootId, "学籍信息", "MENU", "academic:record:list",
                "/academic/record", "academic/record/index", "tickets", 1, 1, 3);
        Long leaveId = ensureMenu(rootId, "请假管理", "MENU", "academic:leave:list",
                "/academic/leave", "academic/leave/index", "calendar", 1, 1, 4);
        Long changeId = ensureMenu(rootId, "学籍异动", "MENU", "academic:change:list",
                "/academic/change", "academic/change/index", "switch", 1, 1, 5);
        Long myId = ensureMenu(rootId, "我的学籍", "MENU", "academic:my:view",
                "/academic/my", "academic/my/index", "user", 1, 1, 6);

        List<Long> manageMenus = new ArrayList<>();
        manageMenus.add(rootId);
        manageMenus.add(majorId);
        manageMenus.add(classId);
        manageMenus.add(recordId);
        manageMenus.add(leaveId);
        manageMenus.add(changeId);
        manageMenus.add(ensureButton(majorId, "新增专业", "academic:major:add", 1));
        manageMenus.add(ensureButton(majorId, "编辑专业", "academic:major:update", 2));
        manageMenus.add(ensureButton(majorId, "删除专业", "academic:major:delete", 3));
        manageMenus.add(ensureButton(majorId, "专业启停", "academic:major:status", 4));
        manageMenus.add(ensureButton(classId, "年级列表", "academic:grade:list", 1));
        manageMenus.add(ensureButton(classId, "新增年级", "academic:grade:add", 2));
        manageMenus.add(ensureButton(classId, "编辑年级", "academic:grade:update", 3));
        manageMenus.add(ensureButton(classId, "删除年级", "academic:grade:delete", 4));
        manageMenus.add(ensureButton(classId, "新增班级", "academic:class:add", 5));
        manageMenus.add(ensureButton(classId, "编辑班级", "academic:class:update", 6));
        manageMenus.add(ensureButton(classId, "删除班级", "academic:class:delete", 7));
        manageMenus.add(ensureButton(recordId, "学籍详情", "academic:record:detail", 1));
        manageMenus.add(ensureButton(recordId, "新增学籍", "academic:record:add", 2));
        manageMenus.add(ensureButton(recordId, "编辑学籍", "academic:record:update", 3));
        manageMenus.add(ensureButton(recordId, "调整学籍状态", "academic:record:status", 4));
        manageMenus.add(ensureButton(leaveId, "请假详情", "academic:leave:detail", 1));
        manageMenus.add(ensureButton(leaveId, "审批请假", "academic:leave:approve", 2));
        manageMenus.add(ensureButton(leaveId, "拒绝请假", "academic:leave:reject", 3));
        manageMenus.add(ensureButton(changeId, "异动详情", "academic:change:detail", 1));
        manageMenus.add(ensureButton(changeId, "新增异动", "academic:change:add", 2));
        manageMenus.add(ensureButton(changeId, "编辑异动", "academic:change:update", 3));
        manageMenus.add(ensureButton(changeId, "审批异动", "academic:change:approve", 4));
        manageMenus.add(ensureButton(changeId, "拒绝异动", "academic:change:reject", 5));

        List<Long> teacherMenus = new ArrayList<>();
        teacherMenus.add(rootId);
        teacherMenus.add(recordId);
        teacherMenus.add(leaveId);
        teacherMenus.add(findMenuId("academic:record:list"));
        teacherMenus.add(findMenuId("academic:record:detail"));
        teacherMenus.add(findMenuId("academic:leave:list"));
        teacherMenus.add(findMenuId("academic:leave:detail"));
        teacherMenus.add(findMenuId("academic:leave:approve"));
        teacherMenus.add(findMenuId("academic:leave:reject"));

        List<Long> studentMenus = new ArrayList<>();
        studentMenus.add(rootId);
        studentMenus.add(myId);
        studentMenus.add(ensureButton(myId, "我的请假列表", "academic:leave:my", 1));
        studentMenus.add(ensureButton(myId, "提交请假", "academic:leave:apply", 2));
        studentMenus.add(ensureButton(myId, "撤回请假", "academic:leave:cancel", 3));

        grantRoleMenus("SUPER_ADMIN", merge(manageMenus, studentMenus));
        grantRoleMenus("ACADEMIC_ADMIN", manageMenus);
        grantRoleMenus("TEACHER", teacherMenus);
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
