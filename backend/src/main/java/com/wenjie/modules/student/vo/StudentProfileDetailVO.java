/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.vo
 * @className: StudentProfileDetailVO
 * @description: TODO 留学生档案详情视图对象
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
import java.util.ArrayList;
import java.util.List;

@Data
public class StudentProfileDetailVO {
    private Long id;
    private Long userId;
    private String studentNo;
    private String applicationNo;
    private String chineseName;
    private String englishName;
    private String gender;
    private LocalDate birthDate;
    private String nationality;
    private String nativeLanguage;
    private String email;
    private String phone;
    private String wechat;
    private String passportNo;
    private String passportCountry;
    private LocalDate passportIssueDate;
    private LocalDate passportExpireDate;
    private String college;
    private String major;
    private String degreeLevel;
    private String grade;
    private String className;
    private LocalDate enrollmentDate;
    private LocalDate expectedGraduationDate;
    private String studentStatus;
    private String avatarUrl;
    private String remark;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<StudentContactVO> contacts = new ArrayList<>();
    private List<StudentEducationVO> educations = new ArrayList<>();
    private List<StudentDocumentVO> documents = new ArrayList<>();
    private List<StudentStatusLogVO> statusLogs = new ArrayList<>();
}
