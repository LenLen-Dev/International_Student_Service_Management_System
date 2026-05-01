package com.wenjie.modules.academic.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AcademicGrade {
    private Long id;
    private String gradeCode;
    private String gradeName;
    private Integer enrollmentYear;
    private Integer graduationYear;
    private Integer status;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
