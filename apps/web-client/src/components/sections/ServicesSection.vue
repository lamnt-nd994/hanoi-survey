<template>
  <section class="section bg-light">
    <div class="container-shell">
      <SectionHeader :title="title || 'LĨNH VỰC HOẠT ĐỘNG'" centered />

      <div v-if="loading" class="mt-20 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <Card v-for="index in 6" :key="index" class="rounded-lg p-5 shadow-sm">
          <div class="flex items-start justify-between">
            <div class="h-4 w-28 rounded bg-neutral-100" />
            <div class="h-9 w-9 rounded-lg bg-neutral-100" />
          </div>
          <div class="mt-5 h-6 w-2/3 rounded bg-neutral-100" />
          <div class="mt-4 h-16 w-full rounded bg-neutral-100" />
        </Card>
      </div>

      <div v-else-if="displayServices.length" class="mt-20 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <router-link
          v-for="(service, index) in displayServices"
          :key="service.slug || service.title"
          :to="service.slug ? { name: 'service-detail', params: { slug: service.slug } } : { name: 'services' }"
          class="group flex h-full flex-col rounded-lg border border-neutral-200 bg-white p-5 shadow-sm transition-colors duration-200 hover:border-accent-green/50"
        >
          <div class="flex items-start justify-between gap-4">
            <span class="text-[11px] font-semibold uppercase tracking-[0.14em] text-accent-green">Lĩnh vực khảo sát</span>
            <span class="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-accent-green/10 text-primary-navy">
              <AppIcon :icon="service.icon || 'circle'" class="h-5 w-5" />
            </span>
          </div>
          <h3 class="mt-4 font-heading text-xl font-bold leading-tight text-primary-navy">{{ service.title }}</h3>
          <p class="mt-3 text-sm leading-7 text-neutral-600">{{ service.description }}</p>
          <span class="mt-auto inline-flex items-center gap-2 pt-5 text-sm font-semibold text-primary-navy">
            Xem chi tiết
            <AppIcon icon="chevronRight" class="h-4 w-4 text-accent-green" />
          </span>
        </router-link>
      </div>

      <Card v-else class="p-8 text-center text-neutral-500">Chưa có dữ liệu dịch vụ từ hệ thống.</Card>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { Card } from '../ui/card'
import AppIcon from '../ui/AppIcon.vue'
import SectionHeader from '../ui/SectionHeader.vue'
import { usePublicContentStore } from '../../stores/publicContent'
import type { HomeSelectedServiceItem, SurveyService } from '../../types/content'

const props = withDefaults(defineProps<{
  title?: string
  mode?: 'latest' | 'manual'
  limit?: number
  selectedItems?: HomeSelectedServiceItem[]
}>(), {
  title: 'Các Dịch Vụ Chính',
  mode: 'latest',
  limit: 6,
  selectedItems: () => [],
})

const publicContentStore = usePublicContentStore()
const displayServices = computed(() => {
  if (props.mode !== 'manual') {
    return [...publicContentStore.services]
      .slice(0, props.limit)
      .map((service) => ({
        ...service,
        icon: service.icon || 'circle',
        description: resolveServiceDescription(service),
      }))
  }

  return props.selectedItems
    .map((selected) => {
      const service = publicContentStore.services.find((item) => item.id === selected.serviceId)
        || publicContentStore.services.find((item) => selected.categoryId != null && item.categoryId === selected.categoryId)
      if (!service) return null
      return {
        ...service,
        icon: selected.icon || service.icon || 'circle',
        description: resolveServiceDescription(service),
      }
    })
    .filter((service): service is NonNullable<typeof service> => service !== null)
})

const loading = computed(() => publicContentStore.loading.services)

onMounted(async () => {
  await publicContentStore.loadServices()
})

function resolveServiceDescription(service: SurveyService) {
  return service.overview || 'Dịch vụ được cập nhật trực tiếp từ hệ thống nội dung.'
}
</script>
