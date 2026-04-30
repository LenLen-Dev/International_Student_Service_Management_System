<template>
  <div class="page-container">
    <el-card class="search-card">
      <el-form :model="searchForm" label-width="84px">
        <el-form-item label="关键字">
          <el-input v-model.trim="searchForm.keyword" placeholder="菜单名称 / 权限标识" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button v-permission="'system:menu:list'" type="primary" :icon="Search" @click="loadMenus">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button v-permission="'system:menu:add'" type="primary" :icon="Plus" @click="openCreateDialog('CATALOG')">新增目录</el-button>
        </div>
      </div>

      <el-table
        v-loading="tableLoading"
        :data="menuTree"
        border
        row-key="id"
        default-expand-all
        :tree-props="{ children: 'children' }"
      >
        <el-table-column prop="menuName" label="菜单名称" min-width="180" />
        <el-table-column prop="menuType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="menuTypeTag(row.menuType)">{{ menuTypeLabel(row.menuType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" min-width="150" show-overflow-tooltip />
        <el-table-column prop="component" label="组件路径" min-width="180" show-overflow-tooltip />
        <el-table-column prop="permissionCode" label="权限标识" min-width="180" show-overflow-tooltip />
        <el-table-column prop="icon" label="图标" min-width="120" />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="visible" label="显示" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.visible === 1 ? 'success' : 'info'">{{ row.visible === 1 ? '显示' : '隐藏' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag class="status-tag" :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="360" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="normalizeMenuType(row.menuType) === 'CATALOG'"
              v-permission="'system:menu:add'"
              link
              type="primary"
              @click="openCreateDialog('CATALOG', row)"
            >
              新增目录
            </el-button>
            <el-button
              v-if="normalizeMenuType(row.menuType) === 'CATALOG'"
              v-permission="'system:menu:add'"
              link
              type="primary"
              @click="openCreateDialog('MENU', row)"
            >
              新增菜单
            </el-button>
            <el-button
              v-if="normalizeMenuType(row.menuType) === 'MENU'"
              v-permission="'system:menu:add'"
              link
              type="primary"
              @click="openCreateDialog('BUTTON', row)"
            >
              新增按钮
            </el-button>
            <el-button v-permission="'system:menu:edit'" link type="primary" :icon="Edit" @click="openEditDialog(row)">编辑</el-button>
            <el-button v-permission="'system:menu:delete'" link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="menuDialogVisible" :title="dialogMode === 'create' ? '新增菜单权限' : '编辑菜单权限'" width="680px" @closed="resetMenuDialog">
      <el-form ref="menuFormRef" :model="menuForm" :rules="menuRules" label-width="108px">
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="menuForm.parentId"
            :data="parentOptions"
            :props="treeSelectProps"
            node-key="id"
            check-strictly
            default-expand-all
            placeholder="请选择上级菜单"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model.trim="menuForm.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="menuForm.menuType" @change="handleMenuTypeChange">
            <el-radio label="CATALOG">目录</el-radio>
            <el-radio label="MENU">菜单</el-radio>
            <el-radio label="BUTTON">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="menuForm.menuType !== 'BUTTON'" label="路由路径" prop="path">
          <el-input v-model.trim="menuForm.path" placeholder="例如 /system/user" />
        </el-form-item>
        <el-form-item v-if="menuForm.menuType === 'MENU'" label="组件路径" prop="component">
          <el-input v-model.trim="menuForm.component" placeholder="例如 system/user/index" />
        </el-form-item>
        <el-form-item v-if="menuForm.menuType !== 'CATALOG'" label="权限标识" prop="permissionCode">
          <el-input v-model.trim="menuForm.permissionCode" placeholder="例如 system:user:list" />
        </el-form-item>
        <el-form-item v-if="menuForm.menuType !== 'BUTTON'" label="图标" prop="icon">
          <el-input v-model.trim="menuForm.icon" placeholder="Element Plus 图标名，例如 User" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="menuForm.sort" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="是否显示" prop="visible">
          <el-radio-group v-model="menuForm.visible" :disabled="menuForm.menuType === 'BUTTON'">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="menuForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitMenuForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Refresh, Search } from '@element-plus/icons-vue'
import { createMenu, deleteMenu, getMenuTree, updateMenu } from '@/api/system/menu'
import type { MenuForm, MenuItem, MenuType } from '@/types/menu'
import { normalizeMenuType } from '@/utils/menu'

const searchForm = reactive({
  keyword: '',
  status: '' as number | string
})

const tableLoading = ref(false)
const submitLoading = ref(false)
const menuTree = ref<MenuItem[]>([])
const menuDialogVisible = ref(false)
const dialogMode = ref<'create' | 'edit'>('create')
const currentMenu = ref<MenuItem | null>(null)
const menuFormRef = ref<FormInstance>()

