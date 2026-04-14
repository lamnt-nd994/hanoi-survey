import { defineStore } from 'pinia'
import { ref } from 'vue'

export type ToastType = 'success' | 'error' | 'info'

export type ToastItem = {
  id: number
  message: string
  type: ToastType
}

export const useToastsStore = defineStore('toasts', () => {
  const items = ref<ToastItem[]>([])
  let nextId = 1

  function show(message: string, type: ToastType = 'info', duration = 3000) {
    const id = nextId++
    items.value.push({ id, message, type })
    window.setTimeout(() => dismiss(id), duration)
  }

  function dismiss(id: number) {
    items.value = items.value.filter((item) => item.id !== id)
  }

  return { items, show, dismiss }
})
