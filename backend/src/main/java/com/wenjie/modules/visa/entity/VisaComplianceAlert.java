package com.wenjie.modules.visa.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VisaComplianceAlert {
    private Long id;
    private Long studentId;
    private String alertType;
    private String alertLevel;
    private String targetType;
    private Long targetId;
    private String title;
    private String content;
    private LocalDate expireDate;
    private Integer remainingDays;
    private String alertStatus;
    private Long handlerId;
    private String handlerName;
    private LocalDateTime handleTime;
    private String handleRemark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String studentNo;
    private String chineseName;
    private String englishName;
    private Long userId;
    private String email;
}
