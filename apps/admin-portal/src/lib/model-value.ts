export type ModelValue = string | number | null

export type ModelModifiers = {
  number?: boolean
}

export function coerceModelValue(value: string, modifiers?: ModelModifiers): ModelValue {
  if (!modifiers?.number) return value
  if (value === '') return null
  const parsed = Number(value)
  return Number.isNaN(parsed) ? value : parsed
}
