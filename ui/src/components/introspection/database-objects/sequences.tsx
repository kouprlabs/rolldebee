import { Table, Thead, Tbody, Tr, Th, Td, Text } from '@chakra-ui/react'
import { useAppSelector } from '@/store/hook'
import CodeField from '../../fields/code'

type SequencesProps = {
  connectionId: string
}

const Sequences = ({ connectionId }: SequencesProps) => {
  const sequences = useAppSelector(
    (state) =>
      state.entities.introspections.items[connectionId]?.sequences || [],
  )
  if (sequences && sequences.length > 0) {
    return (
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>DDL</Th>
          </Tr>
        </Thead>
        <Tbody>
          {sequences.map((sequence) => (
            <Tr key={sequence.id}>
              <Td>{sequence.name}</Td>
              <Td>
                <CodeField language="sql">{sequence.ddl}</CodeField>
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

export default Sequences
