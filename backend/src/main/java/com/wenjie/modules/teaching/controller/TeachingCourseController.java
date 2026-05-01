package com.wenjie.modules.teaching.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.teaching.dto.TeachingCourseDTO;
import com.wenjie.modules.teaching.dto.TeachingQueryDTO;
import com.wenjie.modules.teaching.entity.TeachingCourse;
import com.wenjie.modules.teaching.service.TeachingService;
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
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/teaching/courses")
@RequiredArgsConstructor
public class TeachingCourseController {

    private final TeachingService teachingService;

    @GetMapping
    @SaCheckPermission("teaching:course:list")
    public Result<PageResult<TeachingCourse>> page(TeachingQueryDTO query) {
        return Result.success(teachingService.pageCourses(query));
    }

    @GetMapping("/enabled")
    @SaCheckPermission("teaching:course:list")
    public Result<List<TeachingCourse>> enabled() {
        return Result.success(teachingService.enabledCourses());
    }

    @PostMapping
    @SaCheckPermission("teaching:course:add")
    @OperationLog(module = "教务与成绩管理", operationType = "CREATE", operationName = "新增课程")
    public Result<Long> create(@Valid @RequestBody TeachingCourseDTO dto) {
        return Result.success(teachingService.createCourse(dto));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("teaching:course:update")
    @OperationLog(module = "教务与成绩管理", operationType = "UPDATE", operationName = "编辑课程")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody TeachingCourseDTO dto) {
        teachingService.updateCourse(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @SaCheckPermission("teaching:course:status")
    @OperationLog(module = "教务与成绩管理", operationType = "STATUS", operationName = "修改课程状态")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        teachingService.updateCourseStatus(id, body.get("status"));
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("teaching:course:delete")
    @OperationLog(module = "教务与成绩管理", operationType = "DELETE", operationName = "删除课程")
    public Result<Void> delete(@PathVariable Long id) {
        teachingService.deleteCourse(id);
        return Result.success();
    }
}
