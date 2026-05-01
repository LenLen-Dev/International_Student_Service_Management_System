<template>
  <div class="page-container teaching-offering-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="关键词"><el-input v-model.trim="query.keyword" placeholder="课程 / 教师 / 教室" clearable /></el-form-item>
        <el-form-item label="学年"><el-input v-model.trim="query.academicYear" placeholder="2025-2026" clearable /></el-form-item>
        <el-form-item label="学期"><el-input v-model.trim="query.semester" placeholder="第一学期" clearable /></el-form-item>
        <el-form-item label="开课状态">
          <el-select v-model="query.offeringStatus" placeholder="全部状态" clearable>
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
        <el-button v-permission="'teaching:offering:add'" type="primary" :icon="Plus" @click="openDialog()">新增教学班</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="courseCode" label="课程编码" min-width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="170" show-overflow-tooltip />
        <el-table-column prop="teacherName" label="教师" width="110" />
        <el-table-column prop="academicYear" label="学年" width="120" />
        <el-table-column prop="semester" label="学期" width="110" />
        <el-table-column label="容量" width="110" align="center"><template #default="{ row }">{{ row.selectedCount }}/{{ row.capacity }}</template></el-table-column>
        <el-table-column prop="classTime" label="上课时间" min-width="150" show-overflow-tooltip />
        <el-table-column prop="classroom" label="地点" min-width="120" show-overflow-tooltip />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(statusOptions, row.offeringStatus)">{{ formatOption(statusOptions, row.offeringStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="showStudents(row)">名单</el-button>
            <el-button v-permission="'teaching:offering:update'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'teaching:offering:status'" link type="warning" @click="openStatus(row)">状态</el-button>
            <el-button v-permission="'teaching:offering:delete'" link type="danger" @click="removeRecord(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRecords" @current-change="loadRecords" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑教学班' : '新增教学班'" width="760px" @closed="resetDialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="112px">
        <div class="form-grid">
          <el-form-item label="课程" prop="courseId">
            <el-select v-model="form.courseId" filterable><el-option v-for="item in courses" :key="item.id" :label="`${item.courseCode} / ${item.courseName}`" :value="item.id" /></el-select>
          </el-form-item>
          <el-form-item label="教师ID"><el-input-number v-model="form.teacherId" :min="1" controls-position="right" placeholder="教师可留空" /></el-form-item>
          <el-form-item label="学年" prop="academicYear"><el-input v-model.trim="form.academicYear" /></el-form-item>
          <el-form-item label="学期" prop="semester"><el-input v-model.trim="form.semester" /></el-form-item>
          <el-form-item label="容量" prop="capacity"><el-input-number v-model="form.capacity" :min="1" controls-position="right" /></el-form-item>
          <el-form-item label="状态" prop="offeringStatus">
            <el-select v-model="form.offeringStatus"><el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
          </el-form-item>
          <el-form-item label="选课开始"><el-date-picker v-model="form.selectionStartTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
          <el-form-item label="选课结束"><el-date-picker v-model="form.selectionEndTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
          <el-form-item label="上课时间"><el-input v-model.trim="form.classTime" /></el-form-item>
          <el-form-item label="上课地点"><el-input v-model.trim="form.classroom" /></el-form-item>
        </div>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="statusVisible" title="修改开课状态" width="420px">
      <el-select v-model="targetStatus" class="full-width"><el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
      <template #footer>
        <el-button @click="statusVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitStatus">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="studentsVisible" title="选课名单" width="720px">
      <el-table :data="students" border stripe>
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column label="姓名"><template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template></el-table-column>
        <el-table-column prop="selectTime" label="选课时间" min-width="170" show-overflow-tooltip />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { getEnabledTeachingCourses } from '@/api/teaching/course'
