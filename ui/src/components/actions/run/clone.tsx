import { useCallback, useState } from 'react'
import { Button, Select, Stack } from '@chakra-ui/react'
import { SectionSpinner, IconPlayArrow } from '@koupr/ui'
import { mutate } from 'swr'
import ActionAPI from '@/api/action'
import ConnectionAPI from '@/api/connection'

const Clone = () => {
  const { data: connections } = ConnectionAPI.useGetAll()
  const [sourceConnectionId, setSourceConnectionId] = useState<string>()
  const [targetConnectionId, setTargetConnectionId] = useState<string>()
  const [sourceInvalid, setSourceInvalid] = useState<boolean>()
  const [targetInvalid, setTargetInvalid] = useState<boolean>()
  const [loading, setLoading] = useState<boolean>()

  const handleClone = useCallback(
    async (sourceConnectionId: string, targetConnectionId: string) => {
      try {
        setLoading(true)
        await ActionAPI.runClone({ sourceConnectionId, targetConnectionId })
        mutate('/actions')
      } finally {
        setLoading(false)
      }
    },
    [],
  )

  if (!connections) {
    return <SectionSpinner />
  }

  return (
    <Stack direction="row" alignItems="center">
      <Select
        placeholder="Select a source"
        w="250px"
        isInvalid={sourceInvalid}
        onChange={(event) => {
          setSourceConnectionId(event.target.value)
          setSourceInvalid(!event.target.value)
        }}
      >
        {connections.map((connection) => (
          <option key={connection.id} value={connection.id}>
            {connection.name}
          </option>
        ))}
      </Select>
      <Select
        placeholder="Select a target"
        w="250px"
        isInvalid={targetInvalid}
        onChange={(event) => {
          setTargetConnectionId(event.target.value)
          setTargetInvalid(!event.target.value)
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
        isLoading={loading}
        isDisabled={
          loading ||
          (sourceConnectionId &&
            targetConnectionId &&
            sourceConnectionId === targetConnectionId)
            ? true
            : false
        }
        onClick={() => {
          setSourceInvalid(!sourceConnectionId)
          setTargetInvalid(!targetConnectionId)
          if (sourceConnectionId && targetConnectionId) {
            handleClone(sourceConnectionId, targetConnectionId)
          }
        }}
      >
        Clone
      </Button>
    </Stack>
  )
}

export default Clone
