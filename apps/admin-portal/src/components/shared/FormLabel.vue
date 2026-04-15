<template>
  <label :class="cn('text-sm font-medium leading-none text-slate-700 peer-disabled:cursor-not-allowed peer-disabled:opacity-70', props.class)">
    <slot />
  </label>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
