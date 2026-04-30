<template>
  <div class="page-container">
    <el-card class="search-card">
      <el-form :model="searchForm" label-width="84px">
        <el-form-item label="用户名">
          <el-input v-model.trim="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model.trim="searchForm.realName" placeholder="请输入真实姓名" clearable />
        </el-form-item>
        <el-form-item label="用户类型">
          <el-select v-model="searchForm.userType" placeholder="全部类型" clearable>
            <el-option v-for="item in userTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button v-permission="'system:user:list'" type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button v-permission="'system:user:add'" type="primary" :icon="Plus" @click="openCreateDialog">新增用户</el-button>
        </div>
      </div>

      <el-table v-loading="tableLoading" :data="pageUsers" border stripe>
        <el-table-column prop="username" label="用户名" min-width="130" />
        <el-table-column prop="realName" label="真实姓名" min-width="130" />
        <el-table-column prop="userType" label="用户类型" min-width="140">
          <template #default="{ row }">
            {{ formatUserType(row.userType) }}
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" min-width="140" />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag class="status-tag" :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="170" show-overflow-tooltip />
        <el-table-column label="操作" width="360" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'system:user:edit'" link type="primary" :icon="Edit" @click="openEditDialog(row)">编辑</el-button>
            <el-button v-permission="'system:user:delete'" link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
            <el-button v-permission="'system:user:status'" link :type="row.status === 1 ? 'warning' : 'success'" @click="handleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button v-permission="'system:user:roles'" link type="primary" @click="openAssignDialog(row)">分配角色</el-button>
            <el-button v-permission="'user:reset-password'" link type="primary" @click="handleResetPassword(row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-pagination
          v-model:current-page="pageQuery.pageNum"
          v-model:page-size="pageQuery.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="filteredUsers.length"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </el-card>

    <el-dialog v-model="userDialogVisible" :title="dialogMode === 'create' ? '新增用户' : '编辑用户'" width="620px" @closed="resetUserDialog">
      <el-form ref="userFormRef" :model="userForm" :rules="userRules" label-width="96px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model.trim="userForm.username" :disabled="dialogMode === 'edit'" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userForm.password" type="password" show-password :placeholder="dialogMode === 'create' ? '请输入密码' : '留空则不修改密码'" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model.trim="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="userForm.userType" placeholder="请选择用户类型">
            <el-option v-for="item in userTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model.trim="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model.trim="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitUserForm">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="assignDialogVisible" title="分配角色" width="560px" @closed="resetAssignDialog">
      <el-skeleton v-if="roleLoading" :rows="4" animated />
      <el-checkbox-group v-else v-model="checkedRoleIds" class="role-checkbox-group">
        <el-checkbox v-for="role in roleOptions" :key="role.id" :label="role.id">
          {{ role.roleName }}（{{ role.roleCode }}）
        </el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="assignLoading" @click="submitAssignRoles">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Refresh, Search } from '@element-plus/icons-vue'
import { assignUserRoles, createUser, deleteUser, getUserList, resetUserPassword, updateUser, updateUserStatus } from '@/api/system/user'
import { getRoleList } from '@/api/system/role'
import type { PageResult } from '@/types/api'
import type { RoleItem } from '@/types/role'
import type { UserForm, UserItem, UserQuery } from '@/types/user'

const userTypeOptions = [
  { label: '管理员', value: 'ADMIN' },
  { label: '系统管理员', value: 'SUPER_ADMIN' },
  { label: '外事管理员', value: 'FOREIGN_ADMIN' },
  { label: '招生管理员', value: 'ADMISSION_ADMIN' },
  { label: '教师', value: 'TEACHER' },
  { label: '留学生', value: 'STUDENT' },
  { label: '服务人员', value: 'SERVICE_STAFF' }
]

const searchForm = reactive<UserQuery>({
  username: '',
  realName: '',
  userType: '',
  status: ''
})

const pageQuery = reactive({
  pageNum: 1,
  pageSize: 10
})

const tableLoading = ref(false)
const submitLoading = ref(false)
const assignLoading = ref(false)
const roleLoading = ref(false)
const rawUsers = ref<UserItem[]>([])
const userDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const dialogMode = ref<'create' | 'edit'>('create')
const currentUser = ref<UserItem | null>(null)
const userFormRef = ref<FormInstance>()
const roleOptions = ref<RoleItem[]>([])
const checkedRoleIds = ref<number[]>([])

const emptyUserForm = (): UserForm => ({
  username: '',
  password: '',
  realName: '',
  userType: 'STUDENT',
  email: '',
  phone: '',
  status: 1,
  roleIds: []
})

const userForm = reactive<UserForm>(emptyUserForm())

const validatePassword = (_rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (dialogMode.value === 'create' && !value) {
    callback(new Error('请输入密码'))
    return
  }
  if (value && value.length < 6) {
    callback(new Error('密码长度不能少于 6 位'))
    return
  }
  callback()
}

