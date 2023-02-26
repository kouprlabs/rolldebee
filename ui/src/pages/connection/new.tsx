import { useNavigate } from 'react-router-dom'
import { Box } from '@chakra-ui/react'
import { variables } from '@koupr/ui'
import Create from '@/components/connection/create'

const ConnectionNewPage = () => {
  const navigate = useNavigate()
  return (
    <Box p={variables.spacing} w="full">
      <Create
        onComplete={() => navigate('/connections')}
        onDismiss={() => navigate('/connections')}
      />
    </Box>
  )
}

export default ConnectionNewPage
