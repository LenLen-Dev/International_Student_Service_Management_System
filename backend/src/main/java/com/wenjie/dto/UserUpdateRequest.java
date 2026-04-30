/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.dto
 * @className: UserUpdateRequest
 * @description: 修改用户请求参数
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
public class UserUpdateRequest {
    @NotBlank(message = "不能为空")
    private String realName;
    private String email;
    private String phone;
    @NotBlank(message = "不能为空")
    private String userType;
    @NotNull(message = "不能为空")
    private Integer status;
    private String password;
}
