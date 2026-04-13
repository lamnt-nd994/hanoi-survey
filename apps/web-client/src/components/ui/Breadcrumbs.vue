<template>
  <nav aria-label="Breadcrumb" class="border-b border-neutral-200/80 bg-white/95">
    <div class="container-shell py-4">
      <ol class="flex flex-wrap items-center gap-2 text-sm text-neutral-500">
        <li v-for="(item, index) in items" :key="`${item.label}-${index}`" class="flex items-center gap-2">
          <router-link
            v-if="item.to && !item.current"
            :to="item.to"
            class="transition-colors hover:text-primary-navy"
          >
            {{ item.label }}
          </router-link>
          <span v-else :class="item.current ? 'font-semibold text-primary-navy' : ''">{{ item.label }}</span>
          <span v-if="index < items.length - 1" class="text-neutral-300">/</span>
        </li>
      </ol>
    </div>
  </nav>
</template>

<script setup lang="ts">
import type { RouteLocationRaw } from 'vue-router'

defineProps<{
  items: Array<{
    label: string
    to?: RouteLocationRaw
    current?: boolean
  }>
}>()
</script>
