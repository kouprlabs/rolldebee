import { Heading } from '@chakra-ui/react'
import List from '@/components/actions/list'
import EnableTriggers from '@/components/actions/run/enable-triggers'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'

const EnableTriggersPage = () => (
  <Container
    topBar={<TopBar heading={<Heading size="md">Enable Triggers</Heading>} />}
  >
    <EnableTriggers />
    <List type="enable_triggers" />
  </Container>
)

export default EnableTriggersPage
