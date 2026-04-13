<template>
  <div class="flex min-h-screen items-center justify-center bg-gray-50 p-4">
    <div class="w-full max-w-sm">
      <div class="mb-8 text-center">
        <div class="mx-auto mb-3 flex h-10 w-10 items-center justify-center rounded-lg bg-emerald-600 text-sm font-bold text-white">H</div>
        <h1 class="text-lg font-bold text-gray-900">Hanoi Survey</h1>
        <p class="mt-1 text-xs text-gray-400">Hệ thống quản trị nội dung</p>
      </div>
      <form @submit.prevent="handleSubmit" class="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
        <h2 class="text-base font-bold text-gray-900">Đăng nhập hệ thống</h2>
        <p class="mt-1 text-xs text-gray-400 mb-6">Nhập thông tin tài khoản để truy cập</p>
        <div class="cms-form-group">
          <label class="cms-form-label">Tên đăng nhập</label>
          <input v-model="username" class="cms-form-control" placeholder="Nhập tên đăng nhập" />
        </div>
        <div class="cms-form-group">
          <label class="cms-form-label">Mật khẩu</label>
          <input type="password" v-model="password" class="cms-form-control" placeholder="Nhập mật khẩu" />
        </div>
        <div v-if="error" class="cms-alert cms-alert-error mb-4">{{ error }}</div>
        <button type="submit" :disabled="loading" class="cms-btn cms-btn-primary w-full justify-center py-2.5 mt-2">
          {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
        </button>
      </form>
      <p class="mt-4 text-center text-xs text-gray-400">Bảo mật JWT & RBAC</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const username = ref('admin')
const password = ref('Admin@123')
const error = ref('')
const loading = ref(false)

async function handleSubmit() {
  loading.value = true; error.value = ''
  try {
    await authStore.login(username.value, password.value)
    router.push('/dashboard')
  } catch (e: any) {
    error.value = e.response?.data?.error?.message || 'Đăng nhập thất bại'
  } finally {
    loading.value = false
  }
}
</script>
