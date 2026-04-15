<template>
  <div class="space-y-6">
    <Card>
      <CardContent class="p-4">
    <details open>
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Hero</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="space-y-2 md:col-span-2">
          <label class="text-sm font-medium leading-none text-slate-700">Tiêu đề</label>
          <Input v-model="homeContent.hero.title" placeholder="Khảo sát chính xác - Nền móng vững chắc" />
          <div class="mt-1 text-xs text-slate-500">Dùng <code>[ ]</code> để tô nổi bật, ví dụ: <code>Dữ liệu [tin cậy] - nền móng [vững bền]</code></div>
        </div>
        <div class="space-y-2 md:col-span-2">
          <label class="text-sm font-medium leading-none text-slate-700">Phụ đề</label>
          <Input v-model="homeContent.hero.subtitle" placeholder="Ha Noi Construction Survey Consultant J.S.C" />
        </div>
        <div class="space-y-2 md:col-span-2">
          <label class="text-sm font-medium leading-none text-slate-700">Ảnh nền</label>
          <div class="relative">
            <Input v-model="homeContent.hero.backgroundImagePath" class="pr-36" placeholder="media/home-hero.jpg" />
            <Button type="button" variant="secondary" size="sm" class="input-action-btn" :disabled="uploadingHomeHeroImage" @click="emit('pick-home-hero')">
              {{ uploadingHomeHeroImage ? `Đang tải ${homeHeroUploadProgress}%` : 'Chọn ảnh' }}
            </Button>
          </div>
        </div>
      </div>

      <div class="mt-4">
        <div class="flex items-center justify-between gap-3">
          <h3 class="text-sm font-semibold text-slate-800">Badge dịch vụ</h3>
          <Button type="button" variant="secondary" @click="homeContent.hero.badges.push(createHomeBadgeItem())">+ Thêm badge</Button>
        </div>
        <div class="mt-3 space-y-3">
          <div v-for="(item, index) in homeContent.hero.badges" :key="`hero-badge-${index}`" class="rounded-lg border border-gray-200 p-3">
            <div class="flex items-center justify-between gap-3">
              <div class="text-sm font-medium text-slate-700">Badge {{ index + 1 }}</div>
              <ActionButton variant="destructive" @click="removeItem(homeContent.hero.badges, index)"><Trash2 class="h-4 w-4" /></ActionButton>
            </div>
            <div class="mt-3 grid gap-4 md:grid-cols-2">
              <div class="space-y-2">
                <label class="text-sm font-medium leading-none text-slate-700">Nhãn</label>
                <Input v-model="item.label" placeholder="Địa hình" />
              </div>
              <div class="space-y-2">
                <label class="text-sm font-medium leading-none text-slate-700">Icon</label>
                <Input v-model="item.iconKey" placeholder="fas fa-map-marked-alt hoặc map-pinned" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <FormField><FormLabel>Nút chính - nhãn</FormLabel><Input v-model="homeContent.hero.primaryCtaLabel" placeholder="Xem dự án" /></FormField>
        <FormField><FormLabel>Nút chính - URL</FormLabel><Input v-model="homeContent.hero.primaryCtaUrl" placeholder="/du-an" /></FormField>
        <FormField><FormLabel>Nút phụ - nhãn</FormLabel><Input v-model="homeContent.hero.secondaryCtaLabel" placeholder="Liên hệ" /></FormField>
        <FormField><FormLabel>Nút phụ - URL</FormLabel><Input v-model="homeContent.hero.secondaryCtaUrl" placeholder="/lien-he" /></FormField>
      </div>
    </details>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
    <details open>
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Số liệu nổi bật</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 flex justify-end">
        <Button type="button" variant="secondary" @click="homeContent.stats.items.push(createHomeStatItem())">+ Thêm số liệu</Button>
      </div>
      <div class="mt-4 space-y-3">
        <div v-for="(item, index) in homeContent.stats.items" :key="`stat-${index}`" class="rounded-lg border border-gray-200 p-3">
          <div class="flex items-center justify-between gap-3">
            <div class="text-sm font-medium text-slate-700">Số liệu {{ index + 1 }}</div>
            <ActionButton variant="destructive" @click="removeItem(homeContent.stats.items, index)"><Trash2 class="h-4 w-4" /></ActionButton>
          </div>
          <div class="mt-3 grid gap-4 md:grid-cols-2">
            <FormField><FormLabel>Giá trị</FormLabel><Input v-model="item.value" placeholder="10+" /></FormField>
            <FormField><FormLabel>Nhãn</FormLabel><Input v-model="item.label" placeholder="Năm kinh nghiệm" /></FormField>
          </div>
        </div>
      </div>
    </details>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
    <details open>
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Về chúng tôi</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <FormField><FormLabel>Eyebrow</FormLabel><Input v-model="homeContent.aboutSection.eyebrow" placeholder="Về chúng tôi" /></FormField>
        <div class="space-y-2">
          <label class="text-sm font-medium leading-none text-slate-700">Ảnh minh họa</label>
          <div class="relative">
            <Input v-model="homeContent.aboutSection.imagePath" class="pr-36" placeholder="media/home-about.jpg" />
            <Button type="button" variant="secondary" size="sm" class="input-action-btn" :disabled="uploadingHomeAboutImage" @click="emit('pick-home-about')">
              {{ uploadingHomeAboutImage ? `Đang tải ${homeAboutUploadProgress}%` : 'Chọn ảnh' }}
            </Button>
          </div>
        </div>
        <FormField class="md:col-span-2"><FormLabel>Tiêu đề</FormLabel><Input v-model="homeContent.aboutSection.title" placeholder="Đơn vị khảo sát xây dựng tập trung vào độ chính xác và tiến độ thực địa" /></FormField>
        <FormField class="md:col-span-2"><FormLabel>Mô tả</FormLabel><Textarea v-model="homeContent.aboutSection.description" rows="4" placeholder="Mô tả ngắn về doanh nghiệp" /></FormField>
        <FormField><FormLabel>Nhãn nút</FormLabel><Input v-model="homeContent.aboutSection.buttonLabel" placeholder="Xem hồ sơ năng lực" /></FormField>
        <FormField><FormLabel>URL nút</FormLabel><Input v-model="homeContent.aboutSection.buttonUrl" placeholder="/gioi-thieu" /></FormField>
      </div>
      <div class="mt-4">
        <div class="flex items-center justify-between gap-3">
          <h3 class="text-sm font-semibold text-slate-800">Điểm nhấn</h3>
          <Button type="button" variant="secondary" @click="homeContent.aboutSection.highlights.push('')">+ Thêm ý</Button>
        </div>
        <div class="mt-3 space-y-3">
          <div v-for="(item, index) in homeContent.aboutSection.highlights" :key="`about-highlight-${index}`" class="rounded-lg border border-gray-200 p-3">
            <div class="flex items-center justify-between gap-3">
              <div class="text-sm font-medium text-slate-700">Ý {{ index + 1 }}</div>
              <ActionButton variant="destructive" @click="removeItem(homeContent.aboutSection.highlights, index)"><Trash2 class="h-4 w-4" /></ActionButton>
            </div>
            <Textarea v-model="homeContent.aboutSection.highlights[index]" class="mt-3" rows="3" placeholder="Nội dung điểm nhấn" />
          </div>
        </div>
      </div>
    </details>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
    <details open>
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Khối dịch vụ</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <FormField><FormLabel>Eyebrow</FormLabel><Input v-model="homeContent.servicesSection.eyebrow" placeholder="Lĩnh vực" /></FormField>
        <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Chế độ hiển thị</label><Select v-model="homeContent.servicesSection.mode"><option value="latest">Mới nhất theo số lượng</option><option value="manual">Chọn thủ công</option></Select></div>
        <FormField class="md:col-span-2"><FormLabel>Tiêu đề</FormLabel><Input v-model="homeContent.servicesSection.title" placeholder="Các Dịch Vụ Chính" /></FormField>
        <FormField class="md:col-span-2"><FormLabel>Mô tả</FormLabel><Textarea v-model="homeContent.servicesSection.description" rows="3" placeholder="Mô tả ngắn section dịch vụ" /></FormField>
      </div>
      <FormField v-if="homeContent.servicesSection.mode === 'latest'" class="mt-4 max-w-xs"><FormLabel>Số lượng hiển thị</FormLabel><Input v-model.number="homeContent.servicesSection.limit" type="number" min="1" /></FormField>
      <div v-else class="mt-4">
        <div class="flex items-center justify-between gap-3"><h3 class="text-sm font-semibold text-slate-800">Dịch vụ được chọn</h3><Button type="button" variant="secondary" @click="homeContent.servicesSection.selectedItems.push(createSelectedServiceItem())">+ Thêm dịch vụ</Button></div>
        <div class="mt-3 space-y-3">
          <div v-for="(item, index) in homeContent.servicesSection.selectedItems" :key="`selected-service-${index}`" class="rounded-lg border border-gray-200 p-3">
            <div class="flex items-center justify-between gap-3"><div class="text-sm font-medium text-slate-700">Dịch vụ {{ index + 1 }}</div><ActionButton variant="destructive" @click="removeItem(homeContent.servicesSection.selectedItems, index)"><Trash2 class="h-4 w-4" /></ActionButton></div>
            <div class="mt-3 grid gap-4 md:grid-cols-2">
              <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Chọn dịch vụ</label><Select v-model.number="item.serviceId"><option :value="null">-- Chọn dịch vụ --</option><option v-for="service in availableServices" :key="service.id" :value="service.id">{{ service.title }}</option></Select></div>
              <FormField><FormLabel>Icon</FormLabel><Input v-model="item.icon" placeholder="fas fa-map-marked-alt hoặc map-pinned" /></FormField>
            </div>
          </div>
        </div>
      </div>
    </details>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
    <details open>
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Khối dự án</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <FormField><FormLabel>Eyebrow</FormLabel><Input v-model="homeContent.projectsSection.eyebrow" placeholder="Dự án" /></FormField>
        <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Chế độ hiển thị</label><Select v-model="homeContent.projectsSection.mode"><option value="latest">Mới nhất theo số lượng</option><option value="manual">Chọn thủ công</option></Select></div>
        <FormField class="md:col-span-2"><FormLabel>Tiêu đề</FormLabel><Input v-model="homeContent.projectsSection.title" placeholder="Dự Án Tiêu Biểu" /></FormField>
        <FormField class="md:col-span-2"><FormLabel>Mô tả</FormLabel><Textarea v-model="homeContent.projectsSection.description" rows="3" placeholder="Mô tả ngắn section dự án" /></FormField>
      </div>
      <FormField v-if="homeContent.projectsSection.mode === 'latest'" class="mt-4 max-w-xs"><FormLabel>Số lượng hiển thị</FormLabel><Input v-model.number="homeContent.projectsSection.limit" type="number" min="1" /></FormField>
      <div v-else class="mt-4">
        <div class="flex items-center justify-between gap-3"><h3 class="text-sm font-semibold text-slate-800">Dự án được chọn</h3><Button type="button" variant="secondary" @click="homeContent.projectsSection.selectedItems.push(createSelectedProjectItem())">+ Thêm dự án</Button></div>
        <div class="mt-3 space-y-3">
          <div v-for="(item, index) in homeContent.projectsSection.selectedItems" :key="`selected-project-${index}`" class="rounded-lg border border-gray-200 p-3">
            <div class="flex items-center justify-between gap-3"><div class="text-sm font-medium text-slate-700">Dự án {{ index + 1 }}</div><ActionButton variant="destructive" @click="removeItem(homeContent.projectsSection.selectedItems, index)"><Trash2 class="h-4 w-4" /></ActionButton></div>
            <div class="mt-3 space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Chọn dự án</label><Select v-model.number="item.projectId"><option :value="null">-- Chọn dự án --</option><option v-for="project in availableProjects" :key="project.id" :value="project.id">{{ project.title }}</option></Select></div>
          </div>
        </div>
      </div>
    </details>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
    <details open>
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">CTA cuối trang</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 flex justify-end"><label class="flex items-center gap-2 text-sm text-slate-700"><input v-model="homeContent.finalCta.showPhoneButton" type="checkbox" />Hiện nút gọi điện</label></div>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <FormField class="md:col-span-2"><FormLabel>Eyebrow</FormLabel><Input v-model="homeContent.finalCta.eyebrow" placeholder="Tư vấn nhanh ngoài hiện trường" /></FormField>
        <FormField class="md:col-span-2"><FormLabel>Tiêu đề</FormLabel><Input v-model="homeContent.finalCta.title" placeholder="Sẵn sàng khảo sát cho dự án mới?" /></FormField>
        <FormField class="md:col-span-2"><FormLabel>Mô tả</FormLabel><Textarea v-model="homeContent.finalCta.description" rows="3" placeholder="Mô tả CTA cuối trang" /></FormField>
        <FormField><FormLabel>Nút chính - nhãn</FormLabel><Input v-model="homeContent.finalCta.primaryButtonLabel" placeholder="Xem dự án" /></FormField>
        <FormField><FormLabel>Nút chính - URL</FormLabel><Input v-model="homeContent.finalCta.primaryButtonUrl" placeholder="/du-an" /></FormField>
        <FormField><FormLabel>Nút phụ - nhãn</FormLabel><Input v-model="homeContent.finalCta.secondaryButtonLabel" placeholder="Liên hệ" /></FormField>
        <FormField><FormLabel>Nút phụ - URL</FormLabel><Input v-model="homeContent.finalCta.secondaryButtonUrl" placeholder="/lien-he" /></FormField>
      </div>
    </details>
      </CardContent>
    </Card>

    <Card class="border-dashed border-gray-300 bg-gray-50">
      <CardContent class="p-4">
      <div class="text-sm font-medium text-slate-700">Preview contentJson</div>
      <Textarea :model-value="serializedHomeContent" class="mt-3 font-mono text-xs min-h-[260px]" rows="12" readonly />
      </CardContent>
    </Card>
  </div>
