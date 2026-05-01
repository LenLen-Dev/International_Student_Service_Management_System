/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.enums
 * @className: ContactTypeEnum
 * @description: TODO 联系人类型枚举
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
public enum ContactTypeEnum {
    EMERGENCY("紧急联系人"),
    FAMILY("家庭联系人"),
    GUARDIAN("监护人"),
    OTHER("其他");

    private final String label;
}
