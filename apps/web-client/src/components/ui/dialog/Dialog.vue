<template>
  <DialogRoot v-bind="$props">
    <slot />
  </DialogRoot>
</template>

<script setup lang="ts">
import { DialogRoot } from 'reka-ui'

defineProps<{
  open?: boolean
  defaultOpen?: boolean
  modal?: boolean
}>()
</script>