</template>

<script setup lang="ts">
import ActionButton from '@/components/shared/ActionButton.vue'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import { Trash2 } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Select } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import type { HomeHeroBadgeItem, HomePageContent, HomeSelectedProjectItem, HomeSelectedServiceItem, HomeStatItem, ProjectEntity, ServiceEntity } from '@/types'

defineProps<{
  homeContent: HomePageContent
  availableServices: ServiceEntity[]
  availableProjects: ProjectEntity[]
  serializedHomeContent: string
  uploadingHomeHeroImage: boolean
  homeHeroUploadProgress: number
  uploadingHomeAboutImage: boolean
  homeAboutUploadProgress: number
}>()

const emit = defineEmits<{
  (e: 'pick-home-hero'): void
  (e: 'pick-home-about'): void
}>()

function removeItem<T>(items: T[], index: number) {
  items.splice(index, 1)
}

function createHomeBadgeItem(): HomeHeroBadgeItem {
  return { label: '', iconKey: 'terrain' }
}

function createHomeStatItem(): HomeStatItem {
  return { value: '', label: '' }
}

function createSelectedServiceItem(): HomeSelectedServiceItem {
  return { serviceId: null, icon: '' }
}

function createSelectedProjectItem(): HomeSelectedProjectItem {
  return { projectId: null }
}
</script>
