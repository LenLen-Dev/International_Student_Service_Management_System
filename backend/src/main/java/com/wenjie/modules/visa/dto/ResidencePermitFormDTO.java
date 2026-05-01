package com.wenjie.modules.visa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResidencePermitFormDTO {
    @NotNull(message = "学生不能为空")
    private Long studentId;
    private String permitNo;
    @NotBlank(message = "居留许可类型不能为空")
    private String permitType;
    private String residenceAddress;
    private LocalDate issueDate;
    private LocalDate validFrom;
    private LocalDate validUntil;
    @NotBlank(message = "居留许可状态不能为空")
    private String status;
    private String remark;
}
