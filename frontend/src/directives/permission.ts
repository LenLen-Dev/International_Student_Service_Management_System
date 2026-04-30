import type { Directive, DirectiveBinding } from 'vue'
import { hasPermission } from '@/utils/permission'

const permissionDirective: Directive<HTMLElement, string | string[]> = {
  mounted(el: HTMLElement, binding: DirectiveBinding<string | string[]>) {
    if (!hasPermission(binding.value)) {
      el.parentNode?.removeChild(el)
    }
  },
  updated(el: HTMLElement, binding: DirectiveBinding<string | string[]>) {
    if (!hasPermission(binding.value)) {
      el.parentNode?.removeChild(el)
    }
  }
}

export default permissionDirective
