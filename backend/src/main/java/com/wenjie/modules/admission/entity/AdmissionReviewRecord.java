package com.wenjie.modules.admission.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdmissionReviewRecord {
    private Long id;
    private Long applicationId;
    private String actionType;
    private String fromStatus;
    private String toStatus;
    private String opinion;
    private Long operatorId;
    private String operatorName;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
