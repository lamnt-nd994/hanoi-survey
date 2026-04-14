<template>
  <div class="py-14 text-center">
    <div class="mb-3 text-4xl text-slate-300">&#9776;</div>
    <p class="text-sm text-slate-400">{{ message }}</p>
  </div>
</template>

<script setup lang="ts">
defineProps<{ message?: string }>()
</script>
