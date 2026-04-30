/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.dto
 * @className: MenuRequest
 * @description: 菜单权限新增与修改请求参数
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuRequest {
    @NotNull(message = "不能为空")
    private Long parentId;
    @NotBlank(message = "不能为空")
    private String menuName;
    @NotBlank(message = "不能为空")
    private String menuType;
    private String permissionCode;
    private String path;
    private String component;
    private String icon;
    @NotNull(message = "不能为空")
    private Integer visible;
    @NotNull(message = "不能为空")
    private Integer status;
    private Integer sort;
}
