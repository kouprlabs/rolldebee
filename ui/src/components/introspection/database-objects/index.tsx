import { useCallback, useEffect, useState } from 'react'
import {
  Center,
  Stack,
  Tabs,
  TabList,
  TabPanels,
  Tab,
  TabPanel,
  Button,
  Text,
  Heading,
  useToken,
  IconButton,
  Alert,
  AlertDescription,
  AlertIcon,
  CloseButton,
} from '@chakra-ui/react'
import { variables, Spinner, IconError, IconPlay } from '@koupr/ui'
import { FiChevronLeft } from 'react-icons/fi'
import ConnectionAPI from '@/api/connection'
import IntrospectAPI from '@/api/introspection'
import { introspectionSet } from '@/store/entities/introspections'
import { useAppDispatch, useAppSelector } from '@/store/hook'
import Constraints from './constraints'
import Indexes from './indexes'
import MaterializedViews from './materialized-views'
import Sequences from './sequences'
import Sources from './sources'
import Tables from './tables'
import Views from './views'

type DatabaseObjectsProps = {
  connectionId: string
  onDismiss: () => void
}

const DatabaseObjects = ({ connectionId, onDismiss }: DatabaseObjectsProps) => {
  const dispatch = useAppDispatch()
  const introspection = useAppSelector(
    (state) => state.entities.introspections.items[connectionId]
  )
  const { data: connection } = ConnectionAPI.useGetById(connectionId)
  const [loading, setLoading] = useState(false)
  const [failed, setFailed] = useState(false)
  const [red500] = useToken('colors', ['red.500'])
  const [isCacheData, setIsCacheDataData] = useState(true)
  const [showCacheNotice, setShowCacheNotice] = useState(false)

  const handleIntrospect = useCallback(async () => {
    setLoading(true)
    try {
      const introspection = await IntrospectAPI.create({ connectionId })
      dispatch(introspectionSet({ key: connectionId, value: introspection }))
      setFailed(false)
    } catch {
      setFailed(true)
    } finally {
      setLoading(false)
    }
  }, [connectionId, dispatch])

  useEffect(() => {
    if (loading || failed) {
      return
    }
    if (!introspection) {
      setIsCacheDataData(false)
      handleIntrospect()
    } else if (isCacheData) {
      setShowCacheNotice(true)
    }
  }, [introspection, isCacheData, loading, failed, handleIntrospect])

  return (
    <Stack direction="column" spacing={variables.spacingLg}>
      <Stack direction="row" alignItems="center">
        <IconButton
          variant="ghost"
          icon={<FiChevronLeft fontSize="20px" />}
          aria-label=""
          onClick={() => onDismiss()}
        />
        <Heading flexShrink={0} size="md">
          Introspection of {connection && connection.name}
        </Heading>
        <Stack
          flexGrow={1}
          direction="row"
          spacing={variables.spacing}
          justifyContent="flex-end"
        >
          <Button
            colorScheme="blue"
            leftIcon={<IconPlay fontSize="16px" />}
            isDisabled={loading}
            onClick={() => handleIntrospect()}
          >
            Introspect
          </Button>
        </Stack>
      </Stack>
      {loading && (
        <Center h="300px" w="100%">
          <Stack
            direction="column"
            alignItems="center"
            justifyContent="center"
            spacing={variables.spacing}
          >
            <Spinner />
            <Text fontSize="16px">Introspecting</Text>
          </Stack>
        </Center>
      )}
      {failed && !loading && (
        <Center h="300px" w="100%">
          <Stack
            direction="column"
            alignItems="center"
            justifyContent="center"
            spacing={variables.spacing}
          >
            <IconError fontSize="23px" color={red500} />
            <Text color={red500} fontSize="16px">
              Introspection failed
            </Text>
          </Stack>
        </Center>
      )}
      {showCacheNotice && !loading && (
        <Alert status="info">
          <AlertIcon />
          <AlertDescription>
            These results are coming from the cache, rerun to get the latest
            version.
          </AlertDescription>
          <CloseButton
            position="absolute"
            right="8px"
            top="8px"
            onClick={() => setShowCacheNotice(false)}
          />
        </Alert>
      )}
      {introspection && !loading && !failed && (
        <Tabs>
          <TabList>
            <Tab>Tables</Tab>
            <Tab>Views</Tab>
            <Tab>Materialized Views</Tab>
            <Tab>Constraints</Tab>
            <Tab>Indexes</Tab>
            <Tab>Sources</Tab>
            <Tab>Sequences</Tab>
          </TabList>
          <TabPanels>
            <TabPanel>
              <Tables connectionId={connectionId} />
            </TabPanel>
            <TabPanel>
              <Views connectionId={connectionId} />
            </TabPanel>
            <TabPanel>
              <MaterializedViews connectionId={connectionId} />
            </TabPanel>
            <TabPanel>
              <Constraints connectionId={connectionId} />
            </TabPanel>
            <TabPanel>
              <Indexes connectionId={connectionId} />
            </TabPanel>
            <TabPanel>
              <Sources connectionId={connectionId} />
            </TabPanel>
            <TabPanel>
              <Sequences connectionId={connectionId} />
            </TabPanel>
          </TabPanels>
        </Tabs>
      )}
    </Stack>
  )
}

export default DatabaseObjects
