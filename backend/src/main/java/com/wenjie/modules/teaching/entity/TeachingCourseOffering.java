package com.wenjie.modules.teaching.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeachingCourseOffering {
    private Long id;
    private Long courseId;
    private Long teacherId;
    private String teacherName;
    private String academicYear;
    private String semester;
    private Integer capacity;
    private Integer selectedCount;
    private LocalDateTime selectionStartTime;
    private LocalDateTime selectionEndTime;
    private String classTime;
    private String classroom;
    private String offeringStatus;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String courseCode;
    private String courseName;
    private String courseType;
    private String college;
    private java.math.BigDecimal credits;
    private Integer totalHours;
}
