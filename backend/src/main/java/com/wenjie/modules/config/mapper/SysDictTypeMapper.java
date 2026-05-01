package com.wenjie.modules.config.mapper;

import com.wenjie.modules.config.entity.SysDictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDictTypeMapper {
    SysDictType selectById(@Param("id") Long id);
    SysDictType selectByCode(@Param("dictCode") String dictCode);
    List<SysDictType> selectList(@Param("keyword") String keyword, @Param("status") Integer status);
    int insert(SysDictType entity);
    int update(SysDictType entity);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int logicalDelete(@Param("id") Long id);
}
