/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.controller
 * @className: StudentDocumentController
 * @description: TODO 留学生附件材料接口控制器
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
import com.wenjie.modules.student.dto.StudentDocumentDTO;
import com.wenjie.modules.student.service.StudentDocumentService;
import com.wenjie.modules.student.vo.StudentDocumentVO;
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

@Tag(name = "留学生附件材料管理")
@Validated
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentDocumentController {

    private final StudentDocumentService documentService;

    @Operation(summary = "查询附件材料列表")
    @GetMapping("/profiles/{studentId}/documents")
    @SaCheckPermission("student:document:list")
    public Result<List<StudentDocumentVO>> listByStudentId(@PathVariable Long studentId) {
        return Result.success(documentService.listByStudentId(studentId));
    }

    @Operation(summary = "新增附件材料")
    @PostMapping("/profiles/{studentId}/documents")
    @SaCheckPermission("student:document:add")
    public Result<Long> createDocument(@PathVariable Long studentId, @Valid @RequestBody StudentDocumentDTO dto) {
        return Result.success(documentService.createDocument(studentId, dto));
    }

    @Operation(summary = "修改附件材料")
    @PutMapping("/documents/{id}")
    @SaCheckPermission("student:document:update")
    public Result<Void> updateDocument(@PathVariable Long id, @Valid @RequestBody StudentDocumentDTO dto) {
        documentService.updateDocument(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除附件材料")
    @DeleteMapping("/documents/{id}")
    @SaCheckPermission("student:document:delete")
    public Result<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return Result.success();
    }
}
