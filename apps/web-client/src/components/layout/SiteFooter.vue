<template>
  <footer class="mt-20 bg-primary-navy text-[#888]">
    <div class="container-shell grid gap-10 py-14 md:grid-cols-[1.25fr_1fr_1fr] md:gap-12">
      <div>
        <div class="flex items-center gap-3">
          <div v-if="logoPath" class="flex h-16 w-[11rem] items-center justify-center">
            <img :src="resolveMediaUrl(logoPath)" :alt="siteName" width="176" height="48" loading="lazy" decoding="async" class="max-h-12 w-full object-contain" />
          </div>
          <div v-else class="flex h-12 w-12 items-center justify-center rounded-2xl bg-accent-green text-sm font-bold text-white">
            HS
          </div>
          <div v-if="shortName" class="text-lg font-extrabold text-white">
            {{ shortName }}
          </div>
        </div>

        <div class="mt-4 text-lg font-extrabold text-white">{{ siteName }}</div>
        <div class="mt-2 text-sm leading-6 text-neutral-400">
          <strong class="font-semibold text-white/90">{{ companyName }}</strong>
        </div>
        <div class="mt-3 space-y-1 text-sm leading-6 text-neutral-400">
          <div v-if="licenseNumber || licenseIssuedDate" class="flex flex-wrap gap-x-5 gap-y-1">
            <span v-if="licenseNumber"><strong class="text-white/90">Giấy phép số:</strong> {{ licenseNumber }}</span>
            <span v-if="licenseIssuedDate"><strong class="text-white/90">Ngày cấp:</strong> {{ licenseIssuedDate }}</span>
          </div>
          <div v-if="taxCode"><strong class="text-white/90">Mã số thuế:</strong> {{ taxCode }}</div>
        </div>
        <p class="mt-5 text-sm leading-7 text-neutral-400">
          {{ footerText || 'Khảo sát chính xác - Nền móng vững chắc.' }}
        </p>

        <SocialLinks
          v-if="facebookUrl || youtubeUrl || linkedinUrl"
          class="mt-6"
          :facebook-url="facebookUrl"
          :youtube-url="youtubeUrl"
          :linkedin-url="linkedinUrl"
        />
      </div>

      <div>
        <h3 class="border-l-4 border-accent-green pl-3 text-base font-bold uppercase text-white">Liên kết nhanh</h3>
        <ul class="mt-5 space-y-3 text-sm text-neutral-400">
          <li><router-link :to="{ name: 'about' }" class="transition-all hover:pl-1 hover:text-accent-green">Giới thiệu</router-link></li>
          <li><router-link :to="{ name: 'services' }" class="transition-all hover:pl-1 hover:text-accent-green">Dịch vụ</router-link></li>
          <li><router-link :to="{ name: 'projects' }" class="transition-all hover:pl-1 hover:text-accent-green">Dự án</router-link></li>
          <li><router-link :to="{ name: 'news' }" class="transition-all hover:pl-1 hover:text-accent-green">Tin tức</router-link></li>
          <li><router-link :to="{ name: 'contact' }" class="transition-all hover:pl-1 hover:text-accent-green">Liên hệ</router-link></li>
        </ul>
      </div>

      <div>
        <h3 class="border-l-4 border-accent-green pl-3 text-base font-bold uppercase text-white">Liên hệ</h3>
        <ul class="mt-5 space-y-4 text-sm text-neutral-400">
          <li class="flex items-start gap-3">
            <AppIcon icon="location" class="mt-0.5 h-5 w-5 shrink-0 text-accent-green" />
            <span>{{ address }}</span>
          </li>
          <li class="flex items-center gap-3">
            <AppIcon icon="phone" class="h-5 w-5 shrink-0 text-accent-green" />
            <a :href="`tel:${phone}`" class="transition-colors hover:text-accent-green">{{ phone }}</a>
          </li>
          <li class="flex items-center gap-3">
            <AppIcon icon="mail" class="h-5 w-5 shrink-0 text-accent-green" />
            <a :href="`mailto:${email}`" class="transition-colors hover:text-accent-green">{{ email }}</a>
          </li>
          <li v-if="workingHours" class="flex items-center gap-3">
            <AppIcon icon="clock" class="h-5 w-5 shrink-0 text-accent-green" />
            <span>{{ workingHours }}</span>
          </li>
        </ul>
      </div>
    </div>

    <div class="border-t border-white/10 px-4 py-6">
      <div class="container-shell text-center text-sm text-neutral-500">
        <p>&copy; 2026 HANOI CSC. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { resolveMediaUrl } from '../../lib/media'
import AppIcon from '../ui/AppIcon.vue'
import SocialLinks from '../ui/SocialLinks.vue'

defineProps<{
  siteName: string
  shortName: string
  companyName: string
  logoPath: string
  footerText: string
  address: string
  email: string
  phone: string
  workingHours: string
  licenseNumber: string
  taxCode: string
  licenseIssuedDate: string
  facebookUrl: string
  youtubeUrl: string
  linkedinUrl: string
}>()
</script>
