/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.dto
 * @className: StudentStatusUpdateDTO
 * @description: TODO 留学生状态修改参数
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentStatusUpdateDTO {
    @NotBlank(message = "不能为空")
    private String newStatus;
    private String changeReason;
    private String remark;
}
