import { DatabaseObject } from './common-models'
import { apiFetch } from './fetch'

export type Comparison = {
  diffs: Diff[]
}

export type DiffType = 'ADDED' | 'MODIFIED' | 'DELETED'

export type Diff = {
  databaseObject: DatabaseObject
  type: DiffType
  ddls: string[]
}

export type CreateOptions = {
  sourceConnectionId: string
  targetConnectionId: string
}

export default class ComparisonAPI {
  static async create(options: CreateOptions): Promise<Comparison> {
    return apiFetch(`/comparisons`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }
}
