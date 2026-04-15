<template>
  <div class="fixed bottom-6 right-6 z-50 flex min-h-[7.75rem] flex-col gap-3 lg:bottom-8 lg:right-8" aria-label="Quick contact actions">
    <a
      v-if="zaloHref"
      :href="zaloHref"
      target="_blank"
      rel="noopener noreferrer"
      class="flex h-14 w-14 items-center justify-center rounded-full bg-[#0068FF] text-white shadow-glow transition-transform hover:scale-110 focus:outline-none focus:ring-2 focus:ring-[#0068FF] focus:ring-offset-2"
      aria-label="Chat Zalo"
    >
      <svg class="h-9 w-9" viewBox="0 0 48 48" role="img" aria-hidden="true" focusable="false">
        <path fill="#fff" d="M10.5 8h27A6.5 6.5 0 0 1 44 14.5v16A6.5 6.5 0 0 1 37.5 37H24.2l-8.5 5.4c-1 .6-2.2-.1-2.2-1.3V37h-3A6.5 6.5 0 0 1 4 30.5v-16A6.5 6.5 0 0 1 10.5 8Z" />
        <text x="24" y="27.5" fill="#0068FF" text-anchor="middle" font-size="12.5" font-weight="800" letter-spacing="-.3">Zalo</text>
      </svg>
    </a>
    <span
      v-else
      class="flex h-14 w-14 items-center justify-center rounded-full bg-[#0068FF]/15 text-transparent shadow-glow"
      aria-hidden="true"
    >
      <svg class="h-9 w-9 opacity-0" viewBox="0 0 48 48" role="img" aria-hidden="true" focusable="false">
        <path fill="currentColor" d="M10.5 8h27A6.5 6.5 0 0 1 44 14.5v16A6.5 6.5 0 0 1 37.5 37H24.2l-8.5 5.4c-1 .6-2.2-.1-2.2-1.3V37h-3A6.5 6.5 0 0 1 4 30.5v-16A6.5 6.5 0 0 1 10.5 8Z" />
      </svg>
    </span>

    <a
      v-if="mergedContactInfo.phone"
      :href="`tel:${mergedContactInfo.phone}`"
      class="flex h-14 w-14 items-center justify-center rounded-full bg-accent-green text-white shadow-glow transition-transform hover:scale-110 focus:outline-none focus:ring-2 focus:ring-accent-green focus:ring-offset-2"
      aria-label="Goi hotline"
    >
      <AppIcon icon="phone" class="h-7 w-7" />
    </a>
    <span
      v-else
      class="flex h-14 w-14 items-center justify-center rounded-full bg-accent-green/15 text-transparent shadow-glow"
      aria-hidden="true"
    >
      <AppIcon icon="phone" class="h-7 w-7 opacity-0" />
    </span>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { storeToRefs } from 'pinia'
import { useSiteSettingsStore } from '../stores/siteSettings'
import AppIcon from './ui/AppIcon.vue'

const siteSettingsStore = useSiteSettingsStore()
const { mergedContactInfo } = storeToRefs(siteSettingsStore)

const zaloHref = computed(() => normalizeZaloHref(mergedContactInfo.value.zaloUrl))

function normalizeZaloHref(value: string | null | undefined) {
  const raw = value?.trim()
  if (!raw) return ''

  if (/^https?:\/\//i.test(raw)) return raw
  if (/^(www\.)?zalo\.me\//i.test(raw)) return `https://${raw.replace(/^www\./i, '')}`

  const digits = raw.replace(/\D/g, '')
  return digits ? `https://zalo.me/${digits}` : ''
}
</script>
