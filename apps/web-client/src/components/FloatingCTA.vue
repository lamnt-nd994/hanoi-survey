<template>
  <div class="fixed bottom-6 right-6 z-50 flex flex-col gap-3 lg:bottom-8 lg:right-8">
    <a
      v-if="mergedContactInfo.zaloUrl"
      :href="mergedContactInfo.zaloUrl"
      target="_blank"
      rel="noopener noreferrer"
      class="flex h-14 w-14 items-center justify-center rounded-full bg-[#0068FF] text-white shadow-glow transition-transform hover:scale-110 focus:outline-none focus:ring-2 focus:ring-[#0068FF] focus:ring-offset-2"
      aria-label="Chat Zalo"
    >
      <AppIcon icon="zalo" class="h-7 w-7" />
    </a>
    <a
      v-if="mergedContactInfo.phone"
      :href="`tel:${mergedContactInfo.phone}`"
      class="flex h-14 w-14 items-center justify-center rounded-full bg-accent-green text-white shadow-glow transition-transform hover:scale-110 focus:outline-none focus:ring-2 focus:ring-accent-green focus:ring-offset-2"
      aria-label="Gọi hotline"
    >
      <AppIcon icon="phone" class="h-7 w-7" />
    </a>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useSiteSettingsStore } from '../stores/siteSettings'
import AppIcon from './ui/AppIcon.vue'

const siteSettingsStore = useSiteSettingsStore()
const { mergedContactInfo } = storeToRefs(siteSettingsStore)
</script>
