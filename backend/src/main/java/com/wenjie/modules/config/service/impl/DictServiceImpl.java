package com.wenjie.modules.config.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.modules.config.dto.DictDataRequest;
import com.wenjie.modules.config.dto.DictTypeRequest;
import com.wenjie.modules.config.entity.SysDictData;
import com.wenjie.modules.config.entity.SysDictType;
import com.wenjie.modules.config.mapper.SysDictDataMapper;
import com.wenjie.modules.config.mapper.SysDictTypeMapper;
import com.wenjie.modules.config.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final SysDictTypeMapper dictTypeMapper;
    private final SysDictDataMapper dictDataMapper;

    @Override
    public List<SysDictType> listTypes(String keyword, Integer status) {
        return dictTypeMapper.selectList(keyword, status);
    }

    @Override
    public Long createType(DictTypeRequest request) {
        if (dictTypeMapper.selectByCode(request.getDictCode()) != null) {
            throw new BusinessException("字典编码已存在");
        }
        SysDictType entity = toTypeEntity(null, request);
        dictTypeMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void updateType(Long id, DictTypeRequest request) {
        ensureType(id);
        SysDictType same = dictTypeMapper.selectByCode(request.getDictCode());
        if (same != null && !same.getId().equals(id)) {
            throw new BusinessException("字典编码已存在");
        }
        dictTypeMapper.update(toTypeEntity(id, request));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteType(Long id) {
        SysDictType type = ensureType(id);
        dictTypeMapper.logicalDelete(id);
        dictDataMapper.logicalDeleteByDictCode(type.getDictCode());
    }

    @Override
    public void updateTypeStatus(Long id, Integer status) {
        ensureType(id);
        dictTypeMapper.updateStatus(id, status);
    }

    @Override
    public List<SysDictData> listData(String dictCode, Integer status) {
        ensureType(dictCode);
        return dictDataMapper.selectByDictCode(dictCode, status);
    }

    @Override
    public Long createData(DictDataRequest request) {
        ensureType(request.getDictCode());
        if (dictDataMapper.selectByCodeValue(request.getDictCode(), request.getDictValue()) != null) {
            throw new BusinessException("字典值已存在");
        }
        SysDictData entity = toDataEntity(null, request);
        dictDataMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void updateData(Long id, DictDataRequest request) {
        ensureData(id);
        ensureType(request.getDictCode());
        SysDictData same = dictDataMapper.selectByCodeValue(request.getDictCode(), request.getDictValue());
        if (same != null && !same.getId().equals(id)) {
            throw new BusinessException("字典值已存在");
        }
        dictDataMapper.update(toDataEntity(id, request));
    }

    @Override
    public void deleteData(Long id) {
        ensureData(id);
        dictDataMapper.logicalDelete(id);
    }

    @Override
    public void updateDataStatus(Long id, Integer status) {
        ensureData(id);
        dictDataMapper.updateStatus(id, status);
    }

    private SysDictType ensureType(Long id) {
        SysDictType type = dictTypeMapper.selectById(id);
        if (type == null) {
            throw new BusinessException(404, "字典类型不存在");
        }
        return type;
    }

    private SysDictType ensureType(String dictCode) {
        SysDictType type = dictTypeMapper.selectByCode(dictCode);
        if (type == null) {
            throw new BusinessException(404, "字典类型不存在");
        }
        return type;
    }

    private SysDictData ensureData(Long id) {
        SysDictData data = dictDataMapper.selectById(id);
        if (data == null) {
            throw new BusinessException(404, "字典项不存在");
        }
        return data;
    }

    private SysDictType toTypeEntity(Long id, DictTypeRequest request) {
        SysDictType entity = new SysDictType();
        entity.setId(id);
        entity.setDictName(request.getDictName());
        entity.setDictCode(request.getDictCode());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        entity.setSort(request.getSort() == null ? 0 : request.getSort());
        return entity;
    }

    private SysDictData toDataEntity(Long id, DictDataRequest request) {
        SysDictData entity = new SysDictData();
        entity.setId(id);
        entity.setDictCode(request.getDictCode());
        entity.setDictLabel(request.getDictLabel());
        entity.setDictValue(request.getDictValue());
        entity.setTagType(request.getTagType());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        entity.setSort(request.getSort() == null ? 0 : request.getSort());
        return entity;
    }
}
