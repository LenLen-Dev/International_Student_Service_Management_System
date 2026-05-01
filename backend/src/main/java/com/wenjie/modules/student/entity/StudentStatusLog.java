/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.entity
 * @className: StudentStatusLog
 * @description: TODO 留学生状态变更日志实体
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
public class StudentStatusLog {
    /** 主键ID */
    private Long id;
    /** 留学生档案ID */
    private Long studentId;
    /** 原状态 */
    private String oldStatus;
    /** 新状态 */
    private String newStatus;
    /** 变更原因 */
    private String changeReason;
    /** 操作人ID */
    private Long operatorId;
    /** 操作人名称 */
    private String operatorName;
    /** 备注 */
    private String remark;
    /** 逻辑删除 */
    private Integer deleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
