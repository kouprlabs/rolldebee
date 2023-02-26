import { Link } from 'react-router-dom'
import {
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Link as ChakraLink,
  Stack,
  Avatar,
  HStack,
} from '@chakra-ui/react'
import { variables } from '@koupr/ui'
import { SectionSpinner } from '@koupr/ui'
import ConnectionAPI from '@/api/connection'
import { refreshInterval } from '@/api/options'
import DatabaseTypeField from '@/components/fields/database-type'

const Introspection = () => {
  const { data: connections } = ConnectionAPI.useGetAll({
    refreshInterval,
  })

  if (!connections) {
    return <SectionSpinner />
  }

  return (
    <Stack direction="column" spacing={variables.spacingLg}>
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>Database type</Th>
          </Tr>
        </Thead>
        <Tbody>
          {connections.map((connection) => (
            <Tr key={connection.id}>
              <Td>
                <HStack spacing={variables.spacing}>
                  <Avatar
                    name={connection.name}
                    size="sm"
                    width="40px"
                    height="40px"
                  />
                  <ChakraLink as={Link} to={`/introspection/${connection.id}`}>
                    {connection.name}
                  </ChakraLink>
                </HStack>
              </Td>
              <Td>
                <DatabaseTypeField value={connection.databaseType} />
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>
    </Stack>
  )
}

export default Introspection
