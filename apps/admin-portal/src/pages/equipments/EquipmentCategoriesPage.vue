<template>
  <div>
    <PageHeader title="Danh mục thiết bị" description="Quản lý nhóm thiết bị dùng để phân chia danh mục trên website." />

    <div class="mt-6 grid gap-6 lg:grid-cols-[minmax(0,1.4fr)_minmax(320px,0.9fr)]">
      <div class="cms-card overflow-hidden">
        <div v-if="categoryError" class="m-4 rounded-xl border border-slate-300 bg-slate-100 px-4 py-3 text-sm text-slate-700">{{ categoryError }}</div>
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-slate-200 text-sm">
            <thead class="bg-slate-50 text-left text-slate-500">
              <tr>
                <th class="px-4 py-3 font-semibold">Tên danh mục</th>
                <th class="px-4 py-3 font-semibold">Slug</th>
                <th class="px-4 py-3 font-semibold">Icon</th>
                <th class="px-4 py-3 font-semibold">Thứ tự</th>
                <th class="px-4 py-3 font-semibold">Trạng thái</th>
                <th class="px-4 py-3 font-semibold text-right">Thao tác</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100 bg-white text-slate-700">
              <tr v-for="category in store.categories" :key="category.id">
                <td class="px-4 py-3">
                  <div class="font-medium text-slate-900">{{ category.name }}</div>
                </td>
                <td class="px-4 py-3 text-slate-500">{{ category.slug }}</td>
                <td class="px-4 py-3 text-slate-500">{{ category.icon || 'circle' }}</td>
                <td class="px-4 py-3 text-slate-500">{{ category.sortOrder }}</td>
                <td class="px-4 py-3">
                  <span class="inline-flex rounded-full px-2.5 py-1 text-xs font-semibold" :class="category.active ? 'bg-slate-200 text-slate-700' : 'bg-slate-100 text-slate-500'">
                    {{ category.active ? 'Hiển thị' : 'Ẩn' }}
                  </span>
                </td>
                <td class="px-4 py-3">
                  <div class="flex justify-end gap-2">
                    <button type="button" class="rounded-lg border border-slate-300 px-3 py-1.5 text-xs font-medium text-slate-700 transition hover:bg-slate-50" @click="startEditCategory(category)">Sửa</button>
                    <button type="button" class="rounded-lg border border-slate-300 px-3 py-1.5 text-xs font-medium text-slate-700 transition hover:bg-slate-100" @click="handleDeleteCategory(category.id)">Xóa</button>
                  </div>
                </td>
              </tr>
              <tr v-if="!store.categories.length">
                <td colspan="6" class="px-4 py-10 text-center text-slate-400">Chưa có danh mục thiết bị.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="cms-card p-5">
        <div class="flex items-center justify-between gap-3">
          <div>
            <h3 class="text-base font-semibold text-slate-900">{{ categoryForm.id ? 'Cập nhật danh mục' : 'Tạo danh mục mới' }}</h3>
            <p class="mt-1 text-sm text-slate-500">Slug sẽ tự sinh nếu bạn để trống.</p>
          </div>
          <button v-if="categoryForm.id" type="button" class="text-sm font-medium text-slate-500 hover:text-slate-900" @click="resetCategoryForm">Tạo mới</button>
        </div>

        <form class="mt-5 space-y-4" @submit.prevent="handleCategorySubmit">
          <div>
            <label class="cms-form-label">Tên danh mục <span class="required">*</span></label>
            <input v-model="categoryForm.name" class="cms-form-control" placeholder="Nhập tên danh mục" required />
          </div>
          <div>
            <label class="cms-form-label">Slug</label>
            <input v-model="categoryForm.slug" class="cms-form-control" placeholder="tu-dong-tao-neu-bo-trong" />
          </div>
          <div>
            <label class="cms-form-label">Icon</label>
            <input v-model="categoryForm.icon" class="cms-form-control" placeholder="vd: flask-conical, ruler, waves, fa-solid fa-gears" />
            <p class="mt-1 text-xs text-slate-400">Hỗ trợ Lucide hoặc Font Awesome class.</p>
          </div>
          <div class="grid gap-4 sm:grid-cols-2">
            <div>
              <label class="cms-form-label">Thứ tự</label>
              <input v-model.number="categoryForm.sortOrder" type="number" class="cms-form-control" min="0" />
            </div>
            <div class="flex items-end">
              <label class="flex min-h-[44px] items-center gap-3 rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm font-medium text-slate-700">
                <input v-model="categoryForm.active" type="checkbox" class="h-4 w-4 rounded border-slate-300 text-slate-900 focus:ring-slate-400" />
                Hiển thị trên website
              </label>
            </div>
          </div>
          <button type="submit" class="cms-btn cms-btn-primary w-full" :disabled="categorySaving">
            {{ categorySaving ? 'Đang lưu...' : (categoryForm.id ? 'Cập nhật danh mục' : 'Tạo danh mục') }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import { useEquipmentsStore } from '@/stores/equipments'
import type { Category, CategoryPayload } from '@/types'

const store = useEquipmentsStore()
const categorySaving = ref(false)
const categoryError = ref('')
const categoryForm = reactive<{ id: number | null } & CategoryPayload>({ id: null, name: '', slug: '', parentId: null, icon: '', sortOrder: 0, active: true })

function slugify(value: string) {
  return value.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-+|-+$/g, '')
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
}

function buildCategoryPayload(): CategoryPayload {
  return {
    name: categoryForm.name.trim(),
    slug: (categoryForm.slug || slugify(categoryForm.name)).trim(),
    parentId: null,
    icon: categoryForm.icon?.trim() || '',
    sortOrder: Number.isFinite(categoryForm.sortOrder) ? categoryForm.sortOrder : 0,
    active: categoryForm.active,
  }
}

function extractErrorMessage(error: any) {
  const details = error.response?.data?.error?.details
  if (Array.isArray(details) && details.length) return details.map((item: { field?: string, message?: string }) => `${item.field || 'field'}: ${item.message || 'Không hợp lệ'}`).join('; ')
  return error.response?.data?.error?.message || 'Thao tác thất bại'
}

async function handleCategorySubmit() {
  const payload = buildCategoryPayload()
  if (!payload.name) return void (categoryError.value = 'Tên danh mục là bắt buộc')
  if (!payload.slug) return void (categoryError.value = 'Slug không hợp lệ')
  categorySaving.value = true
  categoryError.value = ''
  try {
    if (categoryForm.id) await store.updateCategory(categoryForm.id, payload)
    else await store.createCategory(payload)
    resetCategoryForm()
  } catch (error: any) {
    categoryError.value = extractErrorMessage(error)
  } finally {
    categorySaving.value = false
  }
}

async function handleDeleteCategory(id: number) {
  if (!confirm('Xác nhận xóa danh mục này?')) return
  categoryError.value = ''
  try {
    await store.removeCategory(id)
    if (categoryForm.id === id) resetCategoryForm()
  } catch (error: any) {
    categoryError.value = extractErrorMessage(error)
  }
}

onMounted(async () => {
  await store.fetchCategories()
})
</script>
