<template>
  <section class="py-16 md:py-24">
    <div class="container-shell">
      <Card class="rounded-[32px] p-8 text-center md:p-10 lg:p-12">
        <div class="mx-auto max-w-3xl">
          <SectionHeader :eyebrow="eyebrow" :title="title" :description="description" centered />

          <div class="mt-8 flex flex-wrap items-center justify-center gap-4">
            <Button as="router-link" :to="primaryButtonUrl" variant="dark">
              {{ primaryButtonLabel }}
            </Button>
            <Button as="router-link" :to="secondaryButtonUrl" variant="secondary" class="border-slate-300 text-slate-700">
              {{ secondaryButtonLabel }}
            </Button>
            <Button v-if="showPhoneButton && phone" as="a" :href="`tel:${phone}`" variant="outline">
              Gọi ngay: {{ phone }}
            </Button>
          </div>
        </div>
      </Card>
    </div>
  </section>
</template>

<script setup lang="ts">
import SectionHeader from '../ui/SectionHeader.vue'
import { Button } from '../ui/button'
import { Card } from '../ui/card'

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
