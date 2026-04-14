<template>
  <RekaSeparator :orientation="orientation" :class="cn(orientation === 'vertical' ? 'h-full w-px' : 'h-px w-full', 'shrink-0 bg-neutral-200', props.class)" />
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { Separator as RekaSeparator } from 'reka-ui'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ orientation?: 'horizontal' | 'vertical'; class?: ClassValue }>(), {
  orientation: 'horizontal',
  class: undefined,
})
</script>
