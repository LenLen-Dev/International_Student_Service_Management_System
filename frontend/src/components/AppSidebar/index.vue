<template>
  <div class="app-sidebar" :class="{ 'is-collapsed': collapsed }">
    <div class="app-sidebar__brand">
      <div class="app-sidebar__logo">IS</div>
      <div v-show="!collapsed" class="app-sidebar__brand-text">
        <strong>留学生服务</strong>
        <span>Management</span>
      </div>
    </div>

    <el-scrollbar class="app-sidebar__scroll">
      <el-menu
        :collapse="collapsed"
        :default-active="activeMenu"
        :router="true"
        background-color="#0f2f5f"
        text-color="#dbeafe"
        active-text-color="#ffffff"
        unique-opened
      >
        <SidebarMenuItem v-for="menu in visibleMenus" :key="menu.id" :item="menu" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed, defineComponent, h } from 'vue'
import { useRoute } from 'vue-router'
import { ElIcon, ElMenuItem, ElSubMenu } from 'element-plus'
import * as Icons from '@element-plus/icons-vue'
import { usePermissionStore } from '@/stores/permission'
import type { MenuItem } from '@/types/menu'
import { getIconName, getVisibleMenuChildren, normalizeRoutePath } from '@/utils/menu'

defineProps<{
  collapsed: boolean
}>()

const route = useRoute()
const permissionStore = usePermissionStore()

const visibleMenus = computed(() =>
  permissionStore.menus.filter((item) => item.visible !== 0 && item.status !== 0 && getVisibleMenuChildren({ ...item, children: [item] }).length)
)

const activeMenu = computed(() => normalizeRoutePath(route.path))

const SidebarMenuItem = defineComponent({
  name: 'SidebarMenuItem',
  props: {
    item: {
      type: Object as () => MenuItem,
      required: true
    }
  },
  setup(props) {
    const renderIcon = () => {
      const iconName = getIconName(props.item.icon)
      const Icon = (Icons as Record<string, unknown>)[iconName] as object | undefined
      return Icon ? h(ElIcon, null, () => h(Icon)) : null
    }

    return () => {
      const children = getVisibleMenuChildren(props.item)
      const path = normalizeRoutePath(props.item.path) || `/menu-${props.item.id}`
      const title = props.item.menuName

      if (children.length) {
        return h(
          ElSubMenu,
          { index: path },
          {
            title: () => [renderIcon(), h('span', title)],
            default: () => children.map((child) => h(SidebarMenuItem, { key: child.id, item: child }))
          }
        )
      }

      return h(
        ElMenuItem,
        { index: path },
        {
          default: () => [renderIcon(), h('span', title)]
        }
      )
    }
  }
})
</script>

<style scoped lang="scss">
.app-sidebar {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #0f2f5f;

  &__brand {
    display: flex;
    align-items: center;
    gap: 10px;
    height: 56px;
    padding: 0 14px;
    color: #fff;
    border-bottom: 1px solid rgb(255 255 255 / 10%);
  }

  &__logo {
    display: grid;
    flex: 0 0 36px;
    width: 36px;
    height: 36px;
    place-items: center;
    color: #0f2f5f;
    font-size: 14px;
    font-weight: 800;
    background: #fff;
    border-radius: 8px;
  }

  &__brand-text {
    display: flex;
    flex-direction: column;
    min-width: 0;
    line-height: 1.25;

    strong,
    span {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    strong {
      font-size: 15px;
    }

    span {
      color: #bfdbfe;
      font-size: 12px;
    }
  }

  &__scroll {
    flex: 1;
  }

  :deep(.el-menu) {
    border-right: 0;
  }

  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    height: 46px;
    margin: 4px 8px;
    border-radius: 8px;
  }

  :deep(.el-menu-item.is-active) {
    background: #2563eb;
  }

  :deep(.el-menu--collapse .el-sub-menu__title span),
  :deep(.el-menu--collapse .el-menu-item span) {
    display: none;
  }
}
</style>
