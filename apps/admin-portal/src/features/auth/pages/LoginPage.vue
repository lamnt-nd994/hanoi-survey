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
        <FormField>
          <FormLabel>Tên đăng nhập</FormLabel>
          <Input v-model="username" placeholder="Nhập tên đăng nhập" autocomplete="username" />
        </FormField>
        <FormField>
          <FormLabel>Mật khẩu</FormLabel>
          <Input v-model="password" type="password" placeholder="Nhập mật khẩu" autocomplete="current-password" />
        </FormField>
        <div v-if="error" class="mb-4 rounded-md border border-rose-200 bg-rose-50 px-3 py-2 text-sm text-rose-700">{{ error }}</div>
        <Button type="submit" :disabled="loading" class="mt-2 w-full py-2.5">
          {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
        </Button>
      </form>
      <p class="mt-4 text-center text-xs text-gray-400">Bảo mật JWT & RBAC</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import FormField from '@/components/shared/FormField.vue'
import FormLabel from '@/components/shared/FormLabel.vue'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useAuthStore } from '@/stores/auth'
import type { ApiErrorLike } from '@/types/api'

const router = useRouter()
const authStore = useAuthStore()
const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function handleSubmit() {
  loading.value = true; error.value = ''
  try {
    await authStore.login(username.value, password.value)
    router.push('/dashboard')
  } catch (e: unknown) {
    const apiError = e as ApiErrorLike
    error.value = apiError.response?.data?.error?.message || 'Đăng nhập thất bại'
  } finally {
    loading.value = false
  }
}
</script>
