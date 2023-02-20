import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { Introspection } from '@/api/introspection'

type IntrospectionsState = {
  items: Record<string, Introspection>
}

const initialState: IntrospectionsState = {
  items: {},
}

type IntrospectionKeyValue = {
  key: string
  value: Introspection
}

const slice = createSlice({
  name: 'introspections',
  initialState,
  reducers: {
    introspectionSet: (state, action: PayloadAction<IntrospectionKeyValue>) => {
      state.items[action.payload.key] = action.payload.value
    },
    introspectionDeleted: (state, action: PayloadAction<string>) => {
      delete state.items[action.payload]
    },
  },
})

export const { introspectionSet, introspectionDeleted } = slice.actions

export default slice.reducer
