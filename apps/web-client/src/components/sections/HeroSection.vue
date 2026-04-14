<template>
  <section class="hero-section flex min-h-[600px] items-center py-10 text-white">
    <MediaImage
      v-if="heroImagePath"
      :src="heroImagePath"
      :alt="siteName"
      :width="1920"
      :height="1080"
      loading="eager"
      fetch-priority="high"
      class="absolute inset-0 h-full w-full object-cover"
    />
    <div class="absolute inset-0 bg-gradient-to-b from-primary-navy/30 to-primary-navy/72" />

    <div class="container-shell relative">
      <div class="text-center">
        <h1 class="font-heading text-3xl font-extrabold uppercase leading-[1.08] text-white md:text-5xl lg:text-[4.25rem]">
          <span v-for="(line, lineIndex) in heroTitleLines" :key="`hero-line-${lineIndex}`" class="block">
            <template v-for="(part, index) in line" :key="`${part.text}-${lineIndex}-${index}`">
              <span :class="part.highlight ? 'text-[#ff9f2f]' : 'text-white'">{{ part.text }}</span>
            </template>
          </span>
        </h1>

        <h2 class="mt-4 text-sm font-semibold tracking-[0.04em] text-slate-300 md:text-lg">
          {{ subtitle || companyNameEn || shortName || 'Ha Noi Construction Survey Consultant J.S.C' }}
        </h2>

        <div class="mt-8 flex flex-wrap justify-center gap-3">
          <div
            v-for="item in services"
            :key="`${item.label}-${item.icon}`"
            class="inline-flex items-center gap-2 rounded-full border border-white/10 bg-black/15 px-4 py-2 text-sm text-white/90"
          >
            <AppIcon :icon="item.icon" class="h-4 w-4 text-slate-300" />
            <span>{{ item.label }}</span>
          </div>
        </div>

        <div class="mt-10 flex flex-wrap justify-center gap-4">
          <router-link :to="primaryCtaUrl" class="inline-flex min-h-[44px] min-w-[44px] items-center justify-center rounded-full bg-slate-100 px-8 py-3 text-base font-semibold text-slate-900 transition-colors hover:bg-white">
            {{ primaryCtaLabel }}
          </router-link>
          <router-link :to="secondaryCtaUrl" class="inline-flex min-h-[44px] min-w-[44px] items-center justify-center rounded-full border border-white/70 px-8 py-3 text-base font-semibold text-white transition-colors hover:bg-white/10">
            {{ secondaryCtaLabel }}
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { storeToRefs } from 'pinia'
import AppIcon from '../ui/AppIcon.vue'
import MediaImage from '../ui/MediaImage.vue'
import { useSiteSettingsStore } from '../../stores/siteSettings'
import type { HomeHeroBadgeItem } from '../../types/content'

const props = withDefaults(defineProps<{
  title?: string
  subtitle?: string
  backgroundImagePath?: string
  badges?: HomeHeroBadgeItem[]
  primaryCtaLabel?: string
  primaryCtaUrl?: string
  secondaryCtaLabel?: string
  secondaryCtaUrl?: string
}>(), {
  title: '',
  subtitle: '',
  backgroundImagePath: '',
  badges: () => [],
  primaryCtaLabel: 'Xem dự án',
  primaryCtaUrl: '/du-an',
  secondaryCtaLabel: 'Liên hệ',
  secondaryCtaUrl: '/lien-he',
})

const siteSettingsStore = useSiteSettingsStore()
const { siteName, companyNameEn, shortName, slogan, heroBannerPath } = storeToRefs(siteSettingsStore)

const services = computed(() => {
  if (!props.badges.length) {
    return [
      { label: 'Dia hinh', icon: 'terrain' },
      { label: 'Dia chat', icon: 'geology' },
      { label: 'Thi nghiem', icon: 'testing' },
      { label: 'Quan trac', icon: 'monitoring' },
      { label: 'Giam sat', icon: 'supervision' },
      { label: 'Xay lap', icon: 'construction' },
    ]
  }

  return props.badges.map((item) => ({
    label: item.label,
    icon: item.iconKey || 'terrain',
  }))
})

const heroImagePath = computed(() => props.backgroundImagePath || heroBannerPath.value)
const heroTitle = computed(() => props.title || slogan.value || 'Khảo sát [chính xác] - nền móng [vững chắc]')
const heroTitleLines = computed(() => splitHeroTitleIntoLines(heroTitle.value).map((line) => parseHighlightMarkers(line)))

function splitHeroTitleIntoLines(value: string) {
  const cleanedValue = value.replace(/\s+/g, ' ').trim()
  if (!cleanedValue) return ['']

  const explicitLines = cleanedValue.split(/\s*-\s*/).map((item) => item.trim()).filter(Boolean)
  if (explicitLines.length > 1) {
    return explicitLines
  }

  const words = cleanedValue.split(' ')
  if (words.length <= 3) {
    return [cleanedValue]
  }

  const middleIndex = Math.ceil(words.length / 2)
  return [words.slice(0, middleIndex).join(' '), words.slice(middleIndex).join(' ')]
}

function parseHighlightMarkers(value: string) {
  const parts: Array<{ text: string; highlight: boolean }> = []
  const markerPattern = /\[([^\]]+)\]/g
  let lastIndex = 0
  let match: RegExpExecArray | null

  while ((match = markerPattern.exec(value)) !== null) {
    if (match.index > lastIndex) {
      parts.push({ text: value.slice(lastIndex, match.index), highlight: false })
    }

    parts.push({ text: match[1], highlight: true })
    lastIndex = markerPattern.lastIndex
  }

  if (lastIndex < value.length) {
    parts.push({ text: value.slice(lastIndex), highlight: false })
  }

  return parts.length ? parts : [{ text: value, highlight: false }]
}
</script>
