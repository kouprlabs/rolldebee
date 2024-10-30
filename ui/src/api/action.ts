/* eslint-disable react-hooks/rules-of-hooks */
import useSWR from 'swr'
import { DatabaseObject } from './common-models'
import { Diff } from './comparison'
import { apiFetch, apiFetcher } from './fetch'

export type ActionType =
  | 'clone'
  | 'migrate'
  | 'enable_constraints'
  | 'disable_constraints'
  | 'enable_triggers'
  | 'disable_triggers'
  | 'drop_objects'

export type ActionStatus = 'pending' | 'running' | 'succeeded' | 'failed'

export type Action = {
  id: string
  type: ActionType
  status: ActionStatus
  params?: string
  result?: string
  createTime: string
  updateTime?: string
}

export type CloneOptions = {
  sourceConnectionId: string
  targetConnectionId: string
}

export type CloneSummary = {
  failures: CloneFailure[]
  successes: DatabaseObject[]
}

export type CloneFailure = {
  databaseObject: DatabaseObject
  reason: string
}

export type EnableConstraintsSummary = {
  successes: EnableConstraintsSuccess[]
  failures: EnableConstraintsFailure[]
}

export type EnableConstraintsSuccess = {
  tableName: string
  constraintName: string
}

export type EnableConstraintsFailure = {
  tableName: string
  constraintName: string
  reason: string
}

export type DisableConstraintsSummary = {
  successes: DisableConstraintsSuccess[]
  failures: DisableConstraintsFailure[]
}

export type DisableConstraintsSuccess = {
  tableName: string
  constraintName: string
}

export type DisableConstraintsFailure = {
  tableName: string
  constraintName: string
  reason: string
}

export type EnableConstraintsOptions = {
  connectionId: string
}

export type DisableConstraintsOptions = {
  connectionId: string
}

export type MigrationSummary = {
  failures: MigrateFailure[]
}

export type MigrateFailure = {
  diff: Diff
  ddls: MigrateDdl[]
  reason: string
}

export type MigrateDdl = {
  ddl: string
  error: string
}

export type MigrateOptions = {
  sourceConnectionId: string
  targetConnectionId: string
}

export type EnableTriggersSummary = {
  successes: EnableTriggersSuccess[]
  failures: EnableTriggersFailure[]
}

export type EnableTriggersSuccess = {
  name: string
}

export type EnableTriggersFailure = {
  name: string
  reason: string
}

export type DisableTriggersSummary = {
  successes: DisableTriggersSuccess[]
  failures: DisableTriggersFailure[]
}

export type DisableTriggersSuccess = {
  name: string
}

export type DisableTriggersFailure = {
  name: string
  reason: string
}

export type EnableTriggersOptions = {
  connectionId: string
}

export type DisableTriggersOptions = {
  connectionId: string
}

export type DropObjectsOptions = {
  connectionId: string
}

export type DropObjectsSummary = {
  successes: DropObjectsSuccess[]
  failures: DropObjectsFailure[]
}

export type DropObjectsSuccess = {
  name: string
}

export type DropObjectsFailure = {
  name: string
  reason: string
}

export default class ActionAPI {
  static useGetById(id?: string, swrOptions?: any) {
    return useSWR<Action>(id ? `/actions/${id}` : null, apiFetcher, swrOptions)
  }

  static async getById(id: string): Promise<Action> {
    return apiFetch(`/actions/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static useGetAll(swrOptions?: any) {
    return useSWR<Action[]>(`/actions`, apiFetcher, swrOptions)
  }

  static useGetAllByType(type: string, swrOptions?: any) {
    return useSWR<Action[]>(
      `/actions?${new URLSearchParams({ type })}`,
      apiFetcher,
      swrOptions,
    )
  }

  static async delete(id: string) {
    return apiFetch(`/actions/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    })
  }

  static async runClone(options: CloneOptions): Promise<string> {
    return apiFetch(`/actions/run/clone`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static async runMigrate(options: MigrateOptions): Promise<MigrationSummary> {
    return apiFetch(`/actions/run/migrate`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static async runDisableConstraints(
    options: DisableConstraintsOptions,
  ): Promise<DisableConstraintsSummary> {
    return apiFetch(`/actions/run/disable_constraints`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static async runEnableConstraints(
    options: EnableConstraintsOptions,
  ): Promise<EnableConstraintsSummary> {
    return apiFetch(`/actions/run/enable_constraints`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static async runDisableTriggers(
    options: DisableTriggersOptions,
  ): Promise<DisableTriggersSummary> {
    return apiFetch(`/actions/run/disable_triggers`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static async runEnableTriggers(
    options: EnableTriggersOptions,
  ): Promise<EnableTriggersSummary> {
    return apiFetch(`/actions/run/enable_triggers`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static async runDropObjects(
    options: DropObjectsOptions,
  ): Promise<DropObjectsSummary> {
    return apiFetch(`/actions/run/drop_objects`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }
}
