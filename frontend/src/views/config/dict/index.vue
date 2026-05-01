<template>
  <div class="page-container dict-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="80px">
        <el-form-item label="关键词">
          <el-input v-model.trim="query.keyword" placeholder="字典名称 / 编码" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="loadTypes">查询</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div class="dict-layout">
      <el-card class="dict-type-card">
        <div class="toolbar">
          <strong>字典类型</strong>
          <el-button v-permission="'config:dict:add'" type="primary" :icon="Plus" @click="openTypeDialog()">新增</el-button>
        </div>
        <el-table v-loading="typeLoading" :data="dictTypes" highlight-current-row @current-change="selectType">
          <el-table-column prop="dictName" label="名称" min-width="120" />
          <el-table-column prop="dictCode" label="编码" min-width="140" show-overflow-tooltip />
          <el-table-column label="状态" width="82" align="center">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="170" fixed="right">
            <template #default="{ row }">
              <el-button v-permission="'config:dict:update'" link type="primary" @click.stop="openTypeDialog(row)">编辑</el-button>
              <el-button v-permission="'config:dict:status'" link type="warning" @click.stop="toggleTypeStatus(row)">
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button v-permission="'config:dict:delete'" link type="danger" @click.stop="removeType(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card class="dict-data-card">
        <div class="toolbar">
          <div>
            <strong>字典项</strong>
            <span class="muted">{{ currentType?.dictName || '请选择字典类型' }}</span>
          </div>
          <el-button v-permission="'config:dict:add'" type="primary" :icon="Plus" :disabled="!currentType" @click="openDataDialog()">新增字典项</el-button>
        </div>
        <el-table v-loading="dataLoading" :data="dictData" border stripe>
          <el-table-column prop="dictLabel" label="标签" min-width="120" />
          <el-table-column prop="dictValue" label="值" min-width="140" />
          <el-table-column prop="tagType" label="标签样式" width="110">
            <template #default="{ row }">
              <el-tag v-if="row.tagType" :type="row.tagType">{{ row.tagType }}</el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="80" align="center" />
          <el-table-column label="状态" width="90" align="center">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述" min-width="160" show-overflow-tooltip />
          <el-table-column label="操作" width="190" fixed="right">
            <template #default="{ row }">
              <el-button v-permission="'config:dict:update'" link type="primary" @click="openDataDialog(row)">编辑</el-button>
              <el-button v-permission="'config:dict:status'" link type="warning" @click="toggleDataStatus(row)">
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button v-permission="'config:dict:delete'" link type="danger" @click="removeData(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <el-dialog v-model="typeDialogVisible" :title="typeForm.id ? '编辑字典类型' : '新增字典类型'" width="520px" @closed="resetTypeForm">
      <el-form ref="typeFormRef" :model="typeForm" :rules="typeRules" label-width="90px">
        <el-form-item label="字典名称" prop="dictName"><el-input v-model.trim="typeForm.dictName" /></el-form-item>
        <el-form-item label="字典编码" prop="dictCode"><el-input v-model.trim="typeForm.dictCode" /></el-form-item>
        <el-form-item label="排序" prop="sort"><el-input-number v-model="typeForm.sort" :min="0" controls-position="right" /></el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="typeForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model.trim="typeForm.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitType">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="dataDialogVisible" :title="dataForm.id ? '编辑字典项' : '新增字典项'" width="560px" @closed="resetDataForm">
      <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="90px">
        <el-form-item label="字典编码"><el-input v-model="dataForm.dictCode" disabled /></el-form-item>
        <el-form-item label="字典标签" prop="dictLabel"><el-input v-model.trim="dataForm.dictLabel" /></el-form-item>
        <el-form-item label="字典值" prop="dictValue"><el-input v-model.trim="dataForm.dictValue" /></el-form-item>
        <el-form-item label="标签样式">
          <el-select v-model="dataForm.tagType" clearable placeholder="请选择">
            <el-option label="primary" value="primary" />
            <el-option label="success" value="success" />
            <el-option label="warning" value="warning" />
            <el-option label="danger" value="danger" />
            <el-option label="info" value="info" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序"><el-input-number v-model="dataForm.sort" :min="0" controls-position="right" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model.trim="dataForm.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dataDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitData">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import {
  createDictData,
  createDictType,
  deleteDictData,
  deleteDictType,
  getDictData,
  getDictTypes,
  updateDictData,
  updateDictDataStatus,
  updateDictType,
  updateDictTypeStatus
} from '@/api/config/dict'
import type { DictData, DictDataForm, DictType, DictTypeForm } from '@/types/config'

const query = reactive({ keyword: '', status: '' as number | string })
const dictTypes = ref<DictType[]>([])
const dictData = ref<DictData[]>([])
const currentType = ref<DictType | null>(null)
const typeLoading = ref(false)
const dataLoading = ref(false)
const submitLoading = ref(false)
const typeDialogVisible = ref(false)
const dataDialogVisible = ref(false)
const typeFormRef = ref<FormInstance>()
const dataFormRef = ref<FormInstance>()

