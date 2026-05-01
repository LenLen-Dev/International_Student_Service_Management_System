/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.vo
 * @className: StudentProfileListVO
 * @description: TODO 留学生档案列表视图对象
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
public class StudentProfileListVO {
    private Long id;
    private Long userId;
    private String studentNo;
    private String chineseName;
    private String englishName;
    private String gender;
    private String nationality;
    private String college;
    private String major;
    private String degreeLevel;
    private String grade;
    private String studentStatus;
    private Integer status;
    private LocalDateTime createTime;
}
