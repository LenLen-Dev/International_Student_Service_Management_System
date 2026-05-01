package com.wenjie.modules.config.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.wenjie.common.BusinessException;
import com.wenjie.common.PageResult;
import com.wenjie.modules.config.dto.BackupCreateRequest;
import com.wenjie.modules.config.dto.BackupQueryDTO;
import com.wenjie.modules.config.entity.SysDataBackup;
import com.wenjie.modules.config.mapper.SysDataBackupMapper;
import com.wenjie.modules.config.service.DataBackupService;
import com.wenjie.modules.system.entity.SysUser;
import com.wenjie.modules.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBackupServiceImpl implements DataBackupService {

    private static final DateTimeFormatter FILE_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final SysDataBackupMapper backupMapper;
    private final SysUserMapper userMapper;
    private final DataSource dataSource;

    @Value("${app.backup-dir:backups}")
    private String backupDir;

    @Override
    public PageResult<SysDataBackup> page(BackupQueryDTO query) {
        return new PageResult<>(backupMapper.count(query), backupMapper.selectPage(query));
    }

    @Override
    public SysDataBackup createBackup(BackupCreateRequest request) {
        long begin = System.currentTimeMillis();
        LocalDateTime start = LocalDateTime.now();
        String timestamp = FILE_TIME.format(start);
        String backupName = request.getBackupName() == null || request.getBackupName().isBlank()
                ? "数据库备份-" + timestamp
                : request.getBackupName();
        String fileName = "intl_student_backup_" + timestamp + ".sql";
        SysDataBackup backup = new SysDataBackup();
        backup.setBackupName(backupName);
        backup.setFileName(fileName);
        backup.setBackupStatus("RUNNING");
        backup.setStartTime(start);
        backup.setRemark(request.getRemark());
        fillOperator(backup);
        backupMapper.insert(backup);

        try {
            Path directory = Path.of(backupDir).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            Path file = directory.resolve(fileName);
            writeSqlBackup(file);
            backup.setFilePath(file.toString());
            backup.setFileSize(Files.size(file));
            backup.setBackupStatus("SUCCESS");
        } catch (Exception ex) {
            backup.setBackupStatus("FAILED");
            backup.setErrorMessage(limit(ex.getMessage(), 1800));
        } finally {
            backup.setEndTime(LocalDateTime.now());
            backup.setCostTime(System.currentTimeMillis() - begin);
            backupMapper.update(backup);
        }

        if ("FAILED".equals(backup.getBackupStatus())) {
            throw new BusinessException("数据库备份失败：" + backup.getErrorMessage());
        }
        return backup;
    }

    @Override
    public Resource download(Long id) {
        SysDataBackup backup = detail(id);
        if (!"SUCCESS".equals(backup.getBackupStatus()) || backup.getFilePath() == null) {
            throw new BusinessException("备份文件不可下载");
        }
        FileSystemResource resource = new FileSystemResource(backup.getFilePath());
        if (!resource.exists()) {
            throw new BusinessException(404, "备份文件不存在");
        }
        return resource;
    }

    @Override
    public SysDataBackup detail(Long id) {
        SysDataBackup backup = backupMapper.selectById(id);
        if (backup == null) {
            throw new BusinessException(404, "备份记录不存在");
        }
        return backup;
    }

    @Override
    public void delete(Long id) {
        SysDataBackup backup = detail(id);
        backupMapper.logicalDelete(id);
        if (backup.getFilePath() != null) {
            try {
                Files.deleteIfExists(Path.of(backup.getFilePath()));
            } catch (Exception ignored) {
                // 删除数据库记录优先，文件清理失败不阻塞业务。
            }
        }
    }

    private void writeSqlBackup(Path file) throws Exception {
        try (Connection connection = dataSource.getConnection();
             BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            String catalog = connection.getCatalog();
            writer.write("-- International Student Service Management System SQL Backup\n");
            writer.write("-- Database: `" + catalog + "`\n\n");
            writer.write("SET NAMES utf8mb4;\nSET FOREIGN_KEY_CHECKS=0;\n\n");

            for (String table : listTables(connection, catalog)) {
                dumpTable(connection, writer, table);
            }

            writer.write("SET FOREIGN_KEY_CHECKS=1;\n");
        }
    }

    private List<String> listTables(Connection connection, String catalog) throws Exception {
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getTables(catalog, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                String table = rs.getString("TABLE_NAME");
                if (table != null && !table.startsWith("qrtz_")) {
                    tables.add(table);
                }
            }
        }
        tables.sort(String::compareToIgnoreCase);
        return tables;
    }

    private void dumpTable(Connection connection, BufferedWriter writer, String table) throws Exception {
        writer.write("-- ----------------------------\n");
        writer.write("-- Table structure for `" + table + "`\n");
        writer.write("-- ----------------------------\n");
        writer.write("DROP TABLE IF EXISTS `" + table + "`;\n");
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SHOW CREATE TABLE `" + table + "`")) {
            if (rs.next()) {
                writer.write(rs.getString(2));
                writer.write(";\n\n");
            }
        }

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM `" + table + "`")) {
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();
            while (rs.next()) {
                writer.write("INSERT INTO `" + table + "` VALUES (");
                for (int i = 1; i <= columns; i++) {
                    if (i > 1) {
                        writer.write(", ");
                    }
                    Object value = rs.getObject(i);
                    writer.write(sqlValue(value));
                }
                writer.write(");\n");
            }
            writer.write("\n");
        }
    }

    private String sqlValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof Number) {
            return String.valueOf(value);
        }
        String text = String.valueOf(value)
                .replace("\\", "\\\\")
                .replace("'", "''")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
        return "'" + text + "'";
    }

    private void fillOperator(SysDataBackup backup) {
        try {
            Long userId = Long.valueOf(String.valueOf(StpUtil.getLoginId()));
            SysUser user = userMapper.selectById(userId);
            backup.setOperatorId(userId);
            backup.setOperatorName(user == null ? String.valueOf(userId) : user.getRealName());
        } catch (Exception ignored) {
            backup.setOperatorName("未知用户");
        }
    }

    private String limit(String text, int max) {
        if (text == null || text.length() <= max) {
            return text;
        }
        return text.substring(0, max);
    }
}
