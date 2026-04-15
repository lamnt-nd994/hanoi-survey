<template>
  <component :is="as" :class="buttonClass" v-bind="$attrs">
    <slot />
  </component>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { cva, type VariantProps } from 'class-variance-authority'
import type { ClassValue } from 'clsx'
import { cn } from '@/lib/utils'

const buttonVariants = cva(
  'inline-flex min-h-[44px] min-w-[44px] items-center justify-center gap-2 whitespace-nowrap rounded-full text-sm font-semibold transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-60',
  {
    variants: {
      variant: {
        default: 'bg-accent-green text-white hover:bg-accent-dark focus-visible:ring-accent-green',
        secondary: 'border border-neutral-200 bg-white text-primary-navy hover:bg-neutral-50 focus-visible:ring-primary-navy',
        outline: 'border-2 border-primary-navy bg-transparent text-primary-navy hover:bg-primary-navy/5 focus-visible:ring-primary-navy',
        ghost: 'bg-transparent text-primary-navy hover:bg-neutral-100 focus-visible:ring-primary-navy',
        dark: 'bg-slate-900 text-white hover:bg-slate-700 focus-visible:ring-slate-900',
      },
      size: {
        default: 'px-6 py-3',
        sm: 'min-h-9 px-3 py-2',
        lg: 'min-h-12 px-8 py-3 text-base',
        icon: 'h-11 w-11 p-0',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  },
)

type ButtonVariants = VariantProps<typeof buttonVariants>

const props = withDefaults(defineProps<{
  as?: string
  variant?: ButtonVariants['variant']
  size?: ButtonVariants['size']
  class?: ClassValue
}>(), {
  as: 'button',
  variant: 'default',
  size: 'default',
  class: undefined,
})

const buttonClass = computed(() => cn(buttonVariants({ variant: props.variant, size: props.size }), props.class))
</script>
