<template>
  <PageShell>
    <PageHeader title="Vai trò người dùng" description="Tạo vai trò mới, cấu hình khả năng truy cập CMS và gán cho người dùng." />

    <div class="mt-6 grid gap-6 lg:grid-cols-[minmax(0,1.4fr)_minmax(320px,0.9fr)]">
      <DataCard>
        <div v-if="errorMessage" class="m-4 rounded-xl border border-slate-300 bg-slate-100 px-4 py-3 text-sm text-slate-700">{{ errorMessage }}</div>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Tên vai trò</TableHead>
              <TableHead>Mã vai trò</TableHead>
              <TableHead>Truy cập CMS</TableHead>
              <TableHead class="text-right">Thao tác</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow v-for="role in store.roles" :key="role.id">
              <TableCell class="font-medium text-slate-900">{{ role.name }}</TableCell>
              <TableCell class="text-slate-500">{{ role.code }}</TableCell>
              <TableCell>
                <Badge :variant="role.adminAccess ? 'success' : 'secondary'">{{ role.adminAccess ? 'Có' : 'Không' }}</Badge>
              </TableCell>
              <TableCell>
                <div class="flex justify-end gap-2">
                  <Button type="button" variant="secondary" size="sm" :disabled="isLockedRole(role.code)" @click="startEdit(role)">Sửa</Button>
                  <Button type="button" variant="outline" size="sm" :disabled="isLockedRole(role.code)" @click="handleDelete(role.id)">Xóa</Button>
                </div>
              </TableCell>
            </TableRow>
            <TableRow v-if="!store.roles.length">
              <TableCell colspan="4" class="h-32 text-center text-slate-400">Chưa có vai trò.</TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </DataCard>

      <DataCard class="p-5">
        <div class="flex items-center justify-between gap-3">
          <div>
            <h3 class="text-base font-semibold text-slate-900">{{ roleForm.id ? 'Cập nhật vai trò' : 'Tạo vai trò mới' }}</h3>
            <p class="mt-1 text-sm text-slate-500">Mã vai trò sẽ được chuẩn hóa uppercase khi lưu.</p>
            <p v-if="roleForm.code === 'ROLE_ADMIN'" class="mt-1 text-xs text-amber-600">ROLE_ADMIN là vai trò hệ thống và bị khóa chỉnh sửa trên giao diện.</p>
          </div>
          <Button v-if="roleForm.id" type="button" variant="ghost" size="sm" @click="resetForm">Tạo mới</Button>
        </div>

        <form class="mt-5 space-y-4" @submit.prevent="handleSubmit">
          <FormField>
            <FormLabel>Tên vai trò <span class="text-rose-600">*</span></FormLabel>
            <Input v-model="roleForm.name" placeholder="Biên tập viên nội dung" :disabled="isLockedRole(roleForm.code)" required />
          </FormField>

          <FormField>
            <FormLabel>Mã vai trò <span class="text-rose-600">*</span></FormLabel>
            <Input v-model="roleForm.code" placeholder="ROLE_EDITOR" :disabled="isLockedRole(roleForm.code)" required />
          </FormField>

          <div class="flex items-end">
            <label class="flex min-h-[44px] items-center gap-3 rounded-md border border-slate-200 bg-white px-4 py-3 text-sm font-medium text-slate-700">
              <input v-model="roleForm.adminAccess" type="checkbox" class="h-4 w-4 rounded border-slate-300 text-slate-900 focus:ring-slate-400" :disabled="isLockedRole(roleForm.code)" />
              Cho phép truy cập CMS
            </label>
          </div>

          <Button type="submit" class="w-full" :disabled="saving || isLockedRole(roleForm.code)">
            {{ saving ? 'Đang lưu...' : (roleForm.id ? 'Cập nhật vai trò' : 'Tạo vai trò') }}
          </Button>
        </form>
      </DataCard>
    </div>
  </PageShell>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import DataCard from '@/components/shared/DataCard.vue'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useUsersStore } from '@/stores/users'
import type { AdminRoleEntity, AdminRolePayload } from '@/types'
import type { ApiErrorLike } from '@/types/api'

const store = useUsersStore()
const saving = ref(false)
const errorMessage = ref('')
const roleForm = reactive<{ id: number | null } & AdminRolePayload>({
  id: null,
  code: '',
  name: '',
  adminAccess: false,
})

function extractErrorMessage(error: unknown) {
  const apiError = error as ApiErrorLike
  const details = apiError.response?.data?.error?.details
  if (Array.isArray(details) && details.length) {
    return details.map((item: { field?: string; message?: string }) => `${item.field || 'field'}: ${item.message || 'Không hợp lệ'}`).join('; ')
  }
  return apiError.response?.data?.error?.message || 'Thao tác thất bại'
}

function resetForm() {
  roleForm.id = null
  roleForm.code = ''
  roleForm.name = ''
  roleForm.adminAccess = false
  errorMessage.value = ''
}

function startEdit(role: AdminRoleEntity) {
  roleForm.id = role.id
  roleForm.code = role.code
  roleForm.name = role.name
  roleForm.adminAccess = role.adminAccess
  errorMessage.value = ''
}

function isLockedRole(code: string) {
  return code === 'ROLE_ADMIN'
}

async function handleSubmit() {
  saving.value = true
  errorMessage.value = ''
  try {
    const payload: AdminRolePayload = {
      code: roleForm.code.trim(),
      name: roleForm.name.trim(),
      adminAccess: roleForm.adminAccess,
    }
    if (roleForm.id) await store.updateRole(roleForm.id, payload)
    else await store.createRole(payload)
    resetForm()
  } catch (error: unknown) {
    errorMessage.value = extractErrorMessage(error)
  } finally {
    saving.value = false
  }
}

async function handleDelete(id: number) {
  if (!confirm('Xác nhận xóa vai trò này?')) return
  errorMessage.value = ''
  try {
    await store.removeRole(id)
    if (roleForm.id === id) resetForm()
  } catch (error: unknown) {
    errorMessage.value = extractErrorMessage(error)
  }
}

onMounted(async () => {
  await store.fetchRoles()
})
</script>
