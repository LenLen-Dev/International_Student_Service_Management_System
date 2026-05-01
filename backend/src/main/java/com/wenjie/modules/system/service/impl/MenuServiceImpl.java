/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.service.impl
 * @className: MenuServiceImpl
 * @description: 菜单权限管理业务实现
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.modules.system.dto.MenuRequest;
import com.wenjie.modules.system.entity.SysMenu;
import com.wenjie.modules.system.mapper.SysMenuMapper;
import com.wenjie.modules.system.service.MenuService;
import com.wenjie.modules.system.vo.MenuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final SysMenuMapper menuMapper;

    @Override
    public List<MenuVO> tree(String keyword, Integer status) {
        return MenuTreeBuilder.build(menuMapper.selectList(keyword, status));
    }

    @Override
    public Long create(MenuRequest request) {
        SysMenu menu = toEntity(null, request);
        menuMapper.insert(menu);
        return menu.getId();
    }

    @Override
    public void update(Long id, MenuRequest request) {
        ensureExists(id);
        menuMapper.update(toEntity(id, request));
    }

    @Override
    public void delete(Long id) {
        ensureExists(id);
        menuMapper.logicalDelete(id);
    }

    private void ensureExists(Long id) {
        if (menuMapper.selectById(id) == null) {
            throw new BusinessException(404, "菜单不存在");
        }
    }

    private SysMenu toEntity(Long id, MenuRequest request) {
        SysMenu menu = new SysMenu();
        menu.setId(id);
        menu.setParentId(request.getParentId());
        menu.setMenuName(request.getMenuName());
        menu.setMenuType(request.getMenuType());
        menu.setPermissionCode(request.getPermissionCode());
        menu.setPath(request.getPath());
        menu.setComponent(request.getComponent());
        menu.setIcon(request.getIcon());
        menu.setVisible(request.getVisible());
        menu.setStatus(request.getStatus());
        menu.setSort(request.getSort() == null ? 0 : request.getSort());
        return menu;
    }
}
