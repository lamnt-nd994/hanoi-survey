<template>
  <Button :variant="variant" :size="size" :class="cn('min-w-9', props.class)" v-bind="$attrs">
    <slot />
  </Button>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { Button } from '@/components/ui/button'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{
  variant?: 'default' | 'secondary' | 'outline' | 'ghost' | 'destructive'
  size?: 'default' | 'sm' | 'lg' | 'icon'
  class?: ClassValue
}>(), {
  variant: 'ghost',
  size: 'icon',
  class: undefined,
})
</script>
