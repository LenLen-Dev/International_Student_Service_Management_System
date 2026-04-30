<template>
  <el-config-provider :locale="locale">
    <router-view />
  </el-config-provider>
</template>

<script setup lang="ts">
import zhCn from 'element-plus/es/locale/lang/zh-cn'

const locale = {
  ...zhCn,
  el: {
    ...zhCn.el,
    pagination: {
      ...zhCn.el.pagination,
      total: '共 {total} 页',
      sizes: '{size}/页',
      goto: '转到',
      pageClassifier: '页'
    }
  }
}
</script>
