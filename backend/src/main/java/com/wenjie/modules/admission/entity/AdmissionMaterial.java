package com.wenjie.modules.admission.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdmissionMaterial {
    private Long id;
    private Long applicationId;
    private String materialType;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String mimeType;
    private String reviewStatus;
    private String reviewOpinion;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
