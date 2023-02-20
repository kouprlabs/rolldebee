import { Table, Thead, Tbody, Tr, Th, Td, Text } from '@chakra-ui/react'
import { useAppSelector } from '@/store/hook'
import CodeField from '../../fields/code'

type ViewsProps = {
  connectionId: string
}

const Views = ({ connectionId }: ViewsProps) => {
  const views = useAppSelector(
    (state) => state.entities.introspections.items[connectionId]?.views || []
  )
  if (views && views.length > 0) {
    return (
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>DDL</Th>
          </Tr>
        </Thead>
        <Tbody>
          {views.map((view) => (
            <Tr key={view.id}>
              <Td>{view.name}</Td>
              <Td>
                <CodeField language="sql">{view.ddl}</CodeField>
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

export default Views
