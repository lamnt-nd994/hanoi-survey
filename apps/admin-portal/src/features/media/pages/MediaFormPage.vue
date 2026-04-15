<template>
  <PageShell>
    <PageHeader title="Tải lên Media" description="Tải lên ảnh hoặc tài liệu vào thư viện" />
    <AlertBox v-if="error" :message="error" type="error" class="mt-4" />
    <AlertBox v-if="success" :message="success" type="success" class="mt-4" />

    <DataCard class="max-w-[600px] p-6">
      <form class="space-y-5" @submit.prevent="handleSubmit">
        <FormField>
          <FormLabel>Alt text</FormLabel>
          <Input v-model="altText" placeholder="Mô tả ảnh (alt text)" />
        </FormField>
        <FormField>
          <FormLabel>Tệp đính kèm <span class="text-rose-600">*</span></FormLabel>
          <Input type="file" class="h-auto py-2.5" required @change="onFileChange" />
        </FormField>
      <div class="flex gap-3 mt-4 pt-4 border-t border-gray-100">
        <Button type="submit" :disabled="saving">{{ saving ? 'Đang tải lên...' : 'Tải lên' }}</Button>
        <Button as="router-link" to="/media" variant="secondary">Quay lại</Button>
      </div>
      </form>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import DataCard from '@/components/shared/DataCard.vue'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useMediaStore } from '@/stores/media'
import type { ApiErrorLike } from '@/types/api'

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
  } catch (e: unknown) {
    const apiError = e as ApiErrorLike
    error.value = apiError.response?.data?.error?.message || 'Tải lên thất bại'
  }
  finally { saving.value = false }
}
</script>
