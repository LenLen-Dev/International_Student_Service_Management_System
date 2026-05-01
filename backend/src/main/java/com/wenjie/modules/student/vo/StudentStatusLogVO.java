/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.vo
 * @className: StudentStatusLogVO
 * @description: TODO 留学生状态变更日志视图对象
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
public class StudentStatusLogVO {
    private Long id;
    private Long studentId;
    private String oldStatus;
    private String newStatus;
    private String changeReason;
    private Long operatorId;
    private String operatorName;
    private String remark;
    private LocalDateTime createTime;
}
