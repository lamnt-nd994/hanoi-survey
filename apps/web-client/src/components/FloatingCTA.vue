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
      <svg class="h-7 w-7" viewBox="0 0 24 24" fill="currentColor">
        <path d="M12 2C6.48 2 2 6.48 2 12c0 1.82.5 3.53 1.36 5.02L2 22l5.04-1.34C8.5 21.53 10.21 22 12 22c5.52 0 10-4.48 10-10S17.52 2 12 2zm0 18c-1.56 0-3.07-.43-4.38-1.18l-.31-.18-3.22.86.86-3.14-.2-.32C3.63 14.74 3.2 13.21 3.2 11.6c0-4.84 3.96-8.8 8.8-8.8s8.8 3.96 8.8 8.8-3.96 8.8-8.8 8.8zm5.44-7.26c-.3-.15-1.77-.87-2.04-.97-.27-.1-.47-.15-.67.15-.2.3-.77.97-.94 1.17-.17.2-.34.22-.64.07-.3-.15-1.26-.46-2.4-1.48-.89-.79-1.49-1.77-1.67-2.07-.17-.3-.02-.46.13-.61.13-.13.3-.34.45-.51.15-.17.2-.3.3-.5.1-.2.05-.37-.02-.52-.07-.15-.67-1.62-.92-2.22-.25-.58-.5-.5-.67-.51-.17-.01-.37-.01-.57-.01-.2 0-.52.07-.79.37-.27.3-1.04 1.01-1.04 2.47 0 1.46 1.06 2.87 1.21 3.07.15.2 2.09 3.19 5.06 4.47.71.31 1.26.49 1.69.63.71.23 1.36.2 1.87.12.57-.09 1.77-.72 2.02-1.42.25-.7.25-1.3.17-1.42-.07-.13-.27-.2-.57-.35z" />
      </svg>
    </a>
    <a
      v-if="mergedContactInfo.phone"
      :href="`tel:${mergedContactInfo.phone}`"
      class="flex h-14 w-14 items-center justify-center rounded-full bg-accent-green text-white shadow-glow transition-transform hover:scale-110 focus:outline-none focus:ring-2 focus:ring-accent-green focus:ring-offset-2"
      aria-label="Gọi hotline"
    >
      <svg class="h-7 w-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
      </svg>
    </a>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useSiteSettingsStore } from '../stores/siteSettings'

const siteSettingsStore = useSiteSettingsStore()
const { mergedContactInfo } = storeToRefs(siteSettingsStore)

onMounted(async () => {
  await siteSettingsStore.ensureLoaded()
})
</script>
