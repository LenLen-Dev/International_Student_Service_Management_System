<template>
  <el-button
    v-if="visible"
    :type="type"
    :size="size"
    :icon="iconComponent"
    :loading="loading"
    :disabled="disabled"
    :plain="plain"
    :link="link"
    @click="$emit('click', $event)"
  >
    <slot />
  </el-button>
</template>

<script setup lang="ts">
import { computed, type Component } from 'vue'
import * as Icons from '@element-plus/icons-vue'
import { hasPermission } from '@/utils/permission'

const props = withDefaults(
  defineProps<{
    permission?: string | string[]
    type?: '' | 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info'
    size?: '' | 'large' | 'default' | 'small'
    icon?: string | Component
    loading?: boolean
    disabled?: boolean
    plain?: boolean
    link?: boolean
  }>(),
  {
    type: 'primary',
    size: 'default',
    loading: false,
    disabled: false,
    plain: false,
    link: false
  }
)

defineEmits<{
  click: [event: MouseEvent]
}>()

const visible = computed(() => hasPermission(props.permission))
const iconComponent = computed(() => {
  if (!props.icon) {
    return undefined
  }
  return typeof props.icon === 'string' ? ((Icons as Record<string, Component>)[props.icon] ?? undefined) : props.icon
})
</script>
