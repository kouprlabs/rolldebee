import { Config } from './types'

const config: Config = {
  apiURL: '/proxy/api/v1',
}

export function getConfig(): Config {
  return config
}
