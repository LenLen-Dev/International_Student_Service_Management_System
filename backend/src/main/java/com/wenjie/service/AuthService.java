/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.service
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
package com.wenjie.service;

import com.wenjie.dto.LoginRequest;
import com.wenjie.vo.LoginVO;
import com.wenjie.vo.UserInfoVO;

public interface AuthService {
    LoginVO login(LoginRequest request);
    UserInfoVO info();
    void logout();
}
