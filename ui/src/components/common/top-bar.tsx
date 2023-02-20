import { ReactNode } from 'react'
import { Flex } from '@chakra-ui/react'

type TopBarProps = {
  heading?: ReactNode
  button?: ReactNode
}

const TopBar = ({ heading, button }: TopBarProps) => (
  <Flex
    direction="row"
    h="40px"
    justifyContent="space-between"
    alignItems="center"
  >
    {heading}
    {button}
  </Flex>
)

export default TopBar
