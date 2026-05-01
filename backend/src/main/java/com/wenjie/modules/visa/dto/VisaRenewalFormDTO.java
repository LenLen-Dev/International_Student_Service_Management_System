package com.wenjie.modules.visa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VisaRenewalFormDTO {
    @NotNull(message = "学生不能为空")
    private Long studentId;
    @NotBlank(message = "续签业务类型不能为空")
    private String renewalType;
    private Long targetId;
    private LocalDate applicationDate;
    private LocalDate acceptanceDate;
    private LocalDate completeDate;
    @NotBlank(message = "续签状态不能为空")
    private String renewalStatus;
    private String result;
    private LocalDate newValidUntil;
    private String remark;
}
