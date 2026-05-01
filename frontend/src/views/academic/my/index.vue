<template>
  <div class="page-container academic-my-page">
    <el-card class="table-card record-card">
      <template #header>
        <div class="card-header">
          <span>我的学籍</span>
          <el-button :icon="Refresh" @click="loadOverview">刷新</el-button>
        </div>
      </template>
      <el-skeleton v-if="loading" :rows="4" animated />
      <el-empty v-else-if="!overview?.record" description="暂无学籍记录" />
      <el-descriptions v-else :column="3" border>
        <el-descriptions-item label="学号">{{ overview.record.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ overview.record.chineseName || overview.record.englishName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学籍状态">
          <el-tag :type="optionTag(statusOptions, overview.record.studentStatus)">{{ formatOption(statusOptions, overview.record.studentStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="学院">{{ overview.record.college || '-' }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ overview.record.majorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学历层次">{{ formatOption(degreeOptions, overview.record.degreeLevel) }}</el-descriptions-item>
        <el-descriptions-item label="年级">{{ overview.record.gradeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ overview.record.className || '-' }}</el-descriptions-item>
        <el-descriptions-item label="入学日期">{{ overview.record.enrollmentDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预计毕业">{{ overview.record.expectedGraduationDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际离校">{{ overview.record.actualLeaveDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ overview.record.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>我的请假</span>
          <el-button v-permission="'academic:leave:apply'" type="primary" :icon="Plus" @click="openLeaveDialog">提交请假</el-button>
        </div>
      </template>
      <el-table v-loading="loading" :data="overview?.leaves || []" border stripe>
        <el-table-column label="请假类型" width="120">
          <template #default="{ row }">{{ formatOption(leaveTypeOptions, row.leaveType) }}</template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" min-width="170" show-overflow-tooltip />
        <el-table-column prop="endTime" label="结束时间" min-width="170" show-overflow-tooltip />
        <el-table-column prop="reason" label="请假原因" min-width="220" show-overflow-tooltip />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(leaveStatusOptions, row.leaveStatus)">{{ formatOption(leaveStatusOptions, row.leaveStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="approverName" label="审批人" width="110" />
        <el-table-column prop="approveOpinion" label="审批意见" min-width="180" show-overflow-tooltip />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.leaveStatus === 'PENDING'" v-permission="'academic:leave:cancel'" link type="danger" @click="cancelLeave(row)">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="leaveDialogVisible" title="提交请假" width="640px" @closed="resetLeaveDialog">
      <el-form ref="leaveFormRef" :model="leaveForm" :rules="leaveRules" label-width="96px">
        <el-form-item label="请假类型" prop="leaveType">
          <el-select v-model="leaveForm.leaveType">
            <el-option v-for="item in leaveTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="leaveForm.startTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="leaveForm.endTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" />
        </el-form-item>
        <el-form-item label="请假原因" prop="reason"><el-input v-model.trim="leaveForm.reason" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="leaveForm.remark" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="leaveDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitLeave">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Refresh } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { applyMyAcademicLeave, cancelMyAcademicLeave } from '@/api/academic/leave'
import { getMyAcademicOverview } from '@/api/academic/record'
import type { DictOption } from '@/types/config'
import type { AcademicLeave, AcademicLeaveForm, AcademicMyOverview } from '@/types/academic'

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
const fallbackLeaveTypeOptions: DictOption[] = [
  { label: '事假', value: 'PERSONAL' },
  { label: '病假', value: 'SICK' },
  { label: '公假', value: 'OFFICIAL' },
  { label: '其他', value: 'OTHER' }
]
const fallbackLeaveStatusOptions: DictOption[] = [
  { label: '待审批', value: 'PENDING', tagType: 'warning' },
  { label: '已通过', value: 'APPROVED', tagType: 'success' },
  { label: '已拒绝', value: 'REJECTED', tagType: 'danger' },
  { label: '已撤回', value: 'CANCELLED', tagType: 'info' }
]

const loading = ref(false)
const submitLoading = ref(false)
const leaveDialogVisible = ref(false)
const leaveFormRef = ref<FormInstance>()
const overview = ref<AcademicMyOverview>()
const statusOptions = ref<DictOption[]>(fallbackStatusOptions)
const degreeOptions = ref<DictOption[]>(fallbackDegreeOptions)
const leaveTypeOptions = ref<DictOption[]>(fallbackLeaveTypeOptions)
const leaveStatusOptions = ref<DictOption[]>(fallbackLeaveStatusOptions)
const leaveForm = reactive<AcademicLeaveForm>({ leaveType: 'PERSONAL', startTime: '', endTime: '', reason: '', remark: '' })

const leaveRules: FormRules<AcademicLeaveForm> = {
  leaveType: [{ required: true, message: '请选择请假类型', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  reason: [{ required: true, message: '请输入请假原因', trigger: 'blur' }]
}

async function loadOptions() {
  try {
    statusOptions.value = (await getDictOptions('academic_student_status')) || fallbackStatusOptions
    degreeOptions.value = (await getDictOptions('academic_degree_level')) || fallbackDegreeOptions
    leaveTypeOptions.value = (await getDictOptions('academic_leave_type')) || fallbackLeaveTypeOptions
    leaveStatusOptions.value = (await getDictOptions('academic_leave_status')) || fallbackLeaveStatusOptions
  } catch {
    statusOptions.value = fallbackStatusOptions
    degreeOptions.value = fallbackDegreeOptions
    leaveTypeOptions.value = fallbackLeaveTypeOptions
    leaveStatusOptions.value = fallbackLeaveStatusOptions
  }
}

async function loadOverview() {
  loading.value = true
  try {
    overview.value = await getMyAcademicOverview()
  } finally {
    loading.value = false
  }
}

function openLeaveDialog() {
  leaveDialogVisible.value = true
  nextTick(() => leaveFormRef.value?.clearValidate())
}

function resetLeaveDialog() {
  Object.assign(leaveForm, { leaveType: 'PERSONAL', startTime: '', endTime: '', reason: '', remark: '' })
}

async function submitLeave() {
  await leaveFormRef.value?.validate()
  submitLoading.value = true
  try {
    await applyMyAcademicLeave(leaveForm)
    ElMessage.success('请假申请已提交')
    leaveDialogVisible.value = false
    await loadOverview()
  } finally {
    submitLoading.value = false
  }
}

async function cancelLeave(row: AcademicLeave) {
  await ElMessageBox.confirm('确认撤回该请假申请吗？', '撤回确认', { type: 'warning' })
  await cancelMyAcademicLeave(row.id)
  ElMessage.success('请假申请已撤回')
  await loadOverview()
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
.academic-my-page {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  :deep(.el-select),
  :deep(.el-date-editor) {
    width: 100%;
  }
}
</style>
