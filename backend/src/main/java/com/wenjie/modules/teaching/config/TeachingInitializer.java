package com.wenjie.modules.teaching.config;

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
@Order(60)
@RequiredArgsConstructor
public class TeachingInitializer implements ApplicationRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("sql/teaching.sql"));
        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(dataSource);

        ensureRole("ACADEMIC_ADMIN", "教务管理员", "教务与成绩管理业务管理员", 9);
        ensureRole("TEACHER", "教师", "教师角色", 15);
        ensureRole("STUDENT", "留学生", "留学生角色", 20);

        Long rootId = ensureMenu(0L, "教务与成绩管理", "CATALOG", "teaching:manage",
                "/teaching", "Layout", "reading", 1, 1, 50);
        Long courseId = ensureMenu(rootId, "课程信息", "MENU", "teaching:course:list",
                "/teaching/course", "teaching/course/index", "collection", 1, 1, 1);
        Long offeringId = ensureMenu(rootId, "开课管理", "MENU", "teaching:offering:list",
                "/teaching/offering", "teaching/offering/index", "school", 1, 1, 2);
        Long selectionId = ensureMenu(rootId, "学生选课", "MENU", "teaching:selection:available",
                "/teaching/selection", "teaching/selection/index", "checked", 1, 1, 3);
        Long gradeId = ensureMenu(rootId, "成绩管理", "MENU", "teaching:grade:list",
                "/teaching/grade", "teaching/grade/index", "tickets", 1, 1, 4);
        Long attendanceId = ensureMenu(rootId, "出勤管理", "MENU", "teaching:attendance:list",
                "/teaching/attendance", "teaching/attendance/index", "calendar", 1, 1, 5);
        Long alertId = ensureMenu(rootId, "学业预警", "MENU", "teaching:alert:list",
                "/teaching/alert", "teaching/alert/index", "warning", 1, 1, 6);
        Long myId = ensureMenu(rootId, "我的教务", "MENU", "teaching:my:view",
                "/teaching/my", "teaching/my/index", "user", 1, 1, 7);

        List<Long> manageMenus = new ArrayList<>();
        manageMenus.add(rootId);
        manageMenus.add(courseId);
        manageMenus.add(offeringId);
        manageMenus.add(selectionId);
        manageMenus.add(gradeId);
        manageMenus.add(attendanceId);
        manageMenus.add(alertId);
        manageMenus.add(myId);
        manageMenus.add(ensureButton(courseId, "新增课程", "teaching:course:add", 1));
        manageMenus.add(ensureButton(courseId, "编辑课程", "teaching:course:update", 2));
        manageMenus.add(ensureButton(courseId, "删除课程", "teaching:course:delete", 3));
        manageMenus.add(ensureButton(courseId, "课程启停", "teaching:course:status", 4));
        manageMenus.add(ensureButton(offeringId, "新增教学班", "teaching:offering:add", 1));
        manageMenus.add(ensureButton(offeringId, "编辑教学班", "teaching:offering:update", 2));
        manageMenus.add(ensureButton(offeringId, "删除教学班", "teaching:offering:delete", 3));
        manageMenus.add(ensureButton(offeringId, "教学班状态", "teaching:offering:status", 4));
        manageMenus.add(ensureButton(offeringId, "我的开课", "teaching:offering:my", 5));
        manageMenus.add(ensureButton(selectionId, "我的选课", "teaching:selection:my", 1));
        manageMenus.add(ensureButton(selectionId, "选课", "teaching:selection:select", 2));
        manageMenus.add(ensureButton(selectionId, "退课", "teaching:selection:drop", 3));
        manageMenus.add(ensureButton(gradeId, "编辑成绩", "teaching:grade:update", 1));
        manageMenus.add(ensureButton(gradeId, "导入成绩", "teaching:grade:import", 2));
        manageMenus.add(ensureButton(gradeId, "发布成绩", "teaching:grade:publish", 3));
        manageMenus.add(ensureButton(gradeId, "我的成绩", "teaching:grade:my", 4));
        manageMenus.add(ensureButton(attendanceId, "保存出勤", "teaching:attendance:save", 1));
        manageMenus.add(ensureButton(attendanceId, "我的出勤", "teaching:attendance:my", 2));
        manageMenus.add(ensureButton(alertId, "生成预警", "teaching:alert:generate", 1));
        manageMenus.add(ensureButton(alertId, "处理预警", "teaching:alert:handle", 2));
        manageMenus.add(ensureButton(alertId, "我的预警", "teaching:alert:my", 3));

        List<Long> teacherMenus = new ArrayList<>();
        teacherMenus.add(rootId);
        teacherMenus.add(offeringId);
        teacherMenus.add(gradeId);
        teacherMenus.add(attendanceId);
        teacherMenus.add(findMenuId("teaching:offering:list"));
        teacherMenus.add(findMenuId("teaching:offering:add"));
        teacherMenus.add(findMenuId("teaching:offering:update"));
        teacherMenus.add(findMenuId("teaching:offering:delete"));
        teacherMenus.add(findMenuId("teaching:offering:status"));
        teacherMenus.add(findMenuId("teaching:offering:my"));
        teacherMenus.add(findMenuId("teaching:grade:list"));
        teacherMenus.add(findMenuId("teaching:grade:update"));
        teacherMenus.add(findMenuId("teaching:grade:import"));
        teacherMenus.add(findMenuId("teaching:grade:publish"));
        teacherMenus.add(findMenuId("teaching:attendance:list"));
        teacherMenus.add(findMenuId("teaching:attendance:save"));

        List<Long> studentMenus = new ArrayList<>();
        studentMenus.add(rootId);
        studentMenus.add(selectionId);
        studentMenus.add(myId);
        studentMenus.add(findMenuId("teaching:selection:available"));
        studentMenus.add(findMenuId("teaching:selection:my"));
        studentMenus.add(findMenuId("teaching:selection:select"));
        studentMenus.add(findMenuId("teaching:selection:drop"));
        studentMenus.add(findMenuId("teaching:grade:my"));
        studentMenus.add(findMenuId("teaching:attendance:my"));
        studentMenus.add(findMenuId("teaching:alert:my"));

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
