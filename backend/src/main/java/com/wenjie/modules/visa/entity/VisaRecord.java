package com.wenjie.modules.visa.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VisaRecord {
    private Long id;
    private Long studentId;
    private String passportNo;
    private String visaType;
    private String visaNo;
    private String issuePlace;
    private LocalDate issueDate;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private String entryCount;
    private String status;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String studentNo;
    private String chineseName;
    private String englishName;
}
