/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.mapper
 * @className: SysUserRoleMapper
 * @description: 用户角色关系数据访问接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {
    int deleteByUserId(@Param("userId") Long userId);
    int insertBatch(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}
