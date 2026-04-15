<template>
  <div class="flex min-h-[2.5rem] gap-4">
    <template v-for="item in items" :key="item.label">
      <a
        v-if="item.href"
        :href="item.href"
        target="_blank"
        rel="noopener noreferrer"
        class="flex h-10 w-10 items-center justify-center rounded-full bg-white/10 text-white transition-all hover:bg-accent-green"
        :aria-label="item.label"
      >
        <AppIcon :icon="item.icon" class="h-5 w-5" />
      </a>
      <span
        v-else-if="reserveSpace"
        class="flex h-10 w-10 items-center justify-center rounded-full bg-white/5 text-white/30"
        aria-hidden="true"
      >
        <AppIcon :icon="item.icon" class="h-5 w-5" />
      </span>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import AppIcon from './AppIcon.vue'

const props = defineProps<{
  facebookUrl?: string
  youtubeUrl?: string
  linkedinUrl?: string
  reserveSpace?: boolean
}>()

const items = computed(() => [
  { label: 'Facebook', href: props.facebookUrl || '', icon: 'facebook' },
  { label: 'YouTube', href: props.youtubeUrl || '', icon: 'youtube' },
  { label: 'LinkedIn', href: props.linkedinUrl || '', icon: 'linkedin' },
])
</script>
