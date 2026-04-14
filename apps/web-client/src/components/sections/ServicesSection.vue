<template>
  <section class="section bg-light">
    <div class="container-shell">
      <SectionHeader :title="title || 'LĨNH VỰC HOẠT ĐỘNG'" centered />

      <div v-if="loading" class="mt-20 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <Card v-for="index in 6" :key="index" class="p-8 text-center">
          <div class="mx-auto mb-5 h-16 w-16 rounded-2xl bg-neutral-100" />
          <div class="mx-auto h-5 w-2/3 rounded bg-neutral-100" />
          <div class="mx-auto mt-4 h-16 w-full rounded bg-neutral-100" />
        </Card>
      </div>

      <div v-else-if="displayServices.length" class="mt-20 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <Card v-for="service in displayServices" :key="service.slug || service.title" class="p-6 text-center hover:border-accent-green/30 hover:shadow-[0_18px_45px_rgba(46,125,50,0.16)]">
          <div class="mx-auto mb-5 flex h-16 w-16 items-center justify-center text-primary-navy">
            <AppIcon :icon="service.icon || 'circle'" class="h-full w-full text-5xl" />
          </div>
          <h3 class="mb-4 text-lg font-bold text-primary-navy">{{ service.title }}</h3>
          <p class="mb-4 text-sm leading-relaxed text-gray-600">{{ service.description }}</p>
          <router-link :to="service.slug ? { name: 'service-detail', params: { slug: service.slug } } : { name: 'services' }" class="inline-flex items-center text-sm font-bold text-accent-green transition-all hover:underline">
            Xem chi tiết
            <AppIcon icon="chevronRight" class="ml-1 h-4 w-4" />
          </router-link>
        </Card>
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
