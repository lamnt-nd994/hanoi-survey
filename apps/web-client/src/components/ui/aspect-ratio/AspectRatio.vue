<template>
  <RekaAspectRatio :ratio="ratio" :class="props.class">
    <slot />
  </RekaAspectRatio>
</template>

<script setup lang="ts">
import type { ClassValue } from 'clsx'
import { AspectRatio as RekaAspectRatio } from 'reka-ui'

const props = withDefaults(defineProps<{ ratio?: number; class?: ClassValue }>(), {
  ratio: 16 / 9,
  class: undefined,
})
</script>
