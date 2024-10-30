import { useCallback, useEffect, useState } from 'react'
import {
  Alert,
  AlertDescription,
  AlertIcon,
  Button,
  Center,
  CloseButton,
  Heading,
  HStack,
  IconButton,
  Stack,
  Text,
  useToken,
} from '@chakra-ui/react'
import { variables, IconError, IconPlayArrow, Spinner } from '@koupr/ui'
import cx from 'classnames'
import { FiChevronLeft } from 'react-icons/fi'
import ComparisonAPI from '@/api/comparison'
import ConnectionAPI from '@/api/connection'
import { comparisonSet } from '@/store/entities/comparisons'
import { useAppDispatch, useAppSelector } from '@/store/hook'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'
import List from './list'

type ResultProps = {
  sourceConnectionId: string
  targetConnectionId: string
  onDismiss: () => void
}

const Result = ({
  sourceConnectionId,
  targetConnectionId,
  onDismiss,
}: ResultProps) => {
  const dispatch = useAppDispatch()
  const comparison = useAppSelector(
    (state) =>
      state.entities.comparisons.items[
        `${sourceConnectionId}:${targetConnectionId}`
      ]
  )
  const [loading, setLoading] = useState(false)
  const { data: sourceConnection } =
    ConnectionAPI.useGetById(sourceConnectionId)
  const { data: targetConnection } =
    ConnectionAPI.useGetById(targetConnectionId)
  const [failed, setFailed] = useState(false)
  const [red500] = useToken('colors', ['red.500'])
  const [isCacheData, setIsCacheDataData] = useState(true)
  const [showCacheNotice, setShowCacheNotice] = useState(false)

  const handleCompare = useCallback(async () => {
    setLoading(true)
    try {
      const comparison = await ComparisonAPI.create({
        sourceConnectionId,
        targetConnectionId,
      })
      dispatch(
        comparisonSet({
          key: `${sourceConnectionId}:${targetConnectionId}`,
          value: comparison,
        })
      )
      setFailed(false)
    } catch {
      setFailed(true)
    } finally {
      setLoading(false)
    }
  }, [sourceConnectionId, targetConnectionId, dispatch])

  useEffect(() => {
    if (loading || failed) {
      return
    }
    if (!comparison) {
      setIsCacheDataData(false)
      handleCompare()
    } else if (isCacheData) {
      setShowCacheNotice(true)
    }
  }, [comparison, isCacheData, loading, failed, handleCompare])

  return (
    <Container
      topBar={
        <TopBar
          heading={
            <HStack spacing={variables.spacing}>
              <IconButton
                variant="ghost"
                icon={<FiChevronLeft fontSize="20px" />}
                aria-label=""
                onClick={() => onDismiss()}
              />
              <Heading flexShrink={0} size="md">
                Comparison of {sourceConnection && sourceConnection.name} and{' '}
                {targetConnection && targetConnection.name}
              </Heading>
            </HStack>
          }
          button={
            <Button
              colorScheme="blue"
              leftIcon={<IconPlayArrow />}
              isDisabled={loading}
              onClick={() => handleCompare()}
            >
              Compare
            </Button>
          }
        />
      }
    >
      {loading && (
        <Center h="300px" w="100%">
          <Stack
            direction="column"
            alignItems="center"
            justifyContent="center"
            spacing={variables.spacing}
          >
            <Spinner />
            <Text fontSize="16px">Comparing</Text>
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
            <IconError className={cx('text-[23px]')} color={red500} />
            <Text color={red500} fontSize="16px">
              Comparison failed
            </Text>
          </Stack>
        </Center>
      )}
      {showCacheNotice && !loading && (
        <Alert status="info" flexShrink={0}>
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
      {comparison && !loading && !failed && <List comparison={comparison} />}
    </Container>
  )
}

export default Result
