import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Button, Heading, Select, Stack } from '@chakra-ui/react'
import { variables } from '@koupr/ui'
import { SectionSpinner, IconPlayArrow } from '@koupr/ui'
import { MdOutlineCompareArrows } from 'react-icons/md'
import ConnectionAPI from '@/api/connection'
import { refreshInterval } from '@/api/options'

const Comparison = () => {
  const navigate = useNavigate()
  const { data: connections } = ConnectionAPI.useGetAll({ refreshInterval })
  const [sourceConnectionId, setSourceConnectionId] = useState<string>()
  const [targetConnectionId, setTargetConnectionId] = useState<string>()
  const [sourceInvalid, setSourceInvalid] = useState<boolean>()
  const [targetInvalid, setTargetInvalid] = useState<boolean>()

  if (!connections) {
    return <SectionSpinner />
  }

  return (
    <Stack
      direction="column"
      spacing={variables.spacing}
      alignItems="center"
      justifyContent="center"
    >
      <Heading size="md">Select two schemas to compare</Heading>
      <Stack direction="row" spacing={variables.spacing} alignItems="center">
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
        <MdOutlineCompareArrows fontSize="20px" />
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
      </Stack>
      <Box>
        <Button
          colorScheme="blue"
          leftIcon={<IconPlayArrow />}
          isDisabled={
            sourceConnectionId &&
            targetConnectionId &&
            sourceConnectionId === targetConnectionId
              ? true
              : false
          }
          onClick={() => {
            setSourceInvalid(!sourceConnectionId)
            setTargetInvalid(!targetConnectionId)
            if (sourceConnectionId && targetConnectionId) {
              navigate(
                `/comparison/${sourceConnectionId}/${targetConnectionId}`
              )
            }
          }}
        >
          Compare
        </Button>
      </Box>
    </Stack>
  )
}

export default Comparison
