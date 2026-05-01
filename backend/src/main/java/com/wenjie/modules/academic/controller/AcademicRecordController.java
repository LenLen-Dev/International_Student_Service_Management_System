package com.wenjie.modules.academic.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.academic.dto.AcademicQueryDTO;
import com.wenjie.modules.academic.dto.AcademicRecordStatusDTO;
import com.wenjie.modules.academic.dto.AcademicStudentRecordDTO;
import com.wenjie.modules.academic.entity.AcademicStudentRecord;
import com.wenjie.modules.academic.service.AcademicService;
import com.wenjie.modules.academic.vo.AcademicMyOverviewVO;
import com.wenjie.modules.config.annotation.OperationLog;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/academic")
@RequiredArgsConstructor
public class AcademicRecordController {

    private final AcademicService academicService;

    @GetMapping("/records")
    @SaCheckPermission("academic:record:list")
    public Result<PageResult<AcademicStudentRecord>> page(AcademicQueryDTO query) {
        return Result.success(academicService.pageRecords(query));
    }

    @PostMapping("/records")
    @SaCheckPermission("academic:record:add")
    @OperationLog(module = "学籍与在校管理", operationType = "CREATE", operationName = "新增学籍记录")
    public Result<Long> create(@Valid @RequestBody AcademicStudentRecordDTO dto) {
        return Result.success(academicService.createRecord(dto));
    }

    @GetMapping("/records/{id}")
    @SaCheckPermission("academic:record:detail")
    public Result<AcademicStudentRecord> detail(@PathVariable Long id) {
        return Result.success(academicService.getRecord(id));
    }

    @PutMapping("/records/{id}")
    @SaCheckPermission("academic:record:update")
    @OperationLog(module = "学籍与在校管理", operationType = "UPDATE", operationName = "编辑学籍记录")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AcademicStudentRecordDTO dto) {
        academicService.updateRecord(id, dto);
        return Result.success();
    }

    @PutMapping("/records/{id}/status")
    @SaCheckPermission("academic:record:status")
    @OperationLog(module = "学籍与在校管理", operationType = "STATUS", operationName = "调整学籍状态")
    public Result<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody AcademicRecordStatusDTO dto) {
        academicService.updateRecordStatus(id, dto);
        return Result.success();
    }

    @GetMapping("/me")
    @SaCheckPermission("academic:my:view")
    public Result<AcademicMyOverviewVO> myOverview() {
        return Result.success(academicService.myOverview());
    }
}
