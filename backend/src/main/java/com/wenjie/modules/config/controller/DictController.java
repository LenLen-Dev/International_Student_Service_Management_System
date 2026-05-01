package com.wenjie.modules.config.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.config.dto.DictDataRequest;
import com.wenjie.modules.config.dto.DictTypeRequest;
import com.wenjie.modules.config.dto.StatusRequest;
import com.wenjie.modules.config.entity.SysDictData;
import com.wenjie.modules.config.entity.SysDictType;
import com.wenjie.modules.config.service.DictService;
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
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @GetMapping("/dict-types")
    @SaCheckPermission("config:dict:list")
    public Result<List<SysDictType>> listTypes(@RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Integer status) {
        return Result.success(dictService.listTypes(keyword, status));
    }

    @PostMapping("/dict-types")
    @SaCheckPermission("config:dict:add")
    @OperationLog(module = "字典配置", operationType = "CREATE", operationName = "新增字典类型")
    public Result<Long> createType(@Valid @RequestBody DictTypeRequest request) {
        return Result.success(dictService.createType(request));
    }

    @PutMapping("/dict-types/{id}")
    @SaCheckPermission("config:dict:update")
    @OperationLog(module = "字典配置", operationType = "UPDATE", operationName = "编辑字典类型")
    public Result<Void> updateType(@PathVariable Long id, @Valid @RequestBody DictTypeRequest request) {
        dictService.updateType(id, request);
        return Result.success();
    }

    @PutMapping("/dict-types/{id}/status")
    @SaCheckPermission("config:dict:status")
    @OperationLog(module = "字典配置", operationType = "STATUS", operationName = "修改字典类型状态")
    public Result<Void> updateTypeStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        dictService.updateTypeStatus(id, request.getStatus());
        return Result.success();
    }

    @DeleteMapping("/dict-types/{id}")
    @SaCheckPermission("config:dict:delete")
    @OperationLog(module = "字典配置", operationType = "DELETE", operationName = "删除字典类型")
    public Result<Void> deleteType(@PathVariable Long id) {
        dictService.deleteType(id);
        return Result.success();
    }

    @GetMapping("/dict-types/{dictCode}/items")
    @SaCheckLogin
    public Result<List<SysDictData>> listData(@PathVariable String dictCode,
                                              @RequestParam(required = false) Integer status) {
        return Result.success(dictService.listData(dictCode, status));
    }

    @PostMapping("/dict-data")
    @SaCheckPermission("config:dict:add")
    @OperationLog(module = "字典配置", operationType = "CREATE", operationName = "新增字典项")
    public Result<Long> createData(@Valid @RequestBody DictDataRequest request) {
        return Result.success(dictService.createData(request));
    }

    @PutMapping("/dict-data/{id}")
    @SaCheckPermission("config:dict:update")
    @OperationLog(module = "字典配置", operationType = "UPDATE", operationName = "编辑字典项")
    public Result<Void> updateData(@PathVariable Long id, @Valid @RequestBody DictDataRequest request) {
        dictService.updateData(id, request);
        return Result.success();
    }

    @PutMapping("/dict-data/{id}/status")
    @SaCheckPermission("config:dict:status")
    @OperationLog(module = "字典配置", operationType = "STATUS", operationName = "修改字典项状态")
    public Result<Void> updateDataStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        dictService.updateDataStatus(id, request.getStatus());
        return Result.success();
    }

    @DeleteMapping("/dict-data/{id}")
    @SaCheckPermission("config:dict:delete")
    @OperationLog(module = "字典配置", operationType = "DELETE", operationName = "删除字典项")
    public Result<Void> deleteData(@PathVariable Long id) {
        dictService.deleteData(id);
        return Result.success();
    }
}
