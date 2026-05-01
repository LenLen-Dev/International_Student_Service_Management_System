package com.wenjie.modules.visa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VisaRenewalResultDTO {
    @NotBlank(message = "办理结果不能为空")
    private String result;
    @NotBlank(message = "续签状态不能为空")
    private String renewalStatus;
    private LocalDate completeDate;
    private LocalDate newValidUntil;
    private String remark;
}
