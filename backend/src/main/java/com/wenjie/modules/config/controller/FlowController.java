package com.wenjie.modules.config.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.config.dto.FlowNodeRequest;
import com.wenjie.modules.config.dto.FlowTemplateRequest;
import com.wenjie.modules.config.dto.StatusRequest;
import com.wenjie.modules.config.entity.SysFlowNode;
import com.wenjie.modules.config.entity.SysFlowTemplate;
import com.wenjie.modules.config.service.FlowService;
import com.wenjie.modules.config.vo.FlowDetailVO;
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
@RequestMapping("/api/config/flows")
@RequiredArgsConstructor
public class FlowController {

    private final FlowService flowService;

    @GetMapping
    @SaCheckPermission("config:flow:list")
    public Result<List<SysFlowTemplate>> list(@RequestParam(required = false) String keyword,
                                              @RequestParam(required = false) Integer status) {
        return Result.success(flowService.list(keyword, status));
    }

    @PostMapping
    @SaCheckPermission("config:flow:add")
    @OperationLog(module = "流程配置", operationType = "CREATE", operationName = "新增流程模板")
    public Result<Long> create(@Valid @RequestBody FlowTemplateRequest request) {
        return Result.success(flowService.create(request));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("config:flow:list")
    public Result<FlowDetailVO> detail(@PathVariable Long id) {
        return Result.success(flowService.detail(id));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("config:flow:update")
    @OperationLog(module = "流程配置", operationType = "UPDATE", operationName = "编辑流程模板")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody FlowTemplateRequest request) {
        flowService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("config:flow:delete")
    @OperationLog(module = "流程配置", operationType = "DELETE", operationName = "删除流程模板")
    public Result<Void> delete(@PathVariable Long id) {
        flowService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @SaCheckPermission("config:flow:status")
    @OperationLog(module = "流程配置", operationType = "STATUS", operationName = "修改流程状态")
    public Result<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        flowService.updateStatus(id, request.getStatus());
        return Result.success();
    }

    @GetMapping("/{id}/nodes")
    @SaCheckPermission("config:flow:nodes")
    public Result<List<SysFlowNode>> listNodes(@PathVariable Long id) {
        return Result.success(flowService.listNodes(id));
    }

    @PutMapping("/{id}/nodes")
    @SaCheckPermission("config:flow:nodes")
    @OperationLog(module = "流程配置", operationType = "UPDATE", operationName = "保存流程节点")
    public Result<Void> saveNodes(@PathVariable Long id, @Valid @RequestBody List<FlowNodeRequest> nodes) {
        flowService.saveNodes(id, nodes);
        return Result.success();
    }
}
