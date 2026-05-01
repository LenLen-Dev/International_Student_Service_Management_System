package com.wenjie.modules.config.service;

import com.wenjie.common.PageResult;
import com.wenjie.modules.config.dto.BackupCreateRequest;
import com.wenjie.modules.config.dto.BackupQueryDTO;
import com.wenjie.modules.config.entity.SysDataBackup;
import org.springframework.core.io.Resource;

public interface DataBackupService {
    PageResult<SysDataBackup> page(BackupQueryDTO query);
    SysDataBackup createBackup(BackupCreateRequest request);
    Resource download(Long id);
    SysDataBackup detail(Long id);
    void delete(Long id);
}
