/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.service.impl
 * @className: RoleServiceImpl
 * @description: 角色管理业务实现
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
import com.wenjie.modules.system.dto.AssignMenusRequest;
import com.wenjie.modules.system.dto.RoleRequest;
import com.wenjie.modules.system.entity.SysRole;
import com.wenjie.modules.system.mapper.SysRoleMapper;
import com.wenjie.modules.system.mapper.SysRoleMenuMapper;
import com.wenjie.modules.system.service.RoleService;
import com.wenjie.modules.system.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final String SUPER_ADMIN = "SUPER_ADMIN";

    private final SysRoleMapper roleMapper;
    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<RoleVO> list(String keyword, Integer status) {
        return roleMapper.selectList(keyword, status).stream().map(this::toVO).toList();
    }

    @Override
    public Long create(RoleRequest request) {
        if (roleMapper.selectByCode(request.getRoleCode()) != null) {
            throw new BusinessException("角色编码已存在");
        }
        SysRole role = toEntity(null, request);
        roleMapper.insert(role);
        return role.getId();
    }

    @Override
    public void update(Long id, RoleRequest request) {
        SysRole existing = ensureExists(id);
        if (SUPER_ADMIN.equals(existing.getRoleCode()) && !SUPER_ADMIN.equals(request.getRoleCode())) {
            throw new BusinessException("超级管理员角色编码不可修改");
        }
        SysRole sameCode = roleMapper.selectByCode(request.getRoleCode());
        if (sameCode != null && !sameCode.getId().equals(id)) {
            throw new BusinessException("角色编码已存在");
        }
        roleMapper.update(toEntity(id, request));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysRole role = ensureExists(id);
        if (SUPER_ADMIN.equals(role.getRoleCode())) {
            throw new BusinessException("超级管理员角色不可删除");
        }
        roleMapper.logicalDelete(id);
        roleMenuMapper.deleteByRoleId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long id, AssignMenusRequest request) {
        ensureExists(id);
        roleMenuMapper.deleteByRoleId(id);
        if (!request.getMenuIds().isEmpty()) {
            roleMenuMapper.insertBatch(id, request.getMenuIds());
        }
    }

    private SysRole ensureExists(Long id) {
        SysRole role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        return role;
    }

    private SysRole toEntity(Long id, RoleRequest request) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setStatus(request.getStatus());
        role.setSort(request.getSort() == null ? 0 : request.getSort());
        return role;
    }

    private RoleVO toVO(SysRole role) {
        RoleVO vo = new RoleVO();
        vo.setId(role.getId());
        vo.setRoleCode(role.getRoleCode());
        vo.setRoleName(role.getRoleName());
        vo.setDescription(role.getDescription());
        vo.setStatus(role.getStatus());
        vo.setSort(role.getSort());
        vo.setCreateTime(role.getCreateTime());
        vo.setUpdateTime(role.getUpdateTime());
        vo.setMenuIds(roleMenuMapper.selectMenuIdsByRoleId(role.getId()));
        return vo;
    }
}
