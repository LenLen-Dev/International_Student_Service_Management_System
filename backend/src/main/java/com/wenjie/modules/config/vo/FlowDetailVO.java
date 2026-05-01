package com.wenjie.modules.config.vo;

import com.wenjie.modules.config.entity.SysFlowNode;
import com.wenjie.modules.config.entity.SysFlowTemplate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FlowDetailVO {
    private SysFlowTemplate flow;
    private List<SysFlowNode> nodes = new ArrayList<>();
}
