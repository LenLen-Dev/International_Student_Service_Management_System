/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.enums
 * @className: GenderEnum
 * @description: TODO 性别枚举
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {
    MALE("男"),
    FEMALE("女"),
    UNKNOWN("未知");

    private final String label;
}
