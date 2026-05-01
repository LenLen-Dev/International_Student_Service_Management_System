/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.dto
 * @className: AssignMenusRequest
 * @description: 角色分配菜单权限请求参数
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignMenusRequest {
    @NotNull(message = "不能为空")
    private List<Long> menuIds;
}
