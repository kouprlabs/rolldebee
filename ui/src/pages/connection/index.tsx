import { Link } from 'react-router-dom'
import { Button, Heading } from '@chakra-ui/react'
import { FiPlus } from 'react-icons/fi'
import TopBar from '@/components/common/top-bar'
import List from '@/components/connection/list'
import { Container } from '@/components/layout'

const ConnectionIndexPage = () => (
  <Container
    topBar={
      <TopBar
        heading={<Heading size="md">Connections</Heading>}
        button={
          <Button
            as={Link}
            to="/connections/new"
            colorScheme="blue"
            leftIcon={<FiPlus fontSize="14px" />}
          >
            New connection
          </Button>
        }
      />
    }
  >
    <List />
  </Container>
)

export default ConnectionIndexPage
