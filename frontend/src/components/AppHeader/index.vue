<template>
  <div class="app-header">
    <div class="app-header__left">
      <el-button class="app-header__collapse" text @click="$emit('toggle')">
        <el-icon :size="20">
          <Fold v-if="!collapsed" />
          <Expand v-else />
        </el-icon>
      </el-button>
      <div class="app-header__title">留学生服务与管理系统</div>
    </div>

    <div class="app-header__right">
      <div class="app-header__user">
        <el-avatar :size="32" class="app-header__avatar">
          {{ avatarText }}
        </el-avatar>
        <div class="app-header__meta">
          <span class="app-header__name">{{ userStore.userInfo?.realName || userStore.userInfo?.username || '未登录' }}</span>
          <span class="app-header__roles">{{ roleText }}</span>
        </div>
      </div>

      <el-dropdown trigger="click" @command="handleCommand">
        <el-button text>
          <el-icon><ArrowDown /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { ArrowDown, Expand, Fold, SwitchButton } from '@element-plus/icons-vue'
import { usePermissionStore } from '@/stores/permission'
import { useUserStore } from '@/stores/user'

defineProps<{
  collapsed: boolean
}>()

defineEmits<{
  toggle: []
}>()

const router = useRouter()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const avatarText = computed(() => {
  const name = userStore.userInfo?.realName || userStore.userInfo?.username || 'U'
  return name.slice(0, 1).toUpperCase()
})

const roleText = computed(() => (userStore.roles.length ? userStore.roles.join(' / ') : '暂无角色'))

async function handleCommand(command: string) {
  if (command !== 'logout') {
    return
  }

  try {
    await ElMessageBox.confirm('确认退出当前登录账号吗？', '退出登录', {
      confirmButtonText: '退出',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }
  await userStore.logout()
  permissionStore.resetRoutes()
  router.replace('/login')
}
</script>

<style scoped lang="scss">
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 56px;
  padding: 0 18px 0 8px;

  &__left,
  &__right,
  &__user {
    display: flex;
    align-items: center;
    min-width: 0;
  }

  &__left {
    gap: 6px;
  }

  &__right {
    gap: 10px;
  }

  &__collapse {
    width: 40px;
    height: 40px;
  }

  &__title {
    overflow: hidden;
    color: #1f2937;
    font-size: 16px;
    font-weight: 700;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__user {
    gap: 10px;
  }

  &__avatar {
    background: #2563eb;
  }

  &__meta {
    display: flex;
    flex-direction: column;
    min-width: 0;
    line-height: 1.25;
  }

  &__name {
    max-width: 160px;
    overflow: hidden;
    color: #1f2937;
    font-size: 14px;
    font-weight: 600;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__roles {
    max-width: 180px;
    overflow: hidden;
    color: #6b7280;
    font-size: 12px;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

@media (max-width: 768px) {
  .app-header {
    padding-right: 10px;

    &__title {
      max-width: 160px;
      font-size: 14px;
    }

    &__meta {
      display: none;
    }
  }
}
</style>
