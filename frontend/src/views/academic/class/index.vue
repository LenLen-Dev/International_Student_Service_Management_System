<template>
  <div class="page-container academic-class-page">
    <el-tabs v-model="activeTab" class="content-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="年级管理" name="grade">
        <el-card class="search-card">
          <el-form :model="gradeQuery" label-width="80px">
            <el-form-item label="关键词">
              <el-input v-model.trim="gradeQuery.keyword" placeholder="年级编码 / 年级名称" clearable />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="gradeQuery.status" placeholder="全部状态" clearable>
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="searchGrades">查询</el-button>
              <el-button :icon="Refresh" @click="resetGrades">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="table-card">
          <div class="toolbar">
            <el-button v-permission="'academic:grade:add'" type="primary" :icon="Plus" @click="openGradeDialog()">新增年级</el-button>
          </div>
          <el-table v-loading="gradeLoading" :data="grades" border stripe>
            <el-table-column prop="gradeCode" label="年级编码" min-width="130" />
            <el-table-column prop="gradeName" label="年级名称" min-width="130" />
            <el-table-column prop="enrollmentYear" label="入学年份" width="120" align="center" />
            <el-table-column prop="graduationYear" label="预计毕业年份" width="130" align="center" />
            <el-table-column label="状态" width="100" align="center">
              <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button v-permission="'academic:grade:update'" link type="primary" @click="openGradeDialog(row)">编辑</el-button>
                <el-button v-permission="'academic:grade:delete'" link type="danger" @click="removeGrade(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="table-footer">
            <el-pagination v-model:current-page="gradePage.pageNum" v-model:page-size="gradePage.pageSize" :page-sizes="[10, 20, 50]" :total="gradeTotal" layout="total, sizes, prev, pager, next, jumper" @size-change="loadGrades" @current-change="loadGrades" />
          </div>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="班级管理" name="class">
        <el-card class="search-card">
          <el-form :model="classQuery" label-width="80px">
            <el-form-item label="关键词">
              <el-input v-model.trim="classQuery.keyword" placeholder="班级编码 / 班级名称 / 辅导员" clearable />
            </el-form-item>
            <el-form-item label="专业">
              <el-select v-model="classQuery.majorId" placeholder="全部专业" clearable filterable>
                <el-option v-for="item in majorOptions" :key="item.id" :label="`${item.college} / ${item.majorName}`" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="年级">
              <el-select v-model="classQuery.gradeId" placeholder="全部年级" clearable>
                <el-option v-for="item in gradeOptions" :key="item.id" :label="item.gradeName" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="searchClasses">查询</el-button>
              <el-button :icon="Refresh" @click="resetClasses">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="table-card">
          <div class="toolbar">
            <el-button v-permission="'academic:class:add'" type="primary" :icon="Plus" @click="openClassDialog()">新增班级</el-button>
          </div>
          <el-table v-loading="classLoading" :data="classes" border stripe>
            <el-table-column prop="classCode" label="班级编码" min-width="130" />
            <el-table-column prop="className" label="班级名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="college" label="学院" min-width="150" show-overflow-tooltip />
            <el-table-column prop="majorName" label="专业" min-width="150" show-overflow-tooltip />
            <el-table-column prop="gradeName" label="年级" width="110" />
            <el-table-column prop="advisorName" label="班主任/辅导员" min-width="130" />
            <el-table-column label="状态" width="100" align="center">
              <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button v-permission="'academic:class:update'" link type="primary" @click="openClassDialog(row)">编辑</el-button>
                <el-button v-permission="'academic:class:delete'" link type="danger" @click="removeClass(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="table-footer">
            <el-pagination v-model:current-page="classPage.pageNum" v-model:page-size="classPage.pageSize" :page-sizes="[10, 20, 50]" :total="classTotal" layout="total, sizes, prev, pager, next, jumper" @size-change="loadClasses" @current-change="loadClasses" />
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="gradeDialogVisible" :title="gradeForm.id ? '编辑年级' : '新增年级'" width="560px" @closed="resetGradeDialog">
      <el-form ref="gradeFormRef" :model="gradeForm" :rules="gradeRules" label-width="110px">
        <el-form-item label="年级编码" prop="gradeCode"><el-input v-model.trim="gradeForm.gradeCode" /></el-form-item>
        <el-form-item label="年级名称" prop="gradeName"><el-input v-model.trim="gradeForm.gradeName" /></el-form-item>
        <el-form-item label="入学年份" prop="enrollmentYear"><el-input-number v-model="gradeForm.enrollmentYear" :min="2000" :max="2100" controls-position="right" /></el-form-item>
        <el-form-item label="预计毕业年份"><el-input-number v-model="gradeForm.graduationYear" :min="2000" :max="2100" controls-position="right" /></el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="gradeForm.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="gradeForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="gradeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitGrade">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="classDialogVisible" :title="classForm.id ? '编辑班级' : '新增班级'" width="680px" @closed="resetClassDialog">
      <el-form ref="classFormRef" :model="classForm" :rules="classRules" label-width="110px">
        <div class="form-grid">
          <el-form-item label="专业" prop="majorId">
            <el-select v-model="classForm.majorId" filterable>
              <el-option v-for="item in majorOptions" :key="item.id" :label="`${item.college} / ${item.majorName}`" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="年级" prop="gradeId">
            <el-select v-model="classForm.gradeId">
              <el-option v-for="item in gradeOptions" :key="item.id" :label="item.gradeName" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="班级编码" prop="classCode"><el-input v-model.trim="classForm.classCode" /></el-form-item>
          <el-form-item label="班级名称" prop="className"><el-input v-model.trim="classForm.className" /></el-form-item>
          <el-form-item label="辅导员ID"><el-input-number v-model="classForm.advisorId" :min="1" controls-position="right" /></el-form-item>
          <el-form-item label="辅导员姓名"><el-input v-model.trim="classForm.advisorName" /></el-form-item>
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="classForm.status">
              <el-radio :value="1">启用</el-radio>
              <el-radio :value="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
        <el-form-item label="备注"><el-input v-model.trim="classForm.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="classDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitClass">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { getEnabledAcademicMajors } from '@/api/academic/major'
