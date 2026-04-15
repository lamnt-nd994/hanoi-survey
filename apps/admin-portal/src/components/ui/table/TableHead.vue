<template>
  <th :class="cn('h-10 px-4 text-left align-middle text-xs font-semibold uppercase tracking-wide text-slate-500', props.class)">
    <slot />
  </th>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
