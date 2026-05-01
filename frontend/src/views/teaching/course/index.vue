<template>
  <div class="page-container teaching-course-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="关键词"><el-input v-model.trim="query.keyword" placeholder="课程编码 / 课程名称 / 学院" clearable /></el-form-item>
        <el-form-item label="课程类型">
          <el-select v-model="query.courseType" placeholder="全部类型" clearable>
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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
        <el-button v-permission="'teaching:course:add'" type="primary" :icon="Plus" @click="openDialog()">新增课程</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="courseCode" label="课程编码" min-width="130" />
        <el-table-column prop="courseName" label="课程名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="college" label="开课学院" min-width="150" show-overflow-tooltip />
        <el-table-column label="课程类型" width="120"><template #default="{ row }">{{ formatOption(typeOptions, row.courseType) }}</template></el-table-column>
        <el-table-column prop="credits" label="学分" width="90" align="center" />
        <el-table-column prop="totalHours" label="学时" width="90" align="center" />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="210" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'teaching:course:update'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'teaching:course:status'" link type="warning" @click="changeStatus(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
            <el-button v-permission="'teaching:course:delete'" link type="danger" @click="removeRecord(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRecords" @current-change="loadRecords" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑课程' : '新增课程'" width="680px" @closed="resetDialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <div class="form-grid">
          <el-form-item label="课程编码" prop="courseCode"><el-input v-model.trim="form.courseCode" /></el-form-item>
          <el-form-item label="课程名称" prop="courseName"><el-input v-model.trim="form.courseName" /></el-form-item>
          <el-form-item label="课程类型" prop="courseType">
            <el-select v-model="form.courseType"><el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
          </el-form-item>
          <el-form-item label="开课学院"><el-input v-model.trim="form.college" /></el-form-item>
          <el-form-item label="学分" prop="credits"><el-input-number v-model="form.credits" :min="0" :precision="1" controls-position="right" /></el-form-item>
          <el-form-item label="学时"><el-input-number v-model="form.totalHours" :min="0" controls-position="right" /></el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status"><el-radio :value="1">启用</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group>
          </el-form-item>
        </div>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { createTeachingCourse, deleteTeachingCourse, getTeachingCoursePage, updateTeachingCourse, updateTeachingCourseStatus } from '@/api/teaching/course'
import type { DictOption } from '@/types/config'
import type { TeachingCourse, TeachingCourseForm, TeachingQuery } from '@/types/teaching'

const fallbackTypeOptions: DictOption[] = [
  { label: '必修课', value: 'REQUIRED' },
  { label: '选修课', value: 'ELECTIVE' },
  { label: '语言课', value: 'LANGUAGE' },
  { label: '实践课', value: 'PRACTICE' }
]
const query = reactive<TeachingQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<TeachingCourse[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref<FormInstance>()
const typeOptions = ref<DictOption[]>(fallbackTypeOptions)
const form = reactive<TeachingCourseForm>({ courseCode: '', courseName: '', credits: 0, totalHours: undefined, courseType: 'REQUIRED', college: '', status: 1, remark: '' })
const rules: FormRules<TeachingCourseForm> = {
  courseCode: [{ required: true, message: '请输入课程编码', trigger: 'blur' }],
  courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  credits: [{ required: true, message: '请输入学分', trigger: 'blur' }],
  courseType: [{ required: true, message: '请选择课程类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

async function loadOptions() {
  try {
    typeOptions.value = (await getDictOptions('teaching_course_type')) || fallbackTypeOptions
  } catch {
    typeOptions.value = fallbackTypeOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getTeachingCoursePage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
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
  Object.assign(query, { keyword: '', courseType: '', status: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

function openDialog(row?: TeachingCourse) {
  Object.assign(form, row || { id: undefined, courseCode: '', courseName: '', credits: 0, totalHours: undefined, courseType: 'REQUIRED', college: '', status: 1, remark: '' })
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

function resetDialog() {
  Object.assign(form, { id: undefined, courseCode: '', courseName: '', credits: 0, totalHours: undefined, courseType: 'REQUIRED', college: '', status: 1, remark: '' })
}

async function submitForm() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await updateTeachingCourse(form.id, form)
      ElMessage.success('课程已更新')
    } else {
      await createTeachingCourse(form)
      ElMessage.success('课程已新增')
    }
    dialogVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

async function changeStatus(row: TeachingCourse) {
  const status = row.status === 1 ? 0 : 1
  await updateTeachingCourseStatus(row.id, status)
  ElMessage.success(status === 1 ? '课程已启用' : '课程已禁用')
  await loadRecords()
}

async function removeRecord(row: TeachingCourse) {
  await ElMessageBox.confirm(`确认删除课程「${row.courseName}」吗？`, '删除确认', { type: 'warning' })
  await deleteTeachingCourse(row.id)
  ElMessage.success('课程已删除')
  await loadRecords()
}

function formatOption(options: DictOption[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

onMounted(async () => {
  await loadOptions()
  await loadRecords()
})
</script>

<style scoped lang="scss">
.teaching-course-page {
  :deep(.el-select),
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
