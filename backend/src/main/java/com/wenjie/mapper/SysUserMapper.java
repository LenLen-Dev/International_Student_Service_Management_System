/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.mapper
 * @className: SysUserMapper
 * @description: 系统用户数据访问接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.mapper;

import com.wenjie.entity.SysMenu;
import com.wenjie.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectById(@Param("id") Long id);
    SysUser selectByUsername(@Param("username") String username);
    List<SysUser> selectList(@Param("keyword") String keyword, @Param("userType") String userType, @Param("status") Integer status);
    int insert(SysUser user);
    int update(SysUser user);
    int logicalDelete(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);
}
