/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.vo
 * @className: StudentDocumentVO
 * @description: TODO 留学生附件材料视图对象
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentDocumentVO {
    private Long id;
    private Long studentId;
    private String documentType;
    private String documentName;
    private String fileUrl;
    private Long fileSize;
    private String mimeType;
    private String reviewStatus;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
