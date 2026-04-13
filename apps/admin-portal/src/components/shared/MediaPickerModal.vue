<template>
  <div v-if="open" class="fixed inset-0 z-[70] flex items-center justify-center bg-slate-950/60 px-4 py-8" @click.self="$emit('close')">
    <div class="max-h-[90vh] w-full max-w-5xl overflow-hidden rounded-3xl bg-white shadow-2xl">
      <div class="flex items-center justify-between border-b border-slate-200 px-6 py-4">
        <div>
          <h3 class="text-lg font-semibold text-slate-900">Chọn từ thư viện media</h3>
          <p class="mt-1 text-sm text-slate-500">Chọn ảnh hoặc tài liệu đã upload trước đó.</p>
        </div>
        <button type="button" class="rounded-full p-2 text-slate-500 transition hover:bg-slate-100 hover:text-slate-900" @click="$emit('close')">✕</button>
      </div>

      <div class="overflow-y-auto p-6">
        <div v-if="loading" class="rounded-2xl border border-slate-200 bg-slate-50 px-4 py-8 text-center text-sm text-slate-500">Đang tải thư viện media...</div>
        <div v-else-if="!items.length" class="rounded-2xl border border-slate-200 bg-slate-50 px-4 py-8 text-center text-sm text-slate-500">Chưa có file nào trong thư viện.</div>
        <div v-else class="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          <button
            v-for="item in items"
            :key="item.id"
            type="button"
            class="rounded-2xl border border-slate-200 p-4 text-left transition hover:border-slate-300 hover:bg-slate-50"
            @click="$emit('select', item.storagePath)"
          >
            <div class="flex items-start gap-4">
              <div class="flex h-20 w-24 items-center justify-center overflow-hidden rounded-xl border border-slate-200 bg-slate-100">
                <img v-if="isImage(item.mimeType)" :src="resolveMediaUrl(item.storagePath)" :alt="item.fileName" class="h-full w-full object-cover" />
                <div v-else class="text-xs font-semibold uppercase tracking-[0.12em] text-slate-500">PDF</div>
              </div>
              <div class="min-w-0 flex-1">
                <div class="truncate text-sm font-semibold text-slate-900">{{ item.fileName }}</div>
                <div class="mt-1 text-xs text-slate-500">{{ item.mimeType }}</div>
                <div class="mt-2 break-all text-xs text-slate-400">{{ item.storagePath }}</div>
              </div>
            </div>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { MediaEntity } from '@/types'

defineProps<{
  open: boolean
  items: MediaEntity[]
  loading: boolean
}>()

defineEmits<{
  (e: 'close'): void
  (e: 'select', storagePath: string): void
}>()

function resolveMediaUrl(path: string) {
  if (!path) return ''
  if (/^https?:\/\//i.test(path)) return path
  const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
  return `${baseUrl}/${path.replace(/^\/+/, '')}`
}

function isImage(mimeType: string) {
  return mimeType?.startsWith('image/')
}
</script>
