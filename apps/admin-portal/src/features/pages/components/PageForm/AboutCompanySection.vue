<template>
  <div class="space-y-6">
    <Card>
      <CardContent class="p-4">
      <div class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Mở đầu</h2>
        <div class="flex items-center gap-3">
          <span class="text-xs text-slate-500">Quản lý phần xuất hiện đầu tiên của trang giới thiệu</span>
          <Button type="button" variant="secondary" :disabled="uploadingIntroImage" @click="emit('pick-intro-image')">
            {{ uploadingIntroImage ? `Đang tải ${introImageUploadProgress}%` : 'Tải ảnh vào nội dung' }}
          </Button>
        </div>
      </div>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="space-y-2">
          <label class="text-sm font-medium leading-none text-slate-700">Tiêu đề lớn</label>
          <Input v-model="aboutContent.hero.title" placeholder="Về Chúng Tôi" />
        </div>
        <div class="space-y-2">
          <label class="text-sm font-medium leading-none text-slate-700">Ảnh mở đầu</label>
          <div class="relative">
            <Input v-model="aboutContent.intro.imagePath" class="pr-36" placeholder="media/about-intro.jpg" />
            <Button type="button" variant="secondary" size="sm" class="input-action-btn" :disabled="uploadingIntroCoverImage" @click="emit('pick-intro-cover')">
              {{ uploadingIntroCoverImage ? `Đang tải ${introCoverUploadProgress}%` : 'Chọn ảnh' }}
            </Button>
          </div>
        </div>
      </div>
      <div class="space-y-2">
        <label class="text-sm font-medium leading-none text-slate-700">Nội dung giới thiệu</label>
        <p class="text-xs text-slate-500">Nhập các đoạn mô tả bên trái. Bạn có thể định dạng chữ, danh sách, link và chèn ảnh trực tiếp nếu cần.</p>
      </div>
      <div class="flex items-center gap-2">
        <Button type="button" :variant="introEditorMode === 'visual' ? 'default' : 'secondary'" size="sm" @click="emit('update:introEditorMode', 'visual')">Soạn thảo</Button>
        <Button type="button" :variant="introEditorMode === 'html' ? 'default' : 'secondary'" size="sm" @click="emit('update:introEditorMode', 'html')">HTML</Button>
      </div>
      <div class="overflow-hidden rounded-xl border border-slate-300 bg-white shadow-sm intro-quill-wrap">
        <QuillEditor
          v-if="introEditorMode === 'visual'"
          ref="introQuillRef"
          v-model:content="introContentProxy"
          content-type="html"
          theme="snow"
          :toolbar="toolbar"
          placeholder="Nhập nội dung giới thiệu doanh nghiệp..."
          class="intro-quill-editor"
        />
        <div v-else class="p-4">
          <Textarea
            v-model="introContentProxy"
            class="font-mono text-sm min-h-[360px]"
            rows="14"
            placeholder="<p>Nhập nội dung giới thiệu doanh nghiệp...</p>"
          />
        </div>
      </div>
      <div class="mt-3 rounded-xl border border-slate-200 bg-slate-50 p-4">
        <div class="text-xs font-medium uppercase tracking-[0.18em] text-slate-500">Xem trước bố cục mở đầu</div>
        <div class="mt-4 grid gap-6 rounded-xl bg-white p-4 shadow-sm lg:grid-cols-[minmax(0,1fr)_320px] lg:items-start">
          <div>
            <div class="flex items-start gap-4">
              <div class="mt-1 h-14 w-1.5 shrink-0 bg-orange-500"></div>
              <div>
                <div class="text-3xl font-bold text-[#0b4a92]">{{ aboutContent.hero.title || 'Về Chúng Tôi' }}</div>
              </div>
            </div>
            <div class="prose prose-slate mt-6 max-w-none text-sm" v-html="aboutContent.intro.content"></div>
          </div>
          <div class="overflow-hidden rounded-2xl bg-slate-100">
            <img v-if="aboutContent.intro.imagePath" :src="resolveUploadedMediaUrl(aboutContent.intro.imagePath)" width="320" height="256" loading="lazy" decoding="async" class="h-64 w-full object-cover" />
            <div v-else class="flex h-64 items-center justify-center px-6 text-center text-sm text-slate-400">Ảnh mở đầu sẽ hiển thị ở đây</div>
          </div>
        </div>
      </div>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
      <div class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Sứ mệnh, tầm nhìn, giá trị cốt lõi</h2>
        <Button type="button" variant="secondary" @click="aboutContent.coreValues.items.push(createValueItem())">+ Thêm mục</Button>
      </div>
      <div class="mt-4 space-y-2">
        <label class="text-sm font-medium leading-none text-slate-700">Tiêu đề section</label>
        <Input v-model="aboutContent.coreValues.sectionTitle" placeholder="Sứ Mệnh & Tầm Nhìn" />
      </div>
      <div class="space-y-3">
        <div v-for="(item, index) in aboutContent.coreValues.items" :key="`value-${index}`" class="rounded-lg border border-gray-200 p-3">
          <div class="flex items-center justify-between gap-3"><div class="text-sm font-medium text-slate-700">Mục {{ index + 1 }}</div><ActionButton variant="destructive" @click="removeItem(aboutContent.coreValues.items, index)">&#10005;</ActionButton></div>
          <div class="mt-3 grid gap-4 md:grid-cols-2">
            <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Tiêu đề</label><Input v-model="item.title" placeholder="Sứ Mệnh" /></div>
            <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Icon key</label><Input v-model="item.icon" placeholder="bullseye, eye, gem" /></div>
          </div>
          <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Mô tả</label><Textarea v-model="item.description" rows="3" placeholder="Mô tả nội dung" /></div>
        </div>
      </div>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
      <div class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Quá trình phát triển</h2>
        <Button type="button" variant="secondary" @click="aboutContent.timeline.items.push(createTimelineItem())">+ Thêm mốc</Button>
      </div>
      <div class="mt-4 space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Tiêu đề section</label><Input v-model="aboutContent.timeline.sectionTitle" placeholder="Quá Trình Phát Triển" /></div>
      <div class="space-y-3">
        <div v-for="(item, index) in aboutContent.timeline.items" :key="`timeline-${index}`" class="rounded-lg border border-gray-200 p-3">
          <div class="flex items-center justify-between gap-3"><div class="text-sm font-medium text-slate-700">Mốc {{ index + 1 }}</div><ActionButton variant="destructive" @click="removeItem(aboutContent.timeline.items, index)">&#10005;</ActionButton></div>
          <div class="mt-3 grid gap-4 md:grid-cols-2">
            <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Năm / giai đoạn</label><Input v-model="item.year" placeholder="2013" /></div>
            <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Thứ tự</label><Input v-model.number="item.sortOrder" type="number" min="1" /></div>
          </div>
          <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Tiêu đề</label><Input v-model="item.title" placeholder="Thành lập công ty" /></div>
          <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Mô tả</label><Textarea v-model="item.description" rows="3" placeholder="Mô tả cột mốc" /></div>
        </div>
      </div>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
      <div class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Cơ cấu tổ chức</h2>
        <Button type="button" variant="secondary" @click="aboutContent.organization.departments.push('')">+ Thêm bộ phận</Button>
      </div>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Tiêu đề section</label><Input v-model="aboutContent.organization.sectionTitle" placeholder="Cơ Cấu Tổ Chức" /></div>
        <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Heading</label><Input v-model="aboutContent.organization.heading" placeholder="Sự tổ chức khoa học và hiệu quả" /></div>
      </div>
      <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Mô tả</label><Textarea v-model="aboutContent.organization.description" rows="3" placeholder="Mô tả cơ cấu tổ chức" /></div>
      <div class="grid gap-4 md:grid-cols-2">
        <div class="space-y-2">
          <label class="text-sm font-medium leading-none text-slate-700">Ảnh sơ đồ</label>
          <div class="relative">
            <Input v-model="aboutContent.organization.chartImagePath" class="pr-36" placeholder="media/org-chart.png" />
            <Button type="button" variant="secondary" size="sm" class="input-action-btn" :disabled="uploadingOrgChartImage" @click="emit('pick-org-chart')">{{ uploadingOrgChartImage ? `Đang tải ${orgChartUploadProgress}%` : 'Chọn ảnh' }}</Button>
          </div>
        </div>
        <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Chú thích ảnh</label><Input v-model="aboutContent.organization.chartCaption" placeholder="Sơ đồ tổ chức nhân sự" /></div>
      </div>
      <div class="space-y-3">
        <div v-for="(department, index) in aboutContent.organization.departments" :key="`department-${index}`" class="rounded-lg border border-gray-200 p-3">
          <div class="flex items-center justify-between gap-3"><div class="text-sm font-medium text-slate-700">Bộ phận {{ index + 1 }}</div><ActionButton variant="destructive" @click="removeItem(aboutContent.organization.departments, index)">&#10005;</ActionButton></div>
          <Input v-model="aboutContent.organization.departments[index]" class="mt-3" placeholder="Tên bộ phận" />
        </div>
      </div>
      </CardContent>
    </Card>

    <Card>
      <CardContent class="p-4">
      <div class="flex items-center justify-between gap-3">
        <h2 class="text-base font-semibold text-slate-900">Hồ sơ năng lực</h2>
        <Button type="button" variant="secondary" @click="aboutContent.capability.items.push(createCapabilityItem())">+ Thêm mục</Button>
      </div>
      <div class="mt-4 grid gap-4 md:grid-cols-2">
        <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Tiêu đề section</label><Input v-model="aboutContent.capability.sectionTitle" placeholder="Hồ Sơ Năng Lực" /></div>
        <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Heading</label><Input v-model="aboutContent.capability.heading" placeholder="Năng lực và chứng chỉ của chúng tôi" /></div>
      </div>
      <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Mô tả</label><Textarea v-model="aboutContent.capability.description" rows="3" placeholder="Mô tả hồ sơ năng lực" /></div>
      <div class="grid gap-4 md:grid-cols-2">
        <div class="space-y-2">
          <label class="text-sm font-medium leading-none text-slate-700">Ảnh minh họa</label>
          <div class="relative">
            <Input v-model="aboutContent.capability.imagePath" class="pr-36" placeholder="media/capability.jpg" />
            <Button type="button" variant="secondary" size="sm" class="input-action-btn" :disabled="uploadingCapabilityImage" @click="emit('pick-capability-image')">{{ uploadingCapabilityImage ? `Đang tải ${capabilityImageUploadProgress}%` : 'Chọn ảnh' }}</Button>
          </div>
        </div>
      </div>
      <div class="space-y-3">
        <div v-for="(item, index) in aboutContent.capability.items" :key="`capability-${index}`" class="rounded-lg border border-gray-200 p-3">
          <div class="flex items-center justify-between gap-3"><div class="text-sm font-medium text-slate-700">Mục {{ index + 1 }}</div><ActionButton variant="destructive" @click="removeItem(aboutContent.capability.items, index)">&#10005;</ActionButton></div>
          <div class="mt-3 grid gap-4 md:grid-cols-2">
            <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Tên tài liệu</label><Input v-model="item.title" placeholder="Ví dụ: Chứng chỉ năng lực hoạt động khảo sát xây dựng" /></div>
            <div class="space-y-2"><label class="text-sm font-medium leading-none text-slate-700">Nhãn nút</label><Input v-model="item.buttonLabel" placeholder="Xem PDF" /></div>
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium leading-none text-slate-700">File PDF</label>
            <div class="relative">
              <Input v-model="item.pdfFilePath" class="pr-56" placeholder="media/ho-so-nang-luc.pdf" />
              <div class="absolute right-1 top-1 flex gap-2">
                <a v-if="item.pdfFilePath" :href="resolveUploadedMediaUrl(item.pdfFilePath)" target="_blank" rel="noopener noreferrer" class="input-mini-action-btn">Xem</a>
                <Button type="button" variant="secondary" size="sm" class="input-mini-action-btn" :disabled="uploadingCapabilityItemIndex === index" @click="emit('pick-capability-item-pdf', index)">{{ uploadingCapabilityItemIndex === index ? `Đang tải ${capabilityItemUploadProgress}%` : 'Chọn PDF' }}</Button>
              </div>
            </div>
          </div>
        </div>
      </div>
      </CardContent>
    </Card>

    <Card class="border-dashed border-gray-300 bg-gray-50">
      <CardContent class="p-4">
      <div class="text-sm font-medium text-slate-700">Preview contentJson</div>
      <Textarea :model-value="serializedAboutContent" class="mt-3 font-mono text-xs min-h-[260px]" rows="12" readonly />
      </CardContent>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { computed, defineAsyncComponent, ref } from 'vue'
