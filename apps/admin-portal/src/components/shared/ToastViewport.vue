<template>
  <div class="pointer-events-none fixed right-4 top-4 z-[1000] flex w-full max-w-sm flex-col gap-3">
    <div
      v-for="item in store.items"
      :key="item.id"
      class="pointer-events-auto rounded-xl border px-4 py-3 shadow-lg"
      :class="item.type === 'success'
        ? 'border-emerald-200 bg-emerald-50 text-emerald-800'
        : item.type === 'error'
          ? 'border-rose-200 bg-rose-50 text-rose-800'
          : 'border-slate-200 bg-white text-slate-700'"
    >
      <div class="flex items-start justify-between gap-3">
        <p class="text-sm font-medium leading-6">{{ item.message }}</p>
<!--        <button type="button" class="text-xs opacity-60 transition hover:opacity-100" @click="store.dismiss(item.id)">Dong</button>-->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useToastsStore } from '@/stores/toasts'

const store = useToastsStore()
</script>