const emptyMenuForm = (): MenuForm => ({
  parentId: 0,
  menuName: '',
  menuType: 'CATALOG',
  permissionCode: '',
  path: '',
  component: '',
  icon: '',
  visible: 1,
  status: 1,
  sort: 0
})

const menuForm = reactive<MenuForm>(emptyMenuForm())

const treeSelectProps = {
  label: 'menuName',
  value: 'id',
  children: 'children'
}

const parentOptions = computed(() => [
  {
    id: 0,
    parentId: 0,
    menuName: '顶级菜单',
    menuType: 'CATALOG',
    visible: 1,
    status: 1,
    children: menuTree.value
  }
])

const validatePath = (_rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (menuForm.menuType !== 'BUTTON' && !value) {
    callback(new Error('请输入路由路径'))
    return
  }
  callback()
}

const validateComponent = (_rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (menuForm.menuType === 'MENU' && !value) {
    callback(new Error('请输入组件路径'))
    return
  }
  callback()
}

const validatePermission = (_rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (menuForm.menuType === 'BUTTON' && !value) {
    callback(new Error('请输入权限标识'))
    return
  }
  callback()
}

const menuRules: FormRules<MenuForm> = {
  parentId: [{ required: true, message: '请选择上级菜单', trigger: 'change' }],
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }],
  path: [{ validator: validatePath, trigger: 'blur' }],
  component: [{ validator: validateComponent, trigger: 'blur' }],
  permissionCode: [{ validator: validatePermission, trigger: 'blur' }],
  visible: [{ required: true, message: '请选择是否显示', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

async function loadMenus() {
  tableLoading.value = true
  try {
    menuTree.value = await getMenuTree({
      keyword: searchForm.keyword || undefined,
      status: searchForm.status === '' ? undefined : Number(searchForm.status)
    })
  } finally {
    tableLoading.value = false
  }
}

async function handleReset() {
  searchForm.keyword = ''
  searchForm.status = ''
  await loadMenus()
}

function openCreateDialog(type: MenuForm['menuType'], parent?: MenuItem) {
  dialogMode.value = 'create'
  currentMenu.value = null
  Object.assign(menuForm, emptyMenuForm(), {
    menuType: type,
    parentId: parent?.id || 0,
    visible: type === 'BUTTON' ? 0 : 1
  })
  menuDialogVisible.value = true
  nextTick(() => menuFormRef.value?.clearValidate())
}

function openEditDialog(row: MenuItem) {
  const type = normalizeMenuType(row.menuType)
  dialogMode.value = 'edit'
  currentMenu.value = row
  Object.assign(menuForm, {
    id: row.id,
    parentId: row.parentId,
    menuName: row.menuName,
    menuType: type,
    permissionCode: row.permissionCode || '',
    path: row.path || '',
    component: row.component || '',
    icon: row.icon || '',
    visible: type === 'BUTTON' ? 0 : row.visible,
    status: row.status,
    sort: row.sort || 0
  })
  menuDialogVisible.value = true
  nextTick(() => menuFormRef.value?.clearValidate())
}

function resetMenuDialog() {
  Object.assign(menuForm, emptyMenuForm())
  menuFormRef.value?.resetFields()
}

function handleMenuTypeChange() {
  if (menuForm.menuType === 'BUTTON') {
    menuForm.visible = 0
    menuForm.path = ''
    menuForm.component = ''
    menuForm.icon = ''
  } else {
    menuForm.visible = 1
  }

  if (menuForm.menuType === 'CATALOG') {
    menuForm.component = 'Layout'
    menuForm.permissionCode = ''
  }

  nextTick(() => menuFormRef.value?.clearValidate())
}

function buildSubmitPayload(): MenuForm {
  const payload: MenuForm = {
    ...menuForm,
    sort: menuForm.sort || 0,
    visible: menuForm.menuType === 'BUTTON' ? 0 : menuForm.visible
  }

  if (payload.menuType === 'CATALOG') {
    payload.component = payload.component || 'Layout'
    payload.permissionCode = ''
  }

  if (payload.menuType === 'BUTTON') {
    payload.path = ''
    payload.component = ''
    payload.icon = ''
  }

  return payload
}

async function submitMenuForm() {
  await menuFormRef.value?.validate()
  submitLoading.value = true
  try {
    const payload = buildSubmitPayload()
    if (dialogMode.value === 'create') {
      await createMenu(payload)
      ElMessage.success('新增菜单权限成功')
    } else if (currentMenu.value) {
      await updateMenu(currentMenu.value.id, payload)
      ElMessage.success('编辑菜单权限成功')
    }
    menuDialogVisible.value = false
    await loadMenus()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row: MenuItem) {
  try {
    await ElMessageBox.confirm(`确认删除「${row.menuName}」吗？子级菜单不会自动在前端恢复。`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteMenu(row.id)
    ElMessage.success('删除菜单权限成功')
    await loadMenus()
  } catch {
    // Cancelled.
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

onMounted(loadMenus)
</script>
