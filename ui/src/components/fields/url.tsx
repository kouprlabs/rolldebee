import { Stack, useClipboard, Button, Code } from '@chakra-ui/react'

type UrlFieldProps = {
  value: string
}

const UrlField = ({ value }: UrlFieldProps) => {
  const { hasCopied, onCopy } = useClipboard(value)
  return (
    <Stack direction="row" alignItems="center">
      <Code
        px="5px"
        h="24px"
        display="flex"
        alignItems="center"
        justifyContent="flex-start"
      >
        {value}
      </Code>
      <Button size="xs" onClick={onCopy}>
        {hasCopied ? 'Copied' : 'Copy'}
      </Button>
    </Stack>
  )
}

export default UrlField
