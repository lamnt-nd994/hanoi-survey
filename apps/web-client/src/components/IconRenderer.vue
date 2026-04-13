<template>
  <AppIcon :icon="icon" :class="className" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import AppIcon from './ui/AppIcon.vue'

const props = withDefaults(defineProps<{
  icon?: string
  class?: string
}>(), {
  icon: '',
  class: '',
})

const className = computed(() => props.class)
</script>
