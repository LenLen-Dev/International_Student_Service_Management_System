/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.vo
 * @className: StudentContactVO
 * @description: TODO 留学生联系人视图对象
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentContactVO {
    private Long id;
    private Long studentId;
    private String contactType;
    private String contactName;
    private String relationship;
    private String phone;
    private String email;
    private String address;
    private Integer isPrimary;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
