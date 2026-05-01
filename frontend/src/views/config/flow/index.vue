<template>
  <div class="page-container flow-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="80px">
        <el-form-item label="关键词">
          <el-input v-model.trim="query.keyword" placeholder="流程名称 / 编码 / 业务类型" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="loadFlows">查询</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <el-button v-permission="'config:flow:add'" type="primary" :icon="Plus" @click="openFlowDialog()">新增流程</el-button>
      </div>
      <el-table v-loading="loading" :data="flows" border stripe>
        <el-table-column prop="flowName" label="流程名称" min-width="150" />
        <el-table-column prop="flowCode" label="流程编码" min-width="160" />
        <el-table-column prop="businessType" label="业务类型" min-width="140" />
        <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'config:flow:update'" link type="primary" @click="openFlowDialog(row)">编辑</el-button>
            <el-button v-permission="'config:flow:nodes'" link type="primary" @click="openNodeDialog(row)">节点</el-button>
            <el-button v-permission="'config:flow:status'" link type="warning" @click="toggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button v-permission="'config:flow:delete'" link type="danger" @click="removeFlow(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="flowDialogVisible" :title="flowForm.id ? '编辑流程模板' : '新增流程模板'" width="560px" @closed="resetFlowForm">
      <el-form ref="flowFormRef" :model="flowForm" :rules="flowRules" label-width="96px">
        <el-form-item label="流程名称" prop="flowName"><el-input v-model.trim="flowForm.flowName" /></el-form-item>
        <el-form-item label="流程编码" prop="flowCode"><el-input v-model.trim="flowForm.flowCode" /></el-form-item>
        <el-form-item label="业务类型"><el-input v-model.trim="flowForm.businessType" placeholder="如 admission / student_profile" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="flowForm.sort" :min="0" controls-position="right" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="flowForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model.trim="flowForm.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="flowDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitFlow">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="nodeDialogVisible" :title="`节点配置 - ${currentFlow?.flowName || ''}`" width="960px" @closed="resetNodeDialog">
      <div class="node-toolbar">
        <el-button type="primary" :icon="Plus" @click="addNode">新增节点</el-button>
      </div>
      <el-table :data="nodes" border>
        <el-table-column label="节点名称" min-width="150">
          <template #default="{ row }"><el-input v-model.trim="row.nodeName" /></template>
        </el-table-column>
        <el-table-column label="节点编码" min-width="140">
          <template #default="{ row }"><el-input v-model.trim="row.nodeCode" /></template>
        </el-table-column>
        <el-table-column label="节点类型" width="150">
          <template #default="{ row }">
            <el-select v-model="row.nodeType">
              <el-option label="开始" value="START" />
              <el-option label="审批" value="APPROVAL" />
              <el-option label="结束" value="END" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="审批角色" min-width="150">
          <template #default="{ row }"><el-input v-model.trim="row.approverRoleCode" placeholder="角色编码" /></template>
        </el-table-column>
        <el-table-column label="排序" width="110">
          <template #default="{ row }"><el-input-number v-model="row.sort" :min="0" controls-position="right" /></template>
        </el-table-column>
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template #default="{ $index }">
            <el-button link type="danger" @click="nodes.splice($index, 1)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="nodeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitNodes">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { createFlow, deleteFlow, getFlowList, getFlowNodes, saveFlowNodes, updateFlow, updateFlowStatus } from '@/api/config/flow'
import type { FlowNode, FlowTemplate, FlowTemplateForm } from '@/types/config'

const query = reactive({ keyword: '', status: '' as number | string })
const flows = ref<FlowTemplate[]>([])
const nodes = ref<FlowNode[]>([])
const currentFlow = ref<FlowTemplate | null>(null)
const loading = ref(false)
const submitLoading = ref(false)
const flowDialogVisible = ref(false)
const nodeDialogVisible = ref(false)
const flowFormRef = ref<FormInstance>()
const flowForm = reactive<FlowTemplateForm>({ flowName: '', flowCode: '', businessType: '', description: '', status: 1, sort: 0 })

const flowRules: FormRules<FlowTemplateForm> = {
  flowName: [{ required: true, message: '请输入流程名称', trigger: 'blur' }],
  flowCode: [{ required: true, message: '请输入流程编码', trigger: 'blur' }]
}

async function loadFlows() {
  loading.value = true
  try {
    flows.value = await getFlowList({ ...query, status: query.status === '' ? undefined : query.status })
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  Object.assign(query, { keyword: '', status: '' })
  loadFlows()
}

function openFlowDialog(row?: FlowTemplate) {
  Object.assign(flowForm, row || { id: undefined, flowName: '', flowCode: '', businessType: '', description: '', status: 1, sort: 0 })
  flowDialogVisible.value = true
  nextTick(() => flowFormRef.value?.clearValidate())
}

function resetFlowForm() {
  Object.assign(flowForm, { id: undefined, flowName: '', flowCode: '', businessType: '', description: '', status: 1, sort: 0 })
}

async function submitFlow() {
  await flowFormRef.value?.validate()
  submitLoading.value = true
  try {
    if (flowForm.id) {
      await updateFlow(flowForm.id, flowForm)
      ElMessage.success('流程模板已更新')
    } else {
      await createFlow(flowForm)
      ElMessage.success('流程模板已新增')
    }
    flowDialogVisible.value = false
    await loadFlows()
  } finally {
    submitLoading.value = false
  }
}

async function toggleStatus(row: FlowTemplate) {
  await updateFlowStatus(row.id, row.status === 1 ? 0 : 1)
  ElMessage.success('状态已更新')
  await loadFlows()
}

async function removeFlow(row: FlowTemplate) {
  await ElMessageBox.confirm(`确认删除流程模板「${row.flowName}」吗？`, '删除确认', { type: 'warning' })
  await deleteFlow(row.id)
  ElMessage.success('流程模板已删除')
  await loadFlows()
}

async function openNodeDialog(row: FlowTemplate) {
  currentFlow.value = row
  nodes.value = await getFlowNodes(row.id)
  nodeDialogVisible.value = true
}

function addNode() {
  nodes.value.push({ nodeName: '', nodeCode: '', nodeType: 'APPROVAL', approverRoleCode: '', description: '', status: 1, sort: nodes.value.length + 1 })
}

function resetNodeDialog() {
  currentFlow.value = null
  nodes.value = []
}

async function submitNodes() {
  if (!currentFlow.value) return
  if (nodes.value.some((item) => !item.nodeName || !item.nodeType)) {
    ElMessage.warning('请完整填写节点名称和节点类型')
    return
  }
  submitLoading.value = true
  try {
    await saveFlowNodes(currentFlow.value.id, nodes.value)
    ElMessage.success('节点配置已保存')
    nodeDialogVisible.value = false
  } finally {
    submitLoading.value = false
  }
}

onMounted(loadFlows)
</script>

<style scoped lang="scss">
.flow-page {
  :deep(.el-select),
  :deep(.el-input-number) {
    width: 100%;
  }
}

.node-toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 12px;
}
</style>
