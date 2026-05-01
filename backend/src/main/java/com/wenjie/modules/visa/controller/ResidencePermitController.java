package com.wenjie.modules.visa.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.visa.dto.ResidencePermitFormDTO;
import com.wenjie.modules.visa.dto.ResidencePermitQueryDTO;
import com.wenjie.modules.visa.entity.ResidencePermit;
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
@RequestMapping("/api/visa/residence-permits")
@RequiredArgsConstructor
public class ResidencePermitController {

    private final VisaService visaService;

    @GetMapping
    @SaCheckPermission("visa:permit:list")
    public Result<PageResult<ResidencePermit>> page(ResidencePermitQueryDTO query) {
        return Result.success(visaService.pageResidencePermits(query));
    }

    @PostMapping
    @SaCheckPermission("visa:permit:add")
    @OperationLog(module = "签证与居留管理", operationType = "CREATE", operationName = "新增居留许可")
    public Result<Long> create(@Valid @RequestBody ResidencePermitFormDTO dto) {
        return Result.success(visaService.createResidencePermit(dto));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("visa:permit:detail")
    public Result<ResidencePermit> detail(@PathVariable Long id) {
        return Result.success(visaService.getResidencePermit(id));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("visa:permit:update")
    @OperationLog(module = "签证与居留管理", operationType = "UPDATE", operationName = "修改居留许可")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ResidencePermitFormDTO dto) {
        visaService.updateResidencePermit(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("visa:permit:delete")
    @OperationLog(module = "签证与居留管理", operationType = "DELETE", operationName = "删除居留许可")
    public Result<Void> delete(@PathVariable Long id) {
        visaService.deleteResidencePermit(id);
        return Result.success();
    }
}
