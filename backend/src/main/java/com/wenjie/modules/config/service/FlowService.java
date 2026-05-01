package com.wenjie.modules.config.service;

import com.wenjie.modules.config.dto.FlowNodeRequest;
import com.wenjie.modules.config.dto.FlowTemplateRequest;
import com.wenjie.modules.config.entity.SysFlowNode;
import com.wenjie.modules.config.entity.SysFlowTemplate;
import com.wenjie.modules.config.vo.FlowDetailVO;

import java.util.List;

public interface FlowService {
    List<SysFlowTemplate> list(String keyword, Integer status);
    Long create(FlowTemplateRequest request);
    FlowDetailVO detail(Long id);
    void update(Long id, FlowTemplateRequest request);
    void delete(Long id);
    void updateStatus(Long id, Integer status);
    List<SysFlowNode> listNodes(Long flowId);
    void saveNodes(Long flowId, List<FlowNodeRequest> nodes);
}
