import {
  Tag,
  Button,
  useBoolean,
  TagLabel,
  Text,
  Stack,
} from '@chakra-ui/react'
import { Column } from '@/api/introspection'

type TableColumnsFieldProps = {
  columns: Column[]
}

const TableColumnsField = ({ columns }: TableColumnsFieldProps) => {
  const [showMore, setShowMore] = useBoolean(false)

  return (
    <Stack direction="column" alignItems="flex-start">
      {columns.slice(0, showMore ? columns.length - 1 : 5).map((column) => (
        <Tag key={column.id}>
          <TagLabel>{column.name}</TagLabel>
          <Text size="xs" ml="3px" color="gray.500">
            ({column.columnType})
          </Text>
        </Tag>
      ))}
      {columns.length > 5 && (
        <Button variant="outline" size="xs" onClick={setShowMore.toggle}>
          {showMore ? 'Show less' : 'Show more'}
        </Button>
      )}
    </Stack>
  )
}

export default TableColumnsField
