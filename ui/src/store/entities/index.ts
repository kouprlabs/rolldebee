import { combineReducers } from 'redux'
import comparisons from './comparisons'
import introspections from './introspections'

export default combineReducers({
  introspections,
  comparisons,
})
