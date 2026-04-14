<template>
  <PageShell>
    <PageHeader :title="pageTitle" :description="pageDescription" />

    <AlertBox v-if="store.loading" message="Đang tải dữ liệu..." />
    <AlertBox v-if="alert" :message="alert" :type="alertType" />

    <div class="mt-6 space-y-6">
      <DataCard class="p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Thông tin chung</h2>
          <p class="mt-1 text-sm text-slate-500">Thông tin cơ bản hiển thị trên website và footer.</p>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <FormField>
            <FormLabel>Tên công ty <span class="text-rose-600">*</span></FormLabel>
            <Input v-model="form.siteName" placeholder="Tên công ty" />
          </FormField>
          <FormField>
            <FormLabel>Tên công ty tiếng Anh</FormLabel>
            <Input v-model="form.companyNameEn" placeholder="Company name in English" />
          </FormField>
          <FormField>
            <FormLabel>Tên ngắn</FormLabel>
            <Input v-model="form.shortName" placeholder="VD: Hanoi Survey" />
          </FormField>
          <FormField class="md:col-span-2">
            <FormLabel>Slogan</FormLabel>
            <Input v-model="form.slogan" placeholder="Thông điệp thương hiệu" />
          </FormField>
          <FormField class="md:col-span-2">
            <FormLabel>Giới thiệu ngắn</FormLabel>
            <Textarea v-model="form.companyIntro" rows="4" placeholder="Giới thiệu ngắn về công ty" />
          </FormField>
        </div>
      </DataCard>

      <DataCard class="p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Nhận diện thương hiệu</h2>
          <p class="mt-1 text-sm text-slate-500">Upload hoặc chọn từ thư viện media cho logo và banner.</p>
        </div>
        <div class="grid gap-6 lg:grid-cols-3">
          <div>
            <FormLabel>Logo</FormLabel>
            <MediaAssetField label="Logo" :value="form.logoPath" :uploading="fieldUploaders.logo.uploading.value" accept="image/*" @upload="fieldUploaders.logo.openPicker()" @pick="openMediaPicker('logoPath')" @clear="form.logoPath = ''" />
            <input :ref="assignLogoInput" type="file" accept="image/*" class="hidden" @change="fieldUploaders.logo.handleFileSelected" />
          </div>
          <div>
            <FormLabel>Banner</FormLabel>
            <MediaAssetField label="Banner" :value="form.heroBannerPath" :uploading="fieldUploaders.banner.uploading.value" accept="image/*" @upload="fieldUploaders.banner.openPicker()" @pick="openMediaPicker('heroBannerPath')" @clear="form.heroBannerPath = ''" />
            <input :ref="assignBannerInput" type="file" accept="image/*" class="hidden" @change="fieldUploaders.banner.handleFileSelected" />
          </div>
        </div>
      </DataCard>

      <DataCard class="p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Pháp lý</h2>
          <p class="mt-1 text-sm text-slate-500">Thông tin giấy phép và hồ sơ pháp lý.</p>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <FormField>
            <FormLabel>Mã số thuế</FormLabel>
            <Input v-model="form.taxCode" placeholder="Mã số thuế" />
          </FormField>
          <FormField>
            <FormLabel>Giấy phép số</FormLabel>
            <Input v-model="form.licenseNumber" placeholder="Số giấy phép" />
          </FormField>
          <FormField>
            <FormLabel>Nơi cấp giấy phép</FormLabel>
            <Input v-model="form.licenseIssuedBy" placeholder="Cơ quan cấp" />
          </FormField>
          <FormField>
            <FormLabel>Ngày cấp</FormLabel>
            <Input v-model="form.licenseIssuedDate" type="date" />
          </FormField>
          <FormField class="md:col-span-2">
            <FormLabel>File giấy phép</FormLabel>
            <MediaAssetField label="Giấy phép" :value="form.businessLicensePath" :uploading="fieldUploaders.license.uploading.value" accept="image/*,application/pdf" @upload="fieldUploaders.license.openPicker()" @pick="openMediaPicker('businessLicensePath')" @clear="form.businessLicensePath = ''" />
            <input :ref="assignLicenseInput" type="file" accept="image/*,application/pdf" class="hidden" @change="fieldUploaders.license.handleFileSelected" />
          </FormField>
        </div>
      </DataCard>

      <DataCard class="p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Liên hệ</h2>
          <p class="mt-1 text-sm text-slate-500">Thông tin hiển thị ở trang Liên hệ và header/footer website.</p>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <FormField>
            <FormLabel>Điện thoại</FormLabel>
            <Input v-model="form.phone" placeholder="Số điện thoại công ty" />
          </FormField>
          <FormField>
            <FormLabel>Email</FormLabel>
            <Input v-model="form.email" placeholder="info@example.com" />
          </FormField>
          <FormField class="md:col-span-2">
            <FormLabel>Địa chỉ hiển thị ngắn</FormLabel>
            <Input v-model="form.address" placeholder="Địa chỉ ngắn dùng chung" />
          </FormField>
          <FormField class="md:col-span-2">
            <FormLabel>Địa chỉ trụ sở</FormLabel>
            <Input v-model="form.officeAddress" placeholder="Địa chỉ trụ sở chính" />
          </FormField>
          <FormField>
            <FormLabel>Giờ làm việc</FormLabel>
            <Input v-model="form.workingHours" placeholder="Thứ 2 - Thứ 6: 8:00 - 17:30" />
          </FormField>
          <FormField class="md:col-span-2">
            <FormLabel>Google Map Embed URL</FormLabel>
            <Textarea v-model="form.mapEmbed" rows="3" placeholder="https://www.google.com/maps/embed?..." />
          </FormField>
        </div>
      </DataCard>

      <DataCard class="p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Mạng xã hội</h2>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <FormField><FormLabel>Facebook URL</FormLabel><Input v-model="form.facebookUrl" /></FormField>
          <FormField><FormLabel>Zalo URL</FormLabel><Input v-model="form.zaloUrl" /></FormField>
          <FormField><FormLabel>YouTube URL</FormLabel><Input v-model="form.youtubeUrl" /></FormField>
          <FormField><FormLabel>LinkedIn URL</FormLabel><Input v-model="form.linkedinUrl" /></FormField>
        </div>
      </DataCard>

      <DataCard class="p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Footer</h2>
        </div>
        <FormField>
          <FormLabel>Footer text</FormLabel>
          <Textarea v-model="form.footerText" rows="3" placeholder="Nội dung footer" />
        </FormField>
      </DataCard>

      <div class="flex justify-end">
        <Button :disabled="saving" @click="handleSave">{{ saving ? 'Đang lưu...' : 'Lưu thông tin công ty' }}</Button>
      </div>
    </div>

    <MediaPickerModal :open="picker.open" :items="mediaStore.items" :loading="mediaStore.loading" @close="closeMediaPicker" @select="handleMediaSelected" />
  </PageShell>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import AlertBox from '@/components/shared/AlertBox.vue'
