package com.wenjie.modules.academic.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.academic.dto.AcademicMajorDTO;
import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.entity.AcademicMajor;
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
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/academic/majors")
@RequiredArgsConstructor
public class AcademicMajorController {

    private final AcademicService academicService;

    @GetMapping
    @SaCheckPermission("academic:major:list")
    public Result<PageResult<AcademicMajor>> page(AcademicQueryDTO query) {
        return Result.success(academicService.pageMajors(query));
    }

    @GetMapping("/enabled")
    @SaCheckPermission("academic:major:list")
    public Result<List<AcademicMajor>> enabled() {
        return Result.success(academicService.enabledMajors());
    }

    @PostMapping
    @SaCheckPermission("academic:major:add")
    @OperationLog(module = "学籍与在校管理", operationType = "CREATE", operationName = "新增专业")
    public Result<Long> create(@Valid @RequestBody AcademicMajorDTO dto) {
        return Result.success(academicService.createMajor(dto));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("academic:major:update")
    @OperationLog(module = "学籍与在校管理", operationType = "UPDATE", operationName = "编辑专业")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AcademicMajorDTO dto) {
        academicService.updateMajor(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @SaCheckPermission("academic:major:status")
    @OperationLog(module = "学籍与在校管理", operationType = "STATUS", operationName = "修改专业状态")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        academicService.updateMajorStatus(id, body.get("status"));
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("academic:major:delete")
    @OperationLog(module = "学籍与在校管理", operationType = "DELETE", operationName = "删除专业")
    public Result<Void> delete(@PathVariable Long id) {
        academicService.deleteMajor(id);
        return Result.success();
    }
}
