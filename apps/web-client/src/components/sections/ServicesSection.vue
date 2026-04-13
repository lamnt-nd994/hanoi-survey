<template>
  <section class="section bg-light">
    <div class="container-shell">
      <SectionHeader :eyebrow="eyebrow" :title="title || 'LĨNH VỰC HOẠT ĐỘNG'" :description="description" centered show-divider />

      <div v-if="loading" class="services-grid">
        <div v-for="index in 6" :key="index" class="service-card p-8 text-center">
          <div class="mx-auto mb-5 h-16 w-16 rounded-2xl bg-neutral-100" />
          <div class="mx-auto h-5 w-2/3 rounded bg-neutral-100" />
          <div class="mx-auto mt-4 h-16 w-full rounded bg-neutral-100" />
        </div>
      </div>

      <div v-else-if="displayServices.length" class="services-grid">
        <div v-for="service in displayServices" :key="service.slug || service.title" class="service-card text-center">
          <div class="service-icon">
            <AppIcon :icon="service.icon || 'circle'" class="h-full w-full text-5xl" />
          </div>
          <h3>{{ service.title }}</h3>
          <p>{{ service.description }}</p>
          <router-link :to="service.slug ? { name: 'service-detail', params: { slug: service.slug } } : { name: 'services' }" class="btn-link">
            Xem chi tiết
            <AppIcon icon="chevronRight" class="ml-1 h-4 w-4" />
          </router-link>
        </div>
      </div>

      <div v-else class="panel p-8 text-center text-neutral-500">Chưa có dữ liệu dịch vụ từ hệ thống.</div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import AppIcon from '../ui/AppIcon.vue'
import SectionHeader from '../ui/SectionHeader.vue'
import { usePublicContentStore } from '../../stores/publicContent'
import type { HomeSelectedServiceItem, SurveyService } from '../../types/content'

const props = withDefaults(defineProps<{
  eyebrow?: string
  title?: string
  description?: string
  mode?: 'latest' | 'manual'
  limit?: number
  selectedItems?: HomeSelectedServiceItem[]
}>(), {
  eyebrow: '',
  title: 'Các Dịch Vụ Chính',
  description: '',
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
