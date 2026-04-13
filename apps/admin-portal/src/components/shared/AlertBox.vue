<template>
  <div v-if="message" class="cms-alert" :class="type === 'error' ? 'cms-alert-error' : 'cms-alert-success'">
    {{ type === 'error' ? '✕' : '✓' }} {{ message }}
  </div>
</template>

<script setup lang="ts">
defineProps<{ message: string; type?: 'success' | 'error' }>()
</script>
