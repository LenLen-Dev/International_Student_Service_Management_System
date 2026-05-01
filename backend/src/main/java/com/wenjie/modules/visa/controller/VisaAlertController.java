package com.wenjie.modules.visa.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.wenjie.common.PageResult;
import com.wenjie.common.Result;
import com.wenjie.modules.config.annotation.OperationLog;
import com.wenjie.modules.visa.dto.VisaAlertQueryDTO;
import com.wenjie.modules.visa.dto.VisaAlertResolveDTO;
import com.wenjie.modules.visa.dto.VisaNotificationQueryDTO;
import com.wenjie.modules.visa.entity.VisaComplianceAlert;
import com.wenjie.modules.visa.entity.VisaReminderNotification;
import com.wenjie.modules.visa.service.VisaService;
import com.wenjie.modules.visa.vo.VisaMyOverviewVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/visa")
@RequiredArgsConstructor
public class VisaAlertController {

    private final VisaService visaService;

    @GetMapping("/alerts")
    @SaCheckPermission("visa:alert:list")
    public Result<PageResult<VisaComplianceAlert>> pageAlerts(VisaAlertQueryDTO query) {
        return Result.success(visaService.pageAlerts(query));
    }

    @GetMapping("/alerts/{id}")
    @SaCheckPermission("visa:alert:detail")
    public Result<VisaComplianceAlert> alertDetail(@PathVariable Long id) {
        return Result.success(visaService.getAlert(id));
    }

    @PutMapping("/alerts/{id}/resolve")
    @SaCheckPermission("visa:alert:resolve")
    @OperationLog(module = "签证与居留管理", operationType = "RESOLVE", operationName = "处理合规预警")
    public Result<Void> resolveAlert(@PathVariable Long id, @Valid @RequestBody VisaAlertResolveDTO dto) {
        visaService.resolveAlert(id, dto);
        return Result.success();
    }

    @PostMapping("/alerts/generate")
    @SaCheckPermission("visa:alert:generate")
    @OperationLog(module = "签证与居留管理", operationType = "GENERATE_ALERT", operationName = "手动生成合规预警")
    public Result<Integer> generateAlerts() {
        return Result.success(visaService.generateAlerts());
    }

    @PostMapping("/alerts/{id}/notify")
    @SaCheckPermission("visa:alert:notify")
    @OperationLog(module = "签证与居留管理", operationType = "NOTIFY", operationName = "发送预警提醒")
    public Result<Void> notifyAlert(@PathVariable Long id) {
        visaService.notifyAlert(id);
        return Result.success();
    }

    @GetMapping("/notifications")
    @SaCheckPermission("visa:notification:list")
    public Result<PageResult<VisaReminderNotification>> pageNotifications(VisaNotificationQueryDTO query) {
        return Result.success(visaService.pageNotifications(query));
    }

    @PutMapping("/notifications/{id}/read")
    @SaCheckPermission("visa:notification:read")
    public Result<Void> markRead(@PathVariable Long id) {
        visaService.markNotificationRead(id);
        return Result.success();
    }

    @GetMapping("/me")
    @SaCheckPermission("visa:my:view")
    public Result<VisaMyOverviewVO> myOverview() {
        return Result.success(visaService.getMyOverview());
    }

    @GetMapping("/me/notifications")
    @SaCheckPermission("visa:my:notification")
    public Result<PageResult<VisaReminderNotification>> myNotifications(VisaNotificationQueryDTO query) {
        return Result.success(visaService.pageMyNotifications(query));
    }
}
