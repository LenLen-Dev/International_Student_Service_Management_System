<template>
  <div class="page-container backup-page">
    <el-card class="search-card">
      <el-form :model="query" label-width="80px">
        <el-form-item label="备份名">
          <el-input v-model.trim="query.backupName" placeholder="请输入备份名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.backupStatus" placeholder="全部状态" clearable>
            <el-option label="执行中" value="RUNNING" />
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAILED" />
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
        <el-button v-permission="'config:backup:create'" type="primary" :icon="Plus" :loading="backupLoading" @click="openCreateDialog">
          创建备份
        </el-button>
      </div>
      <el-table v-loading="loading" :data="backups" border stripe>
        <el-table-column prop="backupName" label="备份名称" min-width="180" />
        <el-table-column prop="fileName" label="文件名" min-width="220" show-overflow-tooltip />
        <el-table-column label="文件大小" width="120" align="center">
          <template #default="{ row }">{{ formatSize(row.fileSize) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.backupStatus)">{{ statusText(row.backupStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operatorName" label="操作人" width="120" />
        <el-table-column prop="costTime" label="耗时(ms)" width="110" align="center" />
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'config:backup:download'" link type="primary" :disabled="row.backupStatus !== 'SUCCESS'" @click="handleDownload(row)">
              下载
            </el-button>
            <el-button v-permission="'config:backup:delete'" link type="danger" @click="removeBackup(row)">删除</el-button>
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
          @size-change="loadBackups"
          @current-change="loadBackups"
        />
      </div>
    </el-card>

    <el-dialog v-model="createDialogVisible" title="创建数据备份" width="520px" @closed="resetForm">
      <el-form :model="form" label-width="90px">
        <el-form-item label="备份名称"><el-input v-model.trim="form.backupName" placeholder="默认使用当前时间生成" /></el-form-item>
        <el-form-item label="备注"><el-input v-model.trim="form.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="backupLoading" @click="submitBackup">开始备份</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { createBackup, deleteBackup, downloadBackup, getBackupPage } from '@/api/config/backup'
import type { BackupCreateForm, BackupQuery, DataBackup } from '@/types/config'

const query = reactive<BackupQuery>({ pageNum: 1, pageSize: 10 })
const pageQuery = reactive({ pageNum: 1, pageSize: 10 })
const form = reactive<BackupCreateForm>({ backupName: '', remark: '' })
const backups = ref<DataBackup[]>([])
const total = ref(0)
const loading = ref(false)
const backupLoading = ref(false)
const createDialogVisible = ref(false)

function buildQuery(): BackupQuery {
  return {
    ...query,
    pageNum: pageQuery.pageNum,
    pageSize: pageQuery.pageSize,
    backupStatus: query.backupStatus || undefined
  }
}

async function loadBackups() {
  loading.value = true
  try {
    const result = await getBackupPage(buildQuery())
    backups.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageQuery.pageNum = 1
  loadBackups()
}

function handleReset() {
  Object.assign(query, { backupName: '', backupStatus: '' })
  pageQuery.pageNum = 1
  loadBackups()
}

function openCreateDialog() {
  createDialogVisible.value = true
}

function resetForm() {
  Object.assign(form, { backupName: '', remark: '' })
}

async function submitBackup() {
  backupLoading.value = true
  try {
    await createBackup(form)
    ElMessage.success('数据库备份已完成')
    createDialogVisible.value = false
    await loadBackups()
  } finally {
    backupLoading.value = false
  }
}

async function handleDownload(row: DataBackup) {
  const blob = await downloadBackup(row.id)
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = row.fileName || `backup-${row.id}.sql`
  link.click()
  URL.revokeObjectURL(url)
}

async function removeBackup(row: DataBackup) {
  await ElMessageBox.confirm(`确认删除备份「${row.backupName}」吗？备份文件也会一并删除。`, '删除确认', { type: 'warning' })
  await deleteBackup(row.id)
  ElMessage.success('备份已删除')
  await loadBackups()
}

function statusText(status: string) {
  const map: Record<string, string> = { RUNNING: '执行中', SUCCESS: '成功', FAILED: '失败' }
  return map[status] || status
}

function statusTag(status: string) {
  const map: Record<string, 'success' | 'warning' | 'danger' | 'info'> = {
    RUNNING: 'warning',
    SUCCESS: 'success',
    FAILED: 'danger'
  }
  return map[status] || 'info'
}

function formatSize(size?: number) {
  if (!size) return '-'
  if (size < 1024) return `${size} B`
  if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`
  return `${(size / 1024 / 1024).toFixed(1)} MB`
}

onMounted(loadBackups)
</script>

<style scoped lang="scss">
.backup-page {
  :deep(.el-select) {
    width: 100%;
  }
}
</style>
