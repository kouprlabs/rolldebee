import { useParams } from 'react-router-dom'
import Edit from '@/components/connection/edit'

const ConnectionEditPage = () => {
  const { id } = useParams()
  return <Edit id={id as string} />
}

export default ConnectionEditPage
