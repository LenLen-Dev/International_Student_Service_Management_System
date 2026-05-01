package com.wenjie.modules.config.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.config.dto.BackupCreateRequest;
import com.wenjie.modules.config.dto.BackupQueryDTO;
import com.wenjie.modules.config.entity.SysDataBackup;
import com.wenjie.modules.config.service.DataBackupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Validated
@RestController
@RequestMapping("/api/config/backups")
@RequiredArgsConstructor
public class DataBackupController {

    private final DataBackupService backupService;

    @GetMapping
    @SaCheckPermission("config:backup:list")
    public Result<PageResult<SysDataBackup>> page(BackupQueryDTO query) {
        return Result.success(backupService.page(query));
    }

    @PostMapping
    @SaCheckPermission("config:backup:create")
    @OperationLog(module = "数据备份", operationType = "CREATE", operationName = "创建数据库备份")
    public Result<SysDataBackup> create(@Valid @RequestBody(required = false) BackupCreateRequest request) {
        return Result.success(backupService.createBackup(request == null ? new BackupCreateRequest() : request));
    }

    @GetMapping("/{id}/download")
    @SaCheckPermission("config:backup:download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        SysDataBackup backup = backupService.detail(id);
        Resource resource = backupService.download(id);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(backup.getFileName(), StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("config:backup:delete")
    @OperationLog(module = "数据备份", operationType = "DELETE", operationName = "删除数据库备份")
    public Result<Void> delete(@PathVariable Long id) {
        backupService.delete(id);
        return Result.success();
    }
}
