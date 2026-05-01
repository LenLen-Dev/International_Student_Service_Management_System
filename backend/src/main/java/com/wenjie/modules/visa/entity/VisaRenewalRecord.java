package com.wenjie.modules.visa.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VisaRenewalRecord {
    private Long id;
    private Long studentId;
    private String renewalType;
    private Long targetId;
    private LocalDate applicationDate;
    private LocalDate acceptanceDate;
    private LocalDate completeDate;
    private String renewalStatus;
    private String result;
    private LocalDate newValidUntil;
    private Long handlerId;
    private String handlerName;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String studentNo;
    private String chineseName;
    private String englishName;
}
