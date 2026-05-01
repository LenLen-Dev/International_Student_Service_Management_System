<template>
  <div class="page-container visa-my-page">
    <el-card class="summary-card">
      <div class="summary">
        <div>
          <h2>我的签证居留</h2>
          <p>查看本人签证、居留许可、续签进度、合规预警与站内提醒。</p>
        </div>
        <div class="summary-metrics">
          <div><strong>{{ overview.visaRecords.length }}</strong><span>签证</span></div>
          <div><strong>{{ overview.residencePermits.length }}</strong><span>居留许可</span></div>
          <div><strong>{{ openAlerts }}</strong><span>待处理预警</span></div>
          <div><strong>{{ unreadNotifications }}</strong><span>未读提醒</span></div>
        </div>
      </div>
    </el-card>

    <el-card class="table-card">
      <template #header>签证信息</template>
      <el-table v-loading="loading" :data="overview.visaRecords" border stripe>
        <el-table-column label="签证类型" min-width="120"><template #default="{ row }">{{ formatOption(visaTypeOptions, row.visaType) }}</template></el-table-column>
        <el-table-column prop="visaNo" label="签证号" min-width="140" />
        <el-table-column prop="passportNo" label="护照号" min-width="140" />
        <el-table-column prop="validFrom" label="有效期开始" width="120" />
        <el-table-column prop="validUntil" label="有效期截止" width="120" />
        <el-table-column label="状态" width="110"><template #default="{ row }"><el-tag :type="optionTag(visaStatusOptions, row.status)">{{ formatOption(visaStatusOptions, row.status) }}</el-tag></template></el-table-column>
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>居留许可</template>
      <el-table v-loading="loading" :data="overview.residencePermits" border stripe>
        <el-table-column label="许可类型" min-width="140"><template #default="{ row }">{{ formatOption(permitTypeOptions, row.permitType) }}</template></el-table-column>
        <el-table-column prop="permitNo" label="许可编号" min-width="140" />
        <el-table-column prop="residenceAddress" label="居住地址" min-width="180" show-overflow-tooltip />
        <el-table-column prop="validFrom" label="有效期开始" width="120" />
        <el-table-column prop="validUntil" label="有效期截止" width="120" />
        <el-table-column label="状态" width="110"><template #default="{ row }"><el-tag :type="optionTag(permitStatusOptions, row.status)">{{ formatOption(permitStatusOptions, row.status) }}</el-tag></template></el-table-column>
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>续签记录</template>
      <el-table v-loading="loading" :data="overview.renewals" border stripe>
        <el-table-column label="业务类型" min-width="130"><template #default="{ row }">{{ formatOption(renewalTypeOptions, row.renewalType) }}</template></el-table-column>
        <el-table-column prop="applicationDate" label="申请日期" width="120" />
        <el-table-column prop="completeDate" label="办结日期" width="120" />
        <el-table-column prop="newValidUntil" label="新有效期" width="120" />
        <el-table-column label="状态" width="110"><template #default="{ row }"><el-tag :type="optionTag(renewalStatusOptions, row.renewalStatus)">{{ formatOption(renewalStatusOptions, row.renewalStatus) }}</el-tag></template></el-table-column>
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>合规预警</template>
      <el-table v-loading="loading" :data="overview.alerts" border stripe>
        <el-table-column prop="title" label="预警标题" min-width="180" show-overflow-tooltip />
        <el-table-column label="类型" width="110"><template #default="{ row }">{{ formatOption(alertTypeOptions, row.alertType) }}</template></el-table-column>
        <el-table-column label="等级" width="100"><template #default="{ row }"><el-tag :type="optionTag(alertLevelOptions, row.alertLevel)">{{ formatOption(alertLevelOptions, row.alertLevel) }}</el-tag></template></el-table-column>
        <el-table-column prop="expireDate" label="到期日期" width="120" />
        <el-table-column prop="remainingDays" label="剩余天数" width="100" />
        <el-table-column label="状态" width="110"><template #default="{ row }"><el-tag :type="optionTag(alertStatusOptions, row.alertStatus)">{{ formatOption(alertStatusOptions, row.alertStatus) }}</el-tag></template></el-table-column>
        <el-table-column prop="content" label="内容" min-width="220" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>提醒通知</template>
      <el-table v-loading="loading" :data="overview.notifications" border stripe>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="content" label="内容" min-width="240" show-overflow-tooltip />
        <el-table-column label="渠道" width="110"><template #default="{ row }">{{ formatOption(channelOptions, row.channel) }}</template></el-table-column>
        <el-table-column label="发送状态" width="110"><template #default="{ row }"><el-tag :type="optionTag(notificationStatusOptions, row.sendStatus)">{{ formatOption(notificationStatusOptions, row.sendStatus) }}</el-tag></template></el-table-column>
        <el-table-column label="已读" width="90"><template #default="{ row }">{{ row.readStatus === 1 ? '已读' : '未读' }}</template></el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'visa:notification:read'" link type="primary" :disabled="row.readStatus === 1" @click="markRead(row.id)">标记已读</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getDictOptions } from '@/api/config/dict'
import { getMyVisaOverview, markVisaNotificationRead } from '@/api/visa/alert'
import type { DictOption } from '@/types/config'
import type { VisaMyOverview } from '@/types/visa'

const overview = reactive<VisaMyOverview>({ visaRecords: [], residencePermits: [], renewals: [], alerts: [], notifications: [] })
const loading = ref(false)

