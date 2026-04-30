/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.controller
 * @className: UserController
 * @description: 用户管理接口控制器
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.Result;
import com.wenjie.dto.AssignRolesRequest;
import com.wenjie.dto.UserCreateRequest;
import com.wenjie.dto.UserStatusRequest;
import com.wenjie.dto.UserUpdateRequest;
import com.wenjie.service.UserService;
import com.wenjie.vo.UserVO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @SaCheckPermission("system:user:list")
    public Result<List<UserVO>> list(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String userType,
                                    @RequestParam(required = false) Integer status) {
        return Result.success(userService.list(keyword, userType, status));
    }

    @PostMapping
    @SaCheckPermission("system:user:add")
    public Result<Long> create(@Valid @RequestBody UserCreateRequest request) {
        return Result.success(userService.create(request));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("system:user:edit")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        userService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:user:delete")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @SaCheckPermission("system:user:status")
    public Result<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody UserStatusRequest request) {
        userService.updateStatus(id, request);
        return Result.success();
    }

    @PutMapping("/{id}/roles")
    @SaCheckPermission("system:user:roles")
    public Result<Void> assignRoles(@PathVariable Long id, @Valid @RequestBody AssignRolesRequest request) {
        userService.assignRoles(id, request);
        return Result.success();
    }
}
