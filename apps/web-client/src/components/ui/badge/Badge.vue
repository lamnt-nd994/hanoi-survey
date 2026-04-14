<template>
  <component :is="as" :class="badgeClass">
    <slot />
  </component>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { cva, type VariantProps } from 'class-variance-authority'
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const badgeVariants = cva('inline-flex items-center rounded-full px-3 py-1 text-xs font-semibold transition-colors', {
  variants: {
    variant: {
      default: 'bg-accent-green/10 text-accent-green',
      secondary: 'bg-neutral-100 text-neutral-700',
      dark: 'bg-primary-navy text-white',
    },
  },
  defaultVariants: {
    variant: 'default',
  },
})

type BadgeVariants = VariantProps<typeof badgeVariants>

const props = withDefaults(defineProps<{ as?: string; variant?: BadgeVariants['variant']; class?: ClassValue }>(), {
  as: 'span',
  variant: 'default',
  class: undefined,
})

const badgeClass = computed(() => cn(badgeVariants({ variant: props.variant }), props.class))
</script>
