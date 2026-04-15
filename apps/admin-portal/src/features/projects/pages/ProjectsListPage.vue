<template>
  <PageShell>
    <PageHeader title="Dự án" description="Quản lý hồ sơ dự án, vị trí, chủ đầu tư, mô tả và thư viện ảnh">
      <template #actions>
        <Button as="router-link" to="/projects/create" size="sm">＋ Thêm mới</Button>
      </template>
    </PageHeader>

    <DataCard>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Tên dự án</TableHead>
            <TableHead>Danh mục</TableHead>
            <TableHead>Địa điểm</TableHead>
            <TableHead>Trạng thái</TableHead>
            <TableHead class="w-[100px] text-right">Thao tác</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="row in store.items" :key="row.id">
            <TableCell class="font-semibold">{{ row.title }}</TableCell>
            <TableCell class="text-slate-500">{{ row.categoryName }}</TableCell>
            <TableCell class="text-slate-500">{{ row.location }}</TableCell>
            <TableCell><StatusBadge :status="row.status" /></TableCell>
            <TableCell>
              <div class="flex justify-end gap-2">
                <ActionButton as="router-link" :to="`/projects/${row.id}/edit`" class="text-blue-600" title="Sửa">✎</ActionButton>
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
import DataCard from '@/components/shared/DataCard.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
import StatusBadge from '@/components/shared/StatusBadge.vue'
import { Button } from '@/components/ui/button'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useProjectsStore } from '@/stores/projects'

const store = useProjectsStore()

async function handleDelete(id: number) {
  if (!confirm('Xác nhận xóa?')) return
  await store.remove(id)
  await store.fetchList()
}

onMounted(() => store.fetchList())
</script>
