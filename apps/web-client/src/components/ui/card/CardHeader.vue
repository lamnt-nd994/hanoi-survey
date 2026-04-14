<template>
  <div :class="cn('flex flex-col space-y-1.5 p-6', props.class)">
    <slot />
  </div>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
