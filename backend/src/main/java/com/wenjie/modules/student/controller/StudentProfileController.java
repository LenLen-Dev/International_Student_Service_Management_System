/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.controller
 * @className: StudentProfileController
 * @description: TODO 留学生档案接口控制器
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
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.student.dto.StudentProfileCreateDTO;
import com.wenjie.modules.student.dto.StudentProfileQueryDTO;
import com.wenjie.modules.student.dto.StudentProfileUpdateDTO;
import com.wenjie.modules.student.dto.StudentStatusUpdateDTO;
import com.wenjie.modules.student.service.StudentProfileService;
import com.wenjie.modules.student.vo.StudentProfileDetailVO;
import com.wenjie.modules.student.vo.StudentProfileListVO;
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

@Tag(name = "留学生档案管理")
@Validated
@RestController
@RequestMapping("/api/student/profiles")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService profileService;

    @Operation(summary = "分页查询留学生档案")
    @GetMapping
    @SaCheckPermission("student:profile:list")
    public Result<PageResult<StudentProfileListVO>> pageProfiles(@Valid StudentProfileQueryDTO query) {
        return Result.success(profileService.pageProfiles(query));
    }

    @Operation(summary = "新增留学生档案")
    @PostMapping
    @SaCheckPermission("student:profile:add")
    public Result<Long> createProfile(@Valid @RequestBody StudentProfileCreateDTO dto) {
        return Result.success(profileService.createProfile(dto));
    }

    @Operation(summary = "查询留学生档案详情")
    @GetMapping("/{id}")
    @SaCheckPermission("student:profile:detail")
    public Result<StudentProfileDetailVO> getProfileDetail(@PathVariable Long id) {
        return Result.success(profileService.getProfileDetail(id));
    }

    @Operation(summary = "修改留学生档案")
    @PutMapping("/{id}")
    @SaCheckPermission("student:profile:update")
    public Result<Void> updateProfile(@PathVariable Long id, @Valid @RequestBody StudentProfileUpdateDTO dto) {
        profileService.updateProfile(id, dto);
        return Result.success();
    }

    @Operation(summary = "删除留学生档案")
    @DeleteMapping("/{id}")
    @SaCheckPermission("student:profile:delete")
    public Result<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return Result.success();
    }

    @Operation(summary = "修改留学生状态")
    @PutMapping("/{id}/status")
    @SaCheckPermission("student:profile:status")
    public Result<Void> updateProfileStatus(@PathVariable Long id, @Valid @RequestBody StudentStatusUpdateDTO dto) {
        profileService.updateProfileStatus(id, dto);
        return Result.success();
    }

    @Operation(summary = "查询当前登录留学生档案")
    @GetMapping("/me")
    public Result<StudentProfileDetailVO> getMyProfile() {
        return Result.success(profileService.getMyProfile());
    }

    @Operation(summary = "修改当前登录留学生档案")
    @PutMapping("/me")
    public Result<Void> updateMyProfile(@RequestBody StudentProfileUpdateDTO dto) {
        profileService.updateMyProfile(dto);
        return Result.success();
    }
}
