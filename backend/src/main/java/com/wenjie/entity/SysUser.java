/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.entity
 * @className: SysUser
 * @description: 系统用户实体
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private String userType;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
