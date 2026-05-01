package com.wenjie.modules.teaching.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.teaching.dto.TeachingOfferingDTO;
import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.dto.TeachingStatusDTO;
import com.wenjie.modules.teaching.entity.TeachingCourseEnrollment;
import com.wenjie.modules.teaching.entity.TeachingCourseOffering;
import com.wenjie.modules.teaching.service.TeachingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/teaching/offerings")
@RequiredArgsConstructor
public class TeachingOfferingController {

    private final TeachingService teachingService;

    @GetMapping
    @SaCheckPermission("teaching:offering:list")
    public Result<PageResult<TeachingCourseOffering>> page(TeachingQueryDTO query) {
        return Result.success(teachingService.pageOfferings(query));
    }

    @GetMapping("/my")
    @SaCheckPermission("teaching:offering:my")
    public Result<List<TeachingCourseOffering>> myOfferings() {
        return Result.success(teachingService.myOfferings());
    }

    @GetMapping("/{id}/students")
    @SaCheckPermission("teaching:offering:list")
    public Result<List<TeachingCourseEnrollment>> students(@PathVariable Long id) {
        return Result.success(teachingService.offeringStudents(id));
    }

    @PostMapping
    @SaCheckPermission("teaching:offering:add")
    @OperationLog(module = "教务与成绩管理", operationType = "CREATE", operationName = "新增教学班")
    public Result<Long> create(@Valid @RequestBody TeachingOfferingDTO dto) {
        return Result.success(teachingService.createOffering(dto));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("teaching:offering:update")
    @OperationLog(module = "教务与成绩管理", operationType = "UPDATE", operationName = "编辑教学班")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody TeachingOfferingDTO dto) {
        teachingService.updateOffering(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @SaCheckPermission("teaching:offering:status")
    @OperationLog(module = "教务与成绩管理", operationType = "STATUS", operationName = "修改教学班状态")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody TeachingStatusDTO dto) {
        teachingService.updateOfferingStatus(id, dto.getOfferingStatus());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("teaching:offering:delete")
    @OperationLog(module = "教务与成绩管理", operationType = "DELETE", operationName = "删除教学班")
    public Result<Void> delete(@PathVariable Long id) {
        teachingService.deleteOffering(id);
        return Result.success();
    }
}
