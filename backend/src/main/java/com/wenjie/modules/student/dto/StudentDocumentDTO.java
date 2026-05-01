/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.dto
 * @className: StudentDocumentDTO
 * @description: TODO 留学生附件材料参数
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
public class StudentDocumentDTO {
    @NotBlank(message = "不能为空")
    private String documentType;
    @NotBlank(message = "不能为空")
    private String documentName;
    @NotBlank(message = "不能为空")
    private String fileUrl;
    private Long fileSize;
    private String mimeType;
    private String reviewStatus = "PENDING";
    private String remark;
}
