<template>
  <PageShell>
    <PageHeader title="Người dùng" description="Quản lý tài khoản đăng nhập CMS và gán vai trò truy cập.">
      <template #actions>
        <Button as="router-link" to="/users/create" size="sm">＋ Thêm mới</Button>
      </template>
    </PageHeader>

    <DataCard class="p-4">
      <div class="grid gap-3 md:grid-cols-[minmax(0,1fr)_220px_180px_auto]">
        <Input v-model="filters.keyword" placeholder="Tìm theo username, họ tên, email" @keyup.enter="applyFilters" />
        <Select v-model="filters.roleId">
          <option :value="''">Tất cả vai trò</option>
          <option v-for="role in store.roles" :key="role.id" :value="String(role.id)">{{ role.name }}</option>
        </Select>
        <Select v-model="filters.active">
          <option :value="''">Tất cả trạng thái</option>
          <option value="true">Đang hoạt động</option>
          <option value="false">Đã khóa</option>
        </Select>
        <div class="flex gap-2">
          <Button variant="secondary" @click="applyFilters">Lọc</Button>
          <Button v-if="hasActiveFilters" variant="ghost" @click="resetFilters">Xóa lọc</Button>
        </div>
      </div>

      <div v-if="hasActiveFilters" class="mt-3 flex flex-wrap gap-2 text-sm text-slate-600">
        <Badge v-if="filters.keyword" variant="secondary">Từ khóa: {{ filters.keyword }}</Badge>
        <Badge v-if="selectedRoleLabel" variant="secondary">Vai trò: {{ selectedRoleLabel }}</Badge>
        <Badge v-if="filters.active !== ''" variant="secondary">Trạng thái: {{ filters.active === 'true' ? 'Đang hoạt động' : 'Đã khóa' }}</Badge>
      </div>
    </DataCard>

    <DataCard>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Tài khoản</TableHead>
            <TableHead>Thông tin</TableHead>
            <TableHead>Vai trò</TableHead>
            <TableHead>Trạng thái</TableHead>
            <TableHead class="w-40">Thao tác</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="row in store.items" :key="row.id">
            <TableCell>
              <div class="font-semibold text-slate-900">{{ row.username }}</div>
              <div class="text-xs text-slate-400">#{{ row.id }}</div>
            </TableCell>
            <TableCell>
              <div class="font-medium text-slate-900">{{ row.fullName }}</div>
              <div class="text-sm text-slate-500">{{ row.email || 'Chưa cập nhật email' }}</div>
            </TableCell>
            <TableCell>
              <div class="flex flex-wrap gap-2">
                <Badge v-for="role in row.roles" :key="role.id" variant="secondary">{{ role.name }}</Badge>
              </div>
            </TableCell>
            <TableCell>
              <Badge :variant="row.active ? 'success' : 'secondary'">{{ row.active ? 'Đang hoạt động' : 'Đã khóa' }}</Badge>
            </TableCell>
            <TableCell>
              <div class="flex gap-1">
                <ActionButton title="Reset mật khẩu" class="text-slate-400 hover:text-slate-900" @click="handleResetPassword(row.id)">&#128274;</ActionButton>
                <ActionButton as="router-link" :to="`/users/${row.id}/edit`" title="Sửa" class="text-slate-400 hover:text-sky-600">&#9998;</ActionButton>
                <ActionButton title="Xóa" class="text-slate-400 hover:text-rose-600" @click="handleDelete(row.id)"><Trash2 class="h-4 w-4" /></ActionButton>
              </div>
            </TableCell>
          </TableRow>
          <TableRow v-if="!store.items.length">
            <TableCell colspan="5" class="py-10 text-center text-slate-400">Chưa có dữ liệu</TableCell>
          </TableRow>
        </TableBody>
      </Table>

      <div v-if="store.meta.totalPages > 1" class="flex items-center justify-between border-t border-slate-100 px-5 py-3 text-sm text-slate-600">
        <span>Tổng: {{ store.meta.total }} người dùng</span>
        <div class="flex items-center gap-1">
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
import { computed, onMounted, reactive } from 'vue'
import { Trash2 } from 'lucide-vue-next'
import ActionButton from '@/components/shared/ActionButton.vue'
import DataCard from '@/components/shared/DataCard.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select } from '@/components/ui/select'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useUsersStore } from '@/stores/users'

const store = useUsersStore()
const filters = reactive({ keyword: '', roleId: '', active: '' })
const hasActiveFilters = computed(() => !!filters.keyword || !!filters.roleId || filters.active !== '')
const selectedRoleLabel = computed(() => {
  const role = store.roles.find((item) => item.id === Number(filters.roleId))
  return role ? role.name : ''
})

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

function parseBoolean(value: string) {
  if (value === 'true') return true
  if (value === 'false') return false
  return null
}

async function loadData(page = 1) {
  await Promise.all([
    store.fetchRoles(),
    store.fetchList({
      page,
      size: 20,
      keyword: filters.keyword || undefined,
      roleId: filters.roleId ? Number(filters.roleId) : null,
      active: parseBoolean(filters.active),
    }),
  ])
}

async function applyFilters() {
  await loadData(1)
}

async function goToPage(page: number) {
  await loadData(page)
}

async function resetFilters() {
  filters.keyword = ''
  filters.roleId = ''
  filters.active = ''
  await loadData(1)
}

async function handleDelete(id: number) {
  if (!confirm('Xác nhận xóa người dùng này?')) return
  await store.remove(id)
  await loadData(store.meta.page)
}

async function handleResetPassword(id: number) {
  const password = window.prompt('Nhập mật khẩu mới cho tài khoản này')
  if (password === null) return
  if (!password.trim()) {
    alert('Mật khẩu mới không được để trống')
    return
  }
  await store.resetPassword(id, password.trim())
  alert('Đã cập nhật mật khẩu mới')
}

onMounted(loadData)
</script>
