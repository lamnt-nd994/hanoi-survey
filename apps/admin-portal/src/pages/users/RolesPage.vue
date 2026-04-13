<template>
  <div>
    <PageHeader title="Vai trò người dùng" description="Tạo vai trò mới, cấu hình khả năng truy cập CMS và gán cho người dùng." />

    <div class="mt-6 grid gap-6 lg:grid-cols-[minmax(0,1.4fr)_minmax(320px,0.9fr)]">
      <div class="cms-card overflow-hidden">
        <div v-if="errorMessage" class="m-4 rounded-xl border border-slate-300 bg-slate-100 px-4 py-3 text-sm text-slate-700">{{ errorMessage }}</div>
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-slate-200 text-sm">
            <thead class="bg-slate-50 text-left text-slate-500">
              <tr>
                <th class="px-4 py-3 font-semibold">Tên vai trò</th>
                <th class="px-4 py-3 font-semibold">Mã vai trò</th>
                <th class="px-4 py-3 font-semibold">Truy cập CMS</th>
                <th class="px-4 py-3 font-semibold text-right">Thao tác</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100 bg-white text-slate-700">
              <tr v-for="role in store.roles" :key="role.id">
                <td class="px-4 py-3">
                  <div class="font-medium text-slate-900">{{ role.name }}</div>
                </td>
                <td class="px-4 py-3 text-slate-500">{{ role.code }}</td>
                <td class="px-4 py-3">
                  <span class="inline-flex rounded-full px-2.5 py-1 text-xs font-semibold" :class="role.adminAccess ? 'bg-emerald-100 text-emerald-700' : 'bg-slate-100 text-slate-500'">
                    {{ role.adminAccess ? 'Có' : 'Không' }}
                  </span>
                </td>
                <td class="px-4 py-3">
                  <div class="flex justify-end gap-2">
                    <button type="button" class="rounded-lg border border-slate-300 px-3 py-1.5 text-xs font-medium transition" :class="isLockedRole(role.code) ? 'cursor-not-allowed bg-slate-100 text-slate-400' : 'text-slate-700 hover:bg-slate-50'" :disabled="isLockedRole(role.code)" @click="startEdit(role)">Sửa</button>
                    <button type="button" class="rounded-lg border border-slate-300 px-3 py-1.5 text-xs font-medium transition" :class="isLockedRole(role.code) ? 'cursor-not-allowed bg-slate-100 text-slate-400' : 'text-slate-700 hover:bg-slate-100'" :disabled="isLockedRole(role.code)" @click="handleDelete(role.id)">Xóa</button>
                  </div>
                </td>
              </tr>
              <tr v-if="!store.roles.length">
                <td colspan="4" class="px-4 py-10 text-center text-slate-400">Chưa có vai trò.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="cms-card p-5">
        <div class="flex items-center justify-between gap-3">
          <div>
            <h3 class="text-base font-semibold text-slate-900">{{ roleForm.id ? 'Cập nhật vai trò' : 'Tạo vai trò mới' }}</h3>
            <p class="mt-1 text-sm text-slate-500">Mã vai trò sẽ được chuẩn hóa uppercase khi lưu.</p>
            <p v-if="roleForm.code === 'ROLE_ADMIN'" class="mt-1 text-xs text-amber-600">ROLE_ADMIN là vai trò hệ thống và bị khóa chỉnh sửa trên giao diện.</p>
          </div>
          <button v-if="roleForm.id" type="button" class="text-sm font-medium text-slate-500 hover:text-slate-900" @click="resetForm">Tạo mới</button>
        </div>

        <form class="mt-5 space-y-4" @submit.prevent="handleSubmit">
          <div>
            <label class="cms-form-label">Tên vai trò <span class="required">*</span></label>
            <input v-model="roleForm.name" class="cms-form-control" placeholder="Biên tập viên nội dung" :disabled="isLockedRole(roleForm.code)" required />
          </div>

          <div>
            <label class="cms-form-label">Mã vai trò <span class="required">*</span></label>
            <input v-model="roleForm.code" class="cms-form-control" placeholder="ROLE_EDITOR" :disabled="isLockedRole(roleForm.code)" required />
          </div>

          <div class="flex items-end">
            <label class="flex min-h-[44px] items-center gap-3 rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm font-medium text-slate-700">
              <input v-model="roleForm.adminAccess" type="checkbox" class="h-4 w-4 rounded border-slate-300 text-slate-900 focus:ring-slate-400" :disabled="isLockedRole(roleForm.code)" />
              Cho phép truy cập CMS
            </label>
          </div>

          <button type="submit" class="cms-btn cms-btn-primary w-full" :disabled="saving || isLockedRole(roleForm.code)">
            {{ saving ? 'Đang lưu...' : (roleForm.id ? 'Cập nhật vai trò' : 'Tạo vai trò') }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import { useUsersStore } from '@/stores/users'
import type { AdminRoleEntity, AdminRolePayload } from '@/types'

const store = useUsersStore()
const saving = ref(false)
const errorMessage = ref('')
const roleForm = reactive<{ id: number | null } & AdminRolePayload>({
  id: null,
  code: '',
  name: '',
  adminAccess: false,
})

function extractErrorMessage(error: any) {
  const details = error.response?.data?.error?.details
  if (Array.isArray(details) && details.length) {
    return details.map((item: { field?: string; message?: string }) => `${item.field || 'field'}: ${item.message || 'Không hợp lệ'}`).join('; ')
  }
  return error.response?.data?.error?.message || 'Thao tác thất bại'
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
  } catch (error: any) {
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
  } catch (error: any) {
    errorMessage.value = extractErrorMessage(error)
  }
}

onMounted(async () => {
  await store.fetchRoles()
})
</script>
