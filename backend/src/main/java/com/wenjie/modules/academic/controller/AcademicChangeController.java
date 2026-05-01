package com.wenjie.modules.academic.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.academic.dto.AcademicApproveDTO;
import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.dto.AcademicStatusChangeDTO;
import com.wenjie.modules.academic.entity.AcademicStatusChange;
import com.wenjie.modules.academic.service.AcademicService;
import com.wenjie.modules.config.annotation.OperationLog;
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

@Validated
@RestController
@RequestMapping("/api/academic/changes")
@RequiredArgsConstructor
public class AcademicChangeController {

    private final AcademicService academicService;

    @GetMapping
    @SaCheckPermission("academic:change:list")
    public Result<PageResult<AcademicStatusChange>> page(AcademicQueryDTO query) {
        return Result.success(academicService.pageChanges(query));
    }

    @PostMapping
    @SaCheckPermission("academic:change:add")
    @OperationLog(module = "学籍与在校管理", operationType = "CREATE", operationName = "提交学籍异动")
    public Result<Long> create(@Valid @RequestBody AcademicStatusChangeDTO dto) {
        return Result.success(academicService.createChange(dto));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("academic:change:detail")
    public Result<AcademicStatusChange> detail(@PathVariable Long id) {
        return Result.success(academicService.getChange(id));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("academic:change:update")
    @OperationLog(module = "学籍与在校管理", operationType = "UPDATE", operationName = "编辑学籍异动")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AcademicStatusChangeDTO dto) {
        academicService.updateChange(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/approve")
    @SaCheckPermission("academic:change:approve")
    @OperationLog(module = "学籍与在校管理", operationType = "APPROVE", operationName = "审批通过学籍异动")
    public Result<Void> approve(@PathVariable Long id, @RequestBody(required = false) AcademicApproveDTO dto) {
        academicService.approveChange(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/reject")
    @SaCheckPermission("academic:change:reject")
    @OperationLog(module = "学籍与在校管理", operationType = "REJECT", operationName = "审批拒绝学籍异动")
    public Result<Void> reject(@PathVariable Long id, @RequestBody(required = false) AcademicApproveDTO dto) {
        academicService.rejectChange(id, dto);
        return Result.success();
    }
}
