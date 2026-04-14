<template>
  <PageShell>
    <PageHeader title="Thư viện Media" description="Quản lý ảnh và tài liệu, phục vụ upload icon, banner, hồ sơ năng lực">
      <template #actions>
        <Button as="router-link" to="/media/upload" size="sm">＋ Tải lên</Button>
      </template>
    </PageHeader>
    <DataCard>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Tên tệp</TableHead>
            <TableHead>Loại</TableHead>
            <TableHead>Dung lượng</TableHead>
            <TableHead>Ngày tải lên</TableHead>
            <TableHead class="w-24">Thao tác</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="row in store.items" :key="row.id">
            <TableCell class="font-semibold text-slate-900">{{ row.fileName }}</TableCell>
            <TableCell>{{ row.mimeType }}</TableCell>
            <TableCell>{{ row.sizeBytes }}</TableCell>
            <TableCell>{{ row.createdAt }}</TableCell>
            <TableCell>
              <ActionButton variant="ghost" size="icon" title="Xóa" class="text-slate-400 hover:text-rose-600" @click="handleDelete(row.id)">&#10005;</ActionButton>
            </TableCell>
          </TableRow>
          <TableRow v-if="!store.items.length">
            <TableCell colspan="5" class="py-10 text-center text-slate-400">{{ store.loading ? 'Đang tải...' : 'Chưa có dữ liệu' }}</TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import ActionButton from '@/components/shared/ActionButton.vue'
import DataCard from '@/components/shared/DataCard.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import { Button } from '@/components/ui/button'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useMediaStore } from '@/stores/media'

const store = useMediaStore()

async function handleDelete(id: number) {
  if (!confirm('Xác nhận xóa?')) return
  await store.remove(id)
}

onMounted(() => store.fetchList())
</script>
