package com.wenjie.modules.teaching.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TeachingCourse {
    private Long id;
    private String courseCode;
    private String courseName;
    private BigDecimal credits;
    private Integer totalHours;
    private String courseType;
    private String college;
    private Integer status;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
