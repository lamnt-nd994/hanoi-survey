<template>
  <div :class="cn('flex items-center p-6 pt-0', props.class)">
    <slot />
  </div>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
