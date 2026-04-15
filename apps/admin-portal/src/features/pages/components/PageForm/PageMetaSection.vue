<template>
  <div class="space-y-6">
    <FormField>
      <FormLabel>Tiêu đề <span class="text-rose-600">*</span></FormLabel>
      <Input v-model="form.title" @input="emit('title-input')" placeholder="Nhập tiêu đề trang" required />
    </FormField>

    <FormField>
      <FormLabel>Tóm tắt</FormLabel>
      <Textarea v-model="form.summary" placeholder="Mô tả ngắn gọn" />
    </FormField>

    <div class="grid gap-5 md:grid-cols-2">
      <FormField>
        <FormLabel>Template</FormLabel>
        <Select v-model="form.templateCode">
          <option value="default">Mặc định</option>
          <option value="home_landing">Trang chủ</option>
          <option value="about_company">Giới thiệu công ty</option>
        </Select>
      </FormField>
      <FormField>
        <FormLabel>Trạng thái</FormLabel>
        <Select v-model="form.status">
          <option value="DRAFT">Nháp</option>
          <option value="PUBLISHED">Đã xuất bản</option>
          <option value="ARCHIVED">Lưu trữ</option>
        </Select>
      </FormField>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { PagePayload } from '@/types'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import { Input } from '@/components/ui/input'
import { Select } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'

defineProps<{
  form: PagePayload
}>()

const emit = defineEmits<{
  (e: 'title-input'): void
}>()
</script>
