/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.vo
 * @className: MenuVO
 * @description: 菜单权限树响应视图对象
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
import java.util.ArrayList;
import java.util.List;

@Data
public class MenuVO {
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
    private List<MenuVO> children = new ArrayList<>();
}
