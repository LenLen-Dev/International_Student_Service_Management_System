/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.controller
 * @className: RoleController
 * @description: 角色管理接口控制器
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
import com.wenjie.dto.AssignMenusRequest;
import com.wenjie.dto.RoleRequest;
import com.wenjie.service.RoleService;
import com.wenjie.vo.RoleVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @SaCheckPermission("system:role:list")
    public Result<List<RoleVO>> list(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer status) {
        return Result.success(roleService.list(keyword, status));
    }

    @PostMapping
    @SaCheckPermission("system:role:add")
    public Result<Long> create(@Valid @RequestBody RoleRequest request) {
        return Result.success(roleService.create(request));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("system:role:edit")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        roleService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:role:delete")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/menus")
    @SaCheckPermission("system:role:menus")
    public Result<Void> assignMenus(@PathVariable Long id, @Valid @RequestBody AssignMenusRequest request) {
        roleService.assignMenus(id, request);
        return Result.success();
    }
}
