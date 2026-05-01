package com.wenjie.modules.academic.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.academic.dto.AcademicApproveDTO;
import com.wenjie.modules.academic.dto.AcademicLeaveApplyDTO;
import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicLeaveApplication;
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

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/academic/leaves")
@RequiredArgsConstructor
public class AcademicLeaveController {

    private final AcademicService academicService;

    @GetMapping
    @SaCheckPermission("academic:leave:list")
    public Result<PageResult<AcademicLeaveApplication>> page(AcademicQueryDTO query) {
        return Result.success(academicService.pageLeaves(query));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("academic:leave:detail")
    public Result<AcademicLeaveApplication> detail(@PathVariable Long id) {
        return Result.success(academicService.getLeave(id));
    }

    @PutMapping("/{id}/approve")
    @SaCheckPermission("academic:leave:approve")
    @OperationLog(module = "学籍与在校管理", operationType = "APPROVE", operationName = "审批通过请假")
    public Result<Void> approve(@PathVariable Long id, @RequestBody(required = false) AcademicApproveDTO dto) {
        academicService.approveLeave(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/reject")
    @SaCheckPermission("academic:leave:reject")
    @OperationLog(module = "学籍与在校管理", operationType = "REJECT", operationName = "审批拒绝请假")
    public Result<Void> reject(@PathVariable Long id, @RequestBody(required = false) AcademicApproveDTO dto) {
        academicService.rejectLeave(id, dto);
        return Result.success();
    }

    @GetMapping("/me")
    @SaCheckPermission("academic:leave:my")
    public Result<List<AcademicLeaveApplication>> myLeaves() {
        return Result.success(academicService.myLeaves());
    }

    @PostMapping("/me")
    @SaCheckPermission("academic:leave:apply")
    @OperationLog(module = "学籍与在校管理", operationType = "CREATE", operationName = "提交我的请假")
    public Result<Long> applyMyLeave(@Valid @RequestBody AcademicLeaveApplyDTO dto) {
        return Result.success(academicService.applyMyLeave(dto));
    }

    @PutMapping("/me/{id}/cancel")
    @SaCheckPermission("academic:leave:cancel")
    @OperationLog(module = "学籍与在校管理", operationType = "CANCEL", operationName = "撤回我的请假")
    public Result<Void> cancelMyLeave(@PathVariable Long id) {
        academicService.cancelMyLeave(id);
        return Result.success();
    }
}
