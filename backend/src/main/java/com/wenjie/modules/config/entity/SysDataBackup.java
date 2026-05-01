/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.config.entity
 * @className: SysDataBackup
 * @description: 系统数据备份实体
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
public class SysDataBackup {
    private Long id;
    private String backupName;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String backupStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long costTime;
    private Long operatorId;
    private String operatorName;
    private String errorMessage;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
