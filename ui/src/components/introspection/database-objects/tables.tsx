import { Table, Thead, Tbody, Tr, Th, Td, Code, Text } from '@chakra-ui/react'
import { useAppSelector } from '@/store/hook'
import CodeField from '../../fields/code'
import TableColumnsField from '../../fields/table-columns'

type TablesProps = {
  connectionId: string
}

const Tables = ({ connectionId }: TablesProps) => {
  const tables = useAppSelector(
    (state) => state.entities.introspections.items[connectionId]?.tables || [],
  )
  if (tables && tables.length > 0) {
    return (
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>Columns</Th>
            <Th>DDL</Th>
            <Th>Comments</Th>
          </Tr>
        </Thead>
        <Tbody>
          {tables.map((table) => (
            <Tr key={table.id}>
              <Td verticalAlign="top">{table.name}</Td>
              <Td verticalAlign="top">
                <TableColumnsField columns={table.columns} />
              </Td>
              <Td verticalAlign="top">
                <CodeField language="sql">{table.ddl}</CodeField>
              </Td>
              <Td verticalAlign="top">
                <Code>{table.comments}</Code>
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

export default Tables
