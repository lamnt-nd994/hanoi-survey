<template>
  <section class="container-shell py-16 md:py-20">
    <div v-if="eyebrow" class="eyebrow">{{ eyebrow }}</div>
    <h2 v-if="title" class="section-title mt-4 text-primary-navy">{{ title }}</h2>
    <p v-if="copy" class="section-subtitle mt-4 max-w-3xl">{{ copy }}</p>
    <div class="mt-10">
      <slot />
    </div>
  </section>
</template>

<script setup>
defineProps({
  eyebrow: String,
  title: String,
  copy: String
})
</script>
