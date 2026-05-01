package com.wenjie.modules.config.service.impl;

import com.wenjie.common.BusinessException;
import com.wenjie.modules.config.dto.FlowNodeRequest;
import com.wenjie.modules.config.dto.FlowTemplateRequest;
import com.wenjie.modules.config.entity.SysFlowNode;
import com.wenjie.modules.config.entity.SysFlowTemplate;
import com.wenjie.modules.config.mapper.SysFlowNodeMapper;
import com.wenjie.modules.config.mapper.SysFlowTemplateMapper;
import com.wenjie.modules.config.service.FlowService;
import com.wenjie.modules.config.vo.FlowDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {

    private final SysFlowTemplateMapper flowMapper;
    private final SysFlowNodeMapper nodeMapper;

    @Override
    public List<SysFlowTemplate> list(String keyword, Integer status) {
        return flowMapper.selectList(keyword, status);
    }

    @Override
    public Long create(FlowTemplateRequest request) {
        if (flowMapper.selectByCode(request.getFlowCode()) != null) {
            throw new BusinessException("流程编码已存在");
        }
        SysFlowTemplate entity = toEntity(null, request);
        flowMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public FlowDetailVO detail(Long id) {
        FlowDetailVO vo = new FlowDetailVO();
        vo.setFlow(ensureFlow(id));
        vo.setNodes(nodeMapper.selectByFlowId(id));
        return vo;
    }

    @Override
    public void update(Long id, FlowTemplateRequest request) {
        ensureFlow(id);
        SysFlowTemplate same = flowMapper.selectByCode(request.getFlowCode());
        if (same != null && !same.getId().equals(id)) {
            throw new BusinessException("流程编码已存在");
        }
        flowMapper.update(toEntity(id, request));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ensureFlow(id);
        flowMapper.logicalDelete(id);
        nodeMapper.logicalDeleteByFlowId(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        ensureFlow(id);
        flowMapper.updateStatus(id, status);
    }

    @Override
    public List<SysFlowNode> listNodes(Long flowId) {
        ensureFlow(flowId);
        return nodeMapper.selectByFlowId(flowId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNodes(Long flowId, List<FlowNodeRequest> nodes) {
        ensureFlow(flowId);
        nodeMapper.logicalDeleteByFlowId(flowId);
        for (FlowNodeRequest request : nodes) {
            SysFlowNode node = new SysFlowNode();
            node.setFlowId(flowId);
            node.setNodeName(request.getNodeName());
            node.setNodeCode(request.getNodeCode());
            node.setNodeType(request.getNodeType());
            node.setApproverRoleCode(request.getApproverRoleCode());
            node.setDescription(request.getDescription());
            node.setStatus(request.getStatus() == null ? 1 : request.getStatus());
            node.setSort(request.getSort() == null ? 0 : request.getSort());
            nodeMapper.insert(node);
        }
    }

    private SysFlowTemplate ensureFlow(Long id) {
        SysFlowTemplate flow = flowMapper.selectById(id);
        if (flow == null) {
            throw new BusinessException(404, "流程模板不存在");
        }
        return flow;
    }

    private SysFlowTemplate toEntity(Long id, FlowTemplateRequest request) {
        SysFlowTemplate entity = new SysFlowTemplate();
        entity.setId(id);
        entity.setFlowName(request.getFlowName());
        entity.setFlowCode(request.getFlowCode());
        entity.setBusinessType(request.getBusinessType());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        entity.setSort(request.getSort() == null ? 0 : request.getSort());
        return entity;
    }
}
