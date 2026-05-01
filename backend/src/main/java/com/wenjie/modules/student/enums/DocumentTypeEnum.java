/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.enums
 * @className: DocumentTypeEnum
 * @description: TODO 材料类型枚举
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
public enum DocumentTypeEnum {
    PASSPORT("护照"),
    PHOTO("照片"),
    ADMISSION_NOTICE("录取通知书"),
    DEGREE_CERTIFICATE("学历证明"),
    LANGUAGE_SCORE("语言成绩"),
    PHYSICAL_EXAM("体检证明"),
    INSURANCE("保险材料"),
    OTHER("其他");

    private final String label;
}
