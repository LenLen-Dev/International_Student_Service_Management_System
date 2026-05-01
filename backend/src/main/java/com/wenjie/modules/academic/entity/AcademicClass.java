package com.wenjie.modules.academic.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AcademicClass {
    private Long id;
    private Long majorId;
    private Long gradeId;
    private String classCode;
    private String className;
    private Long advisorId;
    private String advisorName;
    private Integer status;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String college;
    private String majorName;
    private String gradeName;
}
