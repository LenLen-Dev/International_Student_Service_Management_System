/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.vo
 * @className: StudentEducationVO
 * @description: TODO 留学生教育背景视图对象
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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentEducationVO {
    private Long id;
    private Long studentId;
    private String schoolName;
    private String country;
    private String degreeLevel;
    private String major;
    private LocalDate startDate;
    private LocalDate endDate;
    private String certificateUrl;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
