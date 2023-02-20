import { apiFetch } from './fetch'

export type CreateOptions = {
  sourceConnectionId: string
  targetConnectionId: string
}

export default class PatchScriptAPI {
  static async clone(options: CreateOptions): Promise<string> {
    return apiFetch(`/patch_scripts`, {
      method: 'POST',
      body: JSON.stringify(options),
      headers: {
        'Content-Type': 'application/json',
      },
    }).then((result) => result.json())
  }
}
