<template>
  <div class="page-container visa-record-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="84px">
        <el-form-item label="学生">
          <el-input v-model.trim="query.studentKeyword" placeholder="学号 / 姓名" clearable />
        </el-form-item>
        <el-form-item label="签证类型">
          <el-select v-model="query.visaType" placeholder="全部类型" clearable>
            <el-option v-for="item in visaTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable>
            <el-option v-for="item in visaStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="有效期">
          <el-date-picker v-model="expireRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <el-button v-permission="'visa:record:add'" type="primary" :icon="Plus" @click="openDialog()">新增签证</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="studentNo" label="学号" min-width="120" />
        <el-table-column label="学生姓名" min-width="150">
          <template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template>
        </el-table-column>
        <el-table-column label="签证类型" min-width="120">
          <template #default="{ row }">{{ formatOption(visaTypeOptions, row.visaType) }}</template>
        </el-table-column>
        <el-table-column prop="visaNo" label="签证号" min-width="140" show-overflow-tooltip />
        <el-table-column prop="passportNo" label="护照号" min-width="140" show-overflow-tooltip />
        <el-table-column prop="validUntil" label="有效期至" width="120" align="center" />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(visaStatusOptions, row.status)">{{ formatOption(visaStatusOptions, row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'visa:record:detail'" link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-permission="'visa:record:update'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'visa:record:delete'" link type="danger" @click="removeRecord(row)">删除</el-button>
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
          @size-change="loadRecords"
          @current-change="loadRecords"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑签证信息' : '新增签证信息'" width="720px" @closed="resetDialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <div class="form-grid">
          <el-form-item label="学生档案ID" prop="studentId"><el-input-number v-model="form.studentId" :min="1" controls-position="right" /></el-form-item>
          <el-form-item label="签证类型" prop="visaType">
            <el-select v-model="form.visaType"><el-option v-for="item in visaTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status"><el-option v-for="item in visaStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
          </el-form-item>
          <el-form-item label="签证号"><el-input v-model.trim="form.visaNo" /></el-form-item>
          <el-form-item label="护照号"><el-input v-model.trim="form.passportNo" /></el-form-item>
          <el-form-item label="签发地"><el-input v-model.trim="form.issuePlace" /></el-form-item>
          <el-form-item label="签发日期"><el-date-picker v-model="form.issueDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="有效期开始"><el-date-picker v-model="form.validFrom" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="有效期截止"><el-date-picker v-model="form.validUntil" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="入境次数"><el-input v-model.trim="form.entryCount" placeholder="单次 / 多次" /></el-form-item>
        </div>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="签证详情" width="720px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="学号">{{ detail.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.chineseName || detail.englishName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="签证类型">{{ formatOption(visaTypeOptions, detail.visaType) }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ formatOption(visaStatusOptions, detail.status) }}</el-descriptions-item>
        <el-descriptions-item label="签证号">{{ detail.visaNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="护照号">{{ detail.passportNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="签发地">{{ detail.issuePlace || '-' }}</el-descriptions-item>
        <el-descriptions-item label="签发日期">{{ detail.issueDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="有效期">{{ detail.validFrom || '-' }} 至 {{ detail.validUntil || '-' }}</el-descriptions-item>
        <el-descriptions-item label="入境次数">{{ detail.entryCount || '-' }}</el-descriptions-item>
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
import { createVisaRecord, deleteVisaRecord, getVisaRecordPage, updateVisaRecord } from '@/api/visa/record'
import type { DictOption } from '@/types/config'
import type { VisaRecord, VisaRecordForm, VisaRecordQuery } from '@/types/visa'

const fallbackVisaTypeOptions: DictOption[] = [
  { label: '学习签证', value: 'STUDY' },
  { label: '访问签证', value: 'VISIT' },
  { label: '居留签证', value: 'RESIDENCE' },
  { label: '其他', value: 'OTHER' }
]
const fallbackVisaStatusOptions: DictOption[] = [
  { label: '有效', value: 'VALID', tagType: 'success' },
  { label: '即将到期', value: 'EXPIRING', tagType: 'warning' },
  { label: '已过期', value: 'EXPIRED', tagType: 'danger' },
  { label: '已注销', value: 'CANCELLED', tagType: 'info' },
  { label: '异常', value: 'ABNORMAL', tagType: 'danger' }
]

const query = reactive<VisaRecordQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const expireRange = ref<string[]>([])
const records = ref<VisaRecord[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const detail = ref<VisaRecord>()
const formRef = ref<FormInstance>()
const visaTypeOptions = ref<DictOption[]>(fallbackVisaTypeOptions)
const visaStatusOptions = ref<DictOption[]>(fallbackVisaStatusOptions)

const form = reactive<VisaRecordForm & { id?: number }>({ studentId: undefined, visaType: 'STUDY', status: 'VALID' })
const rules: FormRules<VisaRecordForm> = {
  studentId: [{ required: true, message: '请输入学生档案ID', trigger: 'blur' }],
  visaType: [{ required: true, message: '请选择签证类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

async function loadOptions() {
  try {
    visaTypeOptions.value = (await getDictOptions('visa_type')) || fallbackVisaTypeOptions
    visaStatusOptions.value = (await getDictOptions('visa_status')) || fallbackVisaStatusOptions
  } catch {
    visaTypeOptions.value = fallbackVisaTypeOptions
    visaStatusOptions.value = fallbackVisaStatusOptions
  }
}

function buildQuery(): VisaRecordQuery {
  return {
    ...query,
    pageNum: pageQuery.pageNum,
    pageSize: pageQuery.pageSize,
    visaType: query.visaType || undefined,
    status: query.status || undefined,
    expireStart: expireRange.value?.[0],
    expireEnd: expireRange.value?.[1]
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getVisaRecordPage(buildQuery())
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
  Object.assign(query, { studentKeyword: '', visaType: '', status: '' })
  expireRange.value = []
  pageQuery.pageNum = 1
  loadRecords()
}

function openDialog(row?: VisaRecord) {
  Object.assign(form, row || { id: undefined, studentId: undefined, visaType: 'STUDY', status: 'VALID', passportNo: '', visaNo: '', issuePlace: '', issueDate: '', validFrom: '', validUntil: '', entryCount: '', remark: '' })
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

function resetDialog() {
  Object.assign(form, { id: undefined, studentId: undefined, visaType: 'STUDY', status: 'VALID', passportNo: '', visaNo: '', issuePlace: '', issueDate: '', validFrom: '', validUntil: '', entryCount: '', remark: '' })
}

async function submitForm() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await updateVisaRecord(form.id, form)
      ElMessage.success('签证信息已更新')
    } else {
      await createVisaRecord(form)
      ElMessage.success('签证信息已新增')
    }
    dialogVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

function openDetail(row: VisaRecord) {
  detail.value = row
  detailVisible.value = true
}

async function removeRecord(row: VisaRecord) {
  await ElMessageBox.confirm(`确认删除签证「${row.visaNo || row.id}」吗？`, '删除确认', { type: 'warning' })
  await deleteVisaRecord(row.id)
  ElMessage.success('签证信息已删除')
  await loadRecords()
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
.visa-record-page {
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
