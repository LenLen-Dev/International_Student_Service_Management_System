<template>
  <el-container class="app-layout">
    <el-aside class="app-layout__aside" :width="asideWidth">
      <AppSidebar :collapsed="collapsed" />
    </el-aside>

    <el-container class="app-layout__main">
      <el-header class="app-layout__header">
        <AppHeader :collapsed="collapsed" @toggle="collapsed = !collapsed" />
      </el-header>

      <el-main class="app-layout__content">
        <AppBreadcrumb />
        <div class="app-layout__view">
          <router-view />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import AppBreadcrumb from '@/components/AppBreadcrumb/index.vue'
import AppHeader from '@/components/AppHeader/index.vue'
import AppSidebar from '@/components/AppSidebar/index.vue'

const collapsed = ref(false)
const asideWidth = computed(() => (collapsed.value ? '64px' : '232px'))
</script>

<style scoped lang="scss">
.app-layout {
  width: 100%;
  min-width: 0;
  height: 100vh;
  background: #f5f7fa;

  &__aside {
    overflow: hidden;
    background: #0f2f5f;
    transition: width 0.2s ease;
  }

  &__main {
    min-width: 0;
  }

  &__header {
    height: 56px;
    padding: 0;
    background: #fff;
    border-bottom: 1px solid #e5e7eb;
  }

  &__content {
    min-width: 0;
    padding: 16px;
    overflow: auto;
  }

  &__view {
    min-width: 0;
    margin-top: 12px;
  }
}

@media (max-width: 768px) {
  .app-layout__aside {
    position: fixed;
    z-index: 20;
    height: 100vh;
  }

  .app-layout__main {
    margin-left: 64px;
  }
}
</style>
