/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.entity
 * @className: StudentContact
 * @description: TODO 留学生联系人实体
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentContact {
    /** 主键ID */
    private Long id;
    /** 留学生档案ID */
    private Long studentId;
    /** 联系人类型 */
    private String contactType;
    /** 联系人姓名 */
    private String contactName;
    /** 关系 */
    private String relationship;
    /** 电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 地址 */
    private String address;
    /** 是否主联系人 */
    private Integer isPrimary;
    /** 逻辑删除 */
    private Integer deleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
