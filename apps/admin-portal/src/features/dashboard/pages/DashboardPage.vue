<template>
  <PageShell>
    <PageHeader title="Tổng quan" description="Tổng quan hệ thống quản trị nội dung" />
    <div class="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
      <DataCard v-for="card in statCards" :key="card.label" class="p-5">
        <div class="flex items-start justify-between">
          <div>
            <div class="text-2xl font-bold tracking-tight text-slate-950">{{ card.value }}</div>
            <div class="mt-1 text-sm font-medium text-slate-500">{{ card.label }}</div>
          </div>
          <div :class="['flex h-9 w-9 items-center justify-center rounded-md text-sm font-semibold', card.iconClass]">{{ card.icon }}</div>
        </div>
      </DataCard>
    </div>
  </PageShell>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import DataCard from '@/components/shared/DataCard.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { useMediaStore } from '@/stores/media'
import { usePagesStore } from '@/stores/pages'
import { usePostsStore } from '@/stores/posts'
import { useSettingsStore } from '@/stores/settings'

const pagesStore = usePagesStore()
const postsStore = usePostsStore()
const mediaStore = useMediaStore()
const settingsStore = useSettingsStore()

const statCards = computed(() => [
  { label: 'Trang tĩnh', value: pagesStore.meta.total, icon: 'P', iconClass: 'bg-emerald-50 text-emerald-700' },
  { label: 'Bài viết', value: postsStore.meta.total, icon: 'B', iconClass: 'bg-sky-50 text-sky-700' },
  { label: 'Thông tin công ty', value: settingsStore.settings?.hotline ? 'Đã cấu hình' : 'Chưa có', icon: 'C', iconClass: 'bg-amber-50 text-amber-700' },
  { label: 'Media', value: mediaStore.items.length, icon: 'M', iconClass: 'bg-violet-50 text-violet-700' },
])

onMounted(async () => {
  await Promise.all([
    pagesStore.fetchList(),
    postsStore.fetchList(),
    mediaStore.fetchList(),
    settingsStore.fetch(),
  ])
})
</script>
