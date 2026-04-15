<template>
  <section class="py-16 md:py-20">
    <div class="container-shell">
      <div class="grid gap-8 lg:grid-cols-[1.05fr_0.95fr] lg:items-center">
        <div class="overflow-hidden rounded-[2rem] border border-neutral-200 bg-neutral-100 shadow-sm">
          <MediaImage v-if="imagePath" :src="imagePath" :alt="siteName" :width="760" :height="380" class="h-[380px] w-full object-cover" />
          <div v-else class="flex h-[320px] items-center justify-center bg-[linear-gradient(135deg,#16365f_0%,#264d7e_48%,#d6dde6_48%,#eef2f6_100%)] px-10 text-center text-white">
            <div>
              <div class="text-xs font-semibold uppercase tracking-[0.22em] text-white/70">Hanoi Survey</div>
              <div class="mt-4 font-heading text-3xl font-bold leading-tight">Khảo sát chính xác cho nền móng và hạ tầng</div>
            </div>
          </div>
        </div>

        <div class="max-w-2xl">
          <h2 class="font-heading text-4xl font-bold leading-tight text-primary-navy">{{ title }}</h2>
          <p class="mt-5 text-base leading-8 text-neutral-600">{{ description }}</p>

          <div class="mt-7 space-y-3">
            <div v-for="item in highlights" :key="item" class="flex items-start gap-3 text-[1.05rem] font-medium text-primary-navy">
              <AppIcon icon="check-circle" class="mt-0.5 h-5 w-5 shrink-0 text-amber-400" />
              <span>{{ item }}</span>
            </div>
          </div>

          <router-link :to="buttonUrl" class="mt-8 inline-flex items-center gap-2 text-lg font-bold text-primary-navy underline-offset-4 transition-colors hover:underline">
            {{ buttonLabel }}
            <AppIcon icon="arrowRight" class="h-5 w-5" />
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import AppIcon from '../ui/AppIcon.vue'
import MediaImage from '../ui/MediaImage.vue'

defineProps<{
  siteName: string
  eyebrow: string
  title: string
  description: string
  imagePath: string
  highlights: string[]
  buttonLabel: string
  buttonUrl: string
}>()
</script>