import { createAcademicClass, createAcademicGrade, deleteAcademicClass, deleteAcademicGrade, getAcademicClassPage, getAcademicGradePage, getEnabledAcademicClasses, getEnabledAcademicGrades, updateAcademicClass, updateAcademicGrade } from '@/api/academic/class'
import type { AcademicClass, AcademicClassForm, AcademicGrade, AcademicGradeForm, AcademicMajor, AcademicQuery } from '@/types/academic'

const activeTab = ref('grade')
const gradeQuery = reactive<AcademicQuery>({ pageNum: 1, pageSize: 10 })
const classQuery = reactive<AcademicQuery>({ pageNum: 1, pageSize: 10 })
const gradePage = reactive({ pageNum: 1, pageSize: 10 })
const classPage = reactive({ pageNum: 1, pageSize: 10 })
const grades = ref<AcademicGrade[]>([])
const classes = ref<AcademicClass[]>([])
const gradeOptions = ref<AcademicGrade[]>([])
const majorOptions = ref<AcademicMajor[]>([])
const gradeTotal = ref(0)
const classTotal = ref(0)
const gradeLoading = ref(false)
const classLoading = ref(false)
const submitLoading = ref(false)
const gradeDialogVisible = ref(false)
const classDialogVisible = ref(false)
const gradeFormRef = ref<FormInstance>()
const classFormRef = ref<FormInstance>()

const gradeForm = reactive<AcademicGradeForm>({ gradeCode: '', gradeName: '', enrollmentYear: new Date().getFullYear(), graduationYear: undefined, status: 1, remark: '' })
const classForm = reactive<AcademicClassForm>({ majorId: undefined, gradeId: undefined, classCode: '', className: '', advisorId: undefined, advisorName: '', status: 1, remark: '' })