const visaTypeOptions = ref<DictOption[]>([{ label: '学习签证', value: 'STUDY' }, { label: '访问签证', value: 'VISIT' }, { label: '居留签证', value: 'RESIDENCE' }, { label: '其他', value: 'OTHER' }])
const visaStatusOptions = ref<DictOption[]>([{ label: '有效', value: 'VALID', tagType: 'success' }, { label: '即将到期', value: 'EXPIRING', tagType: 'warning' }, { label: '已过期', value: 'EXPIRED', tagType: 'danger' }, { label: '已注销', value: 'CANCELLED', tagType: 'info' }, { label: '异常', value: 'ABNORMAL', tagType: 'danger' }])
const permitTypeOptions = ref<DictOption[]>([{ label: '学习类居留许可', value: 'STUDY' }, { label: '团聚类居留许可', value: 'FAMILY' }, { label: '其他', value: 'OTHER' }])
const permitStatusOptions = ref<DictOption[]>(visaStatusOptions.value)
const renewalTypeOptions = ref<DictOption[]>([{ label: '签证续签', value: 'VISA' }, { label: '居留许可续签', value: 'RESIDENCE_PERMIT' }])
const renewalStatusOptions = ref<DictOption[]>([{ label: '待办理', value: 'PENDING', tagType: 'warning' }, { label: '办理中', value: 'PROCESSING', tagType: 'primary' }, { label: '已通过', value: 'APPROVED', tagType: 'success' }, { label: '已拒绝', value: 'REJECTED', tagType: 'danger' }])
const alertTypeOptions = ref<DictOption[]>([{ label: '即将到期', value: 'EXPIRING' }, { label: '已逾期', value: 'EXPIRED' }, { label: '资料缺失', value: 'DATA_MISSING' }, { label: '状态异常', value: 'STATUS_ABNORMAL' }])
const alertLevelOptions = ref<DictOption[]>([{ label: '低', value: 'LOW', tagType: 'info' }, { label: '中', value: 'MEDIUM', tagType: 'warning' }, { label: '高', value: 'HIGH', tagType: 'danger' }])
const alertStatusOptions = ref<DictOption[]>([{ label: '待处理', value: 'OPEN', tagType: 'danger' }, { label: '处理中', value: 'PROCESSING', tagType: 'warning' }, { label: '已处理', value: 'RESOLVED', tagType: 'success' }, { label: '已关闭', value: 'CLOSED', tagType: 'info' }])
const channelOptions = ref<DictOption[]>([{ label: '站内通知', value: 'IN_APP' }, { label: '邮件', value: 'EMAIL' }])
const notificationStatusOptions = ref<DictOption[]>([{ label: '待发送', value: 'PENDING', tagType: 'warning' }, { label: '已发送', value: 'SENT', tagType: 'success' }, { label: '发送失败', value: 'FAILED', tagType: 'danger' }])

const openAlerts = computed(() => overview.alerts.filter((item) => ['OPEN', 'PROCESSING'].includes(item.alertStatus)).length)
const unreadNotifications = computed(() => overview.notifications.filter((item) => item.readStatus === 0).length)

async function loadOptions() {
  const mappings: Array<[string, typeof visaTypeOptions]> = [
    ['visa_type', visaTypeOptions],
    ['visa_status', visaStatusOptions],
    ['residence_permit_type', permitTypeOptions],
    ['residence_permit_status', permitStatusOptions],
    ['visa_renewal_type', renewalTypeOptions],
    ['visa_renewal_status', renewalStatusOptions],
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
      // 保留本地兜底。
    }
  }))
}

async function loadOverview() {
  loading.value = true
  try {
    const data = await getMyVisaOverview()
    Object.assign(overview, {
      visaRecords: data.visaRecords || [],
      residencePermits: data.residencePermits || [],
      renewals: data.renewals || [],
      alerts: data.alerts || [],
      notifications: data.notifications || []
    })
  } finally {
    loading.value = false
  }
}

async function markRead(id: number) {
  await markVisaNotificationRead(id)
  ElMessage.success('已标记为已读')
  await loadOverview()
}

function formatOption(options: DictOption[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

function optionTag(options: DictOption[], value?: string) {
  return (options.find((item) => item.value === value)?.tagType || 'info') as 'success' | 'warning' | 'info' | 'primary' | 'danger'
}

onMounted(async () => {
  await loadOptions()
  await loadOverview()
})
</script>

<style scoped lang="scss">
.visa-my-page {
  .summary {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;

    h2 {
      margin: 0 0 8px;
      font-size: 22px;
    }

    p {
      margin: 0;
      color: #667085;
    }
  }

  .summary-metrics {
    display: grid;
    grid-template-columns: repeat(4, 92px);
    gap: 10px;

    div {
      padding: 12px;
      text-align: center;
      background: #f8fafc;
      border: 1px solid #e5e7eb;
      border-radius: 8px;
    }

    strong {
      display: block;
      color: #1f2937;
      font-size: 22px;
    }

    span {
      color: #667085;
      font-size: 12px;
    }
  }
}

@media (max-width: 900px) {
  .visa-my-page {
    .summary {
      align-items: flex-start;
      flex-direction: column;
    }

    .summary-metrics {
      grid-template-columns: repeat(2, 1fr);
      width: 100%;
    }
  }
}
</style>
