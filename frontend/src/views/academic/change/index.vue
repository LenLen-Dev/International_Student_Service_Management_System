<template>
  <div class="page-container academic-change-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="学生">
          <el-input v-model.trim="query.keyword" placeholder="学号 / 中文名 / 英文名" clearable />
        </el-form-item>
        <el-form-item label="异动类型">
          <el-select v-model="query.changeType" placeholder="全部类型" clearable>
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select v-model="query.changeStatus" placeholder="全部状态" clearable>
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
        <el-button v-permission="'academic:change:add'" type="primary" :icon="Plus" @click="openDialog()">新增异动</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="studentNo" label="学号" min-width="120" />
        <el-table-column label="姓名" min-width="150">
          <template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template>
        </el-table-column>
        <el-table-column label="异动类型" width="120">
          <template #default="{ row }">{{ formatOption(typeOptions, row.changeType) }}</template>
        </el-table-column>
        <el-table-column label="状态变更" min-width="180">
          <template #default="{ row }">{{ formatOption(studentStatusOptions, row.oldStatus) }} -> {{ formatOption(studentStatusOptions, row.newStatus) }}</template>
        </el-table-column>
        <el-table-column prop="newMajorName" label="新专业" min-width="140" show-overflow-tooltip />
        <el-table-column prop="newGradeName" label="新年级" width="110" />
        <el-table-column prop="newClassName" label="新班级" min-width="130" />
        <el-table-column label="审批状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(statusOptions, row.changeStatus)">{{ formatOption(statusOptions, row.changeStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'academic:change:detail'" link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-if="row.changeStatus === 'PENDING'" v-permission="'academic:change:update'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-if="row.changeStatus === 'PENDING'" v-permission="'academic:change:approve'" link type="success" @click="openApprove(row, 'APPROVED')">通过</el-button>
            <el-button v-if="row.changeStatus === 'PENDING'" v-permission="'academic:change:reject'" link type="danger" @click="openApprove(row, 'REJECTED')">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRecords" @current-change="loadRecords" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑学籍异动' : '新增学籍异动'" width="760px" @closed="resetDialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="116px">
        <div class="form-grid">
          <el-form-item label="学生档案ID" prop="studentId"><el-input-number v-model="form.studentId" :min="1" controls-position="right" /></el-form-item>
          <el-form-item label="异动类型" prop="changeType">
            <el-select v-model="form.changeType"><el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
          </el-form-item>
          <el-form-item label="新学籍状态">
            <el-select v-model="form.newStatus" clearable>
              <el-option v-for="item in studentStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="生效日期"><el-date-picker v-model="form.effectiveDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="新专业">
            <el-select v-model="form.newMajorId" clearable filterable>
              <el-option v-for="item in majors" :key="item.id" :label="`${item.college} / ${item.majorName}`" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="新年级">
            <el-select v-model="form.newGradeId" clearable>
              <el-option v-for="item in grades" :key="item.id" :label="item.gradeName" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="新班级">
            <el-select v-model="form.newClassId" clearable filterable>
              <el-option v-for="item in classes" :key="item.id" :label="item.className" :value="item.id" />
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="异动原因" prop="reason"><el-input v-model.trim="form.reason" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="学籍异动详情" width="760px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="学号">{{ detail.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.chineseName || detail.englishName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="异动类型">{{ formatOption(typeOptions, detail.changeType) }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">{{ formatOption(statusOptions, detail.changeStatus) }}</el-descriptions-item>
        <el-descriptions-item label="原状态">{{ formatOption(studentStatusOptions, detail.oldStatus) }}</el-descriptions-item>
        <el-descriptions-item label="新状态">{{ formatOption(studentStatusOptions, detail.newStatus) }}</el-descriptions-item>
        <el-descriptions-item label="新专业">{{ detail.newMajorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="新年级">{{ detail.newGradeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="新班级">{{ detail.newClassName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="生效日期">{{ detail.effectiveDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="原因" :span="2">{{ detail.reason || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批人">{{ detail.approverName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ detail.approveTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批意见" :span="2">{{ detail.approveOpinion || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="approveVisible" :title="approveAction === 'APPROVED' ? '审批通过' : '审批拒绝'" width="520px" @closed="resetApprove">
      <el-form label-width="90px">
        <el-form-item label="审批意见">
          <el-input v-model.trim="approveForm.approveOpinion" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button :type="approveAction === 'APPROVED' ? 'success' : 'danger'" :loading="submitLoading" @click="submitApprove">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { getEnabledAcademicMajors } from '@/api/academic/major'
import { getEnabledAcademicClasses, getEnabledAcademicGrades } from '@/api/academic/class'
import { approveAcademicChange, createAcademicChange, getAcademicChangePage, rejectAcademicChange, updateAcademicChange } from '@/api/academic/change'
import type { DictOption } from '@/types/config'
import type { AcademicApproveForm, AcademicChange, AcademicChangeForm, AcademicClass, AcademicGrade, AcademicMajor, AcademicQuery } from '@/types/academic'

const fallbackTypeOptions: DictOption[] = [
  { label: '休学', value: 'SUSPEND' },
  { label: '复学', value: 'RESUME' },
  { label: '转专业', value: 'TRANSFER_MAJOR' },
  { label: '转班', value: 'TRANSFER_CLASS' },
  { label: '毕业', value: 'GRADUATE' },
  { label: '退学', value: 'DROP_OUT' },
  { label: '离校', value: 'LEAVE_SCHOOL' }
]
const fallbackStatusOptions: DictOption[] = [
  { label: '待审批', value: 'PENDING', tagType: 'warning' },
  { label: '已通过', value: 'APPROVED', tagType: 'success' },
  { label: '已拒绝', value: 'REJECTED', tagType: 'danger' }
]
const fallbackStudentStatusOptions: DictOption[] = [
  { label: '预录取', value: 'PRE_ADMITTED', tagType: 'primary' },
  { label: '在读', value: 'ENROLLED', tagType: 'success' },
  { label: '休学', value: 'SUSPENDED', tagType: 'warning' },
  { label: '已毕业', value: 'GRADUATED', tagType: 'info' },
  { label: '退学', value: 'DROPPED', tagType: 'danger' },
  { label: '已离校', value: 'LEFT', tagType: 'info' }
]

const query = reactive<AcademicQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<AcademicChange[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const approveVisible = ref(false)
const formRef = ref<FormInstance>()
const detail = ref<AcademicChange>()
const activeRow = ref<AcademicChange>()
const approveAction = ref<'APPROVED' | 'REJECTED'>('APPROVED')
const approveForm = reactive<AcademicApproveForm>({ approveOpinion: '' })
const majors = ref<AcademicMajor[]>([])
const grades = ref<AcademicGrade[]>([])
const classes = ref<AcademicClass[]>([])
const typeOptions = ref<DictOption[]>(fallbackTypeOptions)
const statusOptions = ref<DictOption[]>(fallbackStatusOptions)
const studentStatusOptions = ref<DictOption[]>(fallbackStudentStatusOptions)
const form = reactive<AcademicChangeForm>({ studentId: undefined, changeType: 'TRANSFER_CLASS', newStatus: '', newMajorId: undefined, newGradeId: undefined, newClassId: undefined, effectiveDate: '', reason: '', remark: '' })

const rules: FormRules<AcademicChangeForm> = {
  studentId: [{ required: true, message: '请输入学生档案ID', trigger: 'blur' }],
  changeType: [{ required: true, message: '请选择异动类型', trigger: 'change' }],
  reason: [{ required: true, message: '请输入异动原因', trigger: 'blur' }]
}

async function loadOptions() {
  const [majorList, gradeList, classList] = await Promise.all([getEnabledAcademicMajors(), getEnabledAcademicGrades(), getEnabledAcademicClasses()])
  majors.value = majorList
  grades.value = gradeList
  classes.value = classList
  try {
    typeOptions.value = (await getDictOptions('academic_change_type')) || fallbackTypeOptions
    statusOptions.value = (await getDictOptions('academic_change_status')) || fallbackStatusOptions
    studentStatusOptions.value = (await getDictOptions('academic_student_status')) || fallbackStudentStatusOptions
  } catch {
    typeOptions.value = fallbackTypeOptions
    statusOptions.value = fallbackStatusOptions
    studentStatusOptions.value = fallbackStudentStatusOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getAcademicChangePage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
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
  Object.assign(query, { keyword: '', changeType: '', changeStatus: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

function openDialog(row?: AcademicChange) {
  Object.assign(form, row ? {
    id: row.id,
    studentId: row.studentId,
    changeType: row.changeType,
    newStatus: row.newStatus || '',
    newMajorId: row.newMajorId,
    newGradeId: row.newGradeId,
    newClassId: row.newClassId,
    effectiveDate: row.effectiveDate || '',
    reason: row.reason,
    remark: row.remark || ''
  } : { id: undefined, studentId: undefined, changeType: 'TRANSFER_CLASS', newStatus: '', newMajorId: undefined, newGradeId: undefined, newClassId: undefined, effectiveDate: '', reason: '', remark: '' })
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

function resetDialog() {
  Object.assign(form, { id: undefined, studentId: undefined, changeType: 'TRANSFER_CLASS', newStatus: '', newMajorId: undefined, newGradeId: undefined, newClassId: undefined, effectiveDate: '', reason: '', remark: '' })
}

async function submitForm() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await updateAcademicChange(form.id, form)
      ElMessage.success('学籍异动已更新')
    } else {
      await createAcademicChange(form)
      ElMessage.success('学籍异动已新增')
    }
    dialogVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

function openDetail(row: AcademicChange) {
  detail.value = row
  detailVisible.value = true
}

function openApprove(row: AcademicChange, action: 'APPROVED' | 'REJECTED') {
  activeRow.value = row
  approveAction.value = action
  approveVisible.value = true
}

function resetApprove() {
  activeRow.value = undefined
  approveForm.approveOpinion = ''
}

async function submitApprove() {
  if (!activeRow.value) return
  submitLoading.value = true
  try {
    if (approveAction.value === 'APPROVED') {
      await approveAcademicChange(activeRow.value.id, approveForm)
      ElMessage.success('学籍异动已审批通过')
    } else {
      await rejectAcademicChange(activeRow.value.id, approveForm)
      ElMessage.success('学籍异动已审批拒绝')
    }
    approveVisible.value = false
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
.academic-change-page {
  :deep(.el-select),
  :deep(.el-date-editor),
  :deep(.el-input-number) {
    width: 100%;
  }

  .form-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 0 16px;
  }
}
</style>
