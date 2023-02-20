import { ReactNode } from 'react'
import { Box, Button, Stack, useBoolean, useClipboard } from '@chakra-ui/react'
import { BsEye, BsEyeSlash } from 'react-icons/bs'
import { FiCopy } from 'react-icons/fi'
import SyntaxHighlighter from 'react-syntax-highlighter'
import { githubGist } from 'react-syntax-highlighter/dist/cjs/styles/hljs'

type CodeFieldsProps = {
  language: string
  width?: string
  maxHeight?: string
  children?: ReactNode
}

const CodeField = ({
  language,
  width = '450px',
  maxHeight = '200px',
  children,
}: CodeFieldsProps) => {
  const [show, setShow] = useBoolean(false)
  const { hasCopied, onCopy } = useClipboard(children as string)

  if (typeof children !== 'string') {
    return null
  }

  return (
    <Stack direction="column" w={width}>
      <Stack direction="row">
        <Button
          size="sm"
          leftIcon={
            show ? <BsEyeSlash fontSize="14px" /> : <BsEye fontSize="16px" />
          }
          onClick={setShow.toggle}
        >
          {show ? 'Hide' : 'Show'}
        </Button>
        {show && (
          <Button
            leftIcon={<FiCopy fontSize="14px" />}
            size="sm"
            onClick={onCopy}
          >
            {hasCopied ? 'Copied' : 'Copy'}
          </Button>
        )}
      </Stack>
      {show && (
        <Box
          maxH={maxHeight}
          overflowY="scroll"
          border="1px solid"
          borderColor="gray.200"
          borderRadius="7px"
        >
          <SyntaxHighlighter language={language} style={githubGist}>
            {children}
          </SyntaxHighlighter>
        </Box>
      )}
    </Stack>
  )
}

export default CodeField
