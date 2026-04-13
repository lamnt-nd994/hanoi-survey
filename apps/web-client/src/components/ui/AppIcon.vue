<template>
  <i v-if="isFontAwesome" :class="[iconName, iconClass]" aria-hidden="true" />
  <component v-else :is="resolvedIcon" :class="iconClass" :stroke-width="strokeWidth" aria-hidden="true" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {
  Activity,
  ArrowRight,
  Building2,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  Circle,
  Clock3,
  Facebook,
  FlaskConical,
  Hammer,
  Linkedin,
  Mail,
  Map,
  MapPin,
  Menu,
  MessageCircle,
  Mountain,
  Newspaper,
  Phone,
  ScanSearch,
  Settings2,
  ShieldCheck,
  UserRound,
  X,
  Youtube,
  type LucideIcon,
} from 'lucide-vue-next'

const props = withDefaults(defineProps<{
  icon?: string
  class?: string
  strokeWidth?: number
}>(), {
  icon: '',
  class: '',
  strokeWidth: 1.75,
})

const iconRegistry: Record<string, LucideIcon> = {
  Activity,
  ArrowRight,
  Building2,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  Circle,
  Clock3,
  Facebook,
  FlaskConical,
  Hammer,
  Linkedin,
  Mail,
  Map,
  MapPin,
  Menu,
  MessageCircle,
  Mountain,
  Newspaper,
  Phone,
  ScanSearch,
  Settings2,
  ShieldCheck,
  UserRound,
  X,
  Youtube,
}

const legacyIconMap: Record<string, string> = {
  terrain: 'Mountain',
  geology: 'Map',
  testing: 'FlaskConical',
  monitoring: 'Activity',
  supervision: 'ShieldCheck',
  construction: 'Hammer',
  circle: 'Circle',
  'settings-2': 'Settings2',
  settings: 'Settings2',
  phone: 'Phone',
  mail: 'Mail',
  location: 'MapPin',
  mapPin: 'MapPin',
  clock: 'Clock3',
  menu: 'Menu',
  close: 'X',
  chevronDown: 'ChevronDown',
  chevronLeft: 'ChevronLeft',
  chevronRight: 'ChevronRight',
  arrowRight: 'ArrowRight',
  facebook: 'Facebook',
  youtube: 'Youtube',
  linkedin: 'Linkedin',
  zalo: 'MessageCircle',
  news: 'Newspaper',
  project: 'Building2',
  search: 'ScanSearch',
  user: 'UserRound',
}

const iconClass = computed(() => props.class)
const iconName = computed(() => props.icon.trim())
const isFontAwesome = computed(() => /(^|\s)fa([srbld]|-solid|-regular|-brands)?(\s|$)/.test(iconName.value))

const resolvedIcon = computed<LucideIcon>(() => {
  const resolvedName = resolveIconName(iconName.value)
  return iconRegistry[resolvedName] || Circle
})

function resolveIconName(value: string) {
  if (!value) return 'Circle'

  const mappedValue = legacyIconMap[value] || value
  if (iconRegistry[mappedValue]) return mappedValue

  const pascalCase = mappedValue
    .split(/[^a-zA-Z0-9]+/)
    .filter(Boolean)
    .map((segment) => segment.charAt(0).toUpperCase() + segment.slice(1))
    .join('')

  return iconRegistry[pascalCase] ? pascalCase : 'Circle'
}
</script>
