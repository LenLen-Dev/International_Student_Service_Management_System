/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.service
 * @className: AuthService
 * @description: 认证授权业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.service;

import com.wenjie.modules.system.dto.LoginRequest;
import com.wenjie.modules.system.vo.LoginVO;
import com.wenjie.modules.system.vo.UserInfoVO;

public interface AuthService {
    LoginVO login(LoginRequest request);
    UserInfoVO info();
    void logout();
}
