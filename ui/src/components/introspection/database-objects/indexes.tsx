import { ReactNode } from 'react'
import { Table, Thead, Tbody, Tr, Th, Td, Text, Badge } from '@chakra-ui/react'
import { useAppSelector } from '@/store/hook'
import CodeField from '../../fields/code'

type StatusProps = {
  children?: ReactNode
}

const Status = ({ children }: StatusProps) => {
  if (children === 'VALID') {
    return <Badge colorScheme="green">{children}</Badge>
  }
  if (children === 'INVALID') {
    return <Badge colorScheme="red">{children}</Badge>
  }
  return <Badge>{children}</Badge>
}

type IndexesProps = {
  connectionId: string
}

const Indexes = ({ connectionId }: IndexesProps) => {
  const indexes = useAppSelector(
    (state) => state.entities.introspections.items[connectionId]?.indexes || []
  )
  if (indexes && indexes.length > 0) {
    return (
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>Type</Th>
            <Th>Status</Th>
            <Th>DDL</Th>
          </Tr>
        </Thead>
        <Tbody>
          {indexes.map((index) => (
            <Tr key={index.id}>
              <Td>{index.name}</Td>
              <Td>
                <Badge variant="outline">{index.indexType}</Badge>
              </Td>
              <Td>
                <Status>{index.status}</Status>
              </Td>
              <Td>
                <CodeField language="sql">{index.ddl}</CodeField>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>
    )
  } else {
    return <Text>No data.</Text>
  }
}

export default Indexes
