<template>
  <div class="flex gap-4">
    <a
      v-for="item in items"
      :key="item.label"
      :href="item.href"
      target="_blank"
      rel="noopener noreferrer"
      class="flex h-10 w-10 items-center justify-center rounded-full bg-white/10 text-white transition-all hover:bg-accent-green"
      :aria-label="item.label"
    >
      <AppIcon :icon="item.icon" class="h-5 w-5" />
    </a>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import AppIcon from './AppIcon.vue'

const props = defineProps<{
  facebookUrl?: string
  youtubeUrl?: string
  linkedinUrl?: string
}>()

const items = computed(() => [
  { label: 'Facebook', href: props.facebookUrl || '', icon: 'facebook' },
  { label: 'YouTube', href: props.youtubeUrl || '', icon: 'youtube' },
  { label: 'LinkedIn', href: props.linkedinUrl || '', icon: 'linkedin' },
].filter((item) => item.href))
</script>
