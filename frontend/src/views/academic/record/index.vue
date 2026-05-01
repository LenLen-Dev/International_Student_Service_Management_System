<template>
  <div class="page-container academic-record-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="学生">
          <el-input v-model.trim="query.keyword" placeholder="学号 / 中文名 / 英文名" clearable />
        </el-form-item>
        <el-form-item label="专业">
          <el-select v-model="query.majorId" placeholder="全部专业" clearable filterable>
            <el-option v-for="item in majors" :key="item.id" :label="`${item.college} / ${item.majorName}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="年级">
          <el-select v-model="query.gradeId" placeholder="全部年级" clearable>
            <el-option v-for="item in grades" :key="item.id" :label="item.gradeName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="学籍状态">
          <el-select v-model="query.studentStatus" placeholder="全部状态" clearable>
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
        <el-button v-permission="'academic:record:add'" type="primary" :icon="Plus" @click="openDialog()">新增学籍</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="studentNo" label="学号" min-width="120" />
        <el-table-column label="姓名" min-width="150">
          <template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template>
        </el-table-column>
        <el-table-column prop="college" label="学院" min-width="150" show-overflow-tooltip />
        <el-table-column prop="majorName" label="专业" min-width="150" show-overflow-tooltip />
        <el-table-column prop="gradeName" label="年级" width="110" />
        <el-table-column prop="className" label="班级" min-width="130" show-overflow-tooltip />
        <el-table-column label="学籍状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="optionTag(statusOptions, row.studentStatus)">{{ formatOption(statusOptions, row.studentStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enrollmentDate" label="入学日期" width="120" />
        <el-table-column prop="expectedGraduationDate" label="预计毕业" width="120" />
        <el-table-column label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'academic:record:detail'" link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-permission="'academic:record:update'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'academic:record:status'" link type="warning" @click="openStatusDialog(row)">调状态</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRecords" @current-change="loadRecords" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑学籍记录' : '新增学籍记录'" width="760px" @closed="resetDialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="118px">
        <div class="form-grid">
          <el-form-item label="学生档案ID" prop="studentId"><el-input-number v-model="form.studentId" :min="1" controls-position="right" /></el-form-item>
          <el-form-item label="学籍状态" prop="studentStatus">
            <el-select v-model="form.studentStatus"><el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
          </el-form-item>
          <el-form-item label="专业" prop="majorId">
            <el-select v-model="form.majorId" filterable>
              <el-option v-for="item in majors" :key="item.id" :label="`${item.college} / ${item.majorName}`" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="年级" prop="gradeId">
            <el-select v-model="form.gradeId">
              <el-option v-for="item in grades" :key="item.id" :label="item.gradeName" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="班级">
            <el-select v-model="form.classId" clearable filterable>
              <el-option v-for="item in classes" :key="item.id" :label="item.className" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="入学日期"><el-date-picker v-model="form.enrollmentDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="预计毕业日期"><el-date-picker v-model="form.expectedGraduationDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="实际离校日期"><el-date-picker v-model="form.actualLeaveDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        </div>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="statusDialogVisible" title="调整学籍状态" width="560px" @closed="resetStatusDialog">
      <el-form ref="statusFormRef" :model="statusForm" :rules="statusRules" label-width="110px">
        <el-form-item label="学籍状态" prop="studentStatus">
          <el-select v-model="statusForm.studentStatus">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="实际离校日期"><el-date-picker v-model="statusForm.actualLeaveDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="statusForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitStatus">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="学籍详情" width="760px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="学号">{{ detail.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.chineseName || detail.englishName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学院">{{ detail.college || '-' }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ detail.majorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学历层次">{{ formatOption(degreeOptions, detail.degreeLevel) }}</el-descriptions-item>
        <el-descriptions-item label="年级">{{ detail.gradeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ detail.className || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学籍状态">{{ formatOption(statusOptions, detail.studentStatus) }}</el-descriptions-item>
        <el-descriptions-item label="入学日期">{{ detail.enrollmentDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预计毕业">{{ detail.expectedGraduationDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际离校">{{ detail.actualLeaveDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
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
import { createAcademicRecord, getAcademicRecordPage, updateAcademicRecord, updateAcademicRecordStatus } from '@/api/academic/record'
import type { DictOption } from '@/types/config'
import type { AcademicClass, AcademicGrade, AcademicMajor, AcademicQuery, AcademicRecord, AcademicRecordForm, AcademicRecordStatusForm } from '@/types/academic'

const fallbackStatusOptions: DictOption[] = [
  { label: '预录取', value: 'PRE_ADMITTED', tagType: 'primary' },
  { label: '在读', value: 'ENROLLED', tagType: 'success' },
  { label: '休学', value: 'SUSPENDED', tagType: 'warning' },
  { label: '已毕业', value: 'GRADUATED', tagType: 'info' },
  { label: '退学', value: 'DROPPED', tagType: 'danger' },
  { label: '已离校', value: 'LEFT', tagType: 'info' }
]
const fallbackDegreeOptions: DictOption[] = [
  { label: '本科', value: 'BACHELOR' },
  { label: '硕士', value: 'MASTER' },
  { label: '博士', value: 'DOCTOR' }
]

const query = reactive<AcademicQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<AcademicRecord[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const statusDialogVisible = ref(false)
const detailVisible = ref(false)
const formRef = ref<FormInstance>()
const statusFormRef = ref<FormInstance>()
const detail = ref<AcademicRecord>()
const majors = ref<AcademicMajor[]>([])
const grades = ref<AcademicGrade[]>([])
const classes = ref<AcademicClass[]>([])
const statusOptions = ref<DictOption[]>(fallbackStatusOptions)
const degreeOptions = ref<DictOption[]>(fallbackDegreeOptions)

const form = reactive<AcademicRecordForm>({ studentId: undefined, majorId: undefined, gradeId: undefined, classId: undefined, studentStatus: 'ENROLLED', remark: '' })
const statusForm = reactive<AcademicRecordStatusForm & { id?: number }>({ studentStatus: 'ENROLLED', actualLeaveDate: '', remark: '' })

const rules: FormRules<AcademicRecordForm> = {
  studentId: [{ required: true, message: '请输入学生档案ID', trigger: 'blur' }],
  majorId: [{ required: true, message: '请选择专业', trigger: 'change' }],
  gradeId: [{ required: true, message: '请选择年级', trigger: 'change' }],
  studentStatus: [{ required: true, message: '请选择学籍状态', trigger: 'change' }]
}
const statusRules: FormRules<AcademicRecordStatusForm> = {
  studentStatus: [{ required: true, message: '请选择学籍状态', trigger: 'change' }]
}

async function loadOptions() {
  const [majorList, gradeList, classList] = await Promise.all([getEnabledAcademicMajors(), getEnabledAcademicGrades(), getEnabledAcademicClasses()])
  majors.value = majorList
  grades.value = gradeList
  classes.value = classList
  try {
    statusOptions.value = (await getDictOptions('academic_student_status')) || fallbackStatusOptions
    degreeOptions.value = (await getDictOptions('academic_degree_level')) || fallbackDegreeOptions
  } catch {
    statusOptions.value = fallbackStatusOptions
    degreeOptions.value = fallbackDegreeOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getAcademicRecordPage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
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
  Object.assign(query, { keyword: '', majorId: '', gradeId: '', classId: '', studentStatus: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

function openDialog(row?: AcademicRecord) {
  Object.assign(form, row || { id: undefined, studentId: undefined, majorId: undefined, gradeId: undefined, classId: undefined, studentStatus: 'ENROLLED', enrollmentDate: '', expectedGraduationDate: '', actualLeaveDate: '', remark: '' })
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

function resetDialog() {
  Object.assign(form, { id: undefined, studentId: undefined, majorId: undefined, gradeId: undefined, classId: undefined, studentStatus: 'ENROLLED', enrollmentDate: '', expectedGraduationDate: '', actualLeaveDate: '', remark: '' })
}

async function submitForm() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await updateAcademicRecord(form.id, form)
      ElMessage.success('学籍记录已更新')
    } else {
      await createAcademicRecord(form)
      ElMessage.success('学籍记录已新增')
    }
    dialogVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

function openStatusDialog(row: AcademicRecord) {
  Object.assign(statusForm, { id: row.id, studentStatus: row.studentStatus, actualLeaveDate: row.actualLeaveDate || '', remark: row.remark || '' })
  statusDialogVisible.value = true
  nextTick(() => statusFormRef.value?.clearValidate())
}

function resetStatusDialog() {
  Object.assign(statusForm, { id: undefined, studentStatus: 'ENROLLED', actualLeaveDate: '', remark: '' })
}

async function submitStatus() {
  await statusFormRef.value?.validate()
  if (!statusForm.id) return
  submitLoading.value = true
  try {
    await updateAcademicRecordStatus(statusForm.id, statusForm)
    ElMessage.success('学籍状态已更新')
    statusDialogVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

function openDetail(row: AcademicRecord) {
  detail.value = row
  detailVisible.value = true
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
.academic-record-page {
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
