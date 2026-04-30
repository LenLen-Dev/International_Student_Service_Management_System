/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.vo
 * @className: UserInfoVO
 * @description: 当前登录用户信息响应视图对象
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {
    private Long id;
    private String username;
    private String realName;
    private String userType;
    private List<String> roles;
    private List<String> permissions;
    private List<MenuVO> menus;
}
