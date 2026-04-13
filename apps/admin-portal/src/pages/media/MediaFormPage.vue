<template>
  <div>
    <PageHeader title="Tải lên Media" description="Tải lên ảnh hoặc tài liệu vào thư viện" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <AlertBox v-if="success" :message="success" type="success" class="mt-4" />
    <form @submit.prevent="handleSubmit" class="cms-card mt-6 p-6" style="max-width:600px">
      <div class="cms-form-group">
        <label class="cms-form-label">Alt text</label>
        <input v-model="altText" class="cms-form-control" placeholder="Mô tả ảnh (alt text)" />
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Tệp đính kèm <span class="required">*</span></label>
        <input type="file" @change="onFileChange" class="cms-form-control" style="padding:10px" required />
      </div>
      <div class="flex gap-3 mt-4 pt-4 border-t border-gray-100">
        <button type="submit" :disabled="saving" class="cms-btn cms-btn-primary">{{ saving ? 'Đang tải lên...' : 'Tải lên' }}</button>
        <router-link to="/media" class="cms-btn cms-btn-secondary">Quay lại</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import PageHeader from '@/components/shared/PageHeader.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { useMediaStore } from '@/stores/media'

const router = useRouter()
const store = useMediaStore()
const saving = ref(false)
const error = ref('')
const success = ref('')
const altText = ref('')
const file = ref<File | null>(null)

function onFileChange(e: Event) { file.value = (e.target as HTMLInputElement).files?.[0] || null }

async function handleSubmit() {
  if (!file.value) return
  saving.value = true; error.value = ''; success.value = ''
  try {
    await store.upload(file.value, altText.value)
    success.value = 'Tải lên thành công!'
    setTimeout(() => router.push('/media'), 800)
  } catch (e: any) { error.value = e.response?.data?.error?.message || 'Tải lên thất bại' }
  finally { saving.value = false }
}
</script>
