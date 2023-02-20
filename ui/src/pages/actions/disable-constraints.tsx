import { Heading } from '@chakra-ui/react'
import List from '@/components/actions/list'
import DisableConstraints from '@/components/actions/run/disable-constraints'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'

const DisableConstraintsPage = () => (
  <Container
    topBar={
      <TopBar heading={<Heading size="md">Disable Constraints</Heading>} />
    }
  >
    <DisableConstraints />
    <List type="disable_constraints" />
  </Container>
)

export default DisableConstraintsPage
