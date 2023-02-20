import { Heading } from '@chakra-ui/react'
import List from '@/components/actions/list'
import DisableTriggers from '@/components/actions/run/disable-triggers'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'

const DisableTriggersPage = () => (
  <Container
    topBar={<TopBar heading={<Heading size="md">Disable Triggers</Heading>} />}
  >
    <DisableTriggers />
    <List type="disable_triggers" />
  </Container>
)

export default DisableTriggersPage
