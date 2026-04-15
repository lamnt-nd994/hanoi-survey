<template>
  <PageShell>
    <PageHeader title="Danh mục thiết bị" description="Quản lý nhóm thiết bị dùng để phân chia danh mục trên website." />

    <div class="mt-6 grid gap-6 lg:grid-cols-[minmax(0,1.4fr)_minmax(320px,0.9fr)]">
      <DataCard>
        <div v-if="categoryError" class="m-4 rounded-xl border border-slate-300 bg-slate-100 px-4 py-3 text-sm text-slate-700">{{ categoryError }}</div>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Tên danh mục</TableHead>
              <TableHead>Slug</TableHead>
              <TableHead>Icon</TableHead>
              <TableHead>Thứ tự</TableHead>
              <TableHead>Trạng thái</TableHead>
              <TableHead class="text-right">Thao tác</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow v-for="category in store.categories" :key="category.id">
              <TableCell class="font-medium text-slate-900">{{ category.name }}</TableCell>
              <TableCell class="text-slate-500">{{ category.slug }}</TableCell>
              <TableCell class="text-slate-500">{{ category.icon || 'circle' }}</TableCell>
              <TableCell class="text-slate-500">{{ category.sortOrder }}</TableCell>
              <TableCell>
                <Badge :variant="category.active ? 'secondary' : 'outline'">{{ category.active ? 'Hiển thị' : 'Ẩn' }}</Badge>
              </TableCell>
              <TableCell>
                <div class="flex justify-end gap-2">
                  <Button type="button" variant="secondary" size="sm" @click="startEditCategory(category)">Sửa</Button>
                  <Button type="button" variant="outline" size="icon" @click="handleDeleteCategory(category.id)"><Trash2 class="h-4 w-4" /></Button>
                </div>
              </TableCell>
            </TableRow>
            <TableRow v-if="!store.categories.length">
              <TableCell colspan="6" class="h-32 text-center text-slate-400">Chưa có danh mục thiết bị.</TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </DataCard>

      <DataCard class="p-5">
        <div class="flex items-center justify-between gap-3">
          <div>
            <h3 class="text-base font-semibold text-slate-900">{{ categoryForm.id ? 'Cập nhật danh mục' : 'Tạo danh mục mới' }}</h3>
            <p class="mt-1 text-sm text-slate-500">Slug được tự động tạo từ tên danh mục.</p>
          </div>
          <Button v-if="categoryForm.id" type="button" variant="ghost" size="sm" @click="resetCategoryForm">Tạo mới</Button>
        </div>

        <form class="mt-5 space-y-4" @submit.prevent="handleCategorySubmit">
          <FormField>
            <FormLabel>Tên danh mục <span class="text-rose-600">*</span></FormLabel>
            <Input v-model="categoryForm.name" placeholder="Nhập tên danh mục" required @input="onNameChange" />
          </FormField>
          <FormField>
            <FormLabel>Icon</FormLabel>
            <Input v-model="categoryForm.icon" placeholder="vd: flask-conical, ruler, waves, fa-solid fa-gears" />
            <p class="mt-1 text-xs text-slate-400">Hỗ trợ Lucide hoặc Font Awesome class.</p>
          </FormField>
          <div class="grid gap-4 sm:grid-cols-2">
            <FormField>
              <FormLabel>Thứ tự</FormLabel>
              <Input v-model.number="categoryForm.sortOrder" type="number" min="0" />
            </FormField>
            <div class="flex items-end">
              <label class="flex min-h-[44px] items-center gap-3 rounded-md border border-slate-200 bg-white px-4 py-3 text-sm font-medium text-slate-700">
                <input v-model="categoryForm.active" type="checkbox" class="h-4 w-4 rounded border-slate-300 text-slate-900 focus:ring-slate-400" />
                Hiển thị trên website
              </label>
            </div>
          </div>
          <Button type="submit" class="w-full" :disabled="categorySaving">
            {{ categorySaving ? 'Đang lưu...' : (categoryForm.id ? 'Cập nhật danh mục' : 'Tạo danh mục') }}
          </Button>
        </form>
      </DataCard>
    </div>
  </PageShell>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { Trash2 } from 'lucide-vue-next'
