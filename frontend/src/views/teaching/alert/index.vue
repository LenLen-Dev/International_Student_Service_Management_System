<template>
  <div class="page-container teaching-alert-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="关键词"><el-input v-model.trim="query.keyword" placeholder="学生 / 学号 / 标题" clearable /></el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="query.alertType" placeholder="全部类型" clearable>
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警等级">
          <el-select v-model="query.alertLevel" placeholder="全部等级" clearable>
            <el-option v-for="item in levelOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="query.alertStatus" placeholder="全部状态" clearable>
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button v-permission="'teaching:alert:generate'" type="primary" :icon="RefreshRight" :loading="generateLoading" @click="generateAlerts">生成预警</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column label="姓名" width="120"><template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template></el-table-column>
        <el-table-column label="类型" width="120"><template #default="{ row }">{{ formatOption(typeOptions, row.alertType) }}</template></el-table-column>
        <el-table-column label="等级" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(levelOptions, row.alertLevel)">{{ formatOption(levelOptions, row.alertLevel) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="190" show-overflow-tooltip />
        <el-table-column prop="content" label="内容" min-width="240" show-overflow-tooltip />
        <el-table-column label="状态" width="120" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(statusOptions, row.alertStatus)">{{ formatOption(statusOptions, row.alertStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="handlerName" label="处理人" width="110" />
        <el-table-column prop="handleTime" label="处理时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="110" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'teaching:alert:handle'" link type="primary" :disabled="row.alertStatus !== 'PENDING'" @click="openHandle(row)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRecords" @current-change="loadRecords" />
      </div>
    </el-card>

    <el-dialog v-model="handleVisible" title="处理学业预警" width="520px" @closed="resetHandle">
      <el-form :model="handleForm" label-width="96px">
        <el-form-item label="处理状态">
          <el-radio-group v-model="handleForm.alertStatus">
            <el-radio value="HANDLED">已处理</el-radio>
            <el-radio value="CLOSED">已关闭</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理说明"><el-input v-model.trim="handleForm.handleRemark" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitHandle">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, RefreshRight, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { generateTeachingAlerts, getTeachingAlertPage, handleTeachingAlert } from '@/api/teaching/alert'
import type { DictOption } from '@/types/config'
import type { TeachingAcademicAlert, TeachingAlertHandleForm, TeachingQuery } from '@/types/teaching'

const fallbackTypeOptions: DictOption[] = [
  { label: '挂科预警', value: 'FAILED_COURSE' },
  { label: '低分预警', value: 'LOW_SCORE' },
  { label: '缺勤预警', value: 'ABSENCE' },
  { label: '未选课预警', value: 'NO_SELECTION' }
]
const fallbackLevelOptions: DictOption[] = [
  { label: '低', value: 'LOW', tagType: 'info' },
  { label: '中', value: 'MEDIUM', tagType: 'warning' },
  { label: '高', value: 'HIGH', tagType: 'danger' }
]
const fallbackStatusOptions: DictOption[] = [
  { label: '待处理', value: 'PENDING', tagType: 'warning' },
  { label: '已处理', value: 'HANDLED', tagType: 'success' },
  { label: '已关闭', value: 'CLOSED', tagType: 'info' }
]

const query = reactive<TeachingQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<TeachingAcademicAlert[]>([])
const total = ref(0)
const loading = ref(false)
const generateLoading = ref(false)
const submitLoading = ref(false)
const handleVisible = ref(false)
const activeId = ref<number>()
const typeOptions = ref<DictOption[]>(fallbackTypeOptions)
const levelOptions = ref<DictOption[]>(fallbackLevelOptions)
const statusOptions = ref<DictOption[]>(fallbackStatusOptions)
const handleForm = reactive<TeachingAlertHandleForm>({ alertStatus: 'HANDLED', handleRemark: '' })

async function loadOptions() {
  try {
    typeOptions.value = (await getDictOptions('teaching_alert_type')) || fallbackTypeOptions
  } catch {
    typeOptions.value = fallbackTypeOptions
  }
  try {
    levelOptions.value = (await getDictOptions('teaching_alert_level')) || fallbackLevelOptions
  } catch {
    levelOptions.value = fallbackLevelOptions
  }
  try {
    statusOptions.value = (await getDictOptions('teaching_alert_status')) || fallbackStatusOptions
  } catch {
    statusOptions.value = fallbackStatusOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getTeachingAlertPage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
    records.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageQuery.pageNum = 1
  loadRecords()
}

function handleReset() {
  Object.assign(query, { keyword: '', alertType: '', alertLevel: '', alertStatus: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

async function generateAlerts() {
  await ElMessageBox.confirm('确认根据当前成绩、出勤和选课数据生成学业预警吗？', '生成确认', { type: 'warning' })
  generateLoading.value = true
  try {
    const count = await generateTeachingAlerts()
    ElMessage.success(`已生成 ${count || 0} 条预警`)
    await loadRecords()
  } finally {
    generateLoading.value = false
  }
}

function openHandle(row: TeachingAcademicAlert) {
  activeId.value = row.id
  Object.assign(handleForm, { alertStatus: 'HANDLED', handleRemark: '' })
  handleVisible.value = true
}

function resetHandle() {
  activeId.value = undefined
  Object.assign(handleForm, { alertStatus: 'HANDLED', handleRemark: '' })
}

async function submitHandle() {
  if (!activeId.value) return
  submitLoading.value = true
  try {
    await handleTeachingAlert(activeId.value, handleForm)
    ElMessage.success('预警已处理')
    handleVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

function formatOption(options: DictOption[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

function optionTag(options: DictOption[], value?: string) {
  return (options.find((item) => item.value === value)?.tagType || 'info') as 'success' | 'warning' | 'info' | 'primary' | 'danger'
}

onMounted(async () => {
  await loadOptions()
  await loadRecords()
})
</script>

<style scoped lang="scss">
.teaching-alert-page {
  :deep(.el-select) {
    width: 100%;
  }
}
</style>
