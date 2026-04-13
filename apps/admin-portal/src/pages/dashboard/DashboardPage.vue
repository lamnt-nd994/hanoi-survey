<template>
  <div>
    <PageHeader title="Tổng quan" description="Tổng quan hệ thống quản trị nội dung" />
    <div class="cms-stat-grid mt-6">
      <div class="cms-stat-card">
        <div class="cms-stat-top"><div class="cms-stat-icon" style="background:#ecfdf5;color:#059669">&#9733;</div></div>
        <div class="cms-stat-value">{{ pagesStore.meta.total }}</div>
        <div class="cms-stat-label">Trang tĩnh</div>
      </div>
      <div class="cms-stat-card">
        <div class="cms-stat-top"><div class="cms-stat-icon" style="background:#eff6ff;color:#2563eb">&#9998;</div></div>
        <div class="cms-stat-value">{{ postsStore.meta.total }}</div>
        <div class="cms-stat-label">Bài viết</div>
      </div>
      <div class="cms-stat-card">
        <div class="cms-stat-top"><div class="cms-stat-icon" style="background:#fef3c7;color:#d97706">&#9672;</div></div>
        <div class="cms-stat-value">{{ settingsStore.settings?.hotline ? 'Đã cấu hình' : 'Chưa có' }}</div>
        <div class="cms-stat-label">Thông tin công ty</div>
      </div>
      <div class="cms-stat-card">
        <div class="cms-stat-top"><div class="cms-stat-icon" style="background:#f5f3ff;color:#7c3aed">&#9881;</div></div>
        <div class="cms-stat-value">{{ mediaStore.items.length }}</div>
        <div class="cms-stat-label">Media</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import { usePagesStore } from '@/stores/pages'
import { usePostsStore } from '@/stores/posts'
import { useMediaStore } from '@/stores/media'
import { useSettingsStore } from '@/stores/settings'

const pagesStore = usePagesStore()
const postsStore = usePostsStore()
const mediaStore = useMediaStore()
const settingsStore = useSettingsStore()

onMounted(async () => {
  await Promise.all([
    pagesStore.fetchList(),
    postsStore.fetchList(),
    mediaStore.fetchList(),
    settingsStore.fetch(),
  ])
})
</script>
