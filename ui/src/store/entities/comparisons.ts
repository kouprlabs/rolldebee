import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { Comparison } from '@/api/comparison'

type ComparisonsState = {
  items: Record<string, Comparison>
}

const initialState: ComparisonsState = {
  items: {},
}

type ComparisonKeyValue = {
  key: string
  value: Comparison
}

const slice = createSlice({
  name: 'comparisons',
  initialState,
  reducers: {
    comparisonSet: (state, action: PayloadAction<ComparisonKeyValue>) => {
      state.items[action.payload.key] = action.payload.value
    },
    comparisonDeleted: (state, action: PayloadAction<string>) => {
      delete state.items[action.payload]
    },
  },
})

export const { comparisonSet, comparisonDeleted } = slice.actions

export default slice.reducer
