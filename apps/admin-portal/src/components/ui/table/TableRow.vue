<template>
  <tr :class="cn('border-b border-slate-100 transition-colors hover:bg-slate-50', props.class)">
    <slot />
  </tr>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
