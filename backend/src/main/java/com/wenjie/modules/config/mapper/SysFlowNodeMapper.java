package com.wenjie.modules.config.mapper;

import com.wenjie.modules.config.entity.SysFlowNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysFlowNodeMapper {
    List<SysFlowNode> selectByFlowId(@Param("flowId") Long flowId);
    int insert(SysFlowNode entity);
    int logicalDeleteByFlowId(@Param("flowId") Long flowId);
}