import { createTeachingOffering, deleteTeachingOffering, getTeachingOfferingPage, getTeachingOfferingStudents, updateTeachingOffering, updateTeachingOfferingStatus } from '@/api/teaching/offering'
import type { DictOption } from '@/types/config'
import type { TeachingCourse, TeachingEnrollment, TeachingOffering, TeachingOfferingForm, TeachingQuery } from '@/types/teaching'

const fallbackStatusOptions: DictOption[] = [
  { label: '草稿', value: 'DRAFT', tagType: 'info' },
  { label: '开放选课', value: 'OPEN', tagType: 'success' },
  { label: '已关闭', value: 'CLOSED', tagType: 'warning' },
  { label: '已结课', value: 'FINISHED', tagType: 'info' }
]
const query = reactive<TeachingQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<TeachingOffering[]>([])
const courses = ref<TeachingCourse[]>([])
const students = ref<TeachingEnrollment[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const statusVisible = ref(false)
const studentsVisible = ref(false)
const activeId = ref<number>()
const targetStatus = ref('OPEN')
const formRef = ref<FormInstance>()
const statusOptions = ref<DictOption[]>(fallbackStatusOptions)
const form = reactive<TeachingOfferingForm>({ courseId: undefined, teacherId: undefined, academicYear: '', semester: '', capacity: 30, offeringStatus: 'DRAFT', selectionStartTime: '', selectionEndTime: '', classTime: '', classroom: '', remark: '' })
const rules: FormRules<TeachingOfferingForm> = {
  courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
  academicYear: [{ required: true, message: '请输入学年', trigger: 'blur' }],
  semester: [{ required: true, message: '请输入学期', trigger: 'blur' }],
  capacity: [{ required: true, message: '请输入容量', trigger: 'blur' }],
  offeringStatus: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

async function loadOptions() {
  courses.value = await getEnabledTeachingCourses()
  try {
    statusOptions.value = (await getDictOptions('teaching_offering_status')) || fallbackStatusOptions
  } catch {
    statusOptions.value = fallbackStatusOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getTeachingOfferingPage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
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
  Object.assign(query, { keyword: '', academicYear: '', semester: '', offeringStatus: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

function openDialog(row?: TeachingOffering) {
  Object.assign(form, row || { id: undefined, courseId: undefined, teacherId: undefined, academicYear: '', semester: '', capacity: 30, offeringStatus: 'DRAFT', selectionStartTime: '', selectionEndTime: '', classTime: '', classroom: '', remark: '' })
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

function resetDialog() {
  Object.assign(form, { id: undefined, courseId: undefined, teacherId: undefined, academicYear: '', semester: '', capacity: 30, offeringStatus: 'DRAFT', selectionStartTime: '', selectionEndTime: '', classTime: '', classroom: '', remark: '' })
}

async function submitForm() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await updateTeachingOffering(form.id, form)
      ElMessage.success('教学班已更新')
    } else {
      await createTeachingOffering(form)
      ElMessage.success('教学班已新增')
    }
    dialogVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

function openStatus(row: TeachingOffering) {
  activeId.value = row.id
  targetStatus.value = row.offeringStatus
  statusVisible.value = true
}

async function submitStatus() {
  if (!activeId.value) return
  submitLoading.value = true
  try {
    await updateTeachingOfferingStatus(activeId.value, targetStatus.value)
    ElMessage.success('开课状态已更新')
    statusVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

async function showStudents(row: TeachingOffering) {
  students.value = await getTeachingOfferingStudents(row.id)
  studentsVisible.value = true
}

async function removeRecord(row: TeachingOffering) {
  await ElMessageBox.confirm(`确认删除教学班「${row.courseName}」吗？`, '删除确认', { type: 'warning' })
  await deleteTeachingOffering(row.id)
  ElMessage.success('教学班已删除')
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
.teaching-offering-page {
  :deep(.el-select),
  :deep(.el-date-editor),
  :deep(.el-input-number),
  .full-width {
    width: 100%;
  }

  .form-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 0 16px;
  }
}
</style>
