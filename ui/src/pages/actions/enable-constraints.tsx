import { Heading } from '@chakra-ui/react'
import List from '@/components/actions/list'
import EnableConstraints from '@/components/actions/run/enable-constraints'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'

const EnableConstraintsPage = () => (
  <Container
    topBar={
      <TopBar heading={<Heading size="md">Enable Constraints</Heading>} />
    }
  >
    <EnableConstraints />
    <List type="enable_constraints" />
  </Container>
)

export default EnableConstraintsPage
