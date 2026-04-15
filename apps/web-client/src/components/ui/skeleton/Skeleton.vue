<template>
  <div :class="cn('animate-pulse rounded-md bg-neutral-100', props.class)" />
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
