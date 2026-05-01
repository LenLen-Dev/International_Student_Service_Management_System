<template>
  <div class="page-container academic-major-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="80px">
        <el-form-item label="关键词">
          <el-input v-model.trim="query.keyword" placeholder="学院 / 专业编码 / 专业名称" clearable />
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
        <el-button v-permission="'academic:major:add'" type="primary" :icon="Plus" @click="openDialog()">新增专业</el-button>
      </div>
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="college" label="学院" min-width="160" show-overflow-tooltip />
        <el-table-column prop="majorCode" label="专业编码" min-width="130" />
        <el-table-column prop="majorName" label="专业名称" min-width="160" show-overflow-tooltip />
        <el-table-column label="学历层次" width="120">
          <template #default="{ row }">{{ formatOption(degreeOptions, row.degreeLevel) }}</template>
        </el-table-column>
        <el-table-column prop="studyDuration" label="学制" width="90" align="center">
          <template #default="{ row }">{{ row.studyDuration ? `${row.studyDuration} 年` : '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }">
            <el-switch
              v-permission="'academic:major:status'"
              :model-value="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="(value) => changeStatus(row, Number(value))"
            />
            <el-tag v-if="!canUpdateStatus" :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'academic:major:update'" link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button v-permission="'academic:major:delete'" link type="danger" @click="removeRecord(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <el-pagination
          v-model:current-page="pageQuery.pageNum"
          v-model:page-size="pageQuery.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadRecords"
          @current-change="loadRecords"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑专业' : '新增专业'" width="680px" @closed="resetDialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <div class="form-grid">
          <el-form-item label="学院" prop="college"><el-input v-model.trim="form.college" /></el-form-item>
          <el-form-item label="专业编码" prop="majorCode"><el-input v-model.trim="form.majorCode" /></el-form-item>
          <el-form-item label="专业名称" prop="majorName"><el-input v-model.trim="form.majorName" /></el-form-item>
          <el-form-item label="学历层次">
            <el-select v-model="form.degreeLevel" clearable>
              <el-option v-for="item in degreeOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="学制">
            <el-input-number v-model="form.studyDuration" :min="1" :max="10" controls-position="right" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :value="1">启用</el-radio>
              <el-radio :value="0">禁用</el-radio>
            </el-radio-group>
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
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { getDictOptions } from '@/api/config/dict'
import { createAcademicMajor, deleteAcademicMajor, getAcademicMajorPage, updateAcademicMajor, updateAcademicMajorStatus } from '@/api/academic/major'
import { hasPermission } from '@/utils/permission'
import type { DictOption } from '@/types/config'
import type { AcademicMajor, AcademicMajorForm, AcademicQuery } from '@/types/academic'

const fallbackDegreeOptions: DictOption[] = [
  { label: '本科', value: 'BACHELOR' },
  { label: '硕士', value: 'MASTER' },
  { label: '博士', value: 'DOCTOR' }
]

const query = reactive<AcademicQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const records = ref<AcademicMajor[]>([])
const total = ref(0)
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref<FormInstance>()
const degreeOptions = ref<DictOption[]>(fallbackDegreeOptions)
const canUpdateStatus = computed(() => hasPermission('academic:major:status'))

const form = reactive<AcademicMajorForm>({ college: '', majorCode: '', majorName: '', degreeLevel: '', status: 1, remark: '' })
const rules: FormRules<AcademicMajorForm> = {
  college: [{ required: true, message: '请输入学院', trigger: 'blur' }],
  majorCode: [{ required: true, message: '请输入专业编码', trigger: 'blur' }],
  majorName: [{ required: true, message: '请输入专业名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

async function loadOptions() {
  try {
    degreeOptions.value = (await getDictOptions('academic_degree_level')) || fallbackDegreeOptions
  } catch {
    degreeOptions.value = fallbackDegreeOptions
  }
}

async function loadRecords() {
  loading.value = true
  try {
    const result = await getAcademicMajorPage({ ...query, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize })
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
  Object.assign(query, { keyword: '', status: '' })
  pageQuery.pageNum = 1
  loadRecords()
}

function openDialog(row?: AcademicMajor) {
  Object.assign(form, row || { id: undefined, college: '', majorCode: '', majorName: '', degreeLevel: '', studyDuration: undefined, status: 1, remark: '' })
  dialogVisible.value = true
  nextTick(() => formRef.value?.clearValidate())
}

function resetDialog() {
  Object.assign(form, { id: undefined, college: '', majorCode: '', majorName: '', degreeLevel: '', studyDuration: undefined, status: 1, remark: '' })
}

async function submitForm() {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    if (form.id) {
      await updateAcademicMajor(form.id, form)
      ElMessage.success('专业已更新')
    } else {
      await createAcademicMajor(form)
      ElMessage.success('专业已新增')
    }
    dialogVisible.value = false
    await loadRecords()
  } finally {
    submitLoading.value = false
  }
}

async function changeStatus(row: AcademicMajor, status: number) {
  await updateAcademicMajorStatus(row.id, status)
  ElMessage.success(status === 1 ? '专业已启用' : '专业已禁用')
  await loadRecords()
}

async function removeRecord(row: AcademicMajor) {
  await ElMessageBox.confirm(`确认删除专业「${row.majorName}」吗？`, '删除确认', { type: 'warning' })
  await deleteAcademicMajor(row.id)
  ElMessage.success('专业已删除')
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
.academic-major-page {
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
