import { useCallback, useState } from 'react'
import { Button, Select, Stack } from '@chakra-ui/react'
import { IconPlayArrow } from '@koupr/ui'
import { mutate } from 'swr'
import ActionAPI from '@/api/action'
import ConnectionAPI from '@/api/connection'

const DisableTriggers = () => {
  const { data: connections } = ConnectionAPI.useGetAll()
  const [connectionId, setConnectionId] = useState<string>()
  const [invalid, setInvalid] = useState<boolean>()
  const [loading, setLoading] = useState<boolean>()

  const handleRun = useCallback(async (connectionId: string) => {
    try {
      setLoading(true)
      await ActionAPI.runDisableTriggers({ connectionId })
      mutate('/actions').then()
    } finally {
      setLoading(false)
    }
  }, [])

  if (!connections) {
    return null
  }

  return (
    <Stack direction="row" alignItems="center">
      <Select
        placeholder="Select a schema"
        w="250px"
        isInvalid={invalid}
        onChange={(event) => {
          setConnectionId(event.target.value)
          setInvalid(!event.target.value)
        }}
      >
        {connections.map((connection) => (
          <option key={connection.id} value={connection.id}>
            {connection.name}
          </option>
        ))}
      </Select>
      <Button
        colorScheme="blue"
        leftIcon={<IconPlayArrow />}
        isDisabled={loading}
        onClick={async () => {
          setInvalid(!connectionId)
          if (connectionId) {
            await handleRun(connectionId)
          }
        }}
      >
        Disable triggers
      </Button>
    </Stack>
  )
}

export default DisableTriggers
