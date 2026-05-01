package com.wenjie.modules.academic.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.academic.dto.AcademicClassDTO;
import com.wenjie.modules.academic.dto.AcademicGradeDTO;
import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicClass;
import com.wenjie.modules.academic.entity.AcademicGrade;
import com.wenjie.modules.academic.service.AcademicService;
import com.wenjie.modules.config.annotation.OperationLog;
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
@RequestMapping("/api/academic")
@RequiredArgsConstructor
public class AcademicClassController {

    private final AcademicService academicService;

    @GetMapping("/grades")
    @SaCheckPermission("academic:grade:list")
    public Result<PageResult<AcademicGrade>> pageGrades(AcademicQueryDTO query) {
        return Result.success(academicService.pageGrades(query));
    }

    @GetMapping("/grades/enabled")
    @SaCheckPermission("academic:grade:list")
    public Result<List<AcademicGrade>> enabledGrades() {
        return Result.success(academicService.enabledGrades());
    }

    @PostMapping("/grades")
    @SaCheckPermission("academic:grade:add")
    @OperationLog(module = "学籍与在校管理", operationType = "CREATE", operationName = "新增年级")
    public Result<Long> createGrade(@Valid @RequestBody AcademicGradeDTO dto) {
        return Result.success(academicService.createGrade(dto));
    }

    @PutMapping("/grades/{id}")
    @SaCheckPermission("academic:grade:update")
    @OperationLog(module = "学籍与在校管理", operationType = "UPDATE", operationName = "编辑年级")
    public Result<Void> updateGrade(@PathVariable Long id, @Valid @RequestBody AcademicGradeDTO dto) {
        academicService.updateGrade(id, dto);
        return Result.success();
    }

    @DeleteMapping("/grades/{id}")
    @SaCheckPermission("academic:grade:delete")
    @OperationLog(module = "学籍与在校管理", operationType = "DELETE", operationName = "删除年级")
    public Result<Void> deleteGrade(@PathVariable Long id) {
        academicService.deleteGrade(id);
        return Result.success();
    }

    @GetMapping("/classes")
    @SaCheckPermission("academic:class:list")
    public Result<PageResult<AcademicClass>> pageClasses(AcademicQueryDTO query) {
        return Result.success(academicService.pageClasses(query));
    }

    @GetMapping("/classes/enabled")
    @SaCheckPermission("academic:class:list")
    public Result<List<AcademicClass>> enabledClasses() {
        return Result.success(academicService.enabledClasses());
    }

    @PostMapping("/classes")
    @SaCheckPermission("academic:class:add")
    @OperationLog(module = "学籍与在校管理", operationType = "CREATE", operationName = "新增班级")
    public Result<Long> createClass(@Valid @RequestBody AcademicClassDTO dto) {
        return Result.success(academicService.createClass(dto));
    }

    @PutMapping("/classes/{id}")
    @SaCheckPermission("academic:class:update")
    @OperationLog(module = "学籍与在校管理", operationType = "UPDATE", operationName = "编辑班级")
    public Result<Void> updateClass(@PathVariable Long id, @Valid @RequestBody AcademicClassDTO dto) {
        academicService.updateClass(id, dto);
        return Result.success();
    }

    @DeleteMapping("/classes/{id}")
    @SaCheckPermission("academic:class:delete")
    @OperationLog(module = "学籍与在校管理", operationType = "DELETE", operationName = "删除班级")
    public Result<Void> deleteClass(@PathVariable Long id) {
        academicService.deleteClass(id);
        return Result.success();
    }
}
