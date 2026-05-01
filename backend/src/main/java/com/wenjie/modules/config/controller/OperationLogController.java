package com.wenjie.modules.config.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.config.dto.OperationLogQueryDTO;
import com.wenjie.modules.config.entity.SysOperationLog;
import com.wenjie.modules.config.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Validated
@RestController
@RequestMapping("/api/audit/operation-logs")
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogService operationLogService;

    @GetMapping
    @SaCheckPermission("audit:operation-log:list")
    public Result<PageResult<SysOperationLog>> page(OperationLogQueryDTO query) {
        return Result.success(operationLogService.page(query));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("audit:operation-log:detail")
    public Result<SysOperationLog> detail(@PathVariable Long id) {
        return Result.success(operationLogService.detail(id));
    }

    @GetMapping("/export")
    @SaCheckPermission("audit:operation-log:export")
    public ResponseEntity<byte[]> export(OperationLogQueryDTO query) {
        byte[] bytes = operationLogService.export(query);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename("operation-logs.csv", StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(bytes);
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("audit:operation-log:delete")
    @OperationLog(module = "操作日志", operationType = "DELETE", operationName = "删除操作日志")
    public Result<Void> delete(@PathVariable Long id) {
        operationLogService.delete(id);
        return Result.success();
    }
}
