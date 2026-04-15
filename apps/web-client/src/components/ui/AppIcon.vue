<template>
  <component :is="resolvedIcon" :class="iconClass" :stroke-width="strokeWidth" aria-hidden="true" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {
  Activity,
  ArrowRight,
  Building2,
  ChartLine,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  Circle,
  Clock3,
  Droplets,
  Facebook,
  FlaskConical,
  Globe,
  Hammer,
  HardHat,
  Linkedin,
  Mail,
  Map,
  MapPin,
  Menu,
  MessageCircle,
  Mountain,
  Newspaper,
  Phone,
  Pickaxe,
  ScanSearch,
  Settings2,
  ShieldCheck,
  UserRound,
  Waves,
  Wrench,
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
  ChartLine,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  Circle,
  Clock3,
  Droplets,
  Facebook,
  FlaskConical,
  Globe,
  Hammer,
  HardHat,
  Linkedin,
  Mail,
  Map,
  MapPin,
  Menu,
  MessageCircle,
  Mountain,
  Newspaper,
  Phone,
  Pickaxe,
  ScanSearch,
  Settings2,
  ShieldCheck,
  UserRound,
  Waves,
  Wrench,
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
  tools: 'Wrench',
  water: 'Waves',
  'chart-line': 'ChartLine',
  'globe-asia': 'Globe',
  'hard-hat': 'HardHat',
  'map-marked-alt': 'Map',
  flask: 'FlaskConical',
  'fa-tools': 'Wrench',
  'fa-water': 'Waves',
  'fa-chart-line': 'ChartLine',
  'fa-globe-asia': 'Globe',
  'fa-hard-hat': 'HardHat',
  'fa-map-marked-alt': 'Map',
  'fa-flask': 'FlaskConical',
  'fa-check-circle': 'ShieldCheck',
  'check-circle': 'ShieldCheck',
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

const resolvedIcon = computed<LucideIcon>(() => {
  const resolvedName = resolveIconName(iconName.value)
  return iconRegistry[resolvedName] || Circle
})

function resolveIconName(value: string) {
  if (!value) return 'Circle'

  const normalizedValue = normalizeIconName(value)
  const mappedValue = legacyIconMap[normalizedValue] || normalizedValue
  if (iconRegistry[mappedValue]) return mappedValue

  const pascalCase = mappedValue
    .split(/[^a-zA-Z0-9]+/)
    .filter(Boolean)
    .map((segment) => segment.charAt(0).toUpperCase() + segment.slice(1))
    .join('')

  return iconRegistry[pascalCase] ? pascalCase : 'Circle'
}

function normalizeIconName(value: string) {
  const segments = value
    .trim()
    .split(/\s+/)
    .filter(Boolean)
    .filter((segment) => !/^fa(s|r|b|l|d)?$/.test(segment) && !/^fa-(solid|regular|brands)$/.test(segment))

  return segments[segments.length - 1] || value
}
</script>
