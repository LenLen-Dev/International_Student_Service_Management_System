/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.config.entity
 * @className: SysOperationLog
 * @description: 系统操作日志实体
 * @author: Wenjie
 * @createDate: 2026-05-01 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-05-01 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.config.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysOperationLog {
    private Long id;
    private String moduleName;
    private String operationType;
    private String operationName;
    private String requestMethod;
    private String requestUri;
    private String controllerMethod;
    private String requestParams;
    private String responseResult;
    private Integer success;
    private String errorMessage;
    private Long operatorId;
    private String operatorName;
    private String ipAddress;
    private String userAgent;
    private Long costTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
