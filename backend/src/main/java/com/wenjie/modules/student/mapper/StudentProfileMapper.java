/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.mapper
 * @className: StudentProfileMapper
 * @description: TODO 留学生档案数据访问接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.mapper;

import com.wenjie.modules.student.dto.StudentProfileQueryDTO;
import com.wenjie.modules.student.entity.StudentProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentProfileMapper {
    int insert(StudentProfile profile);
    int updateById(StudentProfile profile);
    int deleteById(@Param("id") Long id);
    StudentProfile selectById(@Param("id") Long id);
    List<StudentProfile> selectPage(@Param("query") StudentProfileQueryDTO query, @Param("offset") Integer offset, @Param("limit") Integer limit);
    long countPage(@Param("query") StudentProfileQueryDTO query);
    StudentProfile selectByStudentNo(@Param("studentNo") String studentNo);
    StudentProfile selectByUserId(@Param("userId") Long userId);
    int updateStatus(@Param("id") Long id, @Param("studentStatus") String studentStatus);
}
