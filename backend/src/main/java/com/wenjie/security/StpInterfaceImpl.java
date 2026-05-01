/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.security
 * @className: StpInterfaceImpl
 * @description: Sa-Token角色与权限加载实现
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.security;

import cn.dev33.satoken.stp.StpInterface;
import com.wenjie.modules.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final SysUserMapper userMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return userMapper.selectPermissionsByUserId(Long.valueOf(String.valueOf(loginId)));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userMapper.selectRoleCodesByUserId(Long.valueOf(String.valueOf(loginId)));
    }
}
