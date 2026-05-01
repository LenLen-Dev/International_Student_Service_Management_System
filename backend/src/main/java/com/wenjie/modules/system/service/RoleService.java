/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.service
 * @className: RoleService
 * @description: 角色管理业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.service;

import com.wenjie.modules.system.dto.AssignMenusRequest;
import com.wenjie.modules.system.dto.RoleRequest;
import com.wenjie.modules.system.vo.RoleVO;

import java.util.List;

public interface RoleService {
    List<RoleVO> list(String keyword, Integer status);
    Long create(RoleRequest request);
    void update(Long id, RoleRequest request);
    void delete(Long id);
    void assignMenus(Long id, AssignMenusRequest request);
}
