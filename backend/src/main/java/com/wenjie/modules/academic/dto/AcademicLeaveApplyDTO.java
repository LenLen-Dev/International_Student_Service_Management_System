package com.wenjie.modules.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AcademicLeaveApplyDTO {
    @NotBlank(message = "请假类型不能为空")
    private String leaveType;
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    @NotBlank(message = "请假原因不能为空")
    private String reason;
    private String remark;
}
