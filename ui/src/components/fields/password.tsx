import { useBoolean, Stack, IconButton, Code } from '@chakra-ui/react'
import { BsEye, BsEyeSlash } from 'react-icons/bs'

type PasswordFieldProps = {
  value: string
}

const PasswordField = ({ value }: PasswordFieldProps) => {
  const [show, setShow] = useBoolean()
  return (
    <Stack direction="row" alignItems="center">
      <Code
        px="5px"
        h="24px"
        display="flex"
        alignItems="center"
        justifyContent="center"
      >
        {show ? value : '*********'}
      </Code>
      <IconButton
        size="xs"
        icon={show ? <BsEyeSlash fontSize="14px" /> : <BsEye fontSize="14px" />}
        onClick={setShow.toggle}
        aria-label=""
      />
    </Stack>
  )
}

export default PasswordField
