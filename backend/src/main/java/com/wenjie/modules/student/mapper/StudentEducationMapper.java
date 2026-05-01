/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.mapper
 * @className: StudentEducationMapper
 * @description: TODO 留学生教育背景数据访问接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.mapper;

import com.wenjie.modules.student.entity.StudentEducation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentEducationMapper {
    int insert(StudentEducation education);
    int updateById(StudentEducation education);
    int deleteById(@Param("id") Long id);
    List<StudentEducation> selectByStudentId(@Param("studentId") Long studentId);
    StudentEducation selectById(@Param("id") Long id);
}
