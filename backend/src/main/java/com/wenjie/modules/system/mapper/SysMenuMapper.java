/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.mapper
 * @className: SysMenuMapper
 * @description: 系统菜单权限数据访问接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.mapper;

import com.wenjie.modules.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    SysMenu selectById(@Param("id") Long id);
    List<SysMenu> selectList(@Param("keyword") String keyword, @Param("status") Integer status);
    int insert(SysMenu menu);
    int update(SysMenu menu);
    int logicalDelete(@Param("id") Long id);
}
