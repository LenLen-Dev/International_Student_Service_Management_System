/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.controller
 * @className: StudentEducationController
 * @description: TODO 留学生教育背景接口控制器
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.Result;
import com.wenjie.modules.student.dto.StudentEducationDTO;
import com.wenjie.modules.student.service.StudentEducationService;
import com.wenjie.modules.student.vo.StudentEducationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "留学生教育背景管理")
@Validated
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentEducationController {

    private final StudentEducationService educationService;

    @Operation(summary = "查询教育背景列表")
    @GetMapping("/profiles/{studentId}/educations")
    @SaCheckPermission("student:education:list")
    public Result<List<StudentEducationVO>> listByStudentId(@PathVariable Long studentId) {
        return Result.success(educationService.listByStudentId(studentId));
    }

    @Operation(summary = "新增教育背景")
    @PostMapping("/profiles/{studentId}/educations")
    @SaCheckPermission("student:education:add")
    public Result<Long> createEducation(@PathVariable Long studentId, @Valid @RequestBody StudentEducationDTO dto) {
        return Result.success(educationService.createEducation(studentId, dto));
    }

    @Operation(summary = "修改教育背景")
    @PutMapping("/educations/{id}")
    @SaCheckPermission("student:education:update")
    public Result<Void> updateEducation(@PathVariable Long id, @Valid @RequestBody StudentEducationDTO dto) {
        educationService.updateEducation(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除教育背景")
    @DeleteMapping("/educations/{id}")
    @SaCheckPermission("student:education:delete")
    public Result<Void> deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return Result.success();
    }
}
