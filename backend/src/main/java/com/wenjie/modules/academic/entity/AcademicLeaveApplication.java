package com.wenjie.modules.academic.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AcademicLeaveApplication {
    private Long id;
    private Long studentId;
    private String leaveType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
    private String leaveStatus;
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
    private String className;
}
