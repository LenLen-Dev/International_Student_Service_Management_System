<template>
  <div class="page-container teaching-attendance-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="关键词"><el-input v-model.trim="query.keyword" placeholder="课程 / 学生 / 教师" clearable /></el-form-item>
        <el-form-item label="教学班">
          <el-select v-model="query.offeringId" filterable clearable placeholder="全部教学班">
            <el-option v-for="item in offerings" :key="item.id" :label="offeringLabel(item)" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期"><el-date-picker v-model="query.attendanceDate" type="date" value-format="YYYY-MM-DD" clearable /></el-form-item>
        <el-form-item label="出勤状态">
          <el-select v-model="query.attendanceStatus" placeholder="全部状态" clearable>
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
        <el-button v-permission="'teaching:attendance:save'" type="primary" :icon="Edit" :disabled="!query.offeringId" @click="openAttendanceDialog">维护出勤</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column label="姓名" width="120"><template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template></el-table-column>
        <el-table-column prop="courseName" label="课程" min-width="170" show-overflow-tooltip />
        <el-table-column prop="teacherName" label="教师" width="110" />
        <el-table-column prop="attendanceDate" label="日期" width="120" />
        <el-table-column label="出勤状态" width="120" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(statusOptions, row.attendanceStatus)">{{ formatOption(statusOptions, row.attendanceStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="170" show-overflow-tooltip />
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRecords" @current-change="loadRecords" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="维护出勤" width="820px">
      <div class="attendance-date">
        <span>出勤日期</span>
        <el-date-picker v-model="attendanceDate" type="date" value-format="YYYY-MM-DD" />
      </div>
      <el-table :data="attendanceRows" border stripe max-height="520">
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column label="姓名" width="120"><template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template></el-table-column>
        <el-table-column label="出勤状态" width="180">
          <template #default="{ row }">
            <el-select v-model="row.attendanceStatus"><el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
          </template>
        </el-table-column>
        <el-table-column label="备注" min-width="220"><template #default="{ row }"><el-input v-model.trim="row.remark" /></template></el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitAttendance">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, Refresh, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { getTeachingOfferingPage, getTeachingOfferingStudents } from '@/api/teaching/offering'
import { getTeachingAttendancePage, saveTeachingAttendance } from '@/api/teaching/attendance'
import type { DictOption } from '@/types/config'
import type { TeachingAttendance, TeachingAttendanceItem, TeachingOffering, TeachingQuery } from '@/types/teaching'

type AttendanceRow = TeachingAttendanceItem & Pick<TeachingAttendance, 'studentId' | 'studentNo' | 'chineseName' | 'englishName'>

const fallbackStatusOptions: DictOption[] = [
  { label: '出勤', value: 'PRESENT', tagType: 'success' },
  { label: '迟到', value: 'LATE', tagType: 'warning' },
  { label: '请假', value: 'LEAVE', tagType: 'primary' },
  { label: '缺勤', value: 'ABSENT', tagType: 'danger' }
]
const query = reactive<TeachingQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<TeachingAttendance[]>([])
const offerings = ref<TeachingOffering[]>([])
const attendanceRows = ref<AttendanceRow[]>([])
const statusOptions = ref<DictOption[]>(fallbackStatusOptions)
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const attendanceDate = ref(new Date().toISOString().slice(0, 10))

async function loadOptions() {
  const result = await getTeachingOfferingPage({ pageNum: 1, pageSize: 200 })
  offerings.value = result.records || []
  try {
    statusOptions.value = (await getDictOptions('teaching_attendance_status')) || fallbackStatusOptions
  } catch {
    statusOptions.value = fallbackStatusOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getTeachingAttendancePage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
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
  Object.assign(query, { keyword: '', offeringId: '', attendanceDate: '', attendanceStatus: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

async function openAttendanceDialog() {
  if (!query.offeringId) {
    ElMessage.warning('请先选择教学班')
    return
  }
  attendanceDate.value = String(query.attendanceDate || new Date().toISOString().slice(0, 10))
  const students = await getTeachingOfferingStudents(Number(query.offeringId))
  attendanceRows.value = students.map((item) => ({ studentId: item.studentId, studentNo: item.studentNo, chineseName: item.chineseName, englishName: item.englishName, attendanceStatus: 'PRESENT', remark: '' }))
  dialogVisible.value = true
}

async function submitAttendance() {
  if (!query.offeringId) return
  if (!attendanceDate.value) {
    ElMessage.warning('请选择出勤日期')
    return
  }
  submitLoading.value = true
  try {
    await saveTeachingAttendance(
      Number(query.offeringId),
      attendanceRows.value.map((item) => ({
        studentId: item.studentId,
        attendanceDate: attendanceDate.value,
        attendanceStatus: item.attendanceStatus,
        remark: item.remark
      }))
    )
    ElMessage.success('出勤已保存')
    dialogVisible.value = false
    query.attendanceDate = attendanceDate.value
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
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
.teaching-attendance-page {
  :deep(.el-select),
  :deep(.el-date-editor) {
    width: 100%;
  }

  .attendance-date {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    color: #303133;
  }
}
</style>
