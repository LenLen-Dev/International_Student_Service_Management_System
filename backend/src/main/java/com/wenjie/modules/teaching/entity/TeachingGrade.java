package com.wenjie.modules.teaching.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TeachingGrade {
    private Long id;
    private Long enrollmentId;
    private Long offeringId;
    private Long studentId;
    private BigDecimal usualScore;
    private BigDecimal finalScore;
    private BigDecimal totalScore;
    private BigDecimal gradePoint;
    private Integer passed;
    private String gradeStatus;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String studentNo;
    private String chineseName;
    private String englishName;
    private String courseCode;
    private String courseName;
    private String academicYear;
    private String semester;
    private String teacherName;
}