const gradeRules: FormRules<AcademicGradeForm> = {
  gradeCode: [{ required: true, message: '请输入年级编码', trigger: 'blur' }],
  gradeName: [{ required: true, message: '请输入年级名称', trigger: 'blur' }],
  enrollmentYear: [{ required: true, message: '请输入入学年份', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}
const classRules: FormRules<AcademicClassForm> = {
  majorId: [{ required: true, message: '请选择专业', trigger: 'change' }],
  gradeId: [{ required: true, message: '请选择年级', trigger: 'change' }],
  classCode: [{ required: true, message: '请输入班级编码', trigger: 'blur' }],
  className: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

async function loadGrades() {
  gradeLoading.value = true
  try {
    const result = await getAcademicGradePage({ ...gradeQuery, pageNum: gradePage.pageNum, pageSize: gradePage.pageSize })
    grades.value = result.records || []
    gradeTotal.value = result.total || 0
  } finally {
    gradeLoading.value = false
  }
}

async function loadClasses() {
  classLoading.value = true
  try {
    const result = await getAcademicClassPage({ ...classQuery, pageNum: classPage.pageNum, pageSize: classPage.pageSize })
    classes.value = result.records || []
    classTotal.value = result.total || 0
  } finally {
    classLoading.value = false
  }
}

async function loadOptions() {
  majorOptions.value = await getEnabledAcademicMajors()
  gradeOptions.value = await getEnabledAcademicGrades()
}

function handleTabChange() {
  if (activeTab.value === 'class') {
    loadOptions()
    loadClasses()
  }
}

function searchGrades() {
  gradePage.pageNum = 1
  loadGrades()
}

function resetGrades() {
  Object.assign(gradeQuery, { keyword: '', status: '' })
  gradePage.pageNum = 1
  loadGrades()
}

function searchClasses() {
  classPage.pageNum = 1
  loadClasses()
}

function resetClasses() {
  Object.assign(classQuery, { keyword: '', majorId: '', gradeId: '' })
  classPage.pageNum = 1
  loadClasses()
}

function openGradeDialog(row?: AcademicGrade) {
  Object.assign(gradeForm, row || { id: undefined, gradeCode: '', gradeName: '', enrollmentYear: new Date().getFullYear(), graduationYear: undefined, status: 1, remark: '' })
  gradeDialogVisible.value = true
  nextTick(() => gradeFormRef.value?.clearValidate())
}

function resetGradeDialog() {
  Object.assign(gradeForm, { id: undefined, gradeCode: '', gradeName: '', enrollmentYear: new Date().getFullYear(), graduationYear: undefined, status: 1, remark: '' })
}

async function submitGrade() {
  await gradeFormRef.value?.validate()
  submitLoading.value = true
  try {
    if (gradeForm.id) {
      await updateAcademicGrade(gradeForm.id, gradeForm)
      ElMessage.success('年级已更新')
    } else {
      await createAcademicGrade(gradeForm)
      ElMessage.success('年级已新增')
    }
    gradeDialogVisible.value = false
    await loadGrades()
    gradeOptions.value = await getEnabledAcademicGrades()
  } finally {
    submitLoading.value = false
  }
}

async function removeGrade(row: AcademicGrade) {
  await ElMessageBox.confirm(`确认删除年级「${row.gradeName}」吗？`, '删除确认', { type: 'warning' })
  await deleteAcademicGrade(row.id)
  ElMessage.success('年级已删除')
  await loadGrades()
  gradeOptions.value = await getEnabledAcademicGrades()
}

function openClassDialog(row?: AcademicClass) {
  Object.assign(classForm, row || { id: undefined, majorId: undefined, gradeId: undefined, classCode: '', className: '', advisorId: undefined, advisorName: '', status: 1, remark: '' })
  classDialogVisible.value = true
  nextTick(() => classFormRef.value?.clearValidate())
}

function resetClassDialog() {
  Object.assign(classForm, { id: undefined, majorId: undefined, gradeId: undefined, classCode: '', className: '', advisorId: undefined, advisorName: '', status: 1, remark: '' })
}

async function submitClass() {
  await classFormRef.value?.validate()
  submitLoading.value = true
  try {
    if (classForm.id) {
      await updateAcademicClass(classForm.id, classForm)
      ElMessage.success('班级已更新')
    } else {
      await createAcademicClass(classForm)
      ElMessage.success('班级已新增')
    }
    classDialogVisible.value = false
    await loadClasses()
    await getEnabledAcademicClasses()
  } finally {
    submitLoading.value = false
  }
}

async function removeClass(row: AcademicClass) {
  await ElMessageBox.confirm(`确认删除班级「${row.className}」吗？`, '删除确认', { type: 'warning' })
  await deleteAcademicClass(row.id)
  ElMessage.success('班级已删除')
  await loadClasses()
}

onMounted(async () => {
  await loadOptions()
  await loadGrades()
})
</script>

<style scoped lang="scss">
.academic-class-page {
  .content-tabs {
    :deep(.el-tabs__header) {
      margin: 0 0 12px;
    }
  }

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
