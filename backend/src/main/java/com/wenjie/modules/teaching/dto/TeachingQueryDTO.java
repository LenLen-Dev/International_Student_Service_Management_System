package com.wenjie.modules.teaching.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TeachingQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Integer status;
    private Long courseId;
    private Long offeringId;
    private Long teacherId;
    private String courseType;
    private String academicYear;
    private String semester;
    private String offeringStatus;
    private String enrollmentStatus;
    private String gradeStatus;
    private String attendanceStatus;
    private LocalDate attendanceDate;
    private String alertType;
    private String alertLevel;
    private String alertStatus;

    public int getOffset() {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        return (page - 1) * getLimit();
    }

    public int getLimit() {
        return pageSize == null || pageSize < 1 ? 10 : pageSize;
    }
}
