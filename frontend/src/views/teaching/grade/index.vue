<template>
  <div class="page-container teaching-grade-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="关键词"><el-input v-model.trim="query.keyword" placeholder="课程 / 学生 / 教师" clearable /></el-form-item>
        <el-form-item label="教学班">
          <el-select v-model="query.offeringId" filterable clearable placeholder="全部教学班">
            <el-option v-for="item in offerings" :key="item.id" :label="offeringLabel(item)" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布状态">
          <el-select v-model="query.gradeStatus" placeholder="全部状态" clearable>
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
        <el-button v-permission="'teaching:grade:import'" type="primary" :icon="Edit" :disabled="!query.offeringId" @click="openBatchDialog">批量录入</el-button>
        <el-button v-permission="'teaching:grade:publish'" type="success" :icon="Upload" :disabled="!query.offeringId" @click="publishGrades">发布成绩</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column label="姓名" width="120"><template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template></el-table-column>
        <el-table-column prop="courseName" label="课程" min-width="170" show-overflow-tooltip />
        <el-table-column prop="teacherName" label="教师" width="110" />
        <el-table-column prop="academicYear" label="学年" width="120" />
        <el-table-column prop="semester" label="学期" width="110" />
        <el-table-column prop="usualScore" label="平时" width="90" align="center" />
        <el-table-column prop="finalScore" label="期末" width="90" align="center" />
        <el-table-column prop="totalScore" label="总评" width="90" align="center" />
        <el-table-column prop="gradePoint" label="绩点" width="90" align="center" />
        <el-table-column label="通过" width="90" align="center">
          <template #default="{ row }"><el-tag :type="row.passed === 1 ? 'success' : 'danger'">{{ row.passed === 1 ? '是' : '否' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(statusOptions, row.gradeStatus)">{{ formatOption(statusOptions, row.gradeStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'teaching:grade:update'" link type="primary" :disabled="row.gradeStatus === 'PUBLISHED'" @click="openEditDialog(row)">录入</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRecords" @current-change="loadRecords" />
      </div>
    </el-card>

    <el-dialog v-model="editVisible" title="录入成绩" width="520px" @closed="resetEditDialog">
      <el-form ref="formRef" :model="form" label-width="96px">
        <el-form-item label="平时成绩"><el-input-number v-model="form.usualScore" :min="0" :max="100" :precision="1" controls-position="right" /></el-form-item>
        <el-form-item label="期末成绩"><el-input-number v-model="form.finalScore" :min="0" :max="100" :precision="1" controls-position="right" /></el-form-item>
        <el-form-item label="总评成绩"><el-input-number v-model="form.totalScore" :min="0" :max="100" :precision="1" controls-position="right" placeholder="留空自动计算" /></el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="batchVisible" title="批量录入成绩" width="860px">
      <el-table :data="batchRows" border stripe max-height="520">
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column label="姓名" width="120"><template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template></el-table-column>
        <el-table-column label="平时成绩" width="150"><template #default="{ row }"><el-input-number v-model="row.usualScore" :min="0" :max="100" :precision="1" controls-position="right" /></template></el-table-column>
        <el-table-column label="期末成绩" width="150"><template #default="{ row }"><el-input-number v-model="row.finalScore" :min="0" :max="100" :precision="1" controls-position="right" /></template></el-table-column>
        <el-table-column label="总评成绩" width="150"><template #default="{ row }"><el-input-number v-model="row.totalScore" :min="0" :max="100" :precision="1" controls-position="right" /></template></el-table-column>
        <el-table-column label="备注" min-width="180"><template #default="{ row }"><el-input v-model.trim="row.remark" /></template></el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="batchVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitBatch">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import { Edit, Refresh, Search, Upload } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { getTeachingOfferingPage, getTeachingOfferingStudents } from '@/api/teaching/offering'
import { getTeachingGradePage, importTeachingGrades, publishTeachingGrades, updateTeachingGrade } from '@/api/teaching/grade'
import type { DictOption } from '@/types/config'
import type { TeachingEnrollment, TeachingGrade, TeachingGradeForm, TeachingOffering, TeachingQuery } from '@/types/teaching'

type BatchGradeRow = TeachingGradeForm & Pick<TeachingEnrollment, 'studentNo' | 'chineseName' | 'englishName'>

const fallbackStatusOptions: DictOption[] = [
  { label: '未发布', value: 'DRAFT', tagType: 'info' },
  { label: '已发布', value: 'PUBLISHED', tagType: 'success' }
]
const query = reactive<TeachingQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<TeachingGrade[]>([])
const offerings = ref<TeachingOffering[]>([])
const batchRows = ref<BatchGradeRow[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const editVisible = ref(false)
const batchVisible = ref(false)
const formRef = ref<FormInstance>()
const statusOptions = ref<DictOption[]>(fallbackStatusOptions)
const form = reactive<TeachingGradeForm>({ id: undefined, enrollmentId: undefined, usualScore: undefined, finalScore: undefined, totalScore: undefined, remark: '' })

async function loadOptions() {
  const result = await getTeachingOfferingPage({ pageNum: 1, pageSize: 200 })
  offerings.value = result.records || []
  try {
    statusOptions.value = (await getDictOptions('teaching_grade_status')) || fallbackStatusOptions
  } catch {
    statusOptions.value = fallbackStatusOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getTeachingGradePage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
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
  Object.assign(query, { keyword: '', offeringId: '', gradeStatus: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

function openEditDialog(row: TeachingGrade) {
  Object.assign(form, { id: row.id, enrollmentId: row.enrollmentId, usualScore: row.usualScore, finalScore: row.finalScore, totalScore: row.totalScore, remark: row.remark || '' })
  editVisible.value = true
}

function resetEditDialog() {
  Object.assign(form, { id: undefined, enrollmentId: undefined, usualScore: undefined, finalScore: undefined, totalScore: undefined, remark: '' })
}

async function submitEdit() {
  if (!form.id) return
  submitLoading.value = true
  try {
    await updateTeachingGrade(form.id, form)
    ElMessage.success('成绩已保存')
    editVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

async function openBatchDialog() {
  if (!query.offeringId) {
    ElMessage.warning('请先选择教学班')
    return
  }
  const students = await getTeachingOfferingStudents(Number(query.offeringId))
  batchRows.value = students.map((item) => ({ enrollmentId: item.id, studentNo: item.studentNo, chineseName: item.chineseName, englishName: item.englishName, usualScore: undefined, finalScore: undefined, totalScore: undefined, remark: '' }))
  batchVisible.value = true
}

async function submitBatch() {
  if (!query.offeringId) return
  submitLoading.value = true
  try {
    await importTeachingGrades(Number(query.offeringId), batchRows.value.map(({ studentNo: _studentNo, chineseName: _chineseName, englishName: _englishName, ...item }) => item))
    ElMessage.success('成绩已批量保存')
    batchVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

async function publishGrades() {
  if (!query.offeringId) {
    ElMessage.warning('请先选择教学班')
    return
  }
  await ElMessageBox.confirm('发布后学生端可查看成绩，确认发布当前教学班成绩吗？', '发布确认', { type: 'warning' })
  await publishTeachingGrades(Number(query.offeringId))
  ElMessage.success('成绩已发布')
  await loadRecords()
}

function offeringLabel(item: TeachingOffering) {
  return `${item.courseName || item.courseCode} / ${item.teacherName || '-'} / ${item.academicYear || '-'} ${item.semester || ''}`
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
.teaching-grade-page {
  :deep(.el-select),
  :deep(.el-input-number) {
    width: 100%;
  }
}
</style>
