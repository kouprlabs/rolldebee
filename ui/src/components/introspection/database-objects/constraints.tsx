import { ReactNode } from 'react'
import { Table, Thead, Tbody, Tr, Th, Td, Text, Badge } from '@chakra-ui/react'
import { useAppSelector } from '@/store/hook'
import CodeField from '../../fields/code'

type StatusProps = {
  children?: ReactNode
}

const Status = ({ children }: StatusProps) => {
  if (children === 'ENABLED') {
    return <Badge colorScheme="green">{children}</Badge>
  }
  if (children === 'DISABLED') {
    return <Badge colorScheme="red">{children}</Badge>
  }
  return <Badge>{children}</Badge>
}

type ConstraintsProps = {
  connectionId: string
}

const Constraints = ({ connectionId }: ConstraintsProps) => {
  const constraints = useAppSelector(
    (state) =>
      state.entities.introspections.items[connectionId]?.constraints || []
  )
  if (constraints && constraints.length > 0) {
    return (
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>Type</Th>
            <Th>Status</Th>
            <Th>Validated</Th>
            <Th>Table name</Th>
            <Th>DDL</Th>
          </Tr>
        </Thead>
        <Tbody>
          {constraints.map((constraint) => (
            <Tr key={constraint.id}>
              <Td>{constraint.name}</Td>
              <Td>
                <Badge variant="outline">{constraint.constraintType}</Badge>
              </Td>
              <Td>
                <Status>{constraint.status}</Status>
              </Td>
              <Td>
                <Badge variant="outline">{constraint.validated}</Badge>
              </Td>
              <Td>{constraint.tableName}</Td>
              <Td>
                <CodeField language="sql">{constraint.ddl}</CodeField>
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

export default Constraints
