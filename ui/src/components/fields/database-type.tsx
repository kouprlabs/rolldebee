import { Text } from '@chakra-ui/react'
import { DatabaseType } from '@/api/connection'

type DatabaseTypeProps = {
  value: DatabaseType
}

const DatabaseTypeField = ({ value }: DatabaseTypeProps) => {
  if (value === 'postgres') {
    return <Text>PostgreSQL</Text>
  } else if (value === 'mysql') {
    return <Text>MySQL</Text>
  } else if (value === 'red') {
    return <Text>Oracle</Text>
  }
  return null
}

export default DatabaseTypeField
