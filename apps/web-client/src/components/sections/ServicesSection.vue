<template>
  <section class="section bg-light">
    <div class="container-shell">
      <SectionHeader :eyebrow="eyebrow" :title="title || 'Các Dịch Vụ Chính'" :description="description" centered show-divider />

      <div v-if="loading" class="services-grid">
        <div v-for="index in 6" :key="index" class="service-card p-8 text-center">
          <div class="mx-auto mb-5 h-16 w-16 rounded-2xl bg-neutral-100" />
          <div class="mx-auto h-5 w-2/3 rounded bg-neutral-100" />
          <div class="mx-auto mt-4 h-16 w-full rounded bg-neutral-100" />
        </div>
      </div>

      <div v-else-if="displayCategories.length" class="services-grid">
        <div v-for="category in displayCategories" :key="category.slug || category.name" class="service-card text-center">
          <div class="service-icon">
            <AppIcon :icon="category.icon || 'circle'" class="h-full w-full text-5xl" />
          </div>
          <h3>{{ category.name }}</h3>
          <p>{{ category.description }}</p>
          <router-link :to="category.slug ? { name: 'service-detail', params: { slug: category.slug } } : { name: 'services' }" class="btn-link">
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
import type { HomeSelectedServiceItem, ServiceCategory } from '../../types/content'

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
const displayCategories = computed(() => {
  if (props.mode !== 'manual') {
    return [...publicContentStore.serviceCategories]
      .sort((a, b) => a.sortOrder - b.sortOrder)
      .slice(0, props.limit)
      .map((category) => ({
        ...category,
        icon: resolveCategoryIcon(category),
        description: resolveCategoryDescription(category),
      }))
  }

  return props.selectedItems
    .map((selected) => {
      const category = publicContentStore.serviceCategories.find((item) => item.id === selected.categoryId)
      if (!category) return null
      return {
        ...category,
        icon: selected.icon || 'circle',
        description: resolveCategoryDescription(category),
      }
    })
    .filter((category): category is NonNullable<typeof category> => category !== null)
})

const loading = computed(() => publicContentStore.loading.serviceCategories || publicContentStore.loading.services)

onMounted(async () => {
  await Promise.all([
    publicContentStore.loadServiceCategories(),
    publicContentStore.loadServices(),
  ])
})

function resolveCategoryDescription(category: ServiceCategory) {
  const firstService = publicContentStore.services.find((item) => item.categoryId === category.id && item.overview?.trim())
  return firstService?.overview || `Khám phá các hạng mục thuộc danh mục ${category.name.toLowerCase()} với nội dung được quản trị động từ CMS.`
}

function resolveCategoryIcon(category: ServiceCategory) {
  const firstService = publicContentStore.services.find((item) => item.categoryId === category.id && item.icon?.trim())
  return firstService?.icon || 'circle'
}
</script>
