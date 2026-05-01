/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.entity
 * @className: SysUserRole
 * @description: 用户角色关联实体
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysUserRole {
    private Long id;
    private Long userId;
    private Long roleId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
