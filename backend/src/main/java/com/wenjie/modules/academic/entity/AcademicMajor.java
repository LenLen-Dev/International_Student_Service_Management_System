package com.wenjie.modules.academic.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AcademicMajor {
    private Long id;
    private String college;
    private String majorCode;
    private String majorName;
    private String degreeLevel;
    private Integer studyDuration;
    private Integer status;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
