<template>
  <i v-if="isFontAwesome" :class="[icon, iconClass]" aria-hidden="true" />
  <component :is="lucideComponent" v-else :class="iconClass" :stroke-width="1.75" aria-hidden="true" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import * as LucideIcons from 'lucide-vue-next'

const props = withDefaults(defineProps<{
  icon?: string
  class?: string
}>(), {
  icon: '',
  class: '',
})

const legacyIconMap: Record<string, string> = {
  terrain: 'Mountain',
  geology: 'Map',
  testing: 'FlaskConical',
  monitoring: 'Activity',
  supervision: 'ShieldCheck',
  construction: 'Hammer',
}

const iconClass = computed(() => props.class)
const icon = computed(() => props.icon.trim())
const isFontAwesome = computed(() => /(^|\s)fa([srbld]|-solid|-regular|-brands)?(\s|$)/.test(icon.value))

const lucideComponent = computed<any>(() => {
  const iconName = resolveLucideName(icon.value)
  return (LucideIcons[iconName as keyof typeof LucideIcons] || LucideIcons.Circle) as any
})

function resolveLucideName(value: string) {
  if (!value) return 'Circle'
  const mappedValue = legacyIconMap[value] || value
  if (LucideIcons[mappedValue as keyof typeof LucideIcons]) {
    return mappedValue
  }

  const pascalCase = mappedValue
    .split(/[^a-zA-Z0-9]+/)
    .filter(Boolean)
    .map((segment) => segment.charAt(0).toUpperCase() + segment.slice(1))
    .join('')

  return LucideIcons[pascalCase as keyof typeof LucideIcons] ? pascalCase : 'Circle'
}
</script>
