package com.wenjie.modules.admission.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdmissionApplication {
    private Long id;
    private String applicationNo;
    private Long userId;
    private String chineseName;
    private String englishName;
    private String gender;
    private LocalDate birthDate;
    private String nationality;
    private String email;
    private String phone;
    private String passportNo;
    private String passportCountry;
    private LocalDate passportExpireDate;
    private String applyCollege;
    private String applyMajor;
    private String degreeLevel;
    private String programType;
    private String educationBackground;
    private String applicationStatus;
    private String admissionStatus;
    private String reviewOpinion;
    private Long studentProfileId;
    private String remark;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