import DataCard from '@/components/shared/DataCard.vue'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useEquipmentsStore } from '@/stores/equipments'
import { useToastsStore } from '@/stores/toasts'
import type { Category, CategoryPayload } from '@/types'
import type { ApiErrorLike } from '@/types/api'
import { extractApiError } from '@/utils/files'
import { toSlug } from '@/utils/slug'

const store = useEquipmentsStore()
const categorySaving = ref(false)
const categoryError = ref('')
const slugManuallyEdited = ref(false)
const toasts = useToastsStore()
const categoryForm = reactive<{ id: number | null } & CategoryPayload>({ id: null, name: '', slug: '', parentId: null, icon: '', sortOrder: 0, active: true })

function generateSlug() {
  categoryForm.slug = toSlug(categoryForm.name)
  slugManuallyEdited.value = false
}

function onNameChange() {
  if (!slugManuallyEdited.value && !categoryForm.id) {
    categoryForm.slug = toSlug(categoryForm.name)
  }
}

function resetCategoryForm() {
  categoryForm.id = null
  categoryForm.name = ''
  categoryForm.slug = ''
  categoryForm.parentId = null
  categoryForm.icon = ''
  categoryForm.sortOrder = 0
  categoryForm.active = true
  categoryError.value = ''
  slugManuallyEdited.value = false
}

function startEditCategory(category: Category) {
  categoryForm.id = category.id
  categoryForm.name = category.name
  categoryForm.slug = category.slug
  categoryForm.parentId = category.parentId
  categoryForm.icon = category.icon || ''
  categoryForm.sortOrder = category.sortOrder
  categoryForm.active = category.active
  categoryError.value = ''
  slugManuallyEdited.value = true
}

function buildCategoryPayload(): CategoryPayload {
  return {
    name: categoryForm.name.trim(),
    slug: toSlug(categoryForm.slug || categoryForm.name),
    parentId: null,
    icon: categoryForm.icon?.trim() || '',
    sortOrder: Number.isFinite(categoryForm.sortOrder) ? categoryForm.sortOrder : 0,
    active: categoryForm.active,
  }
}

function extractErrorMessage(error: unknown) {
  const details = (error as ApiErrorLike).response?.data?.error?.details
  if (Array.isArray(details) && details.length) return details.map((item: { field?: string, message?: string }) => `${item.field || 'field'}: ${item.message || 'Không hợp lệ'}`).join('; ')
  return extractApiError(error)
}

async function handleCategorySubmit() {
  const payload = buildCategoryPayload()
  if (!payload.name) {
    categoryError.value = 'Tên danh mục là bắt buộc'
    toasts.show(categoryError.value, 'error')
    return
  }
  if (!payload.slug) {
    categoryError.value = 'Slug không hợp lệ'
    toasts.show(categoryError.value, 'error')
    return
  }
  categorySaving.value = true
  categoryError.value = ''
  try {
    if (categoryForm.id) await store.updateCategory(categoryForm.id, payload)
    else await store.createCategory(payload)
    toasts.show(categoryForm.id ? 'Cập nhật danh mục thiết bị thành công' : 'Tạo danh mục thiết bị thành công', 'success')
    resetCategoryForm()
  } catch (error: unknown) {
    categoryError.value = extractErrorMessage(error)
    toasts.show(categoryError.value, 'error')
  } finally {
    categorySaving.value = false
  }
}

async function handleDeleteCategory(id: number) {
  if (!confirm('Xác nhận xóa danh mục này?')) return
  categoryError.value = ''
  try {
    await store.removeCategory(id)
    toasts.show('Xóa danh mục thiết bị thành công', 'success')
    if (categoryForm.id === id) resetCategoryForm()
  } catch (error: unknown) {
    categoryError.value = extractErrorMessage(error)
    toasts.show(categoryError.value, 'error')
  }
}

onMounted(async () => {
  await store.fetchCategories()
})
</script>
