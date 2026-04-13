<template>
  <div>
    <PageHeader title="Trang tĩnh" description="Quản lý Trang chủ, Giới thiệu, Liên hệ và các trang tĩnh khác">
      <template #actions>
        <router-link to="/pages/create" class="cms-btn cms-btn-primary cms-btn-sm">＋ Thêm mới</router-link>
      </template>
    </PageHeader>
    <AlertBox v-if="store.loading" message="Đang tải dữ liệu..." />
    <div class="cms-card mt-6">
      <div class="cms-table-wrap">
        <table class="cms-table">
          <thead>
            <tr><th>Tiêu đề</th><th>Slug</th><th>Trạng thái</th><th>Ngày cập nhật</th><th style="width:100px">Thao tác</th></tr>
          </thead>
          <tbody>
            <tr v-for="row in store.items" :key="row.id">
              <td class="font-semibold">{{ row.title }}</td>
              <td class="text-gray-500">{{ row.slug }}</td>
              <td><StatusBadge :status="row.status" /></td>
              <td class="text-gray-500">{{ row.updatedAt }}</td>
              <td>
                <div class="cms-actions-cell">
                  <router-link :to="`/pages/${row.id}/edit`" class="cms-act-btn edit" title="Sửa">&#9998;</router-link>
                  <button @click="handleDelete(row.id)" class="cms-act-btn delete" title="Xóa">&#10005;</button>
                </div>
              </td>
            </tr>
            <tr v-if="!store.items.length"><td colspan="5" style="text-align:center;padding:40px;color:#9ca3af">Chưa có dữ liệu</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import StatusBadge from '@/components/shared/StatusBadge.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { usePagesStore } from '@/stores/pages'

const store = usePagesStore()

async function handleDelete(id: number) {
  if (!confirm('Bạn có chắc muốn xóa trang này?')) return
  await store.remove(id)
  await store.fetchList()
}

onMounted(() => store.fetchList())
</script>
