<template>
  <div
    v-if="message"
    class="flex items-center gap-2 rounded-md border px-4 py-2.5 text-sm font-medium"
    :class="type === 'error' ? 'border-rose-200 bg-rose-50 text-rose-600' : 'border-emerald-200 bg-emerald-50 text-emerald-600'"
  >
    {{ type === 'error' ? 'x' : 'OK' }} {{ message }}
  </div>
</template>

<script setup lang="ts">
defineProps<{ message: string; type?: 'success' | 'error' }>()
</script>
