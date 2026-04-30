<template>
  <div class="page-container">
    <el-card class="search-card">
      <el-form :model="searchForm" label-width="84px">
        <el-form-item label="角色名称">
          <el-input v-model.trim="searchForm.roleName" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model.trim="searchForm.roleCode" placeholder="请输入角色编码" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button v-permission="'system:role:list'" type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button v-permission="'system:role:add'" type="primary" :icon="Plus" @click="openCreateDialog">新增角色</el-button>
        </div>
      </div>

      <el-table v-loading="tableLoading" :data="pageRoles" border stripe>
        <el-table-column prop="roleName" label="角色名称" min-width="140" />
        <el-table-column prop="roleCode" label="角色编码" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="220" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag class="status-tag" :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="90" align="center" />
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'system:role:edit'" link type="primary" :icon="Edit" @click="openEditDialog(row)">编辑</el-button>
            <el-button
              v-permission="'system:role:delete'"
              link
              type="danger"
              :icon="Delete"
              :disabled="row.roleCode === SUPER_ADMIN"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
            <el-button v-permission="'system:role:menus'" link type="primary" @click="openAssignDialog(row)">分配权限</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-pagination
          v-model:current-page="pageQuery.pageNum"
          v-model:page-size="pageQuery.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="filteredRoles.length"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </el-card>

    <el-dialog v-model="roleDialogVisible" :title="dialogMode === 'create' ? '新增角色' : '编辑角色'" width="600px" @closed="resetRoleDialog">
      <el-form ref="roleFormRef" :model="roleForm" :rules="roleRules" label-width="96px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model.trim="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model.trim="roleForm.roleCode" :disabled="currentRole?.roleCode === SUPER_ADMIN" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model.trim="roleForm.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="roleForm.sort" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0" :disabled="roleForm.roleCode === SUPER_ADMIN">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitRoleForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="assignDialogVisible" title="分配菜单权限" width="680px" @closed="resetAssignDialog">
      <el-tree
        ref="menuTreeRef"
        v-loading="menuLoading"
        :data="menuTree"
        :props="treeProps"
        node-key="id"
        show-checkbox
        default-expand-all
        class="menu-permission-tree"
      >
        <template #default="{ data }">
          <span class="tree-node">
            <el-tag size="small" :type="menuTypeTag(data.menuType)">{{ menuTypeLabel(data.menuType) }}</el-tag>
            <span>{{ data.menuName }}</span>
            <em v-if="data.permissionCode">{{ data.permissionCode }}</em>
          </span>
        </template>
      </el-tree>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="assignLoading" @click="submitAssignMenus">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules, TreeInstance } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Refresh, Search } from '@element-plus/icons-vue'
import { assignRoleMenus, createRole, deleteRole, getRoleList, updateRole } from '@/api/system/role'
import { getMenuTree } from '@/api/system/menu'
import type { PageResult } from '@/types/api'
import type { MenuItem, MenuType } from '@/types/menu'
import type { RoleForm, RoleItem, RoleQuery } from '@/types/role'
import { normalizeMenuType } from '@/utils/menu'

const SUPER_ADMIN = 'SUPER_ADMIN'

const searchForm = reactive<RoleQuery>({
  roleName: '',
  roleCode: '',
  status: ''
})

const pageQuery = reactive({
  pageNum: 1,
  pageSize: 10
})

const tableLoading = ref(false)
const submitLoading = ref(false)
const assignLoading = ref(false)
const menuLoading = ref(false)
const rawRoles = ref<RoleItem[]>([])
const menuTree = ref<MenuItem[]>([])
const roleDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const dialogMode = ref<'create' | 'edit'>('create')
const currentRole = ref<RoleItem | null>(null)
const roleFormRef = ref<FormInstance>()
const menuTreeRef = ref<TreeInstance>()

const emptyRoleForm = (): RoleForm => ({
  roleCode: '',
  roleName: '',
  description: '',
  status: 1,
  sort: 0
})

const roleForm = reactive<RoleForm>(emptyRoleForm())

const roleRules: FormRules<RoleForm> = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const treeProps = {
  label: 'menuName',
  children: 'children'
}

const filteredRoles = computed(() => {
  return rawRoles.value.filter((role) => {
    const nameMatch = searchForm.roleName ? role.roleName?.includes(String(searchForm.roleName)) : true
    const codeMatch = searchForm.roleCode ? role.roleCode?.includes(String(searchForm.roleCode)) : true
    const statusMatch = searchForm.status === '' || searchForm.status === undefined ? true : role.status === Number(searchForm.status)
    return nameMatch && codeMatch && statusMatch
  })
})

