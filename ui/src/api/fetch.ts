import settings from '@/infra/settings'

export const apiFetch = async (url: string, init?: RequestInit) =>
  handleFailure(await fetch(`${settings.apiUrl}${url}`, init))

export const apiFetcher = (url: string) =>
  apiFetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  }).then((result) => result.json())

async function handleFailure(response: Response) {
  if (response.status <= 299) {
    return response
  }
  if (response.body) {
    throw await response.json()
  } else if (response.statusText) {
    throw response.statusText
  } else {
    throw new Error(`Request failed with status ${response.status}`)
  }
}
