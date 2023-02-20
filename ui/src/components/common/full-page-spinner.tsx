import { Center } from '@chakra-ui/react'
import Spinner from '@/components/common/spinner'

const FullPageSpinner = (props: any) => (
  <Center h="300px" w="100%" {...props}>
    <Spinner />
  </Center>
)

export default FullPageSpinner
