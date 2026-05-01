<template>
  <div class="page-container teaching-selection-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="关键词"><el-input v-model.trim="query.keyword" placeholder="课程 / 教师 / 地点" clearable /></el-form-item>
        <el-form-item label="课程类型">
          <el-select v-model="query.courseType" placeholder="全部类型" clearable>
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="学年"><el-input v-model.trim="query.academicYear" placeholder="2025-2026" clearable /></el-form-item>
        <el-form-item label="学期"><el-input v-model.trim="query.semester" placeholder="第一学期" clearable /></el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="loadAvailable">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>可选课程</template>
      <el-table v-loading="availableLoading" :data="availableRecords" border stripe>
        <el-table-column prop="courseCode" label="课程编码" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="170" show-overflow-tooltip />
        <el-table-column label="课程类型" width="110"><template #default="{ row }">{{ formatOption(typeOptions, row.courseType) }}</template></el-table-column>
        <el-table-column prop="teacherName" label="教师" width="110" />
        <el-table-column prop="academicYear" label="学年" width="120" />
        <el-table-column prop="semester" label="学期" width="110" />
        <el-table-column label="容量" width="100" align="center"><template #default="{ row }">{{ row.selectedCount }}/{{ row.capacity }}</template></el-table-column>
        <el-table-column prop="classTime" label="上课时间" min-width="150" show-overflow-tooltip />
        <el-table-column prop="classroom" label="地点" min-width="120" show-overflow-tooltip />
        <el-table-column label="操作" width="110" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'teaching:selection:select'" link type="primary" @click="selectCourse(row)">选课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>已选课程</template>
      <el-table v-loading="selectedLoading" :data="selectedRecords" border stripe>
        <el-table-column prop="courseCode" label="课程编码" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="170" show-overflow-tooltip />
        <el-table-column prop="teacherName" label="教师" width="110" />
        <el-table-column prop="academicYear" label="学年" width="120" />
        <el-table-column prop="semester" label="学期" width="110" />
        <el-table-column prop="classTime" label="上课时间" min-width="150" show-overflow-tooltip />
        <el-table-column prop="classroom" label="地点" min-width="120" show-overflow-tooltip />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(enrollmentOptions, row.enrollmentStatus)">{{ formatOption(enrollmentOptions, row.enrollmentStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="selectTime" label="选课时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="110" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.enrollmentStatus === 'SELECTED'" v-permission="'teaching:selection:drop'" link type="danger" @click="dropCourse(row)">退课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { dropTeachingCourse, getAvailableTeachingOfferings, getMyTeachingSelections, selectTeachingCourse } from '@/api/teaching/selection'
import type { DictOption } from '@/types/config'
import type { TeachingEnrollment, TeachingOffering, TeachingQuery } from '@/types/teaching'

const fallbackTypeOptions: DictOption[] = [
  { label: '必修课', value: 'REQUIRED' },
  { label: '选修课', value: 'ELECTIVE' },
  { label: '语言课', value: 'LANGUAGE' },
  { label: '实践课', value: 'PRACTICE' }
]
const fallbackEnrollmentOptions: DictOption[] = [
  { label: '已选课', value: 'SELECTED', tagType: 'success' },
  { label: '已退课', value: 'DROPPED', tagType: 'info' }
]

const query = reactive<TeachingQuery>({ pageNum: 1, pageSize: 100 })
const typeOptions = ref<DictOption[]>(fallbackTypeOptions)
const enrollmentOptions = ref<DictOption[]>(fallbackEnrollmentOptions)
const availableRecords = ref<TeachingOffering[]>([])
const selectedRecords = ref<TeachingEnrollment[]>([])
const availableLoading = ref(false)
const selectedLoading = ref(false)

async function loadOptions() {
  try {
    typeOptions.value = (await getDictOptions('teaching_course_type')) || fallbackTypeOptions
  } catch {
    typeOptions.value = fallbackTypeOptions
  }
  try {
    enrollmentOptions.value = (await getDictOptions('teaching_enrollment_status')) || fallbackEnrollmentOptions
  } catch {
    enrollmentOptions.value = fallbackEnrollmentOptions
  }
}

async function loadAvailable() {
  availableLoading.value = true
  try {
    availableRecords.value = await getAvailableTeachingOfferings(query)
  } finally {
    availableLoading.value = false
  }
}

async function loadSelected() {
  selectedLoading.value = true
  try {
    selectedRecords.value = await getMyTeachingSelections()
  } finally {
    selectedLoading.value = false
  }
}

function handleReset() {
  Object.assign(query, { keyword: '', courseType: '', academicYear: '', semester: '' })
  loadAvailable()
}

async function selectCourse(row: TeachingOffering) {
  await selectTeachingCourse(row.id)
  ElMessage.success('选课成功')
  await Promise.all([loadAvailable(), loadSelected()])
}

async function dropCourse(row: TeachingEnrollment) {
  await ElMessageBox.confirm(`确认退选课程「${row.courseName}」吗？`, '退课确认', { type: 'warning' })
  await dropTeachingCourse(row.offeringId)
  ElMessage.success('退课成功')
  await Promise.all([loadAvailable(), loadSelected()])
}

function formatOption(options: DictOption[], value?: string) {
  return options.find((item) => item.value === value)?.label || value || '-'
}

function optionTag(options: DictOption[], value?: string) {
  return (options.find((item) => item.value === value)?.tagType || 'info') as 'success' | 'warning' | 'info' | 'primary' | 'danger'
}

onMounted(async () => {
  await loadOptions()
  await Promise.all([loadAvailable(), loadSelected()])
})
</script>

<style scoped lang="scss">
.teaching-selection-page {
  :deep(.el-select) {
    width: 100%;
  }
}
</style>
