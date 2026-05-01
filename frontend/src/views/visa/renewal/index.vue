<template>
  <div class="page-container visa-renewal-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="84px">
        <el-form-item label="学生"><el-input v-model.trim="query.studentKeyword" placeholder="学号 / 姓名" clearable /></el-form-item>
        <el-form-item label="业务类型">
          <el-select v-model="query.renewalType" placeholder="全部类型" clearable>
            <el-option v-for="item in renewalTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.renewalStatus" placeholder="全部状态" clearable>
            <el-option v-for="item in renewalStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请日期">
          <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <el-button v-permission="'visa:renewal:add'" type="primary" :icon="Plus" @click="openDialog()">新增续签</el-button>
      </div>
      <el-table v-loading="loading" :data="renewals" border stripe>
        <el-table-column prop="studentNo" label="学号" min-width="120" />
        <el-table-column label="学生姓名" min-width="150">
          <template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template>
        </el-table-column>
        <el-table-column label="业务类型" min-width="130">
          <template #default="{ row }">{{ formatOption(renewalTypeOptions, row.renewalType) }}</template>
        </el-table-column>
        <el-table-column prop="targetId" label="关联ID" width="90" align="center" />
        <el-table-column prop="applicationDate" label="申请日期" width="120" align="center" />
        <el-table-column prop="completeDate" label="办结日期" width="120" align="center" />
        <el-table-column prop="newValidUntil" label="新有效期" width="120" align="center" />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(renewalStatusOptions, row.renewalStatus)">{{ formatOption(renewalStatusOptions, row.renewalStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="handlerName" label="经办人" width="120" />
        <el-table-column label="操作" width="270" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'visa:renewal:detail'" link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-permission="'visa:renewal:update'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'visa:renewal:result'" link type="success" @click="openResultDialog(row)">结果</el-button>
            <el-button v-permission="'visa:renewal:delete'" link type="danger" @click="removeRenewal(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRenewals" @current-change="loadRenewals" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑续签记录' : '新增续签记录'" width="760px" @closed="resetDialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="108px">
        <div class="form-grid">
          <el-form-item label="学生档案ID" prop="studentId"><el-input-number v-model="form.studentId" :min="1" controls-position="right" /></el-form-item>
          <el-form-item label="业务类型" prop="renewalType"><el-select v-model="form.renewalType"><el-option v-for="item in renewalTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="关联业务ID"><el-input-number v-model="form.targetId" :min="1" controls-position="right" placeholder="签证或居留许可ID" /></el-form-item>
          <el-form-item label="状态" prop="renewalStatus"><el-select v-model="form.renewalStatus"><el-option v-for="item in renewalStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="申请日期"><el-date-picker v-model="form.applicationDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="受理日期"><el-date-picker v-model="form.acceptanceDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="办结日期"><el-date-picker v-model="form.completeDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="新有效期"><el-date-picker v-model="form.newValidUntil" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="办理结果"><el-input v-model.trim="form.result" placeholder="APPROVED / REJECTED" /></el-form-item>
        </div>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="resultDialogVisible" title="登记办理结果" width="560px" @closed="resetResultDialog">
      <el-form ref="resultFormRef" :model="resultForm" :rules="resultRules" label-width="96px">
        <el-form-item label="办理状态" prop="renewalStatus"><el-select v-model="resultForm.renewalStatus"><el-option v-for="item in renewalStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
        <el-form-item label="办理结果" prop="result"><el-input v-model.trim="resultForm.result" placeholder="APPROVED / REJECTED" /></el-form-item>
        <el-form-item label="办结日期"><el-date-picker v-model="resultForm.completeDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="新有效期"><el-date-picker v-model="resultForm.newValidUntil" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="resultForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resultDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitResult">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="续签详情" width="720px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="学号">{{ detail.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.chineseName || detail.englishName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="业务类型">{{ formatOption(renewalTypeOptions, detail.renewalType) }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ formatOption(renewalStatusOptions, detail.renewalStatus) }}</el-descriptions-item>
        <el-descriptions-item label="关联ID">{{ detail.targetId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="新有效期">{{ detail.newValidUntil || '-' }}</el-descriptions-item>
        <el-descriptions-item label="办理结果">{{ detail.result || '-' }}</el-descriptions-item>
        <el-descriptions-item label="经办人">{{ detail.handlerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { createVisaRenewal, deleteVisaRenewal, getVisaRenewalPage, updateVisaRenewal, updateVisaRenewalResult } from '@/api/visa/renewal'
import type { DictOption } from '@/types/config'
import type { VisaRenewalForm, VisaRenewalQuery, VisaRenewalRecord, VisaRenewalResultForm } from '@/types/visa'

const fallbackRenewalTypeOptions: DictOption[] = [
  { label: '签证续签', value: 'VISA' },
  { label: '居留许可续签', value: 'RESIDENCE_PERMIT' }
]
const fallbackRenewalStatusOptions: DictOption[] = [
  { label: '待办理', value: 'PENDING', tagType: 'warning' },
  { label: '办理中', value: 'PROCESSING', tagType: 'primary' },
  { label: '已通过', value: 'APPROVED', tagType: 'success' },
  { label: '已拒绝', value: 'REJECTED', tagType: 'danger' }
]

const query = reactive<VisaRenewalQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const dateRange = ref<string[]>([])
const renewals = ref<VisaRenewalRecord[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const resultDialogVisible = ref(false)
const detailVisible = ref(false)
const detail = ref<VisaRenewalRecord>()
const currentRow = ref<VisaRenewalRecord>()
const formRef = ref<FormInstance>()
const resultFormRef = ref<FormInstance>()
const renewalTypeOptions = ref<DictOption[]>(fallbackRenewalTypeOptions)
const renewalStatusOptions = ref<DictOption[]>(fallbackRenewalStatusOptions)
const form = reactive<VisaRenewalForm & { id?: number }>({ studentId: undefined, renewalType: 'VISA', renewalStatus: 'PENDING' })
const resultForm = reactive<VisaRenewalResultForm>({ result: 'APPROVED', renewalStatus: 'APPROVED', completeDate: '', newValidUntil: '', remark: '' })
const rules: FormRules<VisaRenewalForm> = {
  studentId: [{ required: true, message: '请输入学生档案ID', trigger: 'blur' }],
  renewalType: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
  renewalStatus: [{ required: true, message: '请选择状态', trigger: 'change' }]
}
const resultRules: FormRules<VisaRenewalResultForm> = {
  renewalStatus: [{ required: true, message: '请选择办理状态', trigger: 'change' }],
  result: [{ required: true, message: '请输入办理结果', trigger: 'blur' }]
}

async function loadOptions() {
  try {
    renewalTypeOptions.value = (await getDictOptions('visa_renewal_type')) || fallbackRenewalTypeOptions
    renewalStatusOptions.value = (await getDictOptions('visa_renewal_status')) || fallbackRenewalStatusOptions
  } catch {
    renewalTypeOptions.value = fallbackRenewalTypeOptions
    renewalStatusOptions.value = fallbackRenewalStatusOptions
  }
}

function buildQuery(): VisaRenewalQuery {
  return { ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize, renewalType: query.renewalType || undefined, renewalStatus: query.renewalStatus || undefined, startDate: dateRange.value?.[0], endDate: dateRange.value?.[1] }
}

async function loadRenewals() {
  loading.value = true
  try {
    const result = await getVisaRenewalPage(buildQuery())
    renewals.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageQuery.pageNum = 1
  loadRenewals()
}

function handleReset() {
  Object.assign(query, { studentKeyword: '', renewalType: '', renewalStatus: '' })
  dateRange.value = []
  pageQuery.pageNum = 1
  loadRenewals()
}

function openDialog(row?: VisaRenewalRecord) {
  Object.assign(form, row || { id: undefined, studentId: undefined, renewalType: 'VISA', targetId: undefined, applicationDate: '', acceptanceDate: '', completeDate: '', renewalStatus: 'PENDING', result: '', newValidUntil: '', remark: '' })
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

function resetDialog() {
  Object.assign(form, { id: undefined, studentId: undefined, renewalType: 'VISA', targetId: undefined, applicationDate: '', acceptanceDate: '', completeDate: '', renewalStatus: 'PENDING', result: '', newValidUntil: '', remark: '' })
}

async function submitForm() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await updateVisaRenewal(form.id, form)
      ElMessage.success('续签记录已更新')
    } else {
      await createVisaRenewal(form)
      ElMessage.success('续签记录已新增')
    }
    dialogVisible.value = false
    await loadRenewals()
  } finally {
    submitLoading.value = false
  }
}

function openResultDialog(row: VisaRenewalRecord) {
  currentRow.value = row
  Object.assign(resultForm, { result: row.result || 'APPROVED', renewalStatus: row.renewalStatus || 'APPROVED', completeDate: row.completeDate || '', newValidUntil: row.newValidUntil || '', remark: row.remark || '' })
  resultDialogVisible.value = true
  nextTick(() => resultFormRef.value?.clearValidate())
}

function resetResultDialog() {
  currentRow.value = undefined
  Object.assign(resultForm, { result: 'APPROVED', renewalStatus: 'APPROVED', completeDate: '', newValidUntil: '', remark: '' })
}

async function submitResult() {
  await resultFormRef.value?.validate()
  if (!currentRow.value) return
  submitLoading.value = true
  try {
    await updateVisaRenewalResult(currentRow.value.id, resultForm)
    ElMessage.success('办理结果已登记')
    resultDialogVisible.value = false
    await loadRenewals()
  } finally {
    submitLoading.value = false
  }
}

function openDetail(row: VisaRenewalRecord) {
  detail.value = row
  detailVisible.value = true
}

async function removeRenewal(row: VisaRenewalRecord) {
  await ElMessageBox.confirm(`确认删除续签记录「${row.id}」吗？`, '删除确认', { type: 'warning' })
  await deleteVisaRenewal(row.id)
  ElMessage.success('续签记录已删除')
  await loadRenewals()
}

function formatOption(options: DictOption[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

function optionTag(options: DictOption[], value?: string) {
  return (options.find((item) => item.value === value)?.tagType || 'info') as 'success' | 'warning' | 'info' | 'primary' | 'danger'
}

onMounted(async () => {
  await loadOptions()
  await loadRenewals()
})
</script>

<style scoped lang="scss">
.visa-renewal-page {
  :deep(.el-select),
  :deep(.el-date-editor) {
    width: 100%;
  }

  .form-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 0 16px;
  }
}
</style>
