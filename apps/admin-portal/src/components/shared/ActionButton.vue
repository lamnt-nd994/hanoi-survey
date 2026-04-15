<template>
  <Button :variant="resolvedVariant" :size="size" :class="buttonClass" v-bind="$attrs">
    <slot />
  </Button>
</template>

<script setup lang="ts">
import { computed } from 'vue'
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

const resolvedVariant = computed(() => props.variant === 'destructive' ? 'ghost' : props.variant)
const buttonClass = computed(() => cn(
  'min-w-9',
  props.variant === 'destructive' && 'text-rose-600 hover:bg-rose-50 hover:text-rose-700',
  props.class,
))
</script>
