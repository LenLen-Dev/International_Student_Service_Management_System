/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.controller
 * @className: StudentStatusLogController
 * @description: TODO 留学生状态日志接口控制器
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
import com.wenjie.modules.student.service.StudentStatusLogService;
import com.wenjie.modules.student.vo.StudentStatusLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "留学生状态日志管理")
@Validated
@RestController
@RequestMapping("/api/student/profiles")
@RequiredArgsConstructor
public class StudentStatusLogController {

    private final StudentStatusLogService statusLogService;

    @Operation(summary = "查询状态变更日志")
    @GetMapping("/{studentId}/status-logs")
    @SaCheckPermission("student:status-log:list")
    public Result<List<StudentStatusLogVO>> listByStudentId(@PathVariable Long studentId) {
        return Result.success(statusLogService.listByStudentId(studentId));
    }
}
