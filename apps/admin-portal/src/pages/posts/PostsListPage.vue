<template>
  <div>
    <PageHeader title="Bài viết" description="Quản lý bài viết tin tức, kiến thức và phân loại theo danh mục">
      <template #actions>
        <router-link to="/posts/create" class="cms-btn cms-btn-primary cms-btn-sm">＋ Thêm mới</router-link>
      </template>
    </PageHeader>
    <div class="cms-card mt-6">
      <div class="flex flex-wrap items-center gap-3 px-5 py-4 border-b border-gray-100">
        <div class="relative flex-1 min-w-[200px] max-w-xs">
          <svg class="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
          <input v-model="keywordInput" @keyup.enter="handleSearch" placeholder="Tìm theo tiêu đề..." class="cms-form-control pl-9 !py-2 text-sm" />
        </div>
        <select v-model.number="categoryFilterInput" @change="handleSearch" class="cms-form-control !py-2 text-sm" style="max-width:200px">
          <option :value="null">Tất cả danh mục</option>
          <option v-for="cat in store.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>
        <button @click="handleSearch" class="cms-btn cms-btn-secondary cms-btn-sm">Tìm</button>
        <button v-if="store.filters.keyword || store.filters.categoryId" @click="handleClearFilter" class="cms-btn cms-btn-sm" style="color:#6b7280">Xóa bộ lọc</button>
      </div>

      <div class="cms-table-wrap">
        <table class="cms-table">
          <thead><tr><th style="width:50px">STT</th><th style="width:60px">Ảnh</th><th>Tiêu đề</th><th>Danh mục</th><th>Trạng thái</th><th>Ngày xuất bản</th><th style="width:100px">Thao tác</th></tr></thead>
          <tbody>
            <tr v-for="(row, index) in store.items" :key="row.id">
              <td class="text-center text-gray-400">{{ (store.meta.page - 1) * store.meta.size + index + 1 }}</td>
              <td>
                <img v-if="row.coverImagePath" :src="mediaUrl(row.coverImagePath)" class="h-10 w-14 rounded object-cover" />
                <span v-else class="inline-block h-10 w-14 rounded bg-gray-100"></span>
              </td>
              <td class="font-semibold">{{ row.title }}</td>
              <td class="text-gray-500">{{ row.categoryName }}</td>
              <td><StatusBadge :status="row.status" /></td>
              <td class="text-gray-500">{{ row.publishedAt }}</td>
              <td>
                <div class="cms-actions-cell">
                  <router-link :to="`/posts/${row.id}/edit`" class="cms-act-btn edit" title="Sửa">&#9998;</router-link>
                  <button @click="handleDelete(row.id)" class="cms-act-btn delete" title="Xóa">&#10005;</button>
                </div>
              </td>
            </tr>
            <tr v-if="!store.items.length && !store.loading"><td colspan="7" style="text-align:center;padding:40px;color:#9ca3af">Chưa có dữ liệu</td></tr>
            <tr v-if="!store.items.length && store.loading"><td colspan="7" style="text-align:center;padding:40px;color:#9ca3af">Đang tải...</td></tr>
          </tbody>
        </table>
      </div>

      <div v-if="store.meta.total > 0" class="flex items-center justify-between px-5 py-3 border-t border-gray-100 text-sm text-gray-600">
        <span>Tổng: {{ store.meta.total }} bài viết</span>
        <div v-if="store.meta.totalPages > 1" class="flex items-center gap-1">
          <button :disabled="store.meta.page <= 1" @click="goToPage(store.meta.page - 1)" class="cms-btn cms-btn-secondary cms-btn-sm" style="padding:4px 10px">&laquo;</button>
          <template v-for="p in visiblePages" :key="p">
            <button v-if="p === '...'" disabled class="px-1 text-gray-400">...</button>
            <button v-else @click="goToPage(p as number)" class="cms-btn cms-btn-sm" :class="p === store.meta.page ? 'cms-btn-primary' : 'cms-btn-secondary'" style="padding:4px 10px">{{ p }}</button>
          </template>
          <button :disabled="store.meta.page >= store.meta.totalPages" @click="goToPage(store.meta.page + 1)" class="cms-btn cms-btn-secondary cms-btn-sm" style="padding:4px 10px">&raquo;</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import StatusBadge from '@/components/shared/StatusBadge.vue'
import { usePostsStore } from '@/stores/posts'

const store = usePostsStore()
const keywordInput = ref(store.filters.keyword)
const categoryFilterInput = ref<number | null>(store.filters.categoryId)

function mediaUrl(path: string) {
  if (!path) return ''
  if (/^https?:\/\//i.test(path)) return path
  const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')
  return `${baseUrl}/${path.replace(/^\/+/, '')}`
}

const visiblePages = computed(() => {
  const { page, totalPages } = store.meta
  if (totalPages <= 7) return Array.from({ length: totalPages }, (_, i) => i + 1)
  const pages: (number | string)[] = []
  pages.push(1)
  if (page > 3) pages.push('...')
  for (let i = Math.max(2, page - 1); i <= Math.min(totalPages - 1, page + 1); i++) pages.push(i)
  if (page < totalPages - 2) pages.push('...')
  pages.push(totalPages)
  return pages
})

async function handleSearch() {
  store.setFilters(keywordInput.value, categoryFilterInput.value || null)
  await store.fetchList(1)
}

async function handleClearFilter() {
  keywordInput.value = ''
  categoryFilterInput.value = null
  store.setFilters('', null)
  await store.fetchList(1)
}

async function goToPage(p: number) {
  await store.fetchList(p)
}

async function handleDelete(id: number) {
  if (!confirm('Bạn có chắc muốn xóa bài viết này?')) return
  await store.remove(id)
  await store.fetchList()
}

onMounted(async () => {
  await Promise.all([store.fetchCategories(), store.fetchList()])
})
</script>
