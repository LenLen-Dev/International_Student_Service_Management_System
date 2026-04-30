/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.security
 * @className: AuthUserContext
 * @description: 当前登录用户上下文工具
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.security;

import cn.dev33.satoken.stp.StpUtil;

public final class AuthUserContext {

    private AuthUserContext() {
    }

    public static Long currentUserId() {
        return Long.valueOf(String.valueOf(StpUtil.getLoginId()));
    }
}
