package com.wenjie.modules.admission.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdmissionNotice {
    private Long id;
    private Long applicationId;
    private String noticeNo;
    private String fileName;
    private String filePath;
    private LocalDate issueDate;
    private Long issuerId;
    private String issuerName;
    private Integer downloadCount;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
