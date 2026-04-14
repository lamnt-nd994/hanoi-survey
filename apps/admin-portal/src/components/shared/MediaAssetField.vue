<template>
  <div class="rounded-2xl border border-slate-200 p-4">
    <div v-if="value" class="mb-4">
      <img
        v-if="isImage(value)"
        :src="mediaUrl(value)"
        :alt="label"
        width="320"
        height="160"
        loading="lazy"
        decoding="async"
        class="h-40 w-full rounded-xl border border-slate-200 bg-slate-50 object-contain"
      />
      <div v-else-if="isPdf(value)" class="rounded-xl border border-slate-200 bg-slate-50 px-4 py-6 text-sm text-slate-600">
        <div class="font-medium text-slate-900">PDF đã chọn</div>
        <a :href="mediaUrl(value)" target="_blank" rel="noopener noreferrer" class="mt-2 inline-block break-all text-sky-600 underline">{{ value }}</a>
      </div>
      <div v-else class="break-all rounded-xl border border-slate-200 bg-slate-50 px-4 py-6 text-sm text-slate-600">{{ value }}</div>
    </div>
    <div v-else class="mb-4 flex h-40 items-center justify-center rounded-xl border border-dashed border-slate-300 bg-slate-50 px-4 py-6 text-center text-sm text-slate-400">Chưa chọn file</div>
    <div class="flex flex-wrap gap-2">
      <button type="button" class="inline-flex h-9 items-center justify-center gap-2 rounded-md bg-slate-900 px-3 text-sm font-medium text-white transition-colors hover:bg-slate-800 disabled:pointer-events-none disabled:opacity-50" @click="$emit('upload')">{{ uploading ? 'Đang upload...' : 'Upload' }}</button>
      <button type="button" class="inline-flex h-9 items-center justify-center gap-2 rounded-md border border-slate-200 bg-white px-3 text-sm font-medium text-slate-700 transition-colors hover:bg-slate-50 disabled:pointer-events-none disabled:opacity-50" @click="$emit('pick')">Chọn từ thư viện</button>
      <button v-if="value" type="button" class="inline-flex h-9 items-center justify-center gap-2 rounded-md border border-slate-200 bg-white px-3 text-sm font-medium text-slate-700 transition-colors hover:bg-slate-50 disabled:pointer-events-none disabled:opacity-50" @click="$emit('clear')">Xóa</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { resolveMediaUrl } from '@/lib/media'

defineProps<{
  label: string
  value: string | null
  uploading: boolean
}>()

defineEmits<{
  (e: 'upload'): void
  (e: 'pick'): void
  (e: 'clear'): void
}>()

function isImage(path: string | null) {
  if (!path) return false
  return /\.(png|jpe?g|gif|webp|svg)$/i.test(path)
}

function isPdf(path: string | null) {
  if (!path) return false
  return /\.pdf$/i.test(path)
}

function mediaUrl(path: string | null) {
  return resolveMediaUrl(path)
}
</script>
