import { ReactNode, useState } from 'react'
import {
  Badge,
  Center,
  Table,
  Tbody,
  Td,
  Th,
  Thead,
  Tr,
  Text,
  IconButton,
  Menu,
  MenuButton,
  MenuItem,
  MenuList,
} from '@chakra-ui/react'
import { Spinner, SectionSpinner } from '@koupr/ui'
import { FiMoreVertical, FiTrash } from 'react-icons/fi'
import { IoHourglassOutline } from 'react-icons/io5'
import ActionAPI, { ActionType } from '@/api/action'
import CodeField from '../fields/code'
import Delete from './delete'

type StatusProps = {
  children?: ReactNode
}

const Status = ({ children }: StatusProps) => {
  if (children === 'pending') {
    return <Badge colorScheme="gray">{children}</Badge>
  }
  if (children === 'running') {
    return <Badge colorScheme="purple">{children}</Badge>
  }
  if (children === 'succeeded') {
    return <Badge colorScheme="green">{children}</Badge>
  }
  if (children === 'failed') {
    return <Badge colorScheme="red">{children}</Badge>
  }
  return <Badge>{children}</Badge>
}

type ListParams = {
  type: ActionType
}

const List = ({ type }: ListParams) => {
  const { data: actions, mutate } = ActionAPI.useGetAllByType(type, {
    refreshInterval: 2500,
  })
  const [actionId, setActionId] = useState<string>()
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false)

  if (!actions) {
    return <SectionSpinner />
  }

  if (actions.length > 0) {
    return (
      <>
        <Table variant="simple">
          <Thead>
            <Tr>
              <Th>Action ID</Th>
              <Th>Type</Th>
              <Th>Status</Th>
              <Th></Th>
              <Th>Params</Th>
              <Th>Result</Th>
              <Th>Start date</Th>
              <Th></Th>
            </Tr>
          </Thead>
          <Tbody>
            {actions.map((action, index) => (
              <Tr key={index}>
                <Td>{action.id}</Td>
                <Td>
                  <Badge variant="outline">
                    {action.type.replaceAll('_', ' ').toUpperCase()}
                  </Badge>
                </Td>
                <Td>
                  <Status>{action.status}</Status>
                </Td>
                <Td>
                  <Center w="25px" h="25px">
                    {action.status === 'running' && <Spinner />}
                    {action.status === 'pending' && (
                      <IoHourglassOutline fontSize="16px" />
                    )}
                  </Center>
                </Td>
                <Td>
                  {action.params && (
                    <CodeField language="json">
                      {JSON.stringify(JSON.parse(action.params), null, 2)}
                    </CodeField>
                  )}
                </Td>
                <Td>
                  {action.status === 'succeeded' && action.result && (
                    <CodeField language="json">
                      {JSON.stringify(JSON.parse(action.result), null, 2)}
                    </CodeField>
                  )}
                  {action.status === 'failed' && action.result && (
                    <CodeField language="text">{action.result}</CodeField>
                  )}
                </Td>
                <Td>{new Date(action.createTime).toLocaleString()}</Td>
                <Td>
                  <Menu>
                    <MenuButton
                      as={IconButton}
                      icon={<FiMoreVertical fontSize="16px" />}
                      variant="ghost"
                      size="sm"
                      aria-label=""
                    />
                    <MenuList>
                      <MenuItem
                        icon={<FiTrash fontSize="14px" />}
                        color="red"
                        onClick={() => {
                          setActionId(action.id)
                          setOpenDeleteDialog(true)
                        }}
                      >
                        Delete
                      </MenuItem>
                    </MenuList>
                  </Menu>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
        {actionId && (
          <Delete
            id={actionId}
            open={openDeleteDialog}
            onComplete={() => {
              mutate()
              setOpenDeleteDialog(false)
            }}
            onDismiss={() => setOpenDeleteDialog(false)}
          />
        )}
      </>
    )
  } else {
    return <Text>No data.</Text>
  }
}

export default List
