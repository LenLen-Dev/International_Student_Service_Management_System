package com.wenjie.modules.academic.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AcademicStudentRecord {
    private Long id;
    private Long studentId;
    private Long majorId;
    private Long gradeId;
    private Long classId;
    private String studentStatus;
    private LocalDate enrollmentDate;
    private LocalDate expectedGraduationDate;
    private LocalDate actualLeaveDate;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String studentNo;
    private String chineseName;
    private String englishName;
    private String college;
    private String majorName;
    private String degreeLevel;
    private String gradeName;
    private String className;
}
