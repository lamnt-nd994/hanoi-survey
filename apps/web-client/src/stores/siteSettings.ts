import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { fetchSiteSettings } from '../lib/api'
import type { SiteSettings } from '../types/content'

export const useSiteSettingsStore = defineStore('site-settings', () => {
  const settings = ref<SiteSettings | null>(null)
  const loading = ref(false)
  const loaded = ref(false)

  const mergedContactInfo = computed(() => ({
    phone: settings.value?.phone || settings.value?.hotline || '',
    email: settings.value?.email || '',
    address: settings.value?.address || settings.value?.officeAddress || '',
    officeAddress: settings.value?.officeAddress || settings.value?.address || '',
    mapEmbed: settings.value?.mapEmbed || '',
    workingHours: settings.value?.workingHours || '',
    facebookUrl: settings.value?.facebookUrl || '',
    zaloUrl: settings.value?.zaloUrl || '',
    youtubeUrl: settings.value?.youtubeUrl || '',
    linkedinUrl: settings.value?.linkedinUrl || '',
  }))

  const siteName = computed(() => settings.value?.siteName || 'Hanoi Survey')
  const companyNameEn = computed(() => settings.value?.companyNameEn || '')
  const companyTradeName = computed(() => settings.value?.companyTradeName || '')
  const shortName = computed(() => settings.value?.shortName || settings.value?.siteName || 'Hanoi Survey')
  const slogan = computed(() => settings.value?.slogan || '')
  const logoPath = computed(() => settings.value?.logoPath || '')
  const footerLogoPath = computed(() => settings.value?.footerLogoPath || settings.value?.logoPath || '')
  const heroBannerPath = computed(() => settings.value?.heroBannerPath || '')
  const companyIntro = computed(() => settings.value?.companyIntro || '')
  const businessLicensePath = computed(() => settings.value?.businessLicensePath || '')
  const footerText = computed(() => settings.value?.footerText || '')
  const website = computed(() => settings.value?.website || '')
  const representativeName = computed(() => settings.value?.representativeName || '')
  const representativeTitle = computed(() => settings.value?.representativeTitle || '')
  const establishmentInfo = computed(() => settings.value?.establishmentInfo || '')

  async function ensureLoaded(force = false) {
    if (loading.value) return
    if (loaded.value && !force) return

    loading.value = true
    try {
      settings.value = await fetchSiteSettings()
      loaded.value = true
    } catch {
      settings.value = null
      loaded.value = true
    } finally {
      loading.value = false
    }
  }

  return {
    settings,
    loading,
    loaded,
    mergedContactInfo,
    siteName,
    companyNameEn,
    companyTradeName,
    shortName,
    slogan,
    logoPath,
    footerLogoPath,
    heroBannerPath,
    companyIntro,
    businessLicensePath,
    footerText,
    website,
    representativeName,
    representativeTitle,
    establishmentInfo,
    ensureLoaded,
  }
})
