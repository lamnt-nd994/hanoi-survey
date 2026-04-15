<template>
  <PageShell>
    <PageHeader title="Danh mục dịch vụ" description="Danh mục dịch vụ đã được bỏ khỏi trải nghiệm quản trị." />

    <DataCard class="p-6 text-sm leading-7 text-slate-600">
      Dịch vụ hiện được quản lý trực tiếp trong danh sách dịch vụ. Trang này không còn nằm trong menu quản trị.
    </DataCard>
  </PageShell>
</template>

<script setup lang="ts">
import DataCard from '@/components/shared/DataCard.vue'
import PageHeader from '@/components/shared/PageHeader.vue'
import PageShell from '@/components/shared/PageShell.vue'
</script>
