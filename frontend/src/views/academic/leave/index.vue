<template>
  <div class="page-container academic-leave-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="86px">
        <el-form-item label="学生">
          <el-input v-model.trim="query.keyword" placeholder="学号 / 中文名 / 英文名" clearable />
        </el-form-item>
        <el-form-item label="请假状态">
          <el-select v-model="query.leaveStatus" placeholder="全部状态" clearable>
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
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="studentNo" label="学号" min-width="120" />
        <el-table-column label="学生姓名" min-width="150">
          <template #default="{ row }">{{ row.chineseName || row.englishName || '-' }}</template>
        </el-table-column>
        <el-table-column prop="className" label="班级" min-width="130" />
        <el-table-column label="请假类型" width="120">
          <template #default="{ row }">{{ formatOption(typeOptions, row.leaveType) }}</template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" min-width="170" show-overflow-tooltip />
        <el-table-column prop="endTime" label="结束时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }"><el-tag :type="optionTag(statusOptions, row.leaveStatus)">{{ formatOption(statusOptions, row.leaveStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="approverName" label="审批人" width="110" />
        <el-table-column label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'academic:leave:detail'" link type="primary" @click="openDetail(row)">详情</el-button>
            <el-button v-if="row.leaveStatus === 'PENDING'" v-permission="'academic:leave:approve'" link type="success" @click="openApprove(row, 'APPROVED')">通过</el-button>
            <el-button v-if="row.leaveStatus === 'PENDING'" v-permission="'academic:leave:reject'" link type="danger" @click="openApprove(row, 'REJECTED')">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination v-model:current-page="pageQuery.pageNum" v-model:page-size="pageQuery.pageSize" :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="loadRecords" @current-change="loadRecords" />
      </div>
    </el-card>

    <el-dialog v-model="detailVisible" title="请假详情" width="760px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="学号">{{ detail.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.chineseName || detail.englishName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请假类型">{{ formatOption(typeOptions, detail.leaveType) }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ formatOption(statusOptions, detail.leaveStatus) }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ detail.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ detail.endTime }}</el-descriptions-item>
        <el-descriptions-item label="请假原因" :span="2">{{ detail.reason || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批人">{{ detail.approverName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ detail.approveTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批意见" :span="2">{{ detail.approveOpinion || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="approveVisible" :title="approveAction === 'APPROVED' ? '审批通过' : '审批拒绝'" width="520px" @closed="resetApprove">
      <el-form label-width="90px">
        <el-form-item label="审批意见">
          <el-input v-model.trim="approveForm.approveOpinion" type="textarea" :rows="4" placeholder="请输入审批意见" />
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
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { approveAcademicLeave, getAcademicLeavePage, rejectAcademicLeave } from '@/api/academic/leave'
import type { DictOption } from '@/types/config'
import type { AcademicApproveForm, AcademicLeave, AcademicQuery } from '@/types/academic'

const fallbackTypeOptions: DictOption[] = [
  { label: '事假', value: 'PERSONAL' },
  { label: '病假', value: 'SICK' },
  { label: '公假', value: 'OFFICIAL' },
  { label: '其他', value: 'OTHER' }
]
const fallbackStatusOptions: DictOption[] = [
  { label: '待审批', value: 'PENDING', tagType: 'warning' },
  { label: '已通过', value: 'APPROVED', tagType: 'success' },
  { label: '已拒绝', value: 'REJECTED', tagType: 'danger' },
  { label: '已撤回', value: 'CANCELLED', tagType: 'info' }
]

const query = reactive<AcademicQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<AcademicLeave[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const detailVisible = ref(false)
const approveVisible = ref(false)
const detail = ref<AcademicLeave>()
const activeRow = ref<AcademicLeave>()
const approveAction = ref<'APPROVED' | 'REJECTED'>('APPROVED')
const approveForm = reactive<AcademicApproveForm>({ approveOpinion: '' })
const typeOptions = ref<DictOption[]>(fallbackTypeOptions)
const statusOptions = ref<DictOption[]>(fallbackStatusOptions)

async function loadOptions() {
  try {
    typeOptions.value = (await getDictOptions('academic_leave_type')) || fallbackTypeOptions
    statusOptions.value = (await getDictOptions('academic_leave_status')) || fallbackStatusOptions
  } catch {
    typeOptions.value = fallbackTypeOptions
    statusOptions.value = fallbackStatusOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getAcademicLeavePage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
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
  Object.assign(query, { keyword: '', leaveStatus: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

function openDetail(row: AcademicLeave) {
  detail.value = row
  detailVisible.value = true
}

function openApprove(row: AcademicLeave, action: 'APPROVED' | 'REJECTED') {
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
      await approveAcademicLeave(activeRow.value.id, approveForm)
      ElMessage.success('请假已审批通过')
    } else {
      await rejectAcademicLeave(activeRow.value.id, approveForm)
      ElMessage.success('请假已审批拒绝')
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