import DataCard from '@/components/shared/DataCard.vue'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import MediaAssetField from '@/components/shared/MediaAssetField.vue'
import MediaPickerModal from '@/components/shared/MediaPickerModal.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { useSettingsStore } from '@/stores/settings'
import { useMediaStore } from '@/stores/media'
import { useImageUpload } from '@/composables/useImageUpload'
import type { SiteSettings } from '@/types'

const store = useSettingsStore()
const mediaStore = useMediaStore()
const route = useRoute()
const alert = ref('')
const alertType = ref<'success' | 'error'>('success')
const saving = ref(false)
const pageTitle = route.path === '/company-info' || route.path === '/contacts' ? 'Thông tin công ty' : 'Cài đặt website'
const pageDescription = route.path === '/company-info' || route.path === '/contacts'
  ? 'Cấu hình toàn bộ thông tin công ty hiển thị trên website.'
  : 'Cấu hình thông tin hiển thị trên website'

const form = reactive<SiteSettings>({
  id: undefined,
  siteName: '',
  companyNameEn: '',
  shortName: '',
  slogan: '',
  logoPath: '',
  footerLogoPath: '',
  heroBannerPath: '',
  companyIntro: '',
  taxCode: '',
  licenseNumber: '',
  businessLicensePath: '',
  licenseIssuedBy: '',
  licenseIssuedDate: null,
  officeAddress: '',
  branchAddress: '',
  hotline: '',
  phone: '',
  email: '',
  website: '',
  address: '',
  mapEmbed: '',
  workingHours: '',
  facebookUrl: '',
  zaloUrl: '',
  youtubeUrl: '',
  linkedinUrl: '',
  footerText: '',
})

