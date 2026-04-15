<template>
  <img
    v-if="src"
    :src="resolvedSrc"
    :alt="alt"
    :width="width"
    :height="height"
    :loading="loading"
    decoding="async"
    :fetchpriority="fetchPriority"
    :class="className"
    @error="$emit('error', $event)"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { resolveMediaUrl } from '../../lib/media'
import { cn } from '../../lib/utils'

const props = withDefaults(defineProps<{
  src?: string | null
  alt: string
  width?: number
  height?: number
  loading?: 'eager' | 'lazy'
  fetchPriority?: 'high' | 'low' | 'auto'
  class?: string
}>(), {
  src: '',
  width: 1200,
  height: 675,
  loading: 'lazy',
  fetchPriority: 'auto',
  class: '',
})

defineEmits<{
  (e: 'error', event: Event): void
}>()

const resolvedSrc = computed(() => resolveMediaUrl(props.src))
const className = computed(() => cn('block', props.class))
</script>
