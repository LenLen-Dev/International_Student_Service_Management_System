/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.config.entity
 * @className: SysDictType
 * @description: 系统字典类型实体
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
public class SysDictType {
    private Long id;
    private String dictName;
    private String dictCode;
    private String description;
    private Integer status;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
