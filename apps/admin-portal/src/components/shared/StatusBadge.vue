<template>
  <span class="cms-badge" :class="badgeClass">{{ label }}</span>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  status: string
}>()

const statusLabels: Record<string, string> = {
  DRAFT: 'Nháp',
  PUBLISHED: 'Đã xuất bản',
  ARCHIVED: 'Lưu trữ',
  NEW: 'Mới',
  IN_PROGRESS: 'Đang xử lý',
  RESOLVED: 'Đã giải quyết',
  CLOSED: 'Đã đóng',
}

const label = computed(() => statusLabels[props.status] || props.status)

const badgeClass = computed(() => {
  const s = props.status
  if (['PUBLISHED', 'RESOLVED', 'CLOSED'].includes(s)) return 'cms-badge-green'
  if (['DRAFT', 'NEW'].includes(s)) return 'cms-badge-amber'
  if (['IN_PROGRESS'].includes(s)) return 'cms-badge-blue'
  return 'cms-badge-gray'
})
</script>
