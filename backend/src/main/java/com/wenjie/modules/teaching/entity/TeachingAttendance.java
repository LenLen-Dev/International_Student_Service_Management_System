package com.wenjie.modules.teaching.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TeachingAttendance {
    private Long id;
    private Long offeringId;
    private Long studentId;
    private LocalDate attendanceDate;
    private String attendanceStatus;
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
