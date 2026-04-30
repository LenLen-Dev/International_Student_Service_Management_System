/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.vo
 * @className: RoleVO
 * @description: 角色响应视图对象
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleVO {
    private Long id;
    private String roleCode;
    private String roleName;
    private String description;
    private Integer status;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<Long> menuIds;
}
