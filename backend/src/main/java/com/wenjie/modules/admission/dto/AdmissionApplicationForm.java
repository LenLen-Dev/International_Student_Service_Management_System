package com.wenjie.modules.admission.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdmissionApplicationForm {
    private String chineseName;
    @NotBlank(message = "英文姓名不能为空")
    private String englishName;
    private String gender = "UNKNOWN";
    private LocalDate birthDate;
    @NotBlank(message = "国籍不能为空")
    private String nationality;
    @Email(message = "邮箱格式不正确")
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
    private String remark;
}
