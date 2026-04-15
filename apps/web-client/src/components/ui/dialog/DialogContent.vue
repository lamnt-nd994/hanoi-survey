<template>
  <DialogPortal>
    <RekaDialogOverlay :class="cn('fixed inset-0 z-[70] bg-slate-950/90', overlayClass)" />
    <RekaDialogContent
      :class="cn('fixed left-1/2 top-1/2 z-[71] w-[calc(100vw-2rem)] -translate-x-1/2 -translate-y-1/2 focus:outline-none', props.class)"
      v-bind="$attrs"
    >
      <slot />
    </RekaDialogContent>
  </DialogPortal>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { DialogContent as RekaDialogContent, DialogOverlay as RekaDialogOverlay, DialogPortal } from 'reka-ui'
import { cn } from '@/lib/utils'

const props = withDefaults(defineProps<{ class?: ClassValue; overlayClass?: ClassValue }>(), {
  class: undefined,
  overlayClass: undefined,
})
</script>
