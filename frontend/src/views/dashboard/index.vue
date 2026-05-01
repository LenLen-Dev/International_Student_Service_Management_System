<template>
  <div class="dashboard-page">
    <section class="dashboard-hero">
      <div>
        <p class="dashboard-hero__eyebrow">Welcome Back</p>
        <h1>{{ welcomeText }}</h1>
        <p class="dashboard-hero__subtitle">
          当前角色：{{ roleText }}。可在系统管理中维护账号、角色和菜单权限，为后续业务模块提供统一权限基础。
        </p>
      </div>
      <div class="dashboard-hero__user">
        <span>{{ userStore.userInfo?.username || '-' }}</span>
        <strong>{{ userStore.userInfo?.realName || '管理员' }}</strong>
      </div>
    </section>

    <section class="dashboard-metrics">
      <div class="metric-card">
        <span>角色数量</span>
        <strong>{{ userStore.roles.length }}</strong>
      </div>
      <div class="metric-card">
        <span>权限数量</span>
        <strong>{{ userStore.permissions.length }}</strong>
      </div>
      <div class="metric-card">
        <span>菜单入口</span>
        <strong>{{ permissionStore.menus.length }}</strong>
      </div>
    </section>

    <section class="module-grid">
      <article v-for="module in modules" :key="module.title" class="module-card" @click="openModule(module)">
        <div class="module-card__icon" :class="module.color">
          <el-icon :size="24">
            <component :is="module.icon" />
          </el-icon>
        </div>
        <div class="module-card__content">
          <h3>{{ module.title }}</h3>
          <p>{{ module.description }}</p>
        </div>
        <el-icon class="module-card__arrow"><ArrowRight /></el-icon>
      </article>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowRight, Collection, Document, Key, OfficeBuilding, Reading, School, Tickets, User } from '@element-plus/icons-vue'
import { usePermissionStore } from '@/stores/permission'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const welcomeText = computed(() => `欢迎，${userStore.userInfo?.realName || userStore.userInfo?.username || '管理员'}`)
const roleText = computed(() => (userStore.roles.length ? userStore.roles.join(' / ') : '暂无角色'))

const modules = [
  {
    title: '用户管理',
    description: '维护后台账号、用户类型、状态和角色分配',
    path: '/system/user',
    icon: User,
    color: 'blue'
  },
  {
    title: '角色管理',
    description: '配置角色信息并分配菜单与按钮权限',
    path: '/system/role',
    icon: Key,
    color: 'green'
  },
  {
    title: '菜单权限',
    description: '维护动态路由、菜单树和按钮权限编码',
    path: '/system/menu',
    icon: Tickets,
    color: 'orange'
  },
  {
    title: '留学生档案',
    description: '留学生基础信息与档案业务入口',
    path: '/student/profiles',
    icon: Collection,
    color: 'cyan'
  },
  {
    title: '招生申请',
    description: '招生申请、材料审核与录取流程入口',
    path: '/admission',
    icon: Document,
    color: 'blue'
  },
  {
    title: '签证管理',
    description: '签证、居留许可和到期提醒业务入口',
    path: '/visa',
    icon: OfficeBuilding,
    color: 'green'
  },
  {
    title: '学籍管理',
    description: '专业、年级班级、请假审批和学籍异动',
    path: '/academic',
    icon: School,
    color: 'orange'
  },
  {
    title: '教务成绩',
    description: '课程、选课、成绩、出勤和学业预警',
    path: '/teaching',
    icon: Reading,
    color: 'cyan'
  }
]

function openModule(module: (typeof modules)[number]) {
  if (module.path === '/admission') {
    const target = userStore.permissions.includes('admission:application:list') ? '/admission/review' : '/admission/my-application'
    router.push(target)
    return
  }

  if (module.path === '/visa') {
    const target = userStore.permissions.includes('visa:record:list') ? '/visa/record' : '/visa/my'
    router.push(target)
    return
  }

  if (module.path === '/academic') {
    const target = userStore.permissions.includes('academic:record:list') ? '/academic/record' : '/academic/my'
    router.push(target)
    return
  }

  if (module.path === '/teaching') {
    const target = userStore.permissions.includes('teaching:course:list') ? '/teaching/course' : '/teaching/my'
    router.push(target)
    return
  }

  if (module.path) {
    router.push(module.path)
    return
  }
  ElMessage.info('该业务模块将在后续迭代中接入')
}
</script>

<style scoped lang="scss">
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.dashboard-hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 28px;
  color: #fff;
  background: linear-gradient(135deg, #0f2f5f, #2563eb);
  border-radius: 8px;
  box-shadow: 0 16px 42px rgb(37 99 235 / 18%);

  h1 {
    margin: 8px 0 10px;
    font-size: 30px;
    line-height: 1.25;
  }

  &__eyebrow {
    margin: 0;
    color: #bfdbfe;
    font-size: 13px;
    font-weight: 700;
    text-transform: uppercase;
  }

  &__subtitle {
    max-width: 720px;
    margin: 0;
    color: #dbeafe;
    line-height: 1.8;
  }

  &__user {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    min-width: 160px;

    span {
      color: #bfdbfe;
      font-size: 13px;
    }

    strong {
      margin-top: 4px;
      font-size: 22px;
    }
  }
}

.dashboard-metrics {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.metric-card,
.module-card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgb(15 23 42 / 5%);
}

.metric-card {
  padding: 20px;

  span {
    color: #6b7280;
    font-size: 14px;
  }

  strong {
    display: block;
    margin-top: 8px;
    color: #1f2937;
    font-size: 30px;
  }
}

.module-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.module-card {
  display: grid;
  grid-template-columns: 48px 1fr 20px;
  gap: 14px;
  align-items: center;
  min-height: 128px;
  padding: 20px;
  cursor: pointer;
  transition:
    transform 0.16s ease,
    box-shadow 0.16s ease;

  &:hover {
    box-shadow: 0 14px 36px rgb(15 23 42 / 10%);
    transform: translateY(-2px);
  }

  &__icon {
    display: grid;
    width: 48px;
    height: 48px;
    place-items: center;
    border-radius: 8px;

    &.blue {
      color: #2563eb;
      background: #dbeafe;
    }

    &.green {
      color: #16a34a;
      background: #dcfce7;
    }

    &.orange {
      color: #f59e0b;
      background: #fef3c7;
    }

    &.cyan {
      color: #0891b2;
      background: #cffafe;
    }
  }

  &__content {
    min-width: 0;

    h3 {
      margin: 0 0 8px;
      color: #1f2937;
      font-size: 17px;
    }

    p {
      margin: 0;
      color: #6b7280;
      font-size: 13px;
      line-height: 1.7;
    }
  }

  &__arrow {
    color: #94a3b8;
  }
}

@media (max-width: 960px) {
  .dashboard-metrics,
  .module-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 640px) {
  .dashboard-hero {
    align-items: flex-start;
    flex-direction: column;

    &__user {
      align-items: flex-start;
    }
  }

  .dashboard-metrics,
  .module-grid {
    grid-template-columns: 1fr;
  }
}
</style>
