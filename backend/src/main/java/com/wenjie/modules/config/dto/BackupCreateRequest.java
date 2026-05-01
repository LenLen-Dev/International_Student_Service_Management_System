package com.wenjie.modules.config.dto;

import lombok.Data;

@Data
public class BackupCreateRequest {
    private String backupName;
    private String remark;
}
