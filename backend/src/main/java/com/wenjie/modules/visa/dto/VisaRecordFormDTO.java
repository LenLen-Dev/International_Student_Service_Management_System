package com.wenjie.modules.visa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VisaRecordFormDTO {
    @NotNull(message = "学生不能为空")
    private Long studentId;
    private String passportNo;
    @NotBlank(message = "签证类型不能为空")
    private String visaType;
    private String visaNo;
    private String issuePlace;
    private LocalDate issueDate;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private String entryCount;
    @NotBlank(message = "签证状态不能为空")
    private String status;
    private String remark;
}
