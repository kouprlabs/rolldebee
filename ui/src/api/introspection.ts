import { DatabaseObject } from './common-models'
import { apiFetch } from './fetch'

export type Introspection = {
  tables: Table[]
  constraints: Constraint[]
  indexes: Index[]
  materializedViews: MaterializedView[]
  views: View[]
  sequences: Sequence[]
  sources: Source[]
}

export type Column = {
  id: string
  name: string
  tableName: string
  columnType: string
  order: number
  comments?: string
  dataLength: number
  dataPrecision: number
  dataScale: number
  nullable: boolean
  ddl: string
}

export interface Table extends DatabaseObject {
  comments?: string
  columns: Column[]
}

export interface Constraint extends DatabaseObject {
  constraintType: string
  tableName: string
  status: string
  validated: string
  refConstraintName?: string
}

export interface Index extends DatabaseObject {
  indexType: String
  status: String
}

export interface MaterializedView extends DatabaseObject {}

export interface View extends DatabaseObject {}

export interface Sequence extends DatabaseObject {}

export interface Source extends DatabaseObject {
  status: string
}

export type CreateOptions = {
  connectionId: string
}

export default class IntrospectAPI {
  static async create(options: CreateOptions): Promise<Introspection> {
    return apiFetch(`/introspections`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }
}
