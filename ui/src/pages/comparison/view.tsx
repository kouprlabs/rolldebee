import { useNavigate, useParams } from 'react-router-dom'
import Result from '@/components/comparison/result'

const ComparisonViewPage = () => {
  const navigate = useNavigate()
  const { sourceId, targetId } = useParams()
  if (sourceId && targetId) {
    return (
      <Result
        sourceConnectionId={sourceId}
        targetConnectionId={targetId}
        onDismiss={() => navigate('/comparison')}
      />
    )
  } else {
    return null
  }
}

export default ComparisonViewPage
