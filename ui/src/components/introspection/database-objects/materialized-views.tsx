import { Table, Thead, Tbody, Tr, Th, Td, Text } from '@chakra-ui/react'
import { useAppSelector } from '@/store/hook'
import CodeField from '../../fields/code'

type MaterializedViewsProps = {
  connectionId: string
}

const MaterializedViews = ({ connectionId }: MaterializedViewsProps) => {
  const materializedViews = useAppSelector(
    (state) =>
      state.entities.introspections.items[connectionId]?.materializedViews || []
  )
  if (materializedViews && materializedViews.length > 0) {
    return (
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>DDL</Th>
          </Tr>
        </Thead>
        <Tbody>
          {materializedViews.map((materializedView) => (
            <Tr key={materializedView.id}>
              <Td>{materializedView.name}</Td>
              <Td>
                <CodeField language="sql">{materializedView.ddl}</CodeField>
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

export default MaterializedViews
