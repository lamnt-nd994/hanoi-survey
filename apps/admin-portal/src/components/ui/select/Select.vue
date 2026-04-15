<template>
  <select
    :value="modelValue ?? ''"
    :class="cn('flex h-10 w-full rounded-md border border-slate-200 bg-white px-3 py-2 text-sm text-slate-900 shadow-sm transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-slate-400 disabled:cursor-not-allowed disabled:opacity-50', props.class)"
    v-bind="$attrs"
    @change="handleChange"
  >
    <slot />
  </select>
</template>

<script setup lang="ts">
import { cn } from '@/lib/utils'
import { coerceModelValue, type ModelModifiers, type ModelValue } from '@/lib/model-value'

const props = withDefaults(defineProps<{ class?: string; modelValue?: ModelValue; modelModifiers?: ModelModifiers }>(), { class: '', modelValue: '', modelModifiers: () => ({}) })
const emit = defineEmits<{ (e: 'update:modelValue', value: ModelValue): void }>()

function handleChange(event: Event) {
  const select = event.target as HTMLSelectElement
  const selected = select.selectedOptions[0] as HTMLOptionElement & { _value?: unknown }
  const rawValue = selected && '_value' in selected ? selected._value : select.value
  if (rawValue === null || typeof rawValue === 'number') {
    emit('update:modelValue', rawValue)
    return
  }
  emit('update:modelValue', coerceModelValue(String(rawValue ?? ''), props.modelModifiers))
}
</script>
