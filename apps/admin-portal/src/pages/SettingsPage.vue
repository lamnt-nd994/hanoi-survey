<template>
  <div>
    <PageHeader :title="pageTitle" :description="pageDescription" />

    <AlertBox v-if="store.loading" message="Đang tải dữ liệu..." />
    <AlertBox v-if="alert" :message="alert" :type="alertType" />

    <div class="mt-6 space-y-6">
      <section class="cms-card p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Thông tin chung</h2>
          <p class="mt-1 text-sm text-slate-500">Thông tin cơ bản hiển thị trên website và footer.</p>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <div class="cms-form-group">
            <label class="cms-form-label">Tên công ty <span class="required">*</span></label>
            <input v-model="form.siteName" class="cms-form-control" placeholder="Tên công ty" />
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Tên công ty tiếng Anh</label>
            <input v-model="form.companyNameEn" class="cms-form-control" placeholder="Company name in English" />
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Tên ngắn</label>
            <input v-model="form.shortName" class="cms-form-control" placeholder="VD: Hanoi Survey" />
          </div>
          <div class="cms-form-group md:col-span-2">
            <label class="cms-form-label">Slogan</label>
            <input v-model="form.slogan" class="cms-form-control" placeholder="Thông điệp thương hiệu" />
          </div>
          <div class="cms-form-group md:col-span-2">
            <label class="cms-form-label">Giới thiệu ngắn</label>
            <textarea v-model="form.companyIntro" class="cms-form-control" rows="4" placeholder="Giới thiệu ngắn về công ty" />
          </div>
        </div>
      </section>

      <section class="cms-card p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Nhận diện thương hiệu</h2>
          <p class="mt-1 text-sm text-slate-500">Upload hoặc chọn từ thư viện media cho logo và banner.</p>
        </div>
        <div class="grid gap-6 lg:grid-cols-3">
          <div>
            <label class="cms-form-label">Logo</label>
            <MediaAssetField label="Logo" :value="form.logoPath" :uploading="fieldUploaders.logo.uploading.value" accept="image/*" @upload="fieldUploaders.logo.openPicker()" @pick="openMediaPicker('logoPath')" @clear="form.logoPath = ''" />
            <input :ref="(el: any) => { fieldUploaders.logo.fileInputRef.value = el }" type="file" accept="image/*" class="hidden" @change="fieldUploaders.logo.handleFileSelected" />
          </div>
          <div>
            <label class="cms-form-label">Banner</label>
            <MediaAssetField label="Banner" :value="form.heroBannerPath" :uploading="fieldUploaders.banner.uploading.value" accept="image/*" @upload="fieldUploaders.banner.openPicker()" @pick="openMediaPicker('heroBannerPath')" @clear="form.heroBannerPath = ''" />
            <input :ref="(el: any) => { fieldUploaders.banner.fileInputRef.value = el }" type="file" accept="image/*" class="hidden" @change="fieldUploaders.banner.handleFileSelected" />
          </div>
        </div>
      </section>

      <section class="cms-card p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Pháp lý</h2>
          <p class="mt-1 text-sm text-slate-500">Thông tin giấy phép và hồ sơ pháp lý.</p>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <div class="cms-form-group">
            <label class="cms-form-label">Mã số thuế</label>
            <input v-model="form.taxCode" class="cms-form-control" placeholder="Mã số thuế" />
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Giấy phép số</label>
            <input v-model="form.licenseNumber" class="cms-form-control" placeholder="Số giấy phép" />
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Nơi cấp giấy phép</label>
            <input v-model="form.licenseIssuedBy" class="cms-form-control" placeholder="Cơ quan cấp" />
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Ngày cấp</label>
            <input v-model="form.licenseIssuedDate" type="date" class="cms-form-control" />
          </div>
          <div class="cms-form-group md:col-span-2">
            <label class="cms-form-label">File giấy phép</label>
            <MediaAssetField label="Giấy phép" :value="form.businessLicensePath" :uploading="fieldUploaders.license.uploading.value" accept="image/*,application/pdf" @upload="fieldUploaders.license.openPicker()" @pick="openMediaPicker('businessLicensePath')" @clear="form.businessLicensePath = ''" />
            <input :ref="(el: any) => { fieldUploaders.license.fileInputRef.value = el }" type="file" accept="image/*,application/pdf" class="hidden" @change="fieldUploaders.license.handleFileSelected" />
          </div>
        </div>
      </section>

      <section class="cms-card p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Liên hệ</h2>
          <p class="mt-1 text-sm text-slate-500">Thông tin hiển thị ở trang Liên hệ và header/footer website.</p>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <div class="cms-form-group">
            <label class="cms-form-label">Điện thoại</label>
            <input v-model="form.phone" class="cms-form-control" placeholder="Số điện thoại công ty" />
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Email</label>
            <input v-model="form.email" class="cms-form-control" placeholder="info@example.com" />
          </div>
          <div class="cms-form-group md:col-span-2">
            <label class="cms-form-label">Địa chỉ hiển thị ngắn</label>
            <input v-model="form.address" class="cms-form-control" placeholder="Địa chỉ ngắn dùng chung" />
          </div>
          <div class="cms-form-group md:col-span-2">
            <label class="cms-form-label">Địa chỉ trụ sở</label>
            <input v-model="form.officeAddress" class="cms-form-control" placeholder="Địa chỉ trụ sở chính" />
          </div>
          <div class="cms-form-group">
            <label class="cms-form-label">Giờ làm việc</label>
            <input v-model="form.workingHours" class="cms-form-control" placeholder="Thứ 2 - Thứ 6: 8:00 - 17:30" />
          </div>
          <div class="cms-form-group md:col-span-2">
            <label class="cms-form-label">Google Map Embed URL</label>
            <textarea v-model="form.mapEmbed" class="cms-form-control" rows="3" placeholder="https://www.google.com/maps/embed?..." />
          </div>
        </div>
      </section>

      <section class="cms-card p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Mạng xã hội</h2>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
          <div class="cms-form-group"><label class="cms-form-label">Facebook URL</label><input v-model="form.facebookUrl" class="cms-form-control" /></div>
          <div class="cms-form-group"><label class="cms-form-label">Zalo URL</label><input v-model="form.zaloUrl" class="cms-form-control" /></div>
          <div class="cms-form-group"><label class="cms-form-label">YouTube URL</label><input v-model="form.youtubeUrl" class="cms-form-control" /></div>
          <div class="cms-form-group"><label class="cms-form-label">LinkedIn URL</label><input v-model="form.linkedinUrl" class="cms-form-control" /></div>
        </div>
      </section>

      <section class="cms-card p-6">
        <div class="mb-5">
          <h2 class="text-base font-semibold text-slate-900">Footer</h2>
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Footer text</label>
          <textarea v-model="form.footerText" class="cms-form-control" rows="3" placeholder="Nội dung footer" />
        </div>
      </section>

      <div class="flex justify-end">
        <button @click="handleSave" class="cms-btn cms-btn-primary" :disabled="saving">{{ saving ? 'Đang lưu...' : 'Lưu thông tin công ty' }}</button>
      </div>
    </div>

    <MediaPickerModal :open="picker.open" :items="mediaStore.items" :loading="mediaStore.loading" @close="closeMediaPicker" @select="handleMediaSelected" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import MediaAssetField from '@/components/shared/MediaAssetField.vue'
import MediaPickerModal from '@/components/shared/MediaPickerModal.vue'
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