import ActionButton from '@/components/shared/ActionButton.vue'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import type { AboutCapabilityItem, AboutPageContent, AboutPageTimelineItem, AboutPageValueItem } from '@/types'
import type { QuillEditorComponent, QuillToolbar } from '@/types/api'

const QuillEditor = defineAsyncComponent(async () => {
  await import('@vueup/vue-quill/dist/vue-quill.snow.css')
  return (await import('@vueup/vue-quill')).QuillEditor
})

const props = defineProps<{
  aboutContent: AboutPageContent
  introEditorMode: 'visual' | 'html'
  toolbar: QuillToolbar
  resolveUploadedMediaUrl: (path: string) => string
  serializedAboutContent: string
  uploadingIntroImage: boolean
  introImageUploadProgress: number
  uploadingIntroCoverImage: boolean
  introCoverUploadProgress: number
  uploadingOrgChartImage: boolean
  orgChartUploadProgress: number
  uploadingCapabilityImage: boolean
  capabilityImageUploadProgress: number
  uploadingCapabilityItemIndex: number | null
  capabilityItemUploadProgress: number
}>()

const emit = defineEmits<{
  (e: 'update:introEditorMode', value: 'visual' | 'html'): void
  (e: 'pick-intro-image'): void
  (e: 'pick-intro-cover'): void
  (e: 'pick-org-chart'): void
  (e: 'pick-capability-image'): void
  (e: 'pick-capability-item-pdf', index: number): void
}>()

const introQuillRef = ref<QuillEditorComponent | null>(null)

const introContentProxy = computed({
  get: () => props.aboutContent.intro.content,
  set: (value: string) => {
    props.aboutContent.intro.content = value
  },
})

function removeItem<T>(items: T[], index: number) {
  items.splice(index, 1)
}

function createValueItem(): AboutPageValueItem {
  return { title: '', icon: '', description: '' }
}

function createTimelineItem(): AboutPageTimelineItem {
  return { year: '', title: '', description: '', sortOrder: 1 }
}

function createCapabilityItem(): AboutCapabilityItem {
  return { title: '', pdfFilePath: '', buttonLabel: 'Xem PDF' }
}

function insertImage(url: string) {
  const quill = introQuillRef.value?.getQuill?.()
  if (!quill) return
  const selection = quill.getSelection(true)
  const index = selection?.index ?? quill.getLength()
  quill.insertEmbed(index, 'image', url, 'user')
  quill.setSelection(index + 1, 0, 'user')
  props.aboutContent.intro.content = quill.root.innerHTML || ''
}

defineExpose({ insertImage })
</script>
