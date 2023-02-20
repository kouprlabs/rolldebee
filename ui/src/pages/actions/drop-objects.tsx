import { Alert, AlertIcon, Heading } from '@chakra-ui/react'
import List from '@/components/actions/list'
import DropObjects from '@/components/actions/run/drop-objects'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'

const DropObjectsPage = () => (
  <Container
    topBar={<TopBar heading={<Heading size="md">Drop Objects</Heading>} />}
  >
    <DropObjects />
    <Alert status="error">
      <AlertIcon />
      {
        "This action is irreversible! Don't run on production systems! It is very important that to backup your data before running this action."
      }
    </Alert>
    <List type="drop_objects" />
  </Container>
)

export default DropObjectsPage
