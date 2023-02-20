import { Link } from 'react-router-dom'
import {
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Center,
  Link as ChakraLink,
  Stack,
  Avatar,
  HStack,
} from '@chakra-ui/react'
import ConnectionAPI from '@/api/connection'
import { refreshInterval } from '@/api/options'
import Spinner from '@/components/common/spinner'
import DatabaseTypeField from '@/components/fields/database-type'
import variables from '@/theme/variables'
import FullPageSpinner from '../common/full-page-spinner'

const Introspection = () => {
  const { data: connections } = ConnectionAPI.useGetAll({
    refreshInterval,
  })

  if (!connections) {
    return <FullPageSpinner />
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
