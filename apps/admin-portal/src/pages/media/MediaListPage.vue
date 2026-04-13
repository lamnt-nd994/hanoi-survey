<template>
  <div>
    <PageHeader title="Thư viện Media" description="Quản lý ảnh và tài liệu, phục vụ upload icon, banner, hồ sơ năng lực">
      <template #actions>
        <router-link to="/media/upload" class="cms-btn cms-btn-primary cms-btn-sm">＋ Tải lên</router-link>
      </template>
    </PageHeader>
    <div class="cms-card mt-6">
      <div class="cms-table-wrap">
        <table class="cms-table">
          <thead><tr><th>Tên tệp</th><th>Loại</th><th>Dung lượng</th><th>Ngày tải lên</th><th style="width:100px">Thao tác</th></tr></thead>
          <tbody>
            <tr v-for="row in store.items" :key="row.id">
              <td class="font-semibold">{{ row.fileName }}</td>
              <td class="text-gray-500">{{ row.mimeType }}</td>
              <td class="text-gray-500">{{ row.sizeBytes }}</td>
              <td class="text-gray-500">{{ row.createdAt }}</td>
              <td>
                <div class="cms-actions-cell">
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
import { useMediaStore } from '@/stores/media'

const store = useMediaStore()
async function handleDelete(id: number) { if (!confirm('Xác nhận xóa?')) return; await store.remove(id); await store.fetchList() }
onMounted(() => store.fetchList())
</script>
