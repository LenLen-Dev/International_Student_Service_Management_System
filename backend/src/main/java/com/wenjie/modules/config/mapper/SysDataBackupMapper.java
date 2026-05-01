package com.wenjie.modules.config.mapper;

import com.wenjie.modules.config.dto.BackupQueryDTO;
import com.wenjie.modules.config.entity.SysDataBackup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDataBackupMapper {
    SysDataBackup selectById(@Param("id") Long id);
    List<SysDataBackup> selectPage(BackupQueryDTO query);
    long count(BackupQueryDTO query);
    int insert(SysDataBackup entity);
    int update(SysDataBackup entity);
    int logicalDelete(@Param("id") Long id);
}
