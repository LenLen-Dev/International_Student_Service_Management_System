package com.wenjie.modules.visa.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.visa.dto.VisaRecordFormDTO;
import com.wenjie.modules.visa.dto.VisaRecordQueryDTO;
import com.wenjie.modules.visa.entity.VisaRecord;
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
@RequestMapping("/api/visa/records")
@RequiredArgsConstructor
public class VisaRecordController {

    private final VisaService visaService;

    @GetMapping
    @SaCheckPermission("visa:record:list")
    public Result<PageResult<VisaRecord>> page(VisaRecordQueryDTO query) {
        return Result.success(visaService.pageVisaRecords(query));
    }

    @PostMapping
    @SaCheckPermission("visa:record:add")
    @OperationLog(module = "签证与居留管理", operationType = "CREATE", operationName = "新增签证信息")
    public Result<Long> create(@Valid @RequestBody VisaRecordFormDTO dto) {
        return Result.success(visaService.createVisaRecord(dto));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("visa:record:detail")
    public Result<VisaRecord> detail(@PathVariable Long id) {
        return Result.success(visaService.getVisaRecord(id));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("visa:record:update")
    @OperationLog(module = "签证与居留管理", operationType = "UPDATE", operationName = "修改签证信息")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody VisaRecordFormDTO dto) {
        visaService.updateVisaRecord(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("visa:record:delete")
    @OperationLog(module = "签证与居留管理", operationType = "DELETE", operationName = "删除签证信息")
    public Result<Void> delete(@PathVariable Long id) {
        visaService.deleteVisaRecord(id);
        return Result.success();
    }
}
