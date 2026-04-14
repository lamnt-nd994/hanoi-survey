<template>
  <div class="space-y-6">
    <div class="cms-form-row">
      <div class="cms-form-group">
        <label class="cms-form-label">Tiêu đề <span class="required">*</span></label>
        <Input v-model="form.title" @input="emit('title-input')" placeholder="Nhập tiêu đề trang" required />
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Slug</label>
        <div class="flex gap-2">
          <Input v-model="form.slug" @input="emit('slug-input')" placeholder="vd: gioi-thieu" />
          <Button type="button" variant="secondary" size="sm" class="h-10 whitespace-nowrap" @click="emit('generate-slug')">Tự tạo</Button>
        </div>
      </div>
    </div>

    <div class="cms-form-group">
      <label class="cms-form-label">Tóm tắt</label>
      <Textarea v-model="form.summary" placeholder="Mô tả ngắn gọn" />
    </div>

    <div class="cms-form-row">
      <div class="cms-form-group">
        <label class="cms-form-label">Template</label>
        <Select v-model="form.templateCode">
          <option value="default">Mặc định</option>
          <option value="home_landing">Trang chủ</option>
          <option value="about_company">Giới thiệu công ty</option>
        </Select>
      </div>
      <div class="cms-form-group">
        <label class="cms-form-label">Trạng thái</label>
        <Select v-model="form.status">
          <option value="DRAFT">Nháp</option>
          <option value="PUBLISHED">Đã xuất bản</option>
          <option value="ARCHIVED">Lưu trữ</option>
        </Select>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { PagePayload } from '@/types'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'

defineProps<{
  form: PagePayload
}>()

const emit = defineEmits<{
  (e: 'title-input'): void
  (e: 'slug-input'): void
  (e: 'generate-slug'): void
}>()
</script>
