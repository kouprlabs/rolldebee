import { ReactNode } from 'react'
import { Badge } from '@chakra-ui/react'

type DiffTypeFieldProps = {
  children?: ReactNode
}

const DiffTypeField = ({ children }: DiffTypeFieldProps) => {
  if (children === 'ADDED') {
    return <Badge colorScheme="green">{children}</Badge>
  } else if (children === 'MODIFIED') {
    return <Badge colorScheme="yellow">{children}</Badge>
  } else if (children === 'DELETED') {
    return <Badge colorScheme="red">{children}</Badge>
  }
  return null
}

export default DiffTypeField
