<template>
  <PageShell>
    <PageHeader title="Trang tĩnh" description="Quản lý Trang chủ, Giới thiệu, Liên hệ và các trang tĩnh khác">
      <template #actions>
        <Button as="router-link" to="/pages/create" size="sm">＋ Thêm mới</Button>
      </template>
    </PageHeader>
    <AlertBox v-if="store.loading" message="Đang tải dữ liệu..." />

    <DataCard>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Tiêu đề</TableHead>
            <TableHead>Slug</TableHead>
            <TableHead>Trạng thái</TableHead>
            <TableHead>Ngày cập nhật</TableHead>
            <TableHead class="w-[100px] text-right">Thao tác</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="row in store.items" :key="row.id">
            <TableCell class="font-semibold">{{ row.title }}</TableCell>
            <TableCell class="text-slate-500">{{ row.slug }}</TableCell>
            <TableCell><StatusBadge :status="row.status" /></TableCell>
            <TableCell class="text-slate-500">{{ row.updatedAt }}</TableCell>
            <TableCell>
              <div class="flex justify-end gap-2">
                <ActionButton as="router-link" :to="`/pages/${row.id}/edit`" class="text-blue-600" title="Sửa">✎</ActionButton>
                <ActionButton variant="destructive" title="Xóa" @click="handleDelete(row.id)"><Trash2 class="h-4 w-4" /></ActionButton>
              </div>
            </TableCell>
          </TableRow>
          <TableRow v-if="!store.items.length">
            <TableCell colspan="5" class="h-32 text-center text-slate-400">Chưa có dữ liệu</TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { Trash2 } from 'lucide-vue-next'
import ActionButton from '@/components/shared/ActionButton.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import DataCard from '@/components/shared/DataCard.vue'
import StatusBadge from '@/components/shared/StatusBadge.vue'
import AlertBox from '@/components/shared/AlertBox.vue'
import { Button } from '@/components/ui/button'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { usePagesStore } from '@/stores/pages'

const store = usePagesStore()

async function handleDelete(id: number) {
  if (!confirm('Bạn có chắc muốn xóa trang này?')) return
  await store.remove(id)
  await store.fetchList()
}

onMounted(() => store.fetchList())
</script>
