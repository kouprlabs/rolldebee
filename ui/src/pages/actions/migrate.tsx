import { Heading } from '@chakra-ui/react'
import List from '@/components/actions/list'
import Migrate from '@/components/actions/run/migrate'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'

const MigratePage = () => (
  <Container topBar={<TopBar heading={<Heading size="md">Migrate</Heading>} />}>
    <Migrate />
    <List type="migrate" />
  </Container>
)

export default MigratePage
