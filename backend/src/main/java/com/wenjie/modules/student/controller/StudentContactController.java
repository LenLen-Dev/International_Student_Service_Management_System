/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.controller
 * @className: StudentContactController
 * @description: TODO 留学生联系人接口控制器
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
import com.wenjie.modules.student.dto.StudentContactDTO;
import com.wenjie.modules.student.service.StudentContactService;
import com.wenjie.modules.student.vo.StudentContactVO;
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

@Tag(name = "留学生联系人管理")
@Validated
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentContactController {

    private final StudentContactService contactService;

    @Operation(summary = "查询联系人列表")
    @GetMapping("/profiles/{studentId}/contacts")
    @SaCheckPermission("student:contact:list")
    public Result<List<StudentContactVO>> listByStudentId(@PathVariable Long studentId) {
        return Result.success(contactService.listByStudentId(studentId));
    }

    @Operation(summary = "新增联系人")
    @PostMapping("/profiles/{studentId}/contacts")
    @SaCheckPermission("student:contact:add")
    public Result<Long> createContact(@PathVariable Long studentId, @Valid @RequestBody StudentContactDTO dto) {
        return Result.success(contactService.createContact(studentId, dto));
    }

    @Operation(summary = "修改联系人")
    @PutMapping("/contacts/{id}")
    @SaCheckPermission("student:contact:update")
    public Result<Void> updateContact(@PathVariable Long id, @Valid @RequestBody StudentContactDTO dto) {
        contactService.updateContact(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除联系人")
    @DeleteMapping("/contacts/{id}")
    @SaCheckPermission("student:contact:delete")
    public Result<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return Result.success();
    }
}
