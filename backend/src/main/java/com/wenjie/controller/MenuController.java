/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.controller
 * @className: MenuController
 * @description: 菜单权限管理接口控制器
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
import com.wenjie.dto.MenuRequest;
import com.wenjie.service.MenuService;
import com.wenjie.vo.MenuVO;
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
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/tree")
    @SaCheckPermission("system:menu:list")
    public Result<List<MenuVO>> tree(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer status) {
        return Result.success(menuService.tree(keyword, status));
    }

    @PostMapping
    @SaCheckPermission("system:menu:add")
    public Result<Long> create(@Valid @RequestBody MenuRequest request) {
        return Result.success(menuService.create(request));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("system:menu:edit")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody MenuRequest request) {
        menuService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("system:menu:delete")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success();
    }
}
