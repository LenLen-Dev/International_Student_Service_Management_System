/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.entity
 * @className: SysMenu
 * @description: 系统菜单与按钮权限实体
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
public class SysMenu {
    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;
    private String permissionCode;
    private String path;
    private String component;
    private String icon;
    private Integer visible;
    private Integer status;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
