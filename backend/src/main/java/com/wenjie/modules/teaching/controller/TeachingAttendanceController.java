package com.wenjie.modules.teaching.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.teaching.dto.TeachingAttendanceSaveDTO;
import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingAttendance;
import com.wenjie.modules.teaching.service.TeachingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/teaching")
@RequiredArgsConstructor
public class TeachingAttendanceController {

    private final TeachingService teachingService;

    @GetMapping("/attendance")
    @SaCheckPermission("teaching:attendance:list")
    public Result<PageResult<TeachingAttendance>> page(TeachingQueryDTO query) {
        return Result.success(teachingService.pageAttendance(query));
    }

    @PutMapping("/offerings/{offeringId}/attendance")
    @SaCheckPermission("teaching:attendance:save")
    @OperationLog(module = "教务与成绩管理", operationType = "SAVE", operationName = "保存出勤")
    public Result<Void> save(@PathVariable Long offeringId, @Valid @RequestBody TeachingAttendanceSaveDTO dto) {
        teachingService.saveAttendance(offeringId, dto);
        return Result.success();
    }

    @GetMapping("/attendance/me")
    @SaCheckPermission("teaching:attendance:my")
    public Result<List<TeachingAttendance>> myAttendance() {
        return Result.success(teachingService.myAttendance());
    }
}
