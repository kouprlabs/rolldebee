import useSWR, { SWRConfiguration } from 'swr'
import { apiFetch, apiFetcher } from './fetch'

export type Connection = {
  id: string
  name: string
  jdbcUrl: string
  jdbcUsername: string
  jdbcPassword?: string
  databaseType: DatabaseType
  createTime: string
  updateTime?: string
}

export type DatabaseType = 'postgres' | 'mysql' | 'red'

export type CreateOptions = {
  name: string
  jdbcUrl: string
  jdbcUsername: string
  jdbcPassword?: string
  databaseType: string
}

export type UpdateOptions = {
  name: string
  jdbcUrl: string
  jdbcUsername: string
  jdbcPassword?: string
}

export default class ConnectionAPI {
  static useGetById(id?: string, swrOptions?: SWRConfiguration) {
    return useSWR<Connection>(
      id ? `/connections/${id}` : null,
      apiFetcher,
      swrOptions,
    )
  }

  static async getById(id: string): Promise<Connection> {
    return apiFetch(`/connections/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static useGetAll(swrOptions?: SWRConfiguration) {
    return useSWR<Connection[]>(`/connections`, apiFetcher, swrOptions)
  }

  static async create(options: CreateOptions): Promise<Connection> {
    return apiFetch(`/connections`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }

  static async delete(id: string) {
    return apiFetch(`/connections/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    })
  }

  static async update(id: string, options: UpdateOptions): Promise<Connection> {
    return apiFetch(`/connections/${id}`, {
      method: 'PATCH',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }
}
