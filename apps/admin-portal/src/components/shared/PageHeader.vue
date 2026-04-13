<template>
  <div class="flex items-center justify-between flex-wrap gap-4">
    <div>
      <h2 class="text-lg font-bold text-gray-900">{{ title }}</h2>
      <p v-if="description" class="mt-1 text-sm text-gray-500">{{ description }}</p>
    </div>
    <div class="flex gap-2">
      <slot name="actions" />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ title: string; description?: string }>()
</script>
