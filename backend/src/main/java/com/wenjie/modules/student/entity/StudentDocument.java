/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.entity
 * @className: StudentDocument
 * @description: TODO 留学生附件材料实体
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentDocument {
    /** 主键ID */
    private Long id;
    /** 留学生档案ID */
    private Long studentId;
    /** 材料类型 */
    private String documentType;
    /** 材料名称 */
    private String documentName;
    /** 文件地址 */
    private String fileUrl;
    /** 文件大小 */
    private Long fileSize;
    /** 文件类型 */
    private String mimeType;
    /** 审核状态 */
    private String reviewStatus;
    /** 备注 */
    private String remark;
    /** 逻辑删除 */
    private Integer deleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
