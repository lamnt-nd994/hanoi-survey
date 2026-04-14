<template>
  <PageShell>
    <PageHeader title="Bài viết" description="Quản lý bài viết tin tức, kiến thức và phân loại theo danh mục">
      <template #actions>
        <Button as="router-link" to="/posts/create" size="sm">＋ Thêm mới</Button>
      </template>
    </PageHeader>

    <DataCard>
      <div class="flex flex-wrap items-center gap-3 border-b border-slate-100 px-5 py-4">
        <div class="relative min-w-[200px] max-w-xs flex-1">
          <svg class="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
          <Input v-model="keywordInput" placeholder="Tìm theo tiêu đề..." class="pl-9" @keyup.enter="handleSearch" />
        </div>
        <Select v-model.number="categoryFilterInput" class="max-w-[200px]" @change="handleSearch">
          <option :value="null">Tất cả danh mục</option>
          <option v-for="cat in store.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </Select>
        <Button variant="secondary" size="sm" @click="handleSearch">Tìm</Button>
        <Button v-if="store.filters.keyword || store.filters.categoryId" variant="ghost" size="sm" @click="handleClearFilter">Xóa bộ lọc</Button>
      </div>

      <Table>
        <TableHeader>
          <TableRow>
            <TableHead class="w-14 text-center">STT</TableHead>
            <TableHead class="w-16">Ảnh</TableHead>
            <TableHead>Tiêu đề</TableHead>
            <TableHead>Danh mục</TableHead>
            <TableHead>Trạng thái</TableHead>
            <TableHead>Ngày xuất bản</TableHead>
            <TableHead class="w-28">Thao tác</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="(row, index) in store.items" :key="row.id">
            <TableCell class="text-center text-slate-400">{{ (store.meta.page - 1) * store.meta.size + index + 1 }}</TableCell>
            <TableCell>
              <img v-if="row.coverImagePath" :src="mediaUrl(row.coverImagePath)" width="56" height="40" loading="lazy" decoding="async" class="h-10 w-14 rounded object-cover" />
              <span v-else class="inline-block h-10 w-14 rounded bg-slate-100"></span>
            </TableCell>
            <TableCell class="font-semibold text-slate-900">{{ row.title }}</TableCell>
            <TableCell>{{ row.categoryName }}</TableCell>
            <TableCell><StatusBadge :status="row.status" /></TableCell>
            <TableCell>{{ row.publishedAt }}</TableCell>
            <TableCell>
              <div class="flex gap-1">
                <ActionButton as="router-link" :to="`/posts/${row.id}/edit`" title="Sửa" class="text-slate-400 hover:text-sky-600">&#9998;</ActionButton>
                <ActionButton title="Xóa" class="text-slate-400 hover:text-rose-600" @click="handleDelete(row.id)">&#10005;</ActionButton>
              </div>
            </TableCell>
          </TableRow>
          <TableRow v-if="!store.items.length">
            <TableCell colspan="7" class="py-10 text-center text-slate-400">{{ store.loading ? 'Đang tải...' : 'Chưa có dữ liệu' }}</TableCell>
          </TableRow>
        </TableBody>
      </Table>

      <div v-if="store.meta.total > 0" class="flex items-center justify-between border-t border-slate-100 px-5 py-3 text-sm text-slate-600">
        <span>Tổng: {{ store.meta.total }} bài viết</span>
        <div v-if="store.meta.totalPages > 1" class="flex items-center gap-1">
          <Button :disabled="store.meta.page <= 1" variant="secondary" size="sm" @click="goToPage(store.meta.page - 1)">&laquo;</Button>
          <template v-for="p in visiblePages" :key="p">
            <Button v-if="p === '...'" disabled variant="ghost" size="sm">...</Button>
            <Button v-else :variant="p === store.meta.page ? 'default' : 'secondary'" size="sm" @click="goToPage(p as number)">{{ p }}</Button>
          </template>
          <Button :disabled="store.meta.page >= store.meta.totalPages" variant="secondary" size="sm" @click="goToPage(store.meta.page + 1)">&raquo;</Button>
        </div>
      </div>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import ActionButton from '@/components/shared/ActionButton.vue'
import DataCard from '@/components/shared/DataCard.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import StatusBadge from '@/components/shared/StatusBadge.vue'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select } from '@/components/ui/select'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { resolveMediaUrl } from '@/lib/media'
import { usePostsStore } from '@/stores/posts'

const store = usePostsStore()
const keywordInput = ref(store.filters.keyword)
const categoryFilterInput = ref<number | null>(store.filters.categoryId)

function mediaUrl(path: string) {
  return resolveMediaUrl(path)
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
