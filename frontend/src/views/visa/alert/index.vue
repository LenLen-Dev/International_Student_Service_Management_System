<template>
  <div class="page-container visa-alert-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="84px">
        <el-form-item label="学生"><el-input v-model.trim="query.studentKeyword" placeholder="学号 / 姓名" clearable /></el-form-item>
        <el-form-item label="预警类型"><el-select v-model="query.alertType" placeholder="全部类型" clearable><el-option v-for="item in alertTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="预警等级"><el-select v-model="query.alertLevel" placeholder="全部等级" clearable><el-option v-for="item in alertLevelOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="处理状态"><el-select v-model="query.alertStatus" placeholder="全部状态" clearable><el-option v-for="item in alertStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="目标类型">
          <el-select v-model="query.targetType" placeholder="全部目标" clearable>
            <el-option label="签证" value="VISA" />
            <el-option label="居留许可" value="PERMIT" />
            <el-option label="学生档案" value="STUDENT" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button v-permission="'visa:alert:generate'" type="primary" :icon="RefreshRight" :loading="generateLoading" @click="handleGenerate">手动生成预警</el-button>
          <el-button v-permission="'visa:notification:list'" :icon="Bell" @click="openNotificationDrawer">通知记录</el-button>
        </div>
      </div>
      <el-table v-loading="loading" :data="alerts" border stripe>
        <el-table-column prop="studentNo" label="学号" min-width="120" />
        <el-table-column label="学生姓名" min-width="150"><template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template></el-table-column>
        <el-table-column prop="title" label="预警标题" min-width="180" show-overflow-tooltip />
        <el-table-column label="类型" width="110" align="center"><template #default="{ row }">{{ formatOption(alertTypeOptions, row.alertType) }}</template></el-table-column>
        <el-table-column label="等级" width="100" align="center"><template #default="{ row }"><el-tag :type="optionTag(alertLevelOptions, row.alertLevel)">{{ formatOption(alertLevelOptions, row.alertLevel) }}</el-tag></template></el-table-column>
        <el-table-column label="目标" width="110" align="center"><template #default="{ row }">{{ targetText(row.targetType) }}</template></el-table-column>
        <el-table-column prop="expireDate" label="到期日期" width="120" align="center" />
        <el-table-column prop="remainingDays" label="剩余天数" width="100" align="center" />
        <el-table-column label="状态" width="110" align="center"><template #default="{ row }"><el-tag :type="optionTag(alertStatusOptions, row.alertStatus)">{{ formatOption(alertStatusOptions, row.alertStatus) }}</el-tag></template></el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'visa:alert:detail'" link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-permission="'visa:alert:resolve'" link type="success" :disabled="['RESOLVED', 'CLOSED'].includes(row.alertStatus)" @click="openResolveDialog(row)">处理</el-button>
            <el-button v-permission="'visa:alert:notify'" link type="warning" @click="sendNotify(row)">提醒</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadAlerts" @current-change="loadAlerts" />
      </div>
    </el-card>

    <el-dialog v-model="resolveDialogVisible" title="处理合规预警" width="560px" @closed="resetResolveDialog">
      <el-form ref="resolveFormRef" :model="resolveForm" :rules="resolveRules" label-width="96px">
        <el-form-item label="处理状态" prop="alertStatus"><el-select v-model="resolveForm.alertStatus"><el-option label="处理中" value="PROCESSING" /><el-option label="已处理" value="RESOLVED" /><el-option label="已关闭" value="CLOSED" /></el-select></el-form-item>
        <el-form-item label="处理说明"><el-input v-model.trim="resolveForm.handleRemark" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resolveDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitResolve">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="预警详情" width="760px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="学号">{{ detail.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.chineseName || detail.englishName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预警类型">{{ formatOption(alertTypeOptions, detail.alertType) }}</el-descriptions-item>
        <el-descriptions-item label="预警等级">{{ formatOption(alertLevelOptions, detail.alertLevel) }}</el-descriptions-item>
        <el-descriptions-item label="目标类型">{{ targetText(detail.targetType) }}</el-descriptions-item>
        <el-descriptions-item label="目标ID">{{ detail.targetId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ detail.expireDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="剩余天数">{{ detail.remainingDays ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">{{ formatOption(alertStatusOptions, detail.alertStatus) }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detail.handlerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">{{ detail.content || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理说明" :span="2">{{ detail.handleRemark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-drawer v-model="notificationDrawerVisible" title="提醒通知记录" size="70%">
      <el-table v-loading="notificationLoading" :data="notifications" border stripe>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column label="渠道" width="110"><template #default="{ row }">{{ formatOption(channelOptions, row.channel) }}</template></el-table-column>
        <el-table-column label="发送状态" width="110"><template #default="{ row }"><el-tag :type="optionTag(notificationStatusOptions, row.sendStatus)">{{ formatOption(notificationStatusOptions, row.sendStatus) }}</el-tag></template></el-table-column>
        <el-table-column label="已读" width="90" align="center"><template #default="{ row }">{{ row.readStatus === 1 ? '已读' : '未读' }}</template></el-table-column>
        <el-table-column prop="recipientName" label="接收人" width="120" />
        <el-table-column prop="sendTime" label="发送时间" min-width="170" show-overflow-tooltip />
        <el-table-column prop="errorMessage" label="错误信息" min-width="180" show-overflow-tooltip />
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="notificationPage.pageNum" v-model:page-size="notificationPage.pageSize" :page-sizes="[10, 20, 50]" :total="notificationTotal" layout="total, sizes, prev, pager, next, jumper" @size-change="loadNotifications" @current-change="loadNotifications" />
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Bell, Refresh, RefreshRight, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { generateVisaAlerts, getVisaAlertPage, getVisaNotificationPage, notifyVisaAlert, resolveVisaAlert } from '@/api/visa/alert'
import type { DictOption } from '@/types/config'
import type { VisaAlertQuery, VisaAlertResolveForm, VisaComplianceAlert, VisaReminderNotification } from '@/types/visa'

const alertTypeOptions = ref<DictOption[]>([
  { label: '即将到期', value: 'EXPIRING', tagType: 'warning' },
  { label: '已逾期', value: 'EXPIRED', tagType: 'danger' },
  { label: '资料缺失', value: 'DATA_MISSING', tagType: 'warning' },
  { label: '状态异常', value: 'STATUS_ABNORMAL', tagType: 'danger' }
])
const alertLevelOptions = ref<DictOption[]>([
  { label: '低', value: 'LOW', tagType: 'info' },
  { label: '中', value: 'MEDIUM', tagType: 'warning' },
  { label: '高', value: 'HIGH', tagType: 'danger' }
])
const alertStatusOptions = ref<DictOption[]>([
  { label: '待处理', value: 'OPEN', tagType: 'danger' },
  { label: '处理中', value: 'PROCESSING', tagType: 'warning' },
  { label: '已处理', value: 'RESOLVED', tagType: 'success' },
  { label: '已关闭', value: 'CLOSED', tagType: 'info' }
])
const channelOptions = ref<DictOption[]>([
  { label: '站内通知', value: 'IN_APP' },
  { label: '邮件', value: 'EMAIL' }
])
const notificationStatusOptions = ref<DictOption[]>([
  { label: '待发送', value: 'PENDING', tagType: 'warning' },
  { label: '已发送', value: 'SENT', tagType: 'success' },
  { label: '发送失败', value: 'FAILED', tagType: 'danger' }
])

const query = reactive<VisaAlertQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const notificationPage = reactive({ pageNum: 1, pageSize: 10 })
const alerts = ref<VisaComplianceAlert[]>([])
const notifications = ref<VisaReminderNotification[]>([])
const total = ref(0)
const notificationTotal = ref(0)
const loading = ref(false)
const notificationLoading = ref(false)
const generateLoading = ref(false)
const submitLoading = ref(false)
const resolveDialogVisible = ref(false)
const detailVisible = ref(false)
const notificationDrawerVisible = ref(false)
const currentAlert = ref<VisaComplianceAlert>()
const detail = ref<VisaComplianceAlert>()
const resolveFormRef = ref<FormInstance>()
const resolveForm = reactive<VisaAlertResolveForm>({ alertStatus: 'RESOLVED', handleRemark: '' })
const resolveRules: FormRules<VisaAlertResolveForm> = {
  alertStatus: [{ required: true, message: '请选择处理状态', trigger: 'change' }]
}

async function loadOptions() {
  const mappings: Array<[string, typeof alertTypeOptions]> = [
    ['visa_alert_type', alertTypeOptions],
    ['visa_alert_level', alertLevelOptions],
    ['visa_alert_status', alertStatusOptions],
    ['visa_notification_channel', channelOptions],
    ['visa_notification_status', notificationStatusOptions]
  ]
  await Promise.all(mappings.map(async ([code, target]) => {
    try {
      const options = await getDictOptions(code)
      if (options.length) target.value = options
    } catch {
      // 使用本地兜底选项。
    }
  }))
}

function buildQuery(): VisaAlertQuery {
  return { ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize, alertType: query.alertType || undefined, alertLevel: query.alertLevel || undefined, alertStatus: query.alertStatus || undefined, targetType: query.targetType || undefined }
}

async function loadAlerts() {
  loading.value = true
  try {
    const result = await getVisaAlertPage(buildQuery())
    alerts.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageQuery.pageNum = 1
  loadAlerts()
}

function handleReset() {
  Object.assign(query, { studentKeyword: '', alertType: '', alertLevel: '', alertStatus: '', targetType: '' })
  pageQuery.pageNum = 1
  loadAlerts()
}

async function handleGenerate() {
  generateLoading.value = true
  try {
    const count = await generateVisaAlerts()
    ElMessage.success(`已生成 ${count} 条预警`)
    await loadAlerts()
  } finally {
    generateLoading.value = false
  }
}

function openResolveDialog(row: VisaComplianceAlert) {
  currentAlert.value = row
  Object.assign(resolveForm, { alertStatus: 'RESOLVED', handleRemark: '' })
  resolveDialogVisible.value = true
  nextTick(() => resolveFormRef.value?.clearValidate())
}

function resetResolveDialog() {
  currentAlert.value = undefined
  Object.assign(resolveForm, { alertStatus: 'RESOLVED', handleRemark: '' })
}

async function submitResolve() {
  await resolveFormRef.value?.validate()
  if (!currentAlert.value) return
  submitLoading.value = true
  try {
    await resolveVisaAlert(currentAlert.value.id, resolveForm)
    ElMessage.success('预警已处理')
    resolveDialogVisible.value = false
    await loadAlerts()
  } finally {
    submitLoading.value = false
  }
}

function openDetail(row: VisaComplianceAlert) {
  detail.value = row
  detailVisible.value = true
}

async function sendNotify(row: VisaComplianceAlert) {
  await notifyVisaAlert(row.id)
  ElMessage.success('提醒已发送')
  await loadNotifications()
}

async function openNotificationDrawer() {
  notificationDrawerVisible.value = true
  await loadNotifications()
}

async function loadNotifications() {
  notificationLoading.value = true
  try {
    const result = await getVisaNotificationPage({ pageNum: notificationPage.pageNum, pageSize: notificationPage.pageSize })
    notifications.value = result.records || []
    notificationTotal.value = result.total || 0
  } finally {
    notificationLoading.value = false
  }
}

function targetText(value?: string) {
  const map: Record<string, string> = { VISA: '签证', PERMIT: '居留许可', STUDENT: '学生档案' }
  return value ? map[value] || value : '-'
}

function formatOption(options: DictOption[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

function optionTag(options: DictOption[], value?: string) {
  return (options.find((item) => item.value === value)?.tagType || 'info') as 'success' | 'warning' | 'info' | 'primary' | 'danger'
}

onMounted(async () => {
  await loadOptions()
  await loadAlerts()
})
</script>

<style scoped lang="scss">
.visa-alert-page {
  :deep(.el-select),
  :deep(.el-date-editor) {
    width: 100%;
  }
}
</style>
