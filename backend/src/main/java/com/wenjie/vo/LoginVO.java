/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.vo
 * @className: LoginVO
 * @description: 登录成功响应视图对象
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginVO {
    private String token;
    private String tokenType;
    private Long expiresIn;
    private UserInfoVO userInfo;
}
