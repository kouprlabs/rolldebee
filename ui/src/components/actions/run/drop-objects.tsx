import { useCallback, useState } from 'react'
import { mutate } from 'swr'
import { Button, Select, Stack } from '@chakra-ui/react'
import ActionAPI from '@/api/action'
import ConnectionAPI from '@/api/connection'
import { IconRun } from '@/components/common/icons'

const DropObjects = () => {
  const { data: connections } = ConnectionAPI.useGetAll()
  const [connectionId, setConnectionId] = useState<string>()
  const [invalid, setInvalid] = useState<boolean>()
  const [loading, setLoading] = useState<boolean>()

  const handleRun = useCallback(async (connectionId: string) => {
    try {
      setLoading(true)
      await ActionAPI.runDropObjects({ connectionId })
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
        colorScheme="red"
        leftIcon={<IconRun fontSize="16px" />}
        isDisabled={loading}
        onClick={() => {
          setInvalid(!connectionId)
          if (connectionId) {
            handleRun(connectionId)
          }
        }}
      >
        Drop objects
      </Button>
    </Stack>
  )
}

export default DropObjects
