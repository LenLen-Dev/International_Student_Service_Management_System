package com.wenjie.modules.config.mapper;

import com.wenjie.modules.config.entity.SysFlowTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysFlowTemplateMapper {
    SysFlowTemplate selectById(@Param("id") Long id);
    SysFlowTemplate selectByCode(@Param("flowCode") String flowCode);
    List<SysFlowTemplate> selectList(@Param("keyword") String keyword, @Param("status") Integer status);
    int insert(SysFlowTemplate entity);
    int update(SysFlowTemplate entity);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int logicalDelete(@Param("id") Long id);
}
