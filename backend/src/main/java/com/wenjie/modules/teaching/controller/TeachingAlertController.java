package com.wenjie.modules.teaching.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.teaching.dto.TeachingAlertHandleDTO;
import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingAcademicAlert;
import com.wenjie.modules.teaching.service.TeachingService;
import com.wenjie.modules.teaching.vo.TeachingMyOverviewVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/teaching")
@RequiredArgsConstructor
public class TeachingAlertController {

    private final TeachingService teachingService;

    @GetMapping("/alerts")
    @SaCheckPermission("teaching:alert:list")
    public Result<PageResult<TeachingAcademicAlert>> page(TeachingQueryDTO query) {
        return Result.success(teachingService.pageAlerts(query));
    }

    @PostMapping("/alerts/generate")
    @SaCheckPermission("teaching:alert:generate")
    @OperationLog(module = "教务与成绩管理", operationType = "GENERATE_ALERT", operationName = "生成学业预警")
    public Result<Integer> generate() {
        return Result.success(teachingService.generateAlerts());
    }

    @PutMapping("/alerts/{id}/handle")
    @SaCheckPermission("teaching:alert:handle")
    @OperationLog(module = "教务与成绩管理", operationType = "HANDLE", operationName = "处理学业预警")
    public Result<Void> handle(@PathVariable Long id, @Valid @RequestBody TeachingAlertHandleDTO dto) {
        teachingService.handleAlert(id, dto);
        return Result.success();
    }

    @GetMapping("/alerts/me")
    @SaCheckPermission("teaching:alert:my")
    public Result<List<TeachingAcademicAlert>> myAlerts() {
        return Result.success(teachingService.myAlerts());
    }

    @GetMapping("/me")
    @SaCheckPermission("teaching:my:view")
    public Result<TeachingMyOverviewVO> myOverview() {
        return Result.success(teachingService.myOverview());
    }
}
