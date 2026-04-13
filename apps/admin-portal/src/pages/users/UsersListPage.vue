<template>
  <div>
    <PageHeader title="Người dùng" description="Quản lý tài khoản đăng nhập CMS và gán vai trò truy cập.">
      <template #actions>
        <router-link to="/users/create" class="cms-btn cms-btn-primary cms-btn-sm">＋ Thêm mới</router-link>
      </template>
    </PageHeader>

    <div class="cms-card mt-6 p-4">
      <div class="grid gap-3 md:grid-cols-[minmax(0,1fr)_220px_180px_auto]">
        <input v-model="filters.keyword" class="cms-form-control" placeholder="Tìm theo username, họ tên, email" @keyup.enter="applyFilters" />
        <select v-model="filters.roleId" class="cms-form-control">
          <option :value="''">Tất cả vai trò</option>
          <option v-for="role in store.roles" :key="role.id" :value="String(role.id)">{{ role.name }}</option>
        </select>
        <select v-model="filters.active" class="cms-form-control">
          <option :value="''">Tất cả trạng thái</option>
          <option value="true">Đang hoạt động</option>
          <option value="false">Đã khóa</option>
        </select>
        <div class="flex gap-2">
          <button class="cms-btn cms-btn-secondary" @click="applyFilters">Lọc</button>
          <button v-if="hasActiveFilters" class="cms-btn cms-btn-sm" style="color:#6b7280" @click="resetFilters">Xóa lọc</button>
        </div>
      </div>

      <div v-if="hasActiveFilters" class="mt-3 flex flex-wrap gap-2 text-sm text-slate-600">
        <span v-if="filters.keyword" class="rounded-full bg-slate-100 px-3 py-1">Từ khóa: {{ filters.keyword }}</span>
        <span v-if="selectedRoleLabel" class="rounded-full bg-slate-100 px-3 py-1">Vai trò: {{ selectedRoleLabel }}</span>
        <span v-if="filters.active !== ''" class="rounded-full bg-slate-100 px-3 py-1">Trạng thái: {{ filters.active === 'true' ? 'Đang hoạt động' : 'Đã khóa' }}</span>
      </div>
    </div>

    <div class="cms-card mt-6">
      <div class="cms-table-wrap">
        <table class="cms-table">
          <thead>
            <tr>
              <th>Tài khoản</th>
              <th>Thông tin</th>
              <th>Vai trò</th>
              <th>Trạng thái</th>
              <th style="width:150px">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in store.items" :key="row.id">
              <td>
                <div class="font-semibold">{{ row.username }}</div>
                <div class="text-xs text-gray-400">#{{ row.id }}</div>
              </td>
              <td>
                <div class="font-medium text-slate-900">{{ row.fullName }}</div>
                <div class="text-sm text-gray-500">{{ row.email || 'Chưa cập nhật email' }}</div>
              </td>
              <td>
                <div class="flex flex-wrap gap-2">
                  <span v-for="role in row.roles" :key="role.id" class="inline-flex rounded-full bg-slate-100 px-2.5 py-1 text-xs font-semibold text-slate-700">
                    {{ role.name }}
                  </span>
                </div>
              </td>
              <td>
                <span class="inline-flex rounded-full px-2.5 py-1 text-xs font-semibold" :class="row.active ? 'bg-emerald-100 text-emerald-700' : 'bg-slate-200 text-slate-600'">
                  {{ row.active ? 'Đang hoạt động' : 'Đã khóa' }}
                </span>
              </td>
              <td>
                <div class="cms-actions-cell">
                  <button class="cms-act-btn" title="Reset mật khẩu" @click="handleResetPassword(row.id)">&#128274;</button>
                  <router-link :to="`/users/${row.id}/edit`" class="cms-act-btn edit" title="Sửa">&#9998;</router-link>
                  <button class="cms-act-btn delete" title="Xóa" @click="handleDelete(row.id)">&#10005;</button>
                </div>
              </td>
            </tr>
            <tr v-if="!store.items.length">
              <td colspan="5" style="text-align:center;padding:40px;color:#9ca3af">Chưa có dữ liệu</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="store.meta.totalPages > 1" class="flex items-center justify-between border-t border-gray-100 px-5 py-3 text-sm text-gray-600">
        <span>Tổng: {{ store.meta.total }} người dùng</span>
        <div class="flex items-center gap-1">
          <button :disabled="store.meta.page <= 1" class="cms-btn cms-btn-secondary cms-btn-sm" style="padding:4px 10px" @click="goToPage(store.meta.page - 1)">&laquo;</button>
          <template v-for="p in visiblePages" :key="p">
            <button v-if="p === '...'" disabled class="px-1 text-gray-400">...</button>
            <button v-else class="cms-btn cms-btn-sm" :class="p === store.meta.page ? 'cms-btn-primary' : 'cms-btn-secondary'" style="padding:4px 10px" @click="goToPage(p as number)">{{ p }}</button>
          </template>
          <button :disabled="store.meta.page >= store.meta.totalPages" class="cms-btn cms-btn-secondary cms-btn-sm" style="padding:4px 10px" @click="goToPage(store.meta.page + 1)">&raquo;</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive } from 'vue'
import PageHeader from '@/components/shared/PageHeader.vue'
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