const pageRoles = computed(() => {
  const start = (pageQuery.pageNum - 1) * pageQuery.pageSize
  return filteredRoles.value.slice(start, start + pageQuery.pageSize)
})

function normalizeRecords<T>(result: T[] | PageResult<T>): T[] {
  if (Array.isArray(result)) {
    return result
  }
  return result?.records || []
}

function buildQueryParams() {
  return {
    keyword: searchForm.roleName || searchForm.roleCode || '',
    status: searchForm.status === '' || searchForm.status === undefined ? undefined : Number(searchForm.status),
    pageNum: pageQuery.pageNum,
    pageSize: pageQuery.pageSize
  }
}

async function loadRoles() {
  tableLoading.value = true
  try {
    rawRoles.value = normalizeRecords(await getRoleList(buildQueryParams()))
  } finally {
    tableLoading.value = false
  }
}

async function handleSearch() {
  pageQuery.pageNum = 1
  await loadRoles()
}

async function handleReset() {
  Object.assign(searchForm, {
    roleName: '',
    roleCode: '',
    status: ''
  })
  pageQuery.pageNum = 1
  await loadRoles()
}

function openCreateDialog() {
  dialogMode.value = 'create'
  currentRole.value = null
  Object.assign(roleForm, emptyRoleForm())
  roleDialogVisible.value = true
  nextTick(() => roleFormRef.value?.clearValidate())
}

function openEditDialog(row: RoleItem) {
  dialogMode.value = 'edit'
  currentRole.value = row
  Object.assign(roleForm, {
    id: row.id,
    roleCode: row.roleCode,
    roleName: row.roleName,
    description: row.description || '',
    status: row.status,
    sort: row.sort || 0
  })
  roleDialogVisible.value = true
  nextTick(() => roleFormRef.value?.clearValidate())
}

function resetRoleDialog() {
  Object.assign(roleForm, emptyRoleForm())
  roleFormRef.value?.resetFields()
}

async function submitRoleForm() {
  await roleFormRef.value?.validate()
  if (roleForm.roleCode === SUPER_ADMIN && roleForm.status === 0) {
    ElMessage.warning('SUPER_ADMIN 不允许禁用')
    return
  }

  submitLoading.value = true
  try {
    if (dialogMode.value === 'create') {
      await createRole(roleForm)
      ElMessage.success('新增角色成功')
    } else if (currentRole.value) {
      await updateRole(currentRole.value.id, roleForm)
      ElMessage.success('编辑角色成功')
    }
    roleDialogVisible.value = false
    await loadRoles()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row: RoleItem) {
  if (row.roleCode === SUPER_ADMIN) {
    ElMessage.warning('SUPER_ADMIN 不允许删除')
    return
  }

  try {
    await ElMessageBox.confirm(`确认删除角色「${row.roleName}」吗？`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteRole(row.id)
    ElMessage.success('删除角色成功')
    await loadRoles()
  } catch {
    // Cancelled.
  }
}

async function openAssignDialog(row: RoleItem) {
  currentRole.value = row
  assignDialogVisible.value = true
  menuLoading.value = true
  try {
    menuTree.value = await getMenuTree({})
    await nextTick()
    menuTreeRef.value?.setCheckedKeys(row.menuIds || [])
  } finally {
    menuLoading.value = false
  }
}

function resetAssignDialog() {
  currentRole.value = null
  menuTreeRef.value?.setCheckedKeys([])
}

async function submitAssignMenus() {
  if (!currentRole.value) {
    return
  }

  const checked = menuTreeRef.value?.getCheckedKeys(false) || []
  const halfChecked = menuTreeRef.value?.getHalfCheckedKeys() || []
  const menuIds = Array.from(new Set([...checked, ...halfChecked])).map((item) => Number(item))

  assignLoading.value = true
  try {
    await assignRoleMenus(currentRole.value.id, menuIds)
    ElMessage.success('分配权限成功')
    assignDialogVisible.value = false
    await loadRoles()
  } finally {
    assignLoading.value = false
  }
}

function menuTypeLabel(type: MenuType) {
  const normalized = normalizeMenuType(type)
  if (normalized === 'CATALOG') return '目录'
  if (normalized === 'MENU') return '菜单'
  return '按钮'
}

function menuTypeTag(type: MenuType) {
  const normalized = normalizeMenuType(type)
  if (normalized === 'CATALOG') return 'primary'
  if (normalized === 'MENU') return 'success'
  return 'warning'
}

onMounted(loadRoles)
</script>

<style scoped lang="scss">
.menu-permission-tree {
  max-height: 520px;
  overflow: auto;
  padding: 8px 4px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}

.tree-node {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  min-width: 0;

  em {
    color: #94a3b8;
    font-style: normal;
    font-size: 12px;
  }
}
</style>
