<template>
  <div class="space-y-6">
    <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Hero</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="cms-form-group md:col-span-2">
          <label class="cms-form-label">Tiêu đề</label>
          <input v-model="homeContent.hero.title" class="cms-form-control" placeholder="Khảo sát chính xác - Nền móng vững chắc" />
          <div class="mt-1 text-xs text-slate-500">Dùng <code>[ ]</code> để tô nổi bật, ví dụ: <code>Dữ liệu [tin cậy] - nền móng [vững bền]</code></div>
        </div>
        <div class="cms-form-group md:col-span-2">
          <label class="cms-form-label">Phụ đề</label>
          <input v-model="homeContent.hero.subtitle" class="cms-form-control" placeholder="Ha Noi Construction Survey Consultant J.S.C" />
        </div>
        <div class="cms-form-group md:col-span-2">
          <label class="cms-form-label">Ảnh nền</label>
          <div class="relative">
            <input v-model="homeContent.hero.backgroundImagePath" class="cms-form-control pr-36" placeholder="media/home-hero.jpg" />
            <button type="button" class="input-action-btn" :disabled="uploadingHomeHeroImage" @click="emit('pick-home-hero')">
              {{ uploadingHomeHeroImage ? `Đang tải ${homeHeroUploadProgress}%` : 'Chọn ảnh' }}
            </button>
          </div>
        </div>
      </div>

      <div class="mt-4">
        <div class="flex items-center justify-between gap-3">
          <h3 class="text-sm font-semibold text-slate-800">Badge dịch vụ</h3>
          <button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.hero.badges.push(createHomeBadgeItem())">+ Thêm badge</button>
        </div>
        <div class="mt-3 space-y-3">
          <div v-for="(item, index) in homeContent.hero.badges" :key="`hero-badge-${index}`" class="rounded-lg border border-gray-200 p-3">
            <div class="flex items-center justify-between gap-3">
              <div class="text-sm font-medium text-slate-700">Badge {{ index + 1 }}</div>
              <button type="button" class="cms-act-btn delete" @click="removeItem(homeContent.hero.badges, index)">&#10005;</button>
            </div>
            <div class="mt-3 grid gap-4 md:grid-cols-2">
              <div class="cms-form-group">
                <label class="cms-form-label">Nhãn</label>
                <input v-model="item.label" class="cms-form-control" placeholder="Địa hình" />
              </div>
              <div class="cms-form-group">
                <label class="cms-form-label">Icon</label>
                <input v-model="item.iconKey" class="cms-form-control" placeholder="fas fa-map-marked-alt hoặc map-pinned" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="cms-form-group"><label class="cms-form-label">Nút chính - nhãn</label><input v-model="homeContent.hero.primaryCtaLabel" class="cms-form-control" placeholder="Xem dự án" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Nút chính - URL</label><input v-model="homeContent.hero.primaryCtaUrl" class="cms-form-control" placeholder="/du-an" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Nút phụ - nhãn</label><input v-model="homeContent.hero.secondaryCtaLabel" class="cms-form-control" placeholder="Liên hệ" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Nút phụ - URL</label><input v-model="homeContent.hero.secondaryCtaUrl" class="cms-form-control" placeholder="/lien-he" /></div>
      </div>
    </details>

    <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Số liệu nổi bật</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 flex justify-end">
        <button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.stats.items.push(createHomeStatItem())">+ Thêm số liệu</button>
      </div>
      <div class="mt-4 space-y-3">
        <div v-for="(item, index) in homeContent.stats.items" :key="`stat-${index}`" class="rounded-lg border border-gray-200 p-3">
          <div class="flex items-center justify-between gap-3">
            <div class="text-sm font-medium text-slate-700">Số liệu {{ index + 1 }}</div>
            <button type="button" class="cms-act-btn delete" @click="removeItem(homeContent.stats.items, index)">&#10005;</button>
          </div>
          <div class="mt-3 grid gap-4 md:grid-cols-2">
            <div class="cms-form-group"><label class="cms-form-label">Giá trị</label><input v-model="item.value" class="cms-form-control" placeholder="10+" /></div>
            <div class="cms-form-group"><label class="cms-form-label">Nhãn</label><input v-model="item.label" class="cms-form-control" placeholder="Năm kinh nghiệm" /></div>
          </div>
        </div>
      </div>
    </details>

    <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Về chúng tôi</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="cms-form-group"><label class="cms-form-label">Eyebrow</label><input v-model="homeContent.aboutSection.eyebrow" class="cms-form-control" placeholder="Về chúng tôi" /></div>
        <div class="cms-form-group">
          <label class="cms-form-label">Ảnh minh họa</label>
          <div class="relative">
            <input v-model="homeContent.aboutSection.imagePath" class="cms-form-control pr-36" placeholder="media/home-about.jpg" />
            <button type="button" class="input-action-btn" :disabled="uploadingHomeAboutImage" @click="emit('pick-home-about')">
              {{ uploadingHomeAboutImage ? `Đang tải ${homeAboutUploadProgress}%` : 'Chọn ảnh' }}
            </button>
          </div>
        </div>
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Tiêu đề</label><input v-model="homeContent.aboutSection.title" class="cms-form-control" placeholder="Đơn vị khảo sát xây dựng tập trung vào độ chính xác và tiến độ thực địa" /></div>
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Mô tả</label><textarea v-model="homeContent.aboutSection.description" class="cms-form-control" rows="4" placeholder="Mô tả ngắn về doanh nghiệp" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Nhãn nút</label><input v-model="homeContent.aboutSection.buttonLabel" class="cms-form-control" placeholder="Xem hồ sơ năng lực" /></div>
        <div class="cms-form-group"><label class="cms-form-label">URL nút</label><input v-model="homeContent.aboutSection.buttonUrl" class="cms-form-control" placeholder="/gioi-thieu" /></div>
      </div>
      <div class="mt-4">
        <div class="flex items-center justify-between gap-3">
          <h3 class="text-sm font-semibold text-slate-800">Điểm nhấn</h3>
          <button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.aboutSection.highlights.push('')">+ Thêm ý</button>
        </div>
        <div class="mt-3 space-y-3">
          <div v-for="(item, index) in homeContent.aboutSection.highlights" :key="`about-highlight-${index}`" class="rounded-lg border border-gray-200 p-3">
            <div class="flex items-center justify-between gap-3">
              <div class="text-sm font-medium text-slate-700">Ý {{ index + 1 }}</div>
              <button type="button" class="cms-act-btn delete" @click="removeItem(homeContent.aboutSection.highlights, index)">&#10005;</button>
            </div>
            <textarea v-model="homeContent.aboutSection.highlights[index]" class="cms-form-control mt-3" rows="3" placeholder="Nội dung điểm nhấn" />
          </div>
        </div>
      </div>
    </details>

    <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Khối dịch vụ</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="cms-form-group"><label class="cms-form-label">Eyebrow</label><input v-model="homeContent.servicesSection.eyebrow" class="cms-form-control" placeholder="Lĩnh vực" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Chế độ hiển thị</label><select v-model="homeContent.servicesSection.mode" class="cms-form-control"><option value="latest">Mới nhất theo số lượng</option><option value="manual">Chọn thủ công</option></select></div>
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Tiêu đề</label><input v-model="homeContent.servicesSection.title" class="cms-form-control" placeholder="Các Dịch Vụ Chính" /></div>
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Mô tả</label><textarea v-model="homeContent.servicesSection.description" class="cms-form-control" rows="3" placeholder="Mô tả ngắn section dịch vụ" /></div>
      </div>
      <div v-if="homeContent.servicesSection.mode === 'latest'" class="mt-4 cms-form-group" style="max-width: 20rem;"><label class="cms-form-label">Số lượng hiển thị</label><input v-model.number="homeContent.servicesSection.limit" type="number" min="1" class="cms-form-control" /></div>
      <div v-else class="mt-4">
        <div class="flex items-center justify-between gap-3"><h3 class="text-sm font-semibold text-slate-800">Dịch vụ được chọn</h3><button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.servicesSection.selectedItems.push(createSelectedServiceItem())">+ Thêm dịch vụ</button></div>
        <div class="mt-3 space-y-3">
          <div v-for="(item, index) in homeContent.servicesSection.selectedItems" :key="`selected-service-${index}`" class="rounded-lg border border-gray-200 p-3">
            <div class="flex items-center justify-between gap-3"><div class="text-sm font-medium text-slate-700">Dịch vụ {{ index + 1 }}</div><button type="button" class="cms-act-btn delete" @click="removeItem(homeContent.servicesSection.selectedItems, index)">&#10005;</button></div>
            <div class="mt-3 grid gap-4 md:grid-cols-2">
              <div class="cms-form-group"><label class="cms-form-label">Chọn dịch vụ</label><select v-model.number="item.serviceId" class="cms-form-control"><option :value="null">-- Chọn dịch vụ --</option><option v-for="service in availableServices" :key="service.id" :value="service.id">{{ service.title }}</option></select></div>
              <div class="cms-form-group"><label class="cms-form-label">Icon</label><input v-model="item.icon" class="cms-form-control" placeholder="fas fa-map-marked-alt hoặc map-pinned" /></div>
            </div>
          </div>
        </div>
      </div>
    </details>

    <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Khối dự án</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="cms-form-group"><label class="cms-form-label">Eyebrow</label><input v-model="homeContent.projectsSection.eyebrow" class="cms-form-control" placeholder="Dự án" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Chế độ hiển thị</label><select v-model="homeContent.projectsSection.mode" class="cms-form-control"><option value="latest">Mới nhất theo số lượng</option><option value="manual">Chọn thủ công</option></select></div>
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Tiêu đề</label><input v-model="homeContent.projectsSection.title" class="cms-form-control" placeholder="Dự Án Tiêu Biểu" /></div>
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Mô tả</label><textarea v-model="homeContent.projectsSection.description" class="cms-form-control" rows="3" placeholder="Mô tả ngắn section dự án" /></div>
      </div>
      <div v-if="homeContent.projectsSection.mode === 'latest'" class="mt-4 cms-form-group" style="max-width: 20rem;"><label class="cms-form-label">Số lượng hiển thị</label><input v-model.number="homeContent.projectsSection.limit" type="number" min="1" class="cms-form-control" /></div>
      <div v-else class="mt-4">
        <div class="flex items-center justify-between gap-3"><h3 class="text-sm font-semibold text-slate-800">Dự án được chọn</h3><button type="button" class="cms-btn cms-btn-secondary" @click="homeContent.projectsSection.selectedItems.push(createSelectedProjectItem())">+ Thêm dự án</button></div>
        <div class="mt-3 space-y-3">
          <div v-for="(item, index) in homeContent.projectsSection.selectedItems" :key="`selected-project-${index}`" class="rounded-lg border border-gray-200 p-3">
            <div class="flex items-center justify-between gap-3"><div class="text-sm font-medium text-slate-700">Dự án {{ index + 1 }}</div><button type="button" class="cms-act-btn delete" @click="removeItem(homeContent.projectsSection.selectedItems, index)">&#10005;</button></div>
            <div class="mt-3 cms-form-group"><label class="cms-form-label">Chọn dự án</label><select v-model.number="item.projectId" class="cms-form-control"><option :value="null">-- Chọn dự án --</option><option v-for="project in availableProjects" :key="project.id" :value="project.id">{{ project.title }}</option></select></div>
          </div>
        </div>
      </div>
    </details>

    <details open class="cms-collapsible rounded-xl border border-gray-200 p-4">
      <summary class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">CTA cuối trang</h2>
        <svg class="collapse-icon h-5 w-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </summary>
      <div class="mt-4 flex justify-end"><label class="flex items-center gap-2 text-sm text-slate-700"><input v-model="homeContent.finalCta.showPhoneButton" type="checkbox" />Hiện nút gọi điện</label></div>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Eyebrow</label><input v-model="homeContent.finalCta.eyebrow" class="cms-form-control" placeholder="Tư vấn nhanh ngoài hiện trường" /></div>
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Tiêu đề</label><input v-model="homeContent.finalCta.title" class="cms-form-control" placeholder="Sẵn sàng khảo sát cho dự án mới?" /></div>
        <div class="cms-form-group md:col-span-2"><label class="cms-form-label">Mô tả</label><textarea v-model="homeContent.finalCta.description" class="cms-form-control" rows="3" placeholder="Mô tả CTA cuối trang" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Nút chính - nhãn</label><input v-model="homeContent.finalCta.primaryButtonLabel" class="cms-form-control" placeholder="Xem dự án" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Nút chính - URL</label><input v-model="homeContent.finalCta.primaryButtonUrl" class="cms-form-control" placeholder="/du-an" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Nút phụ - nhãn</label><input v-model="homeContent.finalCta.secondaryButtonLabel" class="cms-form-control" placeholder="Liên hệ" /></div>
        <div class="cms-form-group"><label class="cms-form-label">Nút phụ - URL</label><input v-model="homeContent.finalCta.secondaryButtonUrl" class="cms-form-control" placeholder="/lien-he" /></div>
      </div>
    </details>

    <section class="rounded-xl border border-dashed border-gray-300 bg-gray-50 p-4">
      <div class="text-sm font-medium text-slate-700">Preview contentJson</div>
      <textarea :value="serializedHomeContent" class="cms-form-control mt-3 font-mono text-xs" rows="12" readonly />
    </section>
  </div>
</template>

<script setup lang="ts">
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
