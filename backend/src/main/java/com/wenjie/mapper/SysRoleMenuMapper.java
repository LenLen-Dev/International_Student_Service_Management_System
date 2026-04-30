/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.mapper
 * @className: SysRoleMenuMapper
 * @description: 角色菜单关系数据访问接口
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
public interface SysRoleMenuMapper {
    int deleteByRoleId(@Param("roleId") Long roleId);
    int insertBatch(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
