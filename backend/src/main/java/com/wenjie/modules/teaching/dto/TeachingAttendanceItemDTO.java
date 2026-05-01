package com.wenjie.modules.teaching.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TeachingAttendanceItemDTO {
    @NotNull(message = "学生不能为空")
    private Long studentId;
    @NotNull(message = "出勤日期不能为空")
    private LocalDate attendanceDate;
    @NotBlank(message = "出勤状态不能为空")
    private String attendanceStatus;
    private String remark;
}
