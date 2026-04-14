<template>
  <td :class="cn('px-4 py-3 align-middle text-slate-700', props.class)">
    <slot />
  </td>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
