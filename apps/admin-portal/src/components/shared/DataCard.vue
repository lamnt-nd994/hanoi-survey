<template>
  <section :class="cn('overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm', props.class)">
    <slot />
  </section>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
