package com.wenjie.modules.teaching.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingCourseEnrollment;
import com.wenjie.modules.teaching.entity.TeachingCourseOffering;
import com.wenjie.modules.teaching.service.TeachingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/teaching/selections")
@RequiredArgsConstructor
public class TeachingSelectionController {

    private final TeachingService teachingService;

    @GetMapping("/available")
    @SaCheckPermission("teaching:selection:available")
    public Result<List<TeachingCourseOffering>> available(TeachingQueryDTO query) {
        return Result.success(teachingService.availableOfferings(query));
    }

    @GetMapping("/me")
    @SaCheckPermission("teaching:selection:my")
    public Result<List<TeachingCourseEnrollment>> mySelections() {
        return Result.success(teachingService.mySelections());
    }

    @PostMapping("/{offeringId}")
    @SaCheckPermission("teaching:selection:select")
    @OperationLog(module = "教务与成绩管理", operationType = "SELECT", operationName = "学生选课")
    public Result<Void> select(@PathVariable Long offeringId) {
        teachingService.selectCourse(offeringId);
        return Result.success();
    }

    @DeleteMapping("/{offeringId}")
    @SaCheckPermission("teaching:selection:drop")
    @OperationLog(module = "教务与成绩管理", operationType = "DROP", operationName = "学生退课")
    public Result<Void> drop(@PathVariable Long offeringId) {
        teachingService.dropCourse(offeringId);
        return Result.success();
    }
}