const picker = reactive<{ open: boolean, field: keyof SiteSettings | '' }>({ open: false, field: '' })

const fieldUploaders = {
  logo: useImageUpload((path) => { form.logoPath = path }),
  banner: useImageUpload((path) => { form.heroBannerPath = path }),
  license: useImageUpload((path) => { form.businessLicensePath = path }),
}

function assignLogoInput(el: unknown) {
  fieldUploaders.logo.fileInputRef.value = el instanceof HTMLInputElement ? el : null
}

function assignBannerInput(el: unknown) {
  fieldUploaders.banner.fileInputRef.value = el instanceof HTMLInputElement ? el : null
}

function assignLicenseInput(el: unknown) {
  fieldUploaders.license.fileInputRef.value = el instanceof HTMLInputElement ? el : null
}

watch(() => store.settings, (val) => {
  if (!val) return
  Object.assign(form, {
    ...val,
    companyNameEn: val.companyNameEn || '',
    shortName: val.shortName || '',
    slogan: val.slogan || '',
    logoPath: val.logoPath || '',
    footerLogoPath: val.footerLogoPath || '',
    heroBannerPath: val.heroBannerPath || '',
    companyIntro: val.companyIntro || '',
    taxCode: val.taxCode || '',
    licenseNumber: val.licenseNumber || '',
    businessLicensePath: val.businessLicensePath || '',
    licenseIssuedBy: val.licenseIssuedBy || '',
    licenseIssuedDate: val.licenseIssuedDate || null,
    officeAddress: val.officeAddress || '',
    branchAddress: val.branchAddress || '',
    hotline: val.hotline || '',
    phone: val.phone || '',
    email: val.email || '',
    website: val.website || '',
    address: val.address || '',
    mapEmbed: val.mapEmbed || '',
    workingHours: val.workingHours || '',
    facebookUrl: val.facebookUrl || '',
    zaloUrl: val.zaloUrl || '',
    youtubeUrl: val.youtubeUrl || '',
    linkedinUrl: val.linkedinUrl || '',
    footerText: val.footerText || '',
  })
}, { immediate: true })

function flash(msg: string, type: 'success' | 'error' = 'success') {
  alert.value = msg
  alertType.value = type
  setTimeout(() => { alert.value = '' }, 3000)
}

function openMediaPicker(field: keyof SiteSettings) {
  picker.field = field
  picker.open = true
  mediaStore.fetchList()
}

function closeMediaPicker() {
  picker.open = false
  picker.field = ''
}

function handleMediaSelected(storagePath: string) {
  if (!picker.field) return
  ;(form[picker.field] as string | null) = storagePath
  closeMediaPicker()
}

async function handleSave() {
  saving.value = true
  try {
    form.hotline = ''
    form.branchAddress = ''
    form.website = ''
    form.footerLogoPath = form.logoPath
    await store.update({ ...form })
    flash('Đã lưu thông tin công ty thành công')
  } catch {
    flash('Có lỗi xảy ra', 'error')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  store.fetch()
})
</script>
