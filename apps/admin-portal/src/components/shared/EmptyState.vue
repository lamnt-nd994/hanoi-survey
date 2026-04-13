<template>
  <div class="cms-empty-state">
    <div class="empty-icon">&#9776;</div>
    <p>{{ message }}</p>
  </div>
</template>

<script setup lang="ts">
defineProps<{ message?: string }>()
</script>
