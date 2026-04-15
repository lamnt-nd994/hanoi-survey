<template>
  <RekaDialogTitle :class="props.class">
    <slot />
  </RekaDialogTitle>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { DialogTitle as RekaDialogTitle } from 'reka-ui'

const props = withDefaults(defineProps<{ class?: ClassValue }>(), { class: undefined })
</script>
