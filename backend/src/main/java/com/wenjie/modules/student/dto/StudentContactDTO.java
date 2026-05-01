/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.dto
 * @className: StudentContactDTO
 * @description: TODO 留学生联系人参数
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StudentContactDTO {
    @NotBlank(message = "不能为空")
    private String contactType;
    @NotBlank(message = "不能为空")
    private String contactName;
    private String relationship;
    @Pattern(regexp = "^$|^[0-9+\\-()\\s]{6,32}$", message = "格式不正确")
    private String phone;
    @Email(message = "格式不正确")
    private String email;
    private String address;
    private Integer isPrimary = 0;
}
