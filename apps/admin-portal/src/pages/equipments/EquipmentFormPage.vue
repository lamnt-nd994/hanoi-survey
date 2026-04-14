<template>
  <div>
    <PageHeader :title="isEdit ? 'Chỉnh sửa thiết bị' : 'Tạo thiết bị mới'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <form @submit.prevent="handleSubmit" class="cms-card mt-6 p-6">
      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Danh mục thiết bị <span class="required">*</span></label>
          <select v-model.number="form.categoryId" class="cms-form-control" required>
            <option :value="0">Chọn danh mục</option>
            <option v-for="category in store.categories" :key="category.id" :value="category.id">{{ category.name }}</option>
          </select>
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Tên thiết bị <span class="required">*</span></label>
          <input v-model="form.name" @input="onNameChange" class="cms-form-control" required />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Slug</label>
          <div class="flex gap-2">
            <input v-model="form.slug" @input="slugManuallyEdited = true" class="cms-form-control" />
            <button type="button" class="cms-btn cms-btn-secondary cms-btn-sm whitespace-nowrap" style="height:38px" @click="generateSlug">Tự tạo</button>
          </div>
        </div>
      </div>
      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Model</label>
          <input v-model="form.model" class="cms-form-control" />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Hãng sản xuất</label>
          <input v-model="form.manufacturer" class="cms-form-control" />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Xuất xứ</label>
          <input v-model="form.origin" class="cms-form-control" />
        </div>
      </div>
      <div class="cms-form-row">
        <div class="cms-form-group">
          <label class="cms-form-label">Đơn vị</label>
          <input v-model="form.unit" class="cms-form-control" placeholder="VD: bộ, cái, máy" />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Số lượng</label>
          <input v-model.number="form.quantity" type="number" min="0" class="cms-form-control" />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Năm sản xuất</label>
          <input v-model.number="form.productionYear" type="number" class="cms-form-control" />
        </div>
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Ảnh bìa</label>
        <div class="flex items-start gap-2">
          <input v-model="form.coverImagePath" class="cms-form-control" placeholder="Đường dẫn ảnh bìa" />
          <button type="button" class="cms-btn cms-btn-secondary flex-shrink-0" :disabled="coverUpload.uploading.value" @click="coverUpload.openPicker()">
            {{ coverUpload.uploading.value ? `${coverUpload.progress.value || 0}%` : 'Chọn ảnh' }}
          </button>
        </div>
        <input :ref="(el: any) => { coverUpload.fileInputRef.value = el }" type="file" accept="image/*" class="hidden" @change="coverUpload.handleFileSelected" />
        <img v-if="form.coverImagePath" :src="mediaUrl(form.coverImagePath)" class="mt-2 max-h-40 rounded border border-gray-200 object-contain" />
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Mô tả</label>
        <textarea v-model="form.description" class="cms-form-control" style="min-height:120px" />
      </div>
      <div class="cms-form-group" style="max-width:240px">
        <label class="cms-form-label">Trạng thái</label>
        <select v-model="form.status" class="cms-form-control">
          <option value="DRAFT">Nháp</option>
          <option value="PUBLISHED">Đã xuất bản</option>
          <option value="ARCHIVED">Lưu trữ</option>
        </select>
      </div>
      <div class="flex gap-3 mt-4 pt-4 border-t border-gray-100">
        <button type="submit" :disabled="saving" class="cms-btn cms-btn-primary">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</button>
        <router-link to="/equipments" class="cms-btn cms-btn-secondary">Quay lại</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { useToastsStore } from '@/stores/toasts'
import { useEquipmentsStore } from '@/stores/equipments'
import { useImageUpload } from '@/composables/useImageUpload'
import type { EquipmentPayload } from '@/types'
import { toSlug } from '@/utils/slug'
import { extractApiError } from '@/utils/files'

const route = useRoute()
const router = useRouter()
const store = useEquipmentsStore()
const saving = ref(false)
const error = ref('')
const slugManuallyEdited = ref(false)
const isEdit = computed(() => !!route.params.id)
const form = reactive<EquipmentPayload>({ categoryId: 0, name: '', slug: '', model: '', manufacturer: '', origin: '', unit: '', quantity: null, productionYear: null, description: '', coverImagePath: '', status: 'DRAFT' })
const toasts = useToastsStore()

const coverUpload = useImageUpload((path) => { form.coverImagePath = path }, { successMessage: 'Đã tải ảnh bìa thiết bị' })

function mediaUrl(path: string) {
  if (!path) return ''
  if (/^https?:\/\//i.test(path)) return path
  const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
  return `${baseUrl}/${path.replace(/^\/+/, '')}`
}

onMounted(async () => {
  await store.fetchCategories()
  if (route.params.id) {
    const entity = await store.getById(Number(route.params.id))
    Object.assign(form, entity)
    slugManuallyEdited.value = true
  }
})

function generateSlug() {
  form.slug = toSlug(form.name)
  slugManuallyEdited.value = false
}

function onNameChange() {
  if (!slugManuallyEdited.value && !isEdit.value) {
    form.slug = toSlug(form.name)
  }
}

async function handleSubmit() {
  if (!form.categoryId) {
    error.value = 'Vui lòng chọn danh mục thiết bị'
    toasts.show(error.value, 'error')
    return
  }
  if (!form.name.trim()) {
    error.value = 'Tên thiết bị không được để trống'
    toasts.show(error.value, 'error')
    return
  }
  if (!(form.slug || toSlug(form.name)).trim()) {
    error.value = 'Slug không hợp lệ'
    toasts.show(error.value, 'error')
    return
  }
  saving.value = true; error.value = ''
  try {
    const payload: EquipmentPayload = {
      categoryId: form.categoryId,
      name: form.name.trim(),
      slug: (form.slug || toSlug(form.name)).trim(),
      model: form.model,
      manufacturer: form.manufacturer,
      origin: form.origin,
      unit: form.unit,
      quantity: form.quantity,
      productionYear: form.productionYear,
      description: form.description,
      coverImagePath: form.coverImagePath,
      status: form.status,
    }
    if (isEdit.value) await store.update(Number(route.params.id), payload)
    else await store.create(payload)
    toasts.show(isEdit.value ? 'Cập nhật thiết bị thành công' : 'Tạo thiết bị thành công', 'success')
    router.push('/equipments')
  } catch (e: any) {
    error.value = extractApiError(e)
    toasts.show(error.value, 'error')
  }
  finally { saving.value = false }
}
</script>
