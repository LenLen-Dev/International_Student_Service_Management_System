package com.wenjie.modules.visa.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResidencePermit {
    private Long id;
    private Long studentId;
    private String permitNo;
    private String permitType;
    private String residenceAddress;
    private LocalDate issueDate;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private String status;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String studentNo;
    private String chineseName;
    private String englishName;
}
