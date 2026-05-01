package com.wenjie.modules.config.service;

import com.wenjie.modules.config.dto.DictDataRequest;
import com.wenjie.modules.config.dto.DictTypeRequest;
import com.wenjie.modules.config.entity.SysDictData;
import com.wenjie.modules.config.entity.SysDictType;

import java.util.List;

public interface DictService {
    List<SysDictType> listTypes(String keyword, Integer status);
    Long createType(DictTypeRequest request);
    void updateType(Long id, DictTypeRequest request);
    void deleteType(Long id);
    void updateTypeStatus(Long id, Integer status);
    List<SysDictData> listData(String dictCode, Integer status);
    Long createData(DictDataRequest request);
    void updateData(Long id, DictDataRequest request);
    void deleteData(Long id);
    void updateDataStatus(Long id, Integer status);
}
