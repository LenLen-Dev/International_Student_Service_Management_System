package com.wenjie.modules.academic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicRecordStatusDTO {
    @NotBlank(message = "学籍状态不能为空")
    private String studentStatus;
    private LocalDate actualLeaveDate;
    private String remark;
}
