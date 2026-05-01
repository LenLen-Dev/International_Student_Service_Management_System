package com.wenjie.modules.config.mapper;

import com.wenjie.modules.config.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDictDataMapper {
    SysDictData selectById(@Param("id") Long id);
    SysDictData selectByCodeValue(@Param("dictCode") String dictCode, @Param("dictValue") String dictValue);
    List<SysDictData> selectByDictCode(@Param("dictCode") String dictCode, @Param("status") Integer status);
    int insert(SysDictData entity);
    int update(SysDictData entity);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int logicalDelete(@Param("id") Long id);
    int logicalDeleteByDictCode(@Param("dictCode") String dictCode);
}
