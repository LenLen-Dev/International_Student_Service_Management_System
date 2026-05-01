/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.service.impl
 * @className: MenuTreeBuilder
 * @description: 菜单权限树构建工具
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.service.impl;

import com.wenjie.modules.system.entity.SysMenu;
import com.wenjie.modules.system.vo.MenuVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class MenuTreeBuilder {

    private MenuTreeBuilder() {
    }

    static List<MenuVO> build(List<SysMenu> menus) {
        Map<Long, MenuVO> voMap = new LinkedHashMap<>();
        menus.stream()
                .sorted(Comparator.comparing(SysMenu::getSort, Comparator.nullsLast(Integer::compareTo)))
                .forEach(menu -> voMap.put(menu.getId(), toVO(menu)));
        List<MenuVO> roots = new ArrayList<>();
        voMap.values().forEach(menu -> {
            if (menu.getParentId() == null || menu.getParentId() == 0 || !voMap.containsKey(menu.getParentId())) {
                roots.add(menu);
            } else {
                voMap.get(menu.getParentId()).getChildren().add(menu);
            }
        });
        sortTree(roots);
        return roots;
    }

    static MenuVO toVO(SysMenu menu) {
        MenuVO vo = new MenuVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setMenuName(menu.getMenuName());
        vo.setMenuType(menu.getMenuType());
        vo.setPermissionCode(menu.getPermissionCode());
        vo.setPath(menu.getPath());
        vo.setComponent(menu.getComponent());
        vo.setIcon(menu.getIcon());
        vo.setVisible(menu.getVisible());
        vo.setStatus(menu.getStatus());
        vo.setSort(menu.getSort());
        vo.setCreateTime(menu.getCreateTime());
        vo.setUpdateTime(menu.getUpdateTime());
        return vo;
    }

    private static void sortTree(List<MenuVO> menus) {
        menus.sort(Comparator.comparing(MenuVO::getSort, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(MenuVO::getId, Comparator.nullsLast(Long::compareTo)));
        menus.forEach(menu -> {
            if (Objects.nonNull(menu.getChildren())) {
                sortTree(menu.getChildren());
            }
        });
    }
}
