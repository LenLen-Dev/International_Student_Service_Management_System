<template>
  <div class="page-container visa-permit-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="84px">
        <el-form-item label="学生"><el-input v-model.trim="query.studentKeyword" placeholder="学号 / 姓名" clearable /></el-form-item>
        <el-form-item label="许可类型">
          <el-select v-model="query.permitType" placeholder="全部类型" clearable>
            <el-option v-for="item in permitTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable>
            <el-option v-for="item in permitStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
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
        <el-button v-permission="'visa:permit:add'" type="primary" :icon="Plus" @click="openDialog()">新增居留许可</el-button>
      </div>
      <el-table v-loading="loading" :data="permits" border stripe>
        <el-table-column prop="studentNo" label="学号" min-width="120" />
        <el-table-column label="学生姓名" min-width="150">
          <template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template>
        </el-table-column>
        <el-table-column label="许可类型" min-width="140">
          <template #default="{ row }">{{ formatOption(permitTypeOptions, row.permitType) }}</template>
        </el-table-column>
        <el-table-column prop="permitNo" label="许可编号" min-width="150" show-overflow-tooltip />
        <el-table-column prop="residenceAddress" label="居住地址" min-width="180" show-overflow-tooltip />
        <el-table-column prop="validUntil" label="有效期至" width="120" align="center" />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(permitStatusOptions, row.status)">{{ formatOption(permitStatusOptions, row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'visa:permit:detail'" link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-permission="'visa:permit:update'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'visa:permit:delete'" link type="danger" @click="removePermit(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadPermits" @current-change="loadPermits" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑居留许可' : '新增居留许可'" width="720px" @closed="resetDialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="108px">
        <div class="form-grid">
          <el-form-item label="学生档案ID" prop="studentId"><el-input-number v-model="form.studentId" :min="1" controls-position="right" /></el-form-item>
          <el-form-item label="许可类型" prop="permitType"><el-select v-model="form.permitType"><el-option v-for="item in permitTypeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="状态" prop="status"><el-select v-model="form.status"><el-option v-for="item in permitStatusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select></el-form-item>
          <el-form-item label="许可编号"><el-input v-model.trim="form.permitNo" /></el-form-item>
          <el-form-item label="签发日期"><el-date-picker v-model="form.issueDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="有效期开始"><el-date-picker v-model="form.validFrom" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="有效期截止"><el-date-picker v-model="form.validUntil" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        </div>
        <el-form-item label="居住地址"><el-input v-model.trim="form.residenceAddress" /></el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="居留许可详情" width="720px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="学号">{{ detail.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.chineseName || detail.englishName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="许可类型">{{ formatOption(permitTypeOptions, detail.permitType) }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ formatOption(permitStatusOptions, detail.status) }}</el-descriptions-item>
        <el-descriptions-item label="许可编号">{{ detail.permitNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="签发日期">{{ detail.issueDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="有效期">{{ detail.validFrom || '-' }} 至 {{ detail.validUntil || '-' }}</el-descriptions-item>
        <el-descriptions-item label="居住地址">{{ detail.residenceAddress || '-' }}</el-descriptions-item>
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
import { createResidencePermit, deleteResidencePermit, getResidencePermitPage, updateResidencePermit } from '@/api/visa/permit'
import type { DictOption } from '@/types/config'
import type { ResidencePermit, ResidencePermitForm, ResidencePermitQuery } from '@/types/visa'

const fallbackPermitTypeOptions: DictOption[] = [
  { label: '学习类居留许可', value: 'STUDY' },
  { label: '团聚类居留许可', value: 'FAMILY' },
  { label: '其他', value: 'OTHER' }
]
const fallbackPermitStatusOptions: DictOption[] = [
  { label: '有效', value: 'VALID', tagType: 'success' },
  { label: '即将到期', value: 'EXPIRING', tagType: 'warning' },
  { label: '已过期', value: 'EXPIRED', tagType: 'danger' },
  { label: '已注销', value: 'CANCELLED', tagType: 'info' },
  { label: '异常', value: 'ABNORMAL', tagType: 'danger' }
]

const query = reactive<ResidencePermitQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const expireRange = ref<string[]>([])
const permits = ref<ResidencePermit[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const detail = ref<ResidencePermit>()
const formRef = ref<FormInstance>()
const permitTypeOptions = ref<DictOption[]>(fallbackPermitTypeOptions)
const permitStatusOptions = ref<DictOption[]>(fallbackPermitStatusOptions)
const form = reactive<ResidencePermitForm & { id?: number }>({ studentId: undefined, permitType: 'STUDY', status: 'VALID' })
const rules: FormRules<ResidencePermitForm> = {
  studentId: [{ required: true, message: '请输入学生档案ID', trigger: 'blur' }],
  permitType: [{ required: true, message: '请选择许可类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

async function loadOptions() {
  try {
    permitTypeOptions.value = (await getDictOptions('residence_permit_type')) || fallbackPermitTypeOptions
    permitStatusOptions.value = (await getDictOptions('residence_permit_status')) || fallbackPermitStatusOptions
  } catch {
    permitTypeOptions.value = fallbackPermitTypeOptions
    permitStatusOptions.value = fallbackPermitStatusOptions
  }
}

function buildQuery(): ResidencePermitQuery {
  return { ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize, permitType: query.permitType || undefined, status: query.status || undefined, expireStart: expireRange.value?.[0], expireEnd: expireRange.value?.[1] }
}

async function loadPermits() {
  loading.value = true
  try {
    const result = await getResidencePermitPage(buildQuery())
    permits.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageQuery.pageNum = 1
  loadPermits()
}

function handleReset() {
  Object.assign(query, { studentKeyword: '', permitType: '', status: '' })
  expireRange.value = []
  pageQuery.pageNum = 1
  loadPermits()
}

function openDialog(row?: ResidencePermit) {
  Object.assign(form, row || { id: undefined, studentId: undefined, permitType: 'STUDY', status: 'VALID', permitNo: '', residenceAddress: '', issueDate: '', validFrom: '', validUntil: '', remark: '' })
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

function resetDialog() {
  Object.assign(form, { id: undefined, studentId: undefined, permitType: 'STUDY', status: 'VALID', permitNo: '', residenceAddress: '', issueDate: '', validFrom: '', validUntil: '', remark: '' })
}

async function submitForm() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await updateResidencePermit(form.id, form)
      ElMessage.success('居留许可已更新')
    } else {
      await createResidencePermit(form)
      ElMessage.success('居留许可已新增')
    }
    dialogVisible.value = false
    await loadPermits()
  } finally {
    submitLoading.value = false
  }
}

function openDetail(row: ResidencePermit) {
  detail.value = row
  detailVisible.value = true
}

async function removePermit(row: ResidencePermit) {
  await ElMessageBox.confirm(`确认删除居留许可「${row.permitNo || row.id}」吗？`, '删除确认', { type: 'warning' })
  await deleteResidencePermit(row.id)
  ElMessage.success('居留许可已删除')
  await loadPermits()
}

function formatOption(options: DictOption[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

function optionTag(options: DictOption[], value?: string) {
  return (options.find((item) => item.value === value)?.tagType || 'info') as 'success' | 'warning' | 'info' | 'primary' | 'danger'
}

onMounted(async () => {
  await loadOptions()
  await loadPermits()
})
</script>

<style scoped lang="scss">
.visa-permit-page {
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
