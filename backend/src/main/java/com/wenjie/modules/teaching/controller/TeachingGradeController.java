package com.wenjie.modules.teaching.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.teaching.dto.TeachingGradeDTO;
import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingGrade;
import com.wenjie.modules.teaching.service.TeachingService;
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
public class TeachingGradeController {

    private final TeachingService teachingService;

    @GetMapping("/grades")
    @SaCheckPermission("teaching:grade:list")
    public Result<PageResult<TeachingGrade>> page(TeachingQueryDTO query) {
        return Result.success(teachingService.pageGrades(query));
    }

    @PutMapping("/grades/{id}")
    @SaCheckPermission("teaching:grade:update")
    @OperationLog(module = "教务与成绩管理", operationType = "UPDATE", operationName = "编辑成绩")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody TeachingGradeDTO dto) {
        teachingService.updateGrade(id, dto);
        return Result.success();
    }

    @PostMapping("/offerings/{offeringId}/grades/import")
    @SaCheckPermission("teaching:grade:import")
    @OperationLog(module = "教务与成绩管理", operationType = "IMPORT", operationName = "批量导入成绩")
    public Result<Void> importGrades(@PathVariable Long offeringId, @RequestBody List<@Valid TeachingGradeDTO> grades) {
        teachingService.importGrades(offeringId, grades);
        return Result.success();
    }

    @PutMapping("/offerings/{offeringId}/grades/publish")
    @SaCheckPermission("teaching:grade:publish")
    @OperationLog(module = "教务与成绩管理", operationType = "PUBLISH", operationName = "发布成绩")
    public Result<Void> publish(@PathVariable Long offeringId) {
        teachingService.publishGrades(offeringId);
        return Result.success();
    }

    @GetMapping("/grades/me")
    @SaCheckPermission("teaching:grade:my")
    public Result<List<TeachingGrade>> myGrades() {
        return Result.success(teachingService.myGrades());
    }
}
