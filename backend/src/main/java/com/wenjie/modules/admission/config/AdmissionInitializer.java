package com.wenjie.modules.admission.config;

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
@Order(30)
@RequiredArgsConstructor
public class AdmissionInitializer implements ApplicationRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("sql/admission.sql"));
        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(dataSource);

        ensureRole("APPLICANT", "申请人", "招生申请人角色", 7);

        Long rootId = ensureMenu(0L, "招生申请管理", "CATALOG", "admission:manage",
                "/admission", "Layout", "document", 1, 1, 10);
        Long myId = ensureMenu(rootId, "我的申请", "MENU", "admission:application:my",
                "/admission/my-application", "admission/my-application/index", "user", 1, 1, 1);
        Long reviewId = ensureMenu(rootId, "申请审核", "MENU", "admission:application:list",
                "/admission/review", "admission/review/index", "tickets", 1, 1, 2);

        List<Long> applicantMenus = new ArrayList<>();
        applicantMenus.add(rootId);
        applicantMenus.add(myId);
        applicantMenus.add(ensureButton(myId, "创建申请", "admission:application:create", 1));
        applicantMenus.add(ensureButton(myId, "编辑申请", "admission:application:update", 2));
        applicantMenus.add(ensureButton(myId, "提交申请", "admission:application:submit", 3));
        applicantMenus.add(ensureButton(myId, "上传材料", "admission:material:upload", 4));
        applicantMenus.add(ensureButton(myId, "删除材料", "admission:material:delete", 5));
        applicantMenus.add(ensureButton(myId, "下载通知书", "admission:notice:download", 6));

        List<Long> reviewMenus = new ArrayList<>();
        reviewMenus.add(rootId);
        reviewMenus.add(reviewId);
        reviewMenus.add(ensureButton(reviewId, "申请详情", "admission:application:detail", 1));
        reviewMenus.add(ensureButton(reviewId, "退回申请", "admission:application:return", 2));
        reviewMenus.add(ensureButton(reviewId, "拒绝申请", "admission:application:reject", 3));
        reviewMenus.add(ensureButton(reviewId, "录取申请", "admission:application:admit", 4));
        reviewMenus.add(ensureButton(reviewId, "审核材料", "admission:material:review", 5));
        reviewMenus.add(ensureButton(reviewId, "生成通知书", "admission:notice:generate", 6));
        reviewMenus.add(findMenuId("admission:notice:download"));

        grantRoleMenus("APPLICANT", applicantMenus);
        grantRoleMenus("ADMISSION_ADMIN", reviewMenus);
        List<Long> superMenus = new ArrayList<>();
        superMenus.addAll(applicantMenus);
        reviewMenus.stream().filter(id -> !superMenus.contains(id)).forEach(superMenus::add);
        grantRoleMenus("SUPER_ADMIN", superMenus);
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
}
