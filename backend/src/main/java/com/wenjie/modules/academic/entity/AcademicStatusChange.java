package com.wenjie.modules.academic.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AcademicStatusChange {
    private Long id;
    private Long studentId;
    private String changeType;
    private String oldStatus;
    private String newStatus;
    private Long oldMajorId;
    private Long newMajorId;
    private Long oldGradeId;
    private Long newGradeId;
    private Long oldClassId;
    private Long newClassId;
    private LocalDate effectiveDate;
    private String reason;
    private String changeStatus;
    private Long approverId;
    private String approverName;
    private LocalDateTime approveTime;
    private String approveOpinion;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String studentNo;
    private String chineseName;
    private String englishName;
    private String newMajorName;
    private String newGradeName;
    private String newClassName;
}
