package com.wenjie.modules.teaching.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class TeachingAttendanceSaveDTO {
    @Valid
    @NotEmpty(message = "出勤记录不能为空")
    private List<TeachingAttendanceItemDTO> records;
}
