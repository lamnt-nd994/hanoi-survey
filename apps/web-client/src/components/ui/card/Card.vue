<template>
  <component :is="as" :class="cn('rounded-2xl border border-neutral-200 bg-white shadow-[0_18px_45px_rgba(15,39,68,0.08)] transition-all', props.class)">
    <slot />
  </component>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ as?: string; class?: ClassValue }>(), {
  as: 'div',
  class: undefined,
})
</script>
