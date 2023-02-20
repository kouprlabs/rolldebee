import {
  Badge,
  Table,
  Tbody,
  Td,
  Th,
  Thead,
  Tr,
  Text,
  Center,
} from '@chakra-ui/react'
import { Comparison } from '@/api/comparison'
import CodeField from '@/components/fields/code'
import DiffTypeField from '@/components/fields/diff-type'

type ListProps = {
  comparison: Comparison
}

const List = ({ comparison }: ListProps) => {
  if (comparison.diffs.length === 0) {
    return (
      <Center h="300px" w="100%">
        <Text fontSize="16px" color="green">
          Schemas are identical.
        </Text>
      </Center>
    )
  } else {
    return (
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>Object type</Th>
            <Th>Change</Th>
            <Th>DDL</Th>
          </Tr>
        </Thead>
        <Tbody>
          {comparison.diffs.map((diff, index) => (
            <Tr key={index}>
              <Td>{diff.databaseObject.name}</Td>
              <Td>
                <Badge variant="outline">
                  {diff.databaseObject.objectType.replaceAll('_', ' ')}
                </Badge>
              </Td>
              <Td>
                <DiffTypeField>{diff.type}</DiffTypeField>
              </Td>
              <Td>
                <CodeField language="sql">{diff.ddls.join('\n')}</CodeField>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>
    )
  }
}

export default List