const userRules: FormRules<UserForm> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ validator: validatePassword, trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  userType: [{ required: true, message: '请选择用户类型', trigger: 'change' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const filteredUsers = computed(() => {
  return rawUsers.value.filter((user) => {
    const usernameMatch = searchForm.username ? user.username?.includes(String(searchForm.username)) : true
    const realNameMatch = searchForm.realName ? user.realName?.includes(String(searchForm.realName)) : true
    const typeMatch = searchForm.userType ? user.userType === searchForm.userType : true
    const statusMatch = searchForm.status === '' || searchForm.status === undefined ? true : user.status === Number(searchForm.status)
    return usernameMatch && realNameMatch && typeMatch && statusMatch
  })
})

const pageUsers = computed(() => {
  const start = (pageQuery.pageNum - 1) * pageQuery.pageSize
  return filteredUsers.value.slice(start, start + pageQuery.pageSize)
})

function normalizeRecords<T>(result: T[] | PageResult<T>): T[] {
  if (Array.isArray(result)) {
    return result
  }
  return result?.records || []
}

function formatUserType(type: string) {
  return userTypeOptions.find((item) => item.value === type)?.label || type || '-'
}

function buildQueryParams() {
  return {
    keyword: searchForm.username || searchForm.realName || '',
    userType: searchForm.userType || undefined,
    status: searchForm.status === '' || searchForm.status === undefined ? undefined : Number(searchForm.status),
    pageNum: pageQuery.pageNum,
    pageSize: pageQuery.pageSize
  }
}

async function loadUsers() {
  tableLoading.value = true
  try {
    rawUsers.value = normalizeRecords(await getUserList(buildQueryParams()))
  } finally {
    tableLoading.value = false
  }
}

async function handleSearch() {
  pageQuery.pageNum = 1
  await loadUsers()
}

async function handleReset() {
  Object.assign(searchForm, {
    username: '',
    realName: '',
    userType: '',
    status: ''
  })
  pageQuery.pageNum = 1
  await loadUsers()
}

function openCreateDialog() {
  dialogMode.value = 'create'
  currentUser.value = null
  Object.assign(userForm, emptyUserForm())
  userDialogVisible.value = true
  nextTick(() => userFormRef.value?.clearValidate())
}

function openEditDialog(row: UserItem) {
  dialogMode.value = 'edit'
  currentUser.value = row
  Object.assign(userForm, {
    id: row.id,
    username: row.username,
    password: '',
    realName: row.realName,
    userType: row.userType,
    email: row.email || '',
    phone: row.phone || '',
    status: row.status,
    roleIds: row.roleIds || []
  })
  userDialogVisible.value = true
  nextTick(() => userFormRef.value?.clearValidate())
}

function resetUserDialog() {
  Object.assign(userForm, emptyUserForm())
  userFormRef.value?.resetFields()
}

async function submitUserForm() {
  await userFormRef.value?.validate()
  submitLoading.value = true
  try {
    if (dialogMode.value === 'create') {
      await createUser(userForm)
      ElMessage.success('新增用户成功')
    } else if (currentUser.value) {
      await updateUser(currentUser.value.id, userForm)
      ElMessage.success('编辑用户成功')
    }
    userDialogVisible.value = false
    await loadUsers()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row: UserItem) {
  try {
    await ElMessageBox.confirm(`确认删除用户「${row.realName || row.username}」吗？`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteUser(row.id)
    ElMessage.success('删除用户成功')
    await loadUsers()
  } catch {
    // Cancelled.
  }
}

async function handleStatus(row: UserItem) {
  const nextStatus = row.status === 1 ? 0 : 1
  try {
    await ElMessageBox.confirm(`确认${nextStatus === 1 ? '启用' : '禁用'}用户「${row.realName || row.username}」吗？`, '状态确认', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await updateUserStatus(row.id, nextStatus)
    ElMessage.success('状态更新成功')
    await loadUsers()
  } catch {
    // Cancelled.
  }
}

async function openAssignDialog(row: UserItem) {
  currentUser.value = row
  checkedRoleIds.value = [...(row.roleIds || [])]
  assignDialogVisible.value = true
  roleLoading.value = true
  try {
    roleOptions.value = normalizeRecords(await getRoleList({}))
  } finally {
    roleLoading.value = false
  }
}

function resetAssignDialog() {
  currentUser.value = null
  checkedRoleIds.value = []
}

async function submitAssignRoles() {
  if (!currentUser.value) {
    return
  }
  assignLoading.value = true
  try {
    await assignUserRoles(currentUser.value.id, checkedRoleIds.value)
    ElMessage.success('分配角色成功')
    assignDialogVisible.value = false
    await loadUsers()
  } finally {
    assignLoading.value = false
  }
}

async function handleResetPassword(row: UserItem) {
  try {
    const result = await ElMessageBox.prompt(`请输入用户「${row.realName || row.username}」的新密码`, '重置密码', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      inputType: 'password',
      inputPattern: /^.{6,}$/,
      inputErrorMessage: '密码长度不能少于 6 位'
    })
    await resetUserPassword(row.id, row, result.value)
    ElMessage.success('重置密码成功')
  } catch {
    // Cancelled.
  }
}

onMounted(loadUsers)
</script>

<style scoped lang="scss">
.role-checkbox-group {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

@media (max-width: 640px) {
  .role-checkbox-group {
    grid-template-columns: 1fr;
  }
}
</style>
