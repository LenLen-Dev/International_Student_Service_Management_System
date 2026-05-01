package com.wenjie.modules.visa.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisaReminderNotification {
    private Long id;
    private Long alertId;
    private Long recipientUserId;
    private String recipientName;
    private String channel;
    private String sendStatus;
    private String title;
    private String content;
    private String errorMessage;
    private Integer readStatus;
    private LocalDateTime readTime;
    private LocalDateTime sendTime;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
