<template>
  <footer class="mt-20 bg-primary-navy text-[#888]">
    <div class="container-shell grid gap-10 py-14 md:grid-cols-[1.25fr_1fr_1fr] md:gap-12">
      <div class="min-h-[26rem]">
        <div class="flex items-center gap-3">
          <div v-if="logoPath" class="flex h-16 w-[11rem] items-center justify-center">
            <img :src="resolveMediaUrl(logoPath)" :alt="siteName" width="176" height="48" loading="lazy" decoding="async" class="max-h-12 w-full object-contain" />
          </div>
          <div v-else class="flex h-12 w-12 items-center justify-center rounded-lg bg-accent-green text-sm font-bold text-white">
            HS
          </div>
          <div v-if="shortName" class="min-h-[1.75rem] text-lg font-extrabold text-white">
            {{ shortName }}
          </div>
          <div v-else-if="isLoading" class="h-6 w-28 rounded bg-white/10"></div>
        </div>

        <div class="mt-4 min-h-[1.75rem] text-lg font-extrabold text-white">{{ siteName }}</div>
        <div class="mt-2 min-h-[1.5rem] text-sm leading-6 text-neutral-400">
          <strong v-if="companyName" class="font-semibold text-white/90">{{ companyName }}</strong>
          <span v-else-if="isLoading" class="inline-block h-4 w-52 rounded bg-white/10"></span>
        </div>
        <div class="mt-3 min-h-[4.5rem] space-y-1 text-sm leading-6 text-neutral-400">
          <div v-if="licenseNumber || licenseIssuedDate || isLoading" class="flex min-h-[2rem] flex-wrap gap-x-5 gap-y-1">
            <span v-if="licenseNumber"><strong class="text-white/90">Giấy phép số:</strong> {{ licenseNumber }}</span>
            <span v-if="licenseIssuedDate"><strong class="text-white/90">Ngày cấp:</strong> {{ formattedLicenseIssuedDate }}</span>
            <span v-if="isLoading && !licenseNumber" class="inline-block h-4 w-32 rounded bg-white/10"></span>
            <span v-if="isLoading && !licenseIssuedDate" class="inline-block h-4 w-24 rounded bg-white/10"></span>
          </div>
          <div v-if="taxCode || isLoading" class="min-h-[2rem]">
            <template v-if="taxCode"><strong class="text-white/90">Mã số thuế:</strong> {{ taxCode }}</template>
            <span v-else class="inline-block h-4 w-40 rounded bg-white/10"></span>
          </div>
        </div>
        <p class="mt-5 min-h-[6rem] text-sm leading-7 text-neutral-400">
          {{ footerText || 'Khảo sát chính xác - Nền móng vững chắc.' }}
        </p>

        <SocialLinks
          class="mt-6"
          :facebook-url="facebookUrl"
          :youtube-url="youtubeUrl"
          :linkedin-url="linkedinUrl"
          :reserve-space="isLoading || Boolean(facebookUrl || youtubeUrl || linkedinUrl)"
        />
      </div>

      <div class="min-h-[17rem]">
        <h3 class="border-l-4 border-accent-green pl-3 text-base font-bold uppercase text-white">Liên kết nhanh</h3>
        <ul class="mt-5 space-y-3 text-sm text-neutral-400">
          <li><router-link :to="{ name: 'about' }" class="transition-all hover:pl-1 hover:text-accent-green">Giới thiệu</router-link></li>
          <li><router-link :to="{ name: 'services' }" class="transition-all hover:pl-1 hover:text-accent-green">Dịch vụ</router-link></li>
          <li><router-link :to="{ name: 'projects' }" class="transition-all hover:pl-1 hover:text-accent-green">Dự án</router-link></li>
          <li><router-link :to="{ name: 'news' }" class="transition-all hover:pl-1 hover:text-accent-green">Tin tức</router-link></li>
          <li><router-link :to="{ name: 'contact' }" class="transition-all hover:pl-1 hover:text-accent-green">Liên hệ</router-link></li>
        </ul>
      </div>

      <div class="min-h-[17rem]">
        <h3 class="border-l-4 border-accent-green pl-3 text-base font-bold uppercase text-white">Liên hệ</h3>
        <ul class="mt-5 min-h-[13rem] space-y-4 text-sm text-neutral-400">
          <li class="flex min-h-[3.5rem] items-start gap-3">
            <AppIcon icon="location" class="mt-0.5 h-5 w-5 shrink-0 text-accent-green" />
            <span v-if="address">{{ address }}</span>
            <span v-else-if="isLoading" class="mt-1 block h-4 w-full max-w-[16rem] rounded bg-white/10"></span>
          </li>
          <li class="flex min-h-[1.75rem] items-center gap-3">
            <AppIcon icon="phone" class="h-5 w-5 shrink-0 text-accent-green" />
            <a v-if="phone" :href="`tel:${phone}`" class="transition-colors hover:text-accent-green">{{ phone }}</a>
            <span v-else-if="isLoading" class="block h-4 w-32 rounded bg-white/10"></span>
          </li>
          <li class="flex min-h-[1.75rem] items-center gap-3">
            <AppIcon icon="mail" class="h-5 w-5 shrink-0 text-accent-green" />
            <a v-if="email" :href="`mailto:${email}`" class="transition-colors hover:text-accent-green">{{ email }}</a>
            <span v-else-if="isLoading" class="block h-4 w-40 rounded bg-white/10"></span>
          </li>
          <li v-if="workingHours" class="flex min-h-[1.75rem] items-center gap-3">
            <AppIcon icon="clock" class="h-5 w-5 shrink-0 text-accent-green" />
            <span>{{ workingHours }}</span>
          </li>
          <li v-else-if="isLoading" class="flex min-h-[1.75rem] items-center gap-3">
            <AppIcon icon="clock" class="h-5 w-5 shrink-0 text-accent-green" />
            <span class="block h-4 w-40 rounded bg-white/10"></span>
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
import { computed } from 'vue'
import { resolveMediaUrl } from '../../lib/media'
import AppIcon from '../ui/AppIcon.vue'
import SocialLinks from '../ui/SocialLinks.vue'

const props = defineProps<{
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
  isLoading?: boolean
}>()

const formattedLicenseIssuedDate = computed(() => formatDateDdMmYyyy(props.licenseIssuedDate))

function formatDateDdMmYyyy(value: string) {
  if (!value) return ''

  const trimmed = value.trim()
  const isoDateMatch = trimmed.match(/^(\d{4})-(\d{2})-(\d{2})/)
  if (isoDateMatch) {
    return `${isoDateMatch[3]}-${isoDateMatch[2]}-${isoDateMatch[1]}`
  }

  const slashDateMatch = trimmed.match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/)
  if (slashDateMatch) {
    return `${slashDateMatch[1].padStart(2, '0')}-${slashDateMatch[2].padStart(2, '0')}-${slashDateMatch[3]}`
  }

  const parsedDate = new Date(trimmed)
  if (Number.isNaN(parsedDate.getTime())) return trimmed

  const day = String(parsedDate.getDate()).padStart(2, '0')
  const month = String(parsedDate.getMonth() + 1).padStart(2, '0')
  const year = parsedDate.getFullYear()
  return `${day}-${month}-${year}`
}
</script>
