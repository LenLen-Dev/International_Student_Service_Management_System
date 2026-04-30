/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.service
 * @className: UserService
 * @description: 用户管理业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.service;

import com.wenjie.dto.AssignRolesRequest;
import com.wenjie.dto.UserCreateRequest;
import com.wenjie.dto.UserStatusRequest;
import com.wenjie.dto.UserUpdateRequest;
import com.wenjie.vo.UserVO;

import java.util.List;

public interface UserService {
    List<UserVO> list(String keyword, String userType, Integer status);
    Long create(UserCreateRequest request);
    void update(Long id, UserUpdateRequest request);
    void delete(Long id);
    void updateStatus(Long id, UserStatusRequest request);
    void assignRoles(Long id, AssignRolesRequest request);
}
