<template>
  <div>
    <PageHeader title="Dịch vụ" description="Quản lý lĩnh vực khảo sát, icon, nội dung và phân loại">
      <template #actions>
        <router-link to="/services/create" class="cms-btn cms-btn-primary cms-btn-sm">＋ Thêm mới</router-link>
      </template>
    </PageHeader>
    <div class="cms-card mt-6">
      <div class="cms-table-wrap">
        <table class="cms-table">
          <thead><tr><th>Tên dịch vụ</th><th>Danh mục</th><th>Slug</th><th>Trạng thái</th><th style="width:100px">Thao tác</th></tr></thead>
          <tbody>
            <tr v-for="row in store.items" :key="row.id">
              <td class="font-semibold">{{ row.title }}</td>
              <td class="text-gray-500">{{ row.serviceCategoryName }}</td>
              <td class="text-gray-500">{{ row.slug }}</td>
              <td><StatusBadge :status="row.status" /></td>
              <td>
                <div class="cms-actions-cell">
                  <router-link :to="`/services/${row.id}/edit`" class="cms-act-btn edit" title="Sửa">&#9998;</router-link>
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
import { useServicesStore } from '@/stores/services'

const store = useServicesStore()
async function handleDelete(id: number) { if (!confirm('Xác nhận xóa?')) return; await store.remove(id); await store.fetchList() }
onMounted(() => store.fetchList())
</script>
