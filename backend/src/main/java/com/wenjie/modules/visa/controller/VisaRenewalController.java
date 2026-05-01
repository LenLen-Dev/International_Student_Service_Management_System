package com.wenjie.modules.visa.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.visa.dto.VisaRenewalFormDTO;
import com.wenjie.modules.visa.dto.VisaRenewalQueryDTO;
import com.wenjie.modules.visa.dto.VisaRenewalResultDTO;
import com.wenjie.modules.visa.entity.VisaRenewalRecord;
import com.wenjie.modules.visa.service.VisaService;
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

@Validated
@RestController
@RequestMapping("/api/visa/renewals")
@RequiredArgsConstructor
public class VisaRenewalController {

    private final VisaService visaService;

    @GetMapping
    @SaCheckPermission("visa:renewal:list")
    public Result<PageResult<VisaRenewalRecord>> page(VisaRenewalQueryDTO query) {
        return Result.success(visaService.pageRenewals(query));
    }

    @PostMapping
    @SaCheckPermission("visa:renewal:add")
    @OperationLog(module = "签证与居留管理", operationType = "CREATE", operationName = "新增续签记录")
    public Result<Long> create(@Valid @RequestBody VisaRenewalFormDTO dto) {
        return Result.success(visaService.createRenewal(dto));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("visa:renewal:detail")
    public Result<VisaRenewalRecord> detail(@PathVariable Long id) {
        return Result.success(visaService.getRenewal(id));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("visa:renewal:update")
    @OperationLog(module = "签证与居留管理", operationType = "UPDATE", operationName = "修改续签记录")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody VisaRenewalFormDTO dto) {
        visaService.updateRenewal(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("visa:renewal:delete")
    @OperationLog(module = "签证与居留管理", operationType = "DELETE", operationName = "删除续签记录")
    public Result<Void> delete(@PathVariable Long id) {
        visaService.deleteRenewal(id);
        return Result.success();
    }

    @PutMapping("/{id}/result")
    @SaCheckPermission("visa:renewal:result")
    @OperationLog(module = "签证与居留管理", operationType = "RESULT", operationName = "登记续签结果")
    public Result<Void> updateResult(@PathVariable Long id, @Valid @RequestBody VisaRenewalResultDTO dto) {
        visaService.updateRenewalResult(id, dto);
        return Result.success();
    }
}
