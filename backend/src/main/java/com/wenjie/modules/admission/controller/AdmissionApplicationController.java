package com.wenjie.modules.admission.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.admission.dto.AdmissionApplicationForm;
import com.wenjie.modules.admission.dto.AdmissionApplicationQuery;
import com.wenjie.modules.admission.dto.AdmissionMaterialReviewRequest;
import com.wenjie.modules.admission.dto.AdmissionReviewRequest;
import com.wenjie.modules.admission.entity.AdmissionApplication;
import com.wenjie.modules.admission.entity.AdmissionMaterial;
import com.wenjie.modules.admission.service.AdmissionApplicationService;
import com.wenjie.modules.admission.vo.AdmissionApplicationDetailVO;
import com.wenjie.modules.config.annotation.OperationLog;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Validated
@RestController
@RequestMapping("/api/admission")
@RequiredArgsConstructor
public class AdmissionApplicationController {

    private final AdmissionApplicationService applicationService;

    @GetMapping("/applications/me")
    @SaCheckPermission("admission:application:my")
    public Result<AdmissionApplicationDetailVO> getMyApplication() {
        return Result.success(applicationService.getMyApplication());
    }

    @PostMapping("/applications/me")
    @SaCheckPermission("admission:application:create")
    @OperationLog(module = "招生申请", operationType = "CREATE", operationName = "创建我的招生申请")
    public Result<Long> createMyApplication(@Valid @RequestBody AdmissionApplicationForm form) {
        return Result.success(applicationService.createMyApplication(form));
    }

    @PutMapping("/applications/me")
    @SaCheckPermission("admission:application:update")
    @OperationLog(module = "招生申请", operationType = "UPDATE", operationName = "更新我的招生申请")
    public Result<Void> updateMyApplication(@Valid @RequestBody AdmissionApplicationForm form) {
        applicationService.updateMyApplication(form);
        return Result.success();
    }

    @PostMapping("/applications/me/submit")
    @SaCheckPermission("admission:application:submit")
    @OperationLog(module = "招生申请", operationType = "SUBMIT", operationName = "提交我的招生申请")
    public Result<Void> submitMyApplication() {
        applicationService.submitMyApplication();
        return Result.success();
    }

    @PostMapping(value = "/applications/me/materials", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SaCheckPermission("admission:material:upload")
    @OperationLog(module = "招生申请", operationType = "UPLOAD", operationName = "上传申请材料")
    public Result<AdmissionMaterial> uploadMyMaterial(@RequestParam String materialType, @RequestPart("file") MultipartFile file) {
        return Result.success(applicationService.uploadMyMaterial(materialType, file));
    }

    @DeleteMapping("/applications/me/materials/{id}")
    @SaCheckPermission("admission:material:delete")
    @OperationLog(module = "招生申请", operationType = "DELETE", operationName = "删除我的申请材料")
    public Result<Void> deleteMyMaterial(@PathVariable Long id) {
        applicationService.deleteMyMaterial(id);
        return Result.success();
    }

    @GetMapping("/applications/me/notice/download")
    @SaCheckPermission("admission:notice:download")
    public ResponseEntity<Resource> downloadMyNotice() {
        Resource resource = applicationService.downloadMyNotice();
        return downloadResponse(resource);
    }

    @GetMapping("/applications")
    @SaCheckPermission("admission:application:list")
    public Result<PageResult<AdmissionApplication>> page(AdmissionApplicationQuery query) {
        return Result.success(applicationService.page(query));
    }

    @GetMapping("/applications/{id}")
    @SaCheckPermission("admission:application:detail")
    public Result<AdmissionApplicationDetailVO> detail(@PathVariable Long id) {
        return Result.success(applicationService.detail(id));
    }

    @PutMapping("/applications/{id}/return")
    @SaCheckPermission("admission:application:return")
    @OperationLog(module = "招生申请", operationType = "RETURN", operationName = "退回招生申请")
    public Result<Void> returnApplication(@PathVariable Long id, @RequestBody AdmissionReviewRequest request) {
        applicationService.returnApplication(id, request);
        return Result.success();
    }

    @PutMapping("/applications/{id}/reject")
    @SaCheckPermission("admission:application:reject")
    @OperationLog(module = "招生申请", operationType = "REJECT", operationName = "拒绝招生申请")
    public Result<Void> rejectApplication(@PathVariable Long id, @RequestBody AdmissionReviewRequest request) {
        applicationService.rejectApplication(id, request);
        return Result.success();
    }

    @PutMapping("/applications/{id}/admit")
    @SaCheckPermission("admission:application:admit")
    @OperationLog(module = "招生申请", operationType = "ADMIT", operationName = "录取招生申请")
    public Result<Void> admitApplication(@PathVariable Long id, @RequestBody AdmissionReviewRequest request) {
        applicationService.admitApplication(id, request);
        return Result.success();
    }

    @PutMapping("/materials/{id}/review")
    @SaCheckPermission("admission:material:review")
    @OperationLog(module = "招生申请", operationType = "REVIEW", operationName = "审核申请材料")
    public Result<Void> reviewMaterial(@PathVariable Long id, @Valid @RequestBody AdmissionMaterialReviewRequest request) {
        applicationService.reviewMaterial(id, request);
        return Result.success();
    }

    @PostMapping("/applications/{id}/notice")
    @SaCheckPermission("admission:notice:generate")
    @OperationLog(module = "招生申请", operationType = "GENERATE_NOTICE", operationName = "生成录取通知书")
    public Result<Void> generateNotice(@PathVariable Long id) {
        applicationService.generateNotice(id);
        return Result.success();
    }

    @GetMapping("/applications/{id}/notice/download")
    @SaCheckPermission("admission:notice:download")
    public ResponseEntity<Resource> downloadNotice(@PathVariable Long id) {
        Resource resource = applicationService.downloadNotice(id);
        return downloadResponse(resource);
    }

    private ResponseEntity<Resource> downloadResponse(Resource resource) {
        String filename = resource.getFilename() == null ? "admission-notice.pdf" : resource.getFilename();
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
