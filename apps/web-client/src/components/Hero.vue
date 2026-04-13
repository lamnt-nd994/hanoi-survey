<template>
  <section class="hero-section flex min-h-[600px] items-center py-10 text-white">
    <img v-if="heroImagePath" :src="resolveMediaUrl(heroImagePath)" :alt="siteName" class="absolute inset-0 h-full w-full object-cover" />
    <div class="absolute inset-0 bg-gradient-to-b from-primary-navy/30 to-primary-navy/72" />

    <div class="container-shell relative">
      <div class="mx-auto max-w-5xl rounded-[28px] border border-white/20 bg-white/10 px-6 py-10 text-center shadow-[0_8px_32px_rgba(0,0,0,0.35)] backdrop-blur-md md:px-10 md:py-12">
        <h1 class="font-heading text-3xl font-extrabold uppercase leading-tight text-white md:text-5xl">
          {{ heroTitle || slogan || 'Khảo sát chính xác - Nền móng vững chắc' }}
        </h1>

        <h2 class="mt-4 text-sm font-semibold uppercase tracking-[0.18em] text-slate-300 md:text-lg">
          {{ heroSubtitle || companyNameEn || shortName || 'Ha Noi Construction Survey Consultant J.S.C' }}
        </h2>

        <div class="mt-8 flex flex-wrap justify-center gap-3">
          <div
            v-for="item in services"
            :key="`${item.label}-${item.icon}`"
            class="inline-flex items-center gap-2 rounded-full border border-white/10 bg-black/15 px-4 py-2 text-sm text-white/90"
          >
            <IconRenderer :icon="item.icon" class="h-4 w-4 text-slate-300" />
            <span>{{ item.label }}</span>
          </div>
        </div>

        <div class="mt-10 flex flex-wrap justify-center gap-4">
          <router-link :to="primaryCtaUrl" class="inline-flex min-h-[44px] min-w-[44px] items-center justify-center rounded-full bg-slate-100 px-8 py-3 text-base font-semibold uppercase tracking-[0.04em] text-slate-900 transition-colors hover:bg-white">
            {{ primaryCtaLabel }}
          </router-link>
          <router-link :to="secondaryCtaUrl" class="inline-flex min-h-[44px] min-w-[44px] items-center justify-center rounded-full border border-white/70 px-8 py-3 text-base font-semibold uppercase tracking-[0.04em] text-white transition-colors hover:bg-white/10">
            {{ secondaryCtaLabel }}
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import IconRenderer from './IconRenderer.vue'
import { resolveMediaUrl } from '../lib/media'
import { useSiteSettingsStore } from '../stores/siteSettings'
import type { HomeHeroBadgeItem } from '../types/content'

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

const heroTitle = computed(() => props.title)
const heroSubtitle = computed(() => props.subtitle)
const primaryCtaLabel = computed(() => props.primaryCtaLabel)
const primaryCtaUrl = computed(() => props.primaryCtaUrl)
const secondaryCtaLabel = computed(() => props.secondaryCtaLabel)
const secondaryCtaUrl = computed(() => props.secondaryCtaUrl)
const heroImagePath = computed(() => props.backgroundImagePath || heroBannerPath.value)

onMounted(() => {
  siteSettingsStore.ensureLoaded()
})
</script>
