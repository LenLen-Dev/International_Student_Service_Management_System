package com.wenjie.modules.teaching.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeachingAcademicAlert {
    private Long id;
    private Long studentId;
    private String alertType;
    private String alertLevel;
    private Long sourceId;
    private String title;
    private String content;
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
}
