/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.service.impl
 * @className: UserServiceImpl
 * @description: 用户管理业务实现
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.dto.AssignRolesRequest;
import com.wenjie.dto.UserCreateRequest;
import com.wenjie.dto.UserStatusRequest;
import com.wenjie.dto.UserUpdateRequest;
import com.wenjie.entity.SysUser;
import com.wenjie.mapper.SysUserMapper;
import com.wenjie.mapper.SysUserRoleMapper;
import com.wenjie.service.UserService;
import com.wenjie.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    @Override
    public List<UserVO> list(String keyword, String userType, Integer status) {
        return userMapper.selectList(keyword, userType, status).stream().map(this::toVO).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(UserCreateRequest request) {
        if (userMapper.selectByUsername(request.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUserType(request.getUserType());
        user.setStatus(request.getStatus());
        userMapper.insert(user);
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            userRoleMapper.insertBatch(user.getId(), request.getRoleIds());
        }
        return user.getId();
    }

    @Override
    public void update(Long id, UserUpdateRequest request) {
        ensureExists(id);
        SysUser user = new SysUser();
        user.setId(id);
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUserType(request.getUserType());
        user.setStatus(request.getStatus());
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(request.getPassword());
        }
        userMapper.update(user);
    }

    @Override
    public void delete(Long id) {
        ensureExists(id);
        userMapper.logicalDelete(id);
        userRoleMapper.deleteByUserId(id);
    }

    @Override
    public void updateStatus(Long id, UserStatusRequest request) {
        ensureExists(id);
        userMapper.updateStatus(id, request.getStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long id, AssignRolesRequest request) {
        ensureExists(id);
        userRoleMapper.deleteByUserId(id);
        if (!request.getRoleIds().isEmpty()) {
            userRoleMapper.insertBatch(id, request.getRoleIds());
        }
    }

    private void ensureExists(Long id) {
        if (userMapper.selectById(id) == null) {
            throw new BusinessException(404, "用户不存在");
        }
    }

    private UserVO toVO(SysUser user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setUserType(user.getUserType());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateTime(user.getUpdateTime());
        vo.setRoles(userMapper.selectRoleCodesByUserId(user.getId()));
        vo.setRoleIds(userRoleMapper.selectRoleIdsByUserId(user.getId()));
        return vo;
    }
}
