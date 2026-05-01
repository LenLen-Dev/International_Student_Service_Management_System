/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.system.service
 * @className: MenuService
 * @description: 菜单权限管理业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.system.service;

import com.wenjie.modules.system.dto.MenuRequest;
import com.wenjie.modules.system.vo.MenuVO;

import java.util.List;

public interface MenuService {
    List<MenuVO> tree(String keyword, Integer status);
    Long create(MenuRequest request);
    void update(Long id, MenuRequest request);
    void delete(Long id);
}
