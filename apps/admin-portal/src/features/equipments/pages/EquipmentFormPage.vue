<template>
  <PageShell>
    <PageHeader :title="isEdit ? 'Chỉnh sửa thiết bị' : 'Tạo thiết bị mới'" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <DataCard class="p-6">
      <form class="space-y-5" @submit.prevent="handleSubmit">
      <div class="grid gap-5 md:grid-cols-2">
        <FormField>
          <FormLabel>Danh mục thiết bị <span class="text-rose-600">*</span></FormLabel>
          <Select v-model.number="form.categoryId" required>
            <option :value="0">Chọn danh mục</option>
            <option v-for="category in store.categories" :key="category.id" :value="category.id">{{ category.name }}</option>
          </Select>
        </FormField>
        <FormField>
          <FormLabel>Tên thiết bị <span class="text-rose-600">*</span></FormLabel>
          <Input v-model="form.name" required @input="onNameChange" />
        </FormField>
      </div>
      <div class="grid gap-5 md:grid-cols-3">
        <FormField>
          <FormLabel>Model</FormLabel>
          <Input v-model="form.model" />
        </FormField>
        <FormField>
          <FormLabel>Hãng sản xuất</FormLabel>
          <Input v-model="form.manufacturer" />
        </FormField>
        <FormField>
          <FormLabel>Xuất xứ</FormLabel>
          <Input v-model="form.origin" />
        </FormField>
      </div>
      <div class="grid gap-5 md:grid-cols-3">
        <FormField>
          <FormLabel>Đơn vị</FormLabel>
          <Input v-model="form.unit" placeholder="VD: bộ, cái, máy" />
        </FormField>
        <FormField>
          <FormLabel>Số lượng</FormLabel>
          <Input v-model.number="form.quantity" type="number" min="0" />
        </FormField>
        <FormField>
          <FormLabel>Năm sản xuất</FormLabel>
          <Input v-model.number="form.productionYear" type="number" />
        </FormField>
      </div>
      <FormField>
        <FormLabel>Ảnh bìa</FormLabel>
        <div class="flex items-start gap-2">
          <Input v-model="form.coverImagePath" placeholder="Đường dẫn ảnh bìa" />
          <Button type="button" variant="secondary" class="flex-shrink-0" :disabled="coverUpload.uploading.value" @click="coverUpload.openPicker()">
            {{ coverUpload.uploading.value ? `${coverUpload.progress.value || 0}%` : 'Chọn ảnh' }}
          </Button>
        </div>
        <input :ref="assignCoverInput" type="file" accept="image/*" class="hidden" @change="coverUpload.handleFileSelected" />
        <img v-if="form.coverImagePath" :src="mediaUrl(form.coverImagePath)" width="320" height="160" loading="lazy" decoding="async" class="mt-2 h-40 w-full max-w-md rounded border border-gray-200 object-contain" />
      </FormField>
      <FormField>
        <FormLabel>Mô tả</FormLabel>
        <Textarea v-model="form.description" class="min-h-[120px]" />
      </FormField>
      <FormField class="max-w-[240px]">
        <FormLabel>Trạng thái</FormLabel>
        <Select v-model="form.status">
          <option value="DRAFT">Nháp</option>
          <option value="PUBLISHED">Đã xuất bản</option>
          <option value="ARCHIVED">Lưu trữ</option>
        </Select>
      </FormField>
      <div class="flex gap-3 mt-4 pt-4 border-t border-gray-100">
        <Button type="submit" :disabled="saving">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới') }}</Button>
        <Button as="router-link" to="/equipments" variant="secondary">Quay lại</Button>
      </div>
      </form>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AlertBox from '@/components/shared/AlertBox.vue'
import DataCard from '@/components/shared/DataCard.vue'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import { useToastsStore } from '@/stores/toasts'
import { useEquipmentsStore } from '@/stores/equipments'
import { useImageUpload } from '@/composables/useImageUpload'
import type { EquipmentPayload } from '@/types'
import { toSlug } from '@/utils/slug'
import { extractApiError } from '@/utils/files'
import { resolveMediaUrl } from '@/lib/media'

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

function assignCoverInput(el: unknown) {
  coverUpload.fileInputRef.value = el instanceof HTMLInputElement ? el : null
}

function mediaUrl(path: string) {
  return resolveMediaUrl(path)
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
  } catch (e: unknown) {
    error.value = extractApiError(e)
    toasts.show(error.value, 'error')
  }
  finally { saving.value = false }
}
</script>