const typeForm = reactive<DictTypeForm>({ dictName: '', dictCode: '', description: '', status: 1, sort: 0 })
const dataForm = reactive<DictDataForm>({ dictCode: '', dictLabel: '', dictValue: '', tagType: '', description: '', status: 1, sort: 0 })

const typeRules: FormRules<DictTypeForm> = {
  dictName: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  dictCode: [{ required: true, message: '请输入字典编码', trigger: 'blur' }]
}
const dataRules: FormRules<DictDataForm> = {
  dictLabel: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
  dictValue: [{ required: true, message: '请输入字典值', trigger: 'blur' }]
}

async function loadTypes() {
  typeLoading.value = true
  try {
    dictTypes.value = await getDictTypes({ ...query, status: query.status === '' ? undefined : query.status })
    if (!currentType.value && dictTypes.value.length) {
      currentType.value = dictTypes.value[0]
      await loadData()
    }
  } finally {
    typeLoading.value = false
  }
}

async function loadData() {
  if (!currentType.value) {
    dictData.value = []
    return
  }
  dataLoading.value = true
  try {
    dictData.value = await getDictData(currentType.value.dictCode)
  } finally {
    dataLoading.value = false
  }
}

function selectType(row?: DictType) {
  currentType.value = row || null
  loadData()
}

function resetQuery() {
  Object.assign(query, { keyword: '', status: '' })
  loadTypes()
}

function openTypeDialog(row?: DictType) {
  Object.assign(typeForm, row || { id: undefined, dictName: '', dictCode: '', description: '', status: 1, sort: 0 })
  typeDialogVisible.value = true
  nextTick(() => typeFormRef.value?.clearValidate())
}

function resetTypeForm() {
  Object.assign(typeForm, { id: undefined, dictName: '', dictCode: '', description: '', status: 1, sort: 0 })
}

async function submitType() {
  await typeFormRef.value?.validate()
  submitLoading.value = true
  try {
    if (typeForm.id) {
      await updateDictType(typeForm.id, typeForm)
      ElMessage.success('字典类型已更新')
    } else {
      await createDictType(typeForm)
      ElMessage.success('字典类型已新增')
    }
    typeDialogVisible.value = false
    currentType.value = null
    await loadTypes()
  } finally {
    submitLoading.value = false
  }
}

async function toggleTypeStatus(row: DictType) {
  await updateDictTypeStatus(row.id, row.status === 1 ? 0 : 1)
  ElMessage.success('状态已更新')
  await loadTypes()
}

async function removeType(row: DictType) {
  await ElMessageBox.confirm(`确认删除字典类型「${row.dictName}」吗？对应字典项也会删除。`, '删除确认', { type: 'warning' })
  await deleteDictType(row.id)
  ElMessage.success('字典类型已删除')
  if (currentType.value?.id === row.id) currentType.value = null
  await loadTypes()
}

function openDataDialog(row?: DictData) {
  if (!currentType.value) return
  Object.assign(
    dataForm,
    row || { id: undefined, dictCode: currentType.value.dictCode, dictLabel: '', dictValue: '', tagType: '', description: '', status: 1, sort: 0 }
  )
  dataDialogVisible.value = true
  nextTick(() => dataFormRef.value?.clearValidate())
}

function resetDataForm() {
  Object.assign(dataForm, { id: undefined, dictCode: currentType.value?.dictCode || '', dictLabel: '', dictValue: '', tagType: '', description: '', status: 1, sort: 0 })
}

async function submitData() {
  await dataFormRef.value?.validate()
  submitLoading.value = true
  try {
    if (dataForm.id) {
      await updateDictData(dataForm.id, dataForm)
      ElMessage.success('字典项已更新')
    } else {
      await createDictData(dataForm)
      ElMessage.success('字典项已新增')
    }
    dataDialogVisible.value = false
    await loadData()
  } finally {
    submitLoading.value = false
  }
}

async function toggleDataStatus(row: DictData) {
  await updateDictDataStatus(row.id, row.status === 1 ? 0 : 1)
  ElMessage.success('状态已更新')
  await loadData()
}

async function removeData(row: DictData) {
  await ElMessageBox.confirm(`确认删除字典项「${row.dictLabel}」吗？`, '删除确认', { type: 'warning' })
  await deleteDictData(row.id)
  ElMessage.success('字典项已删除')
  await loadData()
}

onMounted(loadTypes)
</script>

<style scoped lang="scss">
.dict-layout {
  display: grid;
  grid-template-columns: minmax(420px, 0.9fr) minmax(0, 1.2fr);
  gap: 16px;
}

.muted {
  margin-left: 8px;
  color: #64748b;
  font-size: 13px;
}

.dict-page {
  :deep(.el-select),
  :deep(.el-input-number) {
    width: 100%;
  }
}

@media (max-width: 1100px) {
  .dict-layout {
    grid-template-columns: 1fr;
  }
}
</style>
