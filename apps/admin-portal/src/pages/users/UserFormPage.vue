<template>
  <div>
    <PageHeader :title="isEdit ? 'Cập nhật người dùng' : 'Tạo người dùng'" description="Thiết lập thông tin tài khoản, trạng thái và các vai trò được gán." />

    <div class="cms-card mt-6 p-6">
      <div v-if="errorMessage" class="mb-5 rounded-xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">{{ errorMessage }}</div>

      <form class="space-y-6" @submit.prevent="handleSubmit">
        <div class="grid gap-5 md:grid-cols-2">
          <div class="cms-form-group">
            <label class="cms-form-label">Tên đăng nhập <span class="required">*</span></label>
            <input v-model="form.username" class="cms-form-control" placeholder="admin01" required />
          </div>

          <div class="cms-form-group">
            <label class="cms-form-label">Họ tên <span class="required">*</span></label>
            <input v-model="form.fullName" class="cms-form-control" placeholder="Nguyễn Văn A" required />
          </div>

          <div class="cms-form-group">
            <label class="cms-form-label">Email</label>
            <input v-model="form.email" type="email" class="cms-form-control" placeholder="admin@hanoisurvey.vn" />
          </div>

          <div class="cms-form-group">
            <label class="cms-form-label">{{ isEdit ? 'Mật khẩu mới' : 'Mật khẩu' }} <span v-if="!isEdit" class="required">*</span></label>
            <input v-model="form.password" type="password" class="cms-form-control" :placeholder="isEdit ? 'Để trống nếu không đổi' : 'Nhập mật khẩu đăng nhập'" :required="!isEdit" />
          </div>
        </div>

        <div class="cms-form-group">
          <label class="cms-form-label">Vai trò <span class="required">*</span></label>
          <div class="grid gap-3 sm:grid-cols-2 xl:grid-cols-3">
            <label v-for="role in store.roles" :key="role.id" class="flex min-h-[52px] items-center gap-3 rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700">
              <input v-model="form.roleIds" type="checkbox" :value="role.id" class="h-4 w-4 rounded border-slate-300 focus:ring-slate-400" />
              <span>
                <span class="block font-medium text-slate-900">{{ role.name }}</span>
                <span class="block text-xs text-slate-500">{{ role.code }}<span v-if="role.adminAccess"> • Truy cập CMS</span></span>
              </span>
            </label>
          </div>
        </div>

        <div class="flex items-center justify-between gap-4 rounded-xl border border-slate-200 bg-slate-50 px-4 py-3">
          <label class="flex items-center gap-3 text-sm font-medium text-slate-700">
            <input v-model="form.active" type="checkbox" class="h-4 w-4 rounded border-slate-300 text-slate-900 focus:ring-slate-400" />
            Kích hoạt tài khoản
          </label>
          <div class="flex gap-3">
            <router-link to="/users" class="cms-btn cms-btn-secondary">Quay lại</router-link>
            <button type="submit" class="cms-btn cms-btn-primary" :disabled="saving">{{ saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật người dùng' : 'Tạo người dùng') }}</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageHeader from '@/components/shared/PageHeader.vue'
import { useUsersStore } from '@/stores/users'
import type { AdminUserPayload } from '@/types'

const route = useRoute()
const router = useRouter()
const store = useUsersStore()
const saving = ref(false)
const errorMessage = ref('')

const isEdit = computed(() => !!route.params.id)
const form = reactive<AdminUserPayload>({
  username: '',
  password: '',
  fullName: '',
  email: '',
  active: true,
  roleIds: [],
})

function extractErrorMessage(error: any) {
  const details = error.response?.data?.error?.details
  if (Array.isArray(details) && details.length) {
    return details.map((item: { field?: string; message?: string }) => `${item.field || 'field'}: ${item.message || 'Không hợp lệ'}`).join('; ')
  }
  return error.response?.data?.error?.message || 'Thao tác thất bại'
}

async function loadDetail() {
  if (!isEdit.value) return
  const user = await store.getById(Number(route.params.id))
  form.username = user.username
  form.password = ''
  form.fullName = user.fullName
  form.email = user.email || ''
  form.active = user.active
  form.roleIds = user.roles.map(role => role.id)
}

async function handleSubmit() {
  if (!form.roleIds.length) {
    errorMessage.value = 'Vui lòng chọn ít nhất một vai trò'
    return
  }

  saving.value = true
  errorMessage.value = ''
  try {
    const payload: AdminUserPayload = {
      username: form.username.trim(),
      password: form.password,
      fullName: form.fullName.trim(),
      email: form.email.trim(),
      active: form.active,
      roleIds: form.roleIds,
    }
    if (isEdit.value) await store.update(Number(route.params.id), payload)
    else await store.create(payload)
    router.push('/users')
  } catch (error: any) {
    errorMessage.value = extractErrorMessage(error)
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  await store.fetchRoles()
  await loadDetail()
})
</script>
