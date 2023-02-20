import { Heading } from '@chakra-ui/react'
import TopBar from '@/components/common/top-bar'
import Introspection from '@/components/introspection'
import { Container } from '@/components/layout'

const IntrospectionIndexPage = () => (
  <Container
    topBar={
      <TopBar
        heading={<Heading size="md">Select a schema to introspect</Heading>}
      />
    }
  >
    <Introspection />
  </Container>
)

export default IntrospectionIndexPage
