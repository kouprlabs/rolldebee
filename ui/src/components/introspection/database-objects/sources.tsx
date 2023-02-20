import {
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Text,
  useToken,
  Badge,
} from '@chakra-ui/react'
import { BsCheckCircleFill, BsExclamationCircleFill } from 'react-icons/bs'
import { useAppSelector } from '@/store/hook'
import CodeField from '../../fields/code'

type ViewsProps = {
  connectionId: string
}

const Sources = ({ connectionId }: ViewsProps) => {
  const sources = useAppSelector(
    (state) => state.entities.introspections.items[connectionId]?.sources || []
  )
  const [red500] = useToken('colors', ['red.500'])
  const [green500] = useToken('colors', ['green.500'])

  if (sources && sources.length > 0) {
    return (
      <Table variant="simple">
        <colgroup>
          <col span={1} style={{ width: '5%' }} />
          <col span={1} style={{ width: '25%' }} />
          <col span={1} style={{ width: '10%' }} />
          <col span={1} style={{ width: '60%' }} />
        </colgroup>
        <Thead>
          <Tr>
            <Th>Valid</Th>
            <Th>Name</Th>
            <Th>Type</Th>
            <Th>DDL</Th>
          </Tr>
        </Thead>
        <Tbody>
          {sources.map((source) => (
            <Tr key={source.id}>
              <Td>
                {source.status === 'VALID' && (
                  <BsCheckCircleFill fontSize="16px" color={green500} />
                )}
                {source.status === 'INVALID' && (
                  <BsExclamationCircleFill fontSize="16px" color={red500} />
                )}
              </Td>
              <Td>{source.name}</Td>
              <Td>
                <Badge variant="outline">{source.objectType}</Badge>
              </Td>
              <Td>
                <CodeField language="sql" width="100%" maxHeight="500px">
                  {source.ddl}
                </CodeField>
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

export default Sources
