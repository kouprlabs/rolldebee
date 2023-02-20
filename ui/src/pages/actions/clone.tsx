import { Heading } from '@chakra-ui/react'
import List from '@/components/actions/list'
import Clone from '@/components/actions/run/clone'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'

const ClonePage = () => (
  <Container topBar={<TopBar heading={<Heading size="md">Clone</Heading>} />}>
    <Clone />
    <List type="clone" />
  </Container>
)

export default ClonePage
