<template>
  <div :class="centered ? 'text-center' : ''">
    <div v-if="eyebrow" :class="eyebrowClass">{{ eyebrow }}</div>
    <h2 :class="titleClass">{{ title }}</h2>
    <p v-if="description" :class="descriptionClass">{{ description }}</p>
    <div v-if="showDivider" :class="dividerClass" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(defineProps<{
  eyebrow?: string
  title: string
  description?: string
  centered?: boolean
  light?: boolean
  showDivider?: boolean
}>(), {
  eyebrow: '',
  description: '',
  centered: false,
  light: false,
  showDivider: false,
})

const eyebrowClass = computed(() => [
  'text-xs font-semibold uppercase tracking-[0.08em] md:text-sm',
  props.light ? 'text-white/75' : 'text-accent-green',
])

const titleClass = computed(() => [
  'mt-4 font-heading text-3xl font-extrabold md:text-4xl lg:text-5xl',
  props.light ? 'text-white' : 'text-primary-navy',
])

const descriptionClass = computed(() => [
  'mx-auto mt-4 max-w-3xl text-lg md:text-xl',
  props.light ? 'text-neutral-300' : 'text-neutral-600',
])

const dividerClass = computed(() => [
  'mx-auto mt-4 h-1 w-16',
  props.light ? 'bg-white/70' : 'bg-accent-green',
])
</script>
