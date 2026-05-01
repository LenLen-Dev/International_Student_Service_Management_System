/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.service.impl
 * @className: AuthServiceImpl
 * @description: 认证授权业务实现
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.wenjie.common.BusinessException;
import com.wenjie.modules.system.dto.LoginRequest;
import com.wenjie.modules.system.entity.SysMenu;
import com.wenjie.modules.system.entity.SysUser;
import com.wenjie.modules.system.mapper.SysUserMapper;
import com.wenjie.modules.system.service.AuthService;
import com.wenjie.modules.system.vo.LoginVO;
import com.wenjie.modules.system.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    @Override
    public LoginVO login(LoginRequest request) {
        SysUser user = userMapper.selectByUsername(request.getUsername());
        if (user == null || !request.getPassword().equals(user.getPassword())) {
            throw new BusinessException(401, "账号或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(403, "账号已被禁用");
        }
        StpUtil.login(user.getId());
        return new LoginVO(StpUtil.getTokenValue(), "Bearer", StpUtil.getTokenTimeout(), buildUserInfo(user));
    }

    @Override
    public UserInfoVO info() {
        Long userId = Long.valueOf(String.valueOf(StpUtil.getLoginId()));
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return buildUserInfo(user);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    private UserInfoVO buildUserInfo(SysUser user) {
        List<SysMenu> menus = userMapper.selectMenusByUserId(user.getId());
        UserInfoVO vo = new UserInfoVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setUserType(user.getUserType());
        vo.setRoles(userMapper.selectRoleCodesByUserId(user.getId()));
        vo.setPermissions(userMapper.selectPermissionsByUserId(user.getId()));
        vo.setMenus(MenuTreeBuilder.build(menus));
        return vo;
    }
}
