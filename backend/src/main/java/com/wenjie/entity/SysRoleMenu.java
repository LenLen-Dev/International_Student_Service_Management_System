/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.entity
 * @className: SysRoleMenu
 * @description: 角色菜单权限关联实体
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysRoleMenu {
    private Long id;
    private Long roleId;
    private Long menuId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
