import { useNavigate } from 'react-router-dom'
import { Box } from '@chakra-ui/react'
import Create from '@/components/connection/create'
import variables from '@/theme/variables'

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
