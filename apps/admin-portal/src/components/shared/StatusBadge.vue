<template>
  <Badge :variant="badgeVariant">{{ label }}</Badge>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Badge } from '@/components/ui/badge'

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

const badgeVariant = computed(() => {
  const s = props.status
  if (['PUBLISHED', 'RESOLVED', 'CLOSED'].includes(s)) return 'success'
  if (['DRAFT', 'NEW'].includes(s)) return 'warning'
  if (['IN_PROGRESS'].includes(s)) return 'outline'
  return 'secondary'
})
</script>
