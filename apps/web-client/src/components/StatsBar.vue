<template>
  <section class="stats-bar">
    <div class="container-shell stats-grid">
      <div v-for="stat in displayStats" :key="`${stat.value}-${stat.label}`" class="stat-item stats-grid-item">
        <h3>{{ stat.value }}</h3>
        <p>{{ stat.label }}</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { HomeStatItem } from '../types/content'

const props = withDefaults(defineProps<{
  items?: HomeStatItem[]
}>(), {
  items: () => [],
})

const displayStats = computed(() => props.items.length
  ? props.items
  : [
      { value: '10+', label: 'Năm kinh nghiệm' },
      { value: '500+', label: 'Dự án hoàn thành' },
      { value: '50+', label: 'Kỹ sư chuyên môn' },
      { value: '24/7', label: 'Hỗ trợ khách hàng' },
    ])
</script>
