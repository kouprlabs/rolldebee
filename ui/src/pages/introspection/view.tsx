import { useNavigate, useParams } from 'react-router-dom'
import { Box } from '@chakra-ui/react'
import { variables } from '@koupr/ui'
import DatabaseObjects from '@/components/introspection/database-objects'

const IntrospectionViewPage = () => {
  const navigate = useNavigate()
  const { id } = useParams()
  if (id) {
    return (
      <Box p={variables.spacing} w="full">
        <DatabaseObjects
          connectionId={id as string}
          onDismiss={() => navigate('/introspection')}
        />
      </Box>
    )
  } else {
    return null
  }
}

export default IntrospectionViewPage
