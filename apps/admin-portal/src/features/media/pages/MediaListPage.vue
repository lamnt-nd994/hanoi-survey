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
            <TableHead class="w-20">Xem trước</TableHead>
            <TableHead>Tên tệp</TableHead>
            <TableHead>Loại</TableHead>
            <TableHead>Dung lượng</TableHead>
            <TableHead>Ngày tải lên</TableHead>
            <TableHead class="w-24">Thao tác</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="row in store.items" :key="row.id">
            <TableCell>
              <div v-if="isImage(row.mimeType)" class="overflow-hidden rounded-md border border-slate-200 bg-slate-50">
                <img :src="mediaUrl(row.storagePath)" :alt="row.fileName" class="h-12 w-16 object-cover" loading="lazy" decoding="async" />
              </div>
              <div v-else class="flex h-12 w-16 items-center justify-center rounded-md border border-slate-200 bg-slate-50 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
                File
              </div>
            </TableCell>
            <TableCell class="font-semibold text-slate-900">{{ row.fileName }}</TableCell>
            <TableCell>{{ row.mimeType }}</TableCell>
            <TableCell>{{ row.sizeBytes }}</TableCell>
            <TableCell>{{ row.createdAt }}</TableCell>
            <TableCell>
              <ActionButton variant="ghost" size="icon" title="Xóa" class="text-slate-400 hover:text-rose-600" @click="handleDelete(row.id)"><Trash2 class="h-4 w-4" /></ActionButton>
            </TableCell>
          </TableRow>
          <TableRow v-if="!store.items.length">
            <TableCell colspan="6" class="py-10 text-center text-slate-400">{{ store.loading ? 'Đang tải...' : 'Chưa có dữ liệu' }}</TableCell>
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
import { Button } from '@/components/ui/button'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { isImageMimeType, resolveMediaUrl } from '@/lib/media'
import { useMediaStore } from '@/stores/media'

const store = useMediaStore()

async function handleDelete(id: number) {
  if (!confirm('Xác nhận xóa?')) return
  await store.remove(id)
}

function mediaUrl(path: string) {
  return resolveMediaUrl(path)
}

function isImage(mimeType: string | null | undefined) {
  return isImageMimeType(mimeType)
}

onMounted(() => store.fetchList())
</script>
