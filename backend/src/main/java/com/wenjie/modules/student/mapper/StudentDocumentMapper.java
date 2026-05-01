/**
 * @projectName: International_Student_Service_Management_System
 * @package: com.wenjie.modules.student.mapper
 * @className: StudentDocumentMapper
 * @description: TODO 留学生附件材料数据访问接口
 * @author: Wenjie
 * @createDate: 2026-04-30 00:00
 * @updateUser: Wenjie
 * @updateDate: 2026-04-30 00:00
 * @updateRemark: The modified content
 * @version: 1.0
 * <p>Copyright: Copyright (c) 2026 </p>
 */
package com.wenjie.modules.student.mapper;

import com.wenjie.modules.student.entity.StudentDocument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentDocumentMapper {
    int insert(StudentDocument document);
    int updateById(StudentDocument document);
    int deleteById(@Param("id") Long id);
    List<StudentDocument> selectByStudentId(@Param("studentId") Long studentId);
    StudentDocument selectById(@Param("id") Long id);
}
