/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.mapper
 * @className: StudentContactMapper
 * @description: TODO 留学生联系人数据访问接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.mapper;

import com.wenjie.modules.student.entity.StudentContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentContactMapper {
    int insert(StudentContact contact);
    int updateById(StudentContact contact);
    int deleteById(@Param("id") Long id);
    List<StudentContact> selectByStudentId(@Param("studentId") Long studentId);
    StudentContact selectById(@Param("id") Long id);
}
