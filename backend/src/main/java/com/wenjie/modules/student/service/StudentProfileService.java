/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.service
 * @className: StudentProfileService
 * @description: TODO 留学生档案业务接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.service;

import com.wenjie.common.PageResult;
import com.wenjie.modules.student.dto.StudentProfileCreateDTO;
import com.wenjie.modules.student.dto.StudentProfileQueryDTO;
import com.wenjie.modules.student.dto.StudentProfileUpdateDTO;
import com.wenjie.modules.student.dto.StudentStatusUpdateDTO;
import com.wenjie.modules.student.vo.StudentProfileDetailVO;
import com.wenjie.modules.student.vo.StudentProfileListVO;

public interface StudentProfileService {
    PageResult<StudentProfileListVO> pageProfiles(StudentProfileQueryDTO query);
    Long createProfile(StudentProfileCreateDTO dto);
    void updateProfile(Long id, StudentProfileUpdateDTO dto);
    void deleteProfile(Long id);
    StudentProfileDetailVO getProfileDetail(Long id);
    void updateProfileStatus(Long id, StudentStatusUpdateDTO dto);
    StudentProfileDetailVO getMyProfile();
    void updateMyProfile(StudentProfileUpdateDTO dto);
}
