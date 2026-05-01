<template>
  <div class="page-container teaching-my-page">
    <el-card class="overview-card">
      <div class="overview-title">我的教务</div>
      <div class="overview-subtitle">查看当前学期课程、成绩、出勤与学业预警</div>
      <div class="overview-metrics">
        <div class="metric-item"><span>{{ overview.selections.length }}</span><label>已选课程</label></div>
        <div class="metric-item"><span>{{ overview.grades.length }}</span><label>已发布成绩</label></div>
        <div class="metric-item"><span>{{ overview.attendance.length }}</span><label>出勤记录</label></div>
        <div class="metric-item danger"><span>{{ overview.alerts.length }}</span><label>学业预警</label></div>
      </div>
    </el-card>

    <el-card class="table-card">
      <template #header>我的课程</template>
      <el-table v-loading="loading" :data="overview.selections" border stripe>
        <el-table-column prop="courseCode" label="课程编码" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="170" show-overflow-tooltip />
        <el-table-column prop="teacherName" label="教师" width="110" />
        <el-table-column prop="academicYear" label="学年" width="120" />
        <el-table-column prop="semester" label="学期" width="110" />
        <el-table-column prop="classTime" label="上课时间" min-width="150" show-overflow-tooltip />
        <el-table-column prop="classroom" label="地点" min-width="120" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>我的成绩</template>
      <el-table v-loading="loading" :data="overview.grades" border stripe>
        <el-table-column prop="courseName" label="课程" min-width="170" show-overflow-tooltip />
        <el-table-column prop="usualScore" label="平时" width="90" align="center" />
        <el-table-column prop="finalScore" label="期末" width="90" align="center" />
        <el-table-column prop="totalScore" label="总评" width="90" align="center" />
        <el-table-column prop="gradePoint" label="绩点" width="90" align="center" />
        <el-table-column label="通过" width="90" align="center">
          <template #default="{ row }"><el-tag :type="row.passed === 1 ? 'success' : 'danger'">{{ row.passed === 1 ? '是' : '否' }}</el-tag></template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>我的出勤</template>
      <el-table v-loading="loading" :data="overview.attendance" border stripe>
        <el-table-column prop="courseName" label="课程" min-width="170" show-overflow-tooltip />
        <el-table-column prop="attendanceDate" label="日期" width="120" />
        <el-table-column label="状态" width="120" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(attendanceOptions, row.attendanceStatus)">{{ formatOption(attendanceOptions, row.attendanceStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>我的学业预警</template>
      <el-table v-loading="loading" :data="overview.alerts" border stripe>
        <el-table-column label="类型" width="120"><template #default="{ row }">{{ formatOption(alertTypeOptions, row.alertType) }}</template></el-table-column>
        <el-table-column label="等级" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(alertLevelOptions, row.alertLevel)">{{ formatOption(alertLevelOptions, row.alertLevel) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="content" label="内容" min-width="240" show-overflow-tooltip />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(alertStatusOptions, row.alertStatus)">{{ formatOption(alertStatusOptions, row.alertStatus) }}</el-tag></template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { getDictOptions } from '@/api/config/dict'
import { getMyTeachingOverview } from '@/api/teaching/alert'
import type { DictOption } from '@/types/config'
import type { TeachingMyOverview } from '@/types/teaching'

const overview = reactive<TeachingMyOverview>({ selections: [], grades: [], attendance: [], alerts: [] })
const loading = ref(false)
const attendanceOptions = ref<DictOption[]>([
  { label: '出勤', value: 'PRESENT', tagType: 'success' },
  { label: '迟到', value: 'LATE', tagType: 'warning' },
  { label: '请假', value: 'LEAVE', tagType: 'primary' },
  { label: '缺勤', value: 'ABSENT', tagType: 'danger' }
])
const alertTypeOptions = ref<DictOption[]>([
  { label: '挂科预警', value: 'FAILED_COURSE' },
  { label: '低分预警', value: 'LOW_SCORE' },
  { label: '缺勤预警', value: 'ABSENCE' },
  { label: '未选课预警', value: 'NO_SELECTION' }
])
const alertLevelOptions = ref<DictOption[]>([
  { label: '低', value: 'LOW', tagType: 'info' },
  { label: '中', value: 'MEDIUM', tagType: 'warning' },
  { label: '高', value: 'HIGH', tagType: 'danger' }
])
const alertStatusOptions = ref<DictOption[]>([
  { label: '待处理', value: 'PENDING', tagType: 'warning' },
  { label: '已处理', value: 'HANDLED', tagType: 'success' },
  { label: '已关闭', value: 'CLOSED', tagType: 'info' }
])

async function loadOptions() {
  try {
    attendanceOptions.value = await getDictOptions('teaching_attendance_status')
  } catch {
    // keep fallback options
  }
  try {
    alertTypeOptions.value = await getDictOptions('teaching_alert_type')
  } catch {
    // keep fallback options
  }
  try {
    alertLevelOptions.value = await getDictOptions('teaching_alert_level')
  } catch {
    // keep fallback options
  }
  try {
    alertStatusOptions.value = await getDictOptions('teaching_alert_status')
  } catch {
    // keep fallback options
  }
}

async function loadOverview() {
  loading.value = true
  try {
    const result = await getMyTeachingOverview()
    Object.assign(overview, {
      selections: result.selections || [],
      grades: result.grades || [],
      attendance: result.attendance || [],
      alerts: result.alerts || []
    })
  } finally {
    loading.value = false
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
  await loadOverview()
})
</script>

<style scoped lang="scss">
.teaching-my-page {
  .overview-card {
    margin-bottom: 16px;
  }

  .overview-title {
    color: #1f2f46;
    font-size: 22px;
    font-weight: 700;
  }

  .overview-subtitle {
    margin-top: 6px;
    color: #6b778c;
  }

  .overview-metrics {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 16px;
    margin-top: 20px;
  }

  .metric-item {
    padding: 16px;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    background: #f8fbff;

    span {
      display: block;
      color: #2f6feb;
      font-size: 26px;
      font-weight: 700;
      line-height: 1;
    }

    label {
      display: block;
      margin-top: 8px;
      color: #606266;
    }

    &.danger span {
      color: #e34d59;
    }
  }
}
</style>
