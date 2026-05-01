<template>
  <div class="page-container operation-log-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="80px">
        <el-form-item label="模块">
          <el-input v-model.trim="query.moduleName" placeholder="请输入模块名称" clearable />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="query.operationType" placeholder="全部类型" clearable>
            <el-option label="新增" value="CREATE" />
            <el-option label="编辑" value="UPDATE" />
            <el-option label="删除" value="DELETE" />
            <el-option label="状态" value="STATUS" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作人">
          <el-input v-model.trim="query.operatorName" placeholder="请输入操作人" clearable />
        </el-form-item>
        <el-form-item label="结果">
          <el-select v-model="query.success" placeholder="全部结果" clearable>
            <el-option label="成功" :value="1" />
            <el-option label="失败" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            value-format="YYYY-MM-DD HH:mm:ss"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <el-button v-permission="'audit:operation-log:export'" type="primary" plain :icon="Download" @click="handleExport">导出日志</el-button>
      </div>
      <el-table v-loading="loading" :data="logs" border stripe>
        <el-table-column prop="moduleName" label="模块" min-width="120" />
        <el-table-column prop="operationType" label="类型" width="90" align="center" />
        <el-table-column prop="operationName" label="操作名称" min-width="150" />
        <el-table-column prop="operatorName" label="操作人" min-width="110" />
        <el-table-column prop="requestMethod" label="方法" width="90" align="center" />
        <el-table-column prop="requestUri" label="请求地址" min-width="220" show-overflow-tooltip />
        <el-table-column label="结果" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.success === 1 ? 'success' : 'danger'">{{ row.success === 1 ? '成功' : '失败' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="costTime" label="耗时(ms)" width="110" align="center" />
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'audit:operation-log:detail'" link type="primary" @click="openDetail(row.id)">详情</el-button>
            <el-button v-permission="'audit:operation-log:delete'" link type="danger" @click="removeLog(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination
          v-model:current-page="pageQuery.pageNum"
          v-model:page-size="pageQuery.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadLogs"
          @current-change="loadLogs"
        />
      </div>
    </el-card>

    <el-dialog v-model="detailVisible" title="操作日志详情" width="860px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="模块">{{ detail.moduleName }}</el-descriptions-item>
        <el-descriptions-item label="操作">{{ detail.operationName }}</el-descriptions-item>
        <el-descriptions-item label="操作人">{{ detail.operatorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="结果">{{ detail.success === 1 ? '成功' : '失败' }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">{{ detail.requestMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请求地址">{{ detail.requestUri || '-' }}</el-descriptions-item>
        <el-descriptions-item label="IP">{{ detail.ipAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ detail.costTime || 0 }} ms</el-descriptions-item>
        <el-descriptions-item label="控制器" :span="2">{{ detail.controllerMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="User-Agent" :span="2">{{ detail.userAgent || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-tabs v-if="detail" class="log-tabs">
        <el-tab-pane label="请求参数">
          <pre>{{ detail.requestParams || '-' }}</pre>
        </el-tab-pane>
        <el-tab-pane label="响应结果">
          <pre>{{ detail.responseResult || '-' }}</pre>
        </el-tab-pane>
        <el-tab-pane label="异常信息">
          <pre>{{ detail.errorMessage || '-' }}</pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Download, Refresh, Search } from '@element-plus/icons-vue'
import { deleteOperationLog, exportOperationLogs, getOperationLogDetail, getOperationLogPage } from '@/api/config/log'
import type { OperationLog, OperationLogQuery } from '@/types/config'

const query = reactive<OperationLogQuery>({ pageNum: 1, pageSize: 10, success: '' })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const dateRange = ref<string[]>([])
const logs = ref<OperationLog[]>([])
const detail = ref<OperationLog | null>(null)
const total = ref(0)
const loading = ref(false)
const detailVisible = ref(false)

function buildQuery(): OperationLogQuery {
  return {
    ...query,
    pageNum: pageQuery.pageNum,
    pageSize: pageQuery.pageSize,
    success: query.success === '' ? undefined : query.success,
    startTime: dateRange.value?.[0],
    endTime: dateRange.value?.[1]
  }
}

async function loadLogs() {
  loading.value = true
  try {
    const result = await getOperationLogPage(buildQuery())
    logs.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageQuery.pageNum = 1
  loadLogs()
}

function handleReset() {
  Object.assign(query, { moduleName: '', operationType: '', operatorName: '', success: '' })
  dateRange.value = []
  pageQuery.pageNum = 1
  loadLogs()
}

async function openDetail(id: number) {
  detail.value = await getOperationLogDetail(id)
  detailVisible.value = true
}

async function removeLog(row: OperationLog) {
  await ElMessageBox.confirm(`确认删除操作日志「${row.operationName}」吗？`, '删除确认', { type: 'warning' })
  await deleteOperationLog(row.id)
  ElMessage.success('操作日志已删除')
  await loadLogs()
}

async function handleExport() {
  const blob = await exportOperationLogs(buildQuery())
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `operation-logs-${Date.now()}.csv`
  link.click()
  URL.revokeObjectURL(url)
}

onMounted(loadLogs)
</script>

<style scoped lang="scss">
.operation-log-page {
  :deep(.el-select),
  :deep(.el-date-editor) {
    width: 100%;
  }
}

.log-tabs {
  margin-top: 16px;

  pre {
    max-height: 260px;
    margin: 0;
    padding: 12px;
    overflow: auto;
    color: #334155;
    white-space: pre-wrap;
    background: #f8fafc;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
  }
}
</style>
