import { useCallback, useState } from 'react'
import { Button, Select, Stack } from '@chakra-ui/react'
import { IconPlay } from '@koupr/ui'
import { mutate } from 'swr'
import ActionAPI from '@/api/action'
import ConnectionAPI from '@/api/connection'

const EnableTriggers = () => {
  const { data: connections } = ConnectionAPI.useGetAll()
  const [connectionId, setConnectionId] = useState<string>()
  const [invalid, setInvalid] = useState<boolean>()
  const [loading, setLoading] = useState<boolean>()

  const handleRun = useCallback(async (connectionId: string) => {
    try {
      setLoading(true)
      await ActionAPI.runEnableTriggers({ connectionId })
      mutate('/actions')
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
        leftIcon={<IconPlay fontSize="16px" />}
        isDisabled={loading}
        onClick={() => {
          setInvalid(!connectionId)
          if (connectionId) {
            handleRun(connectionId)
          }
        }}
      >
        Enable triggers
      </Button>
    </Stack>
  )
}

export default EnableTriggers
