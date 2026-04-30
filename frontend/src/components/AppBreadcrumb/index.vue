<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item v-for="item in breadcrumbItems" :key="item.path">
      {{ item.meta?.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const breadcrumbItems = computed(() => {
  return route.matched.filter((item) => {
    const title = item.meta?.title
    return title && title !== '首页'
  })
})
</script>

<style scoped lang="scss">
.app-breadcrumb {
  display: flex;
  align-items: center;
  min-height: 22px;
}
</style>
