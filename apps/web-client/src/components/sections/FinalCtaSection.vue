<template>
  <section class="py-16 md:py-24">
    <div class="container-shell">
      <div class="panel rounded-[32px] p-8 text-center md:p-10 lg:p-12">
        <div class="mx-auto max-w-3xl">
          <SectionHeader :eyebrow="eyebrow" :title="title" :description="description" centered />

          <div class="mt-8 flex flex-wrap items-center justify-center gap-4">
            <router-link :to="primaryButtonUrl" class="inline-flex min-h-[44px] min-w-[44px] items-center justify-center rounded-full bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition-colors hover:bg-slate-700">
              {{ primaryButtonLabel }}
            </router-link>
            <router-link :to="secondaryButtonUrl" class="inline-flex min-h-[44px] min-w-[44px] items-center justify-center rounded-full border border-slate-300 px-6 py-3 text-sm font-semibold text-slate-700 transition-colors hover:bg-slate-50">
              {{ secondaryButtonLabel }}
            </router-link>
            <a v-if="showPhoneButton && phone" :href="`tel:${phone}`" class="btn-outline">
              Gọi ngay: {{ phone }}
            </a>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import SectionHeader from '../ui/SectionHeader.vue'

defineProps<{
  eyebrow: string
  title: string
  description: string
  primaryButtonLabel: string
  primaryButtonUrl: string
  secondaryButtonLabel: string
  secondaryButtonUrl: string
  showPhoneButton: boolean
  phone: string
}>()
</script>
