import { progressAnatomy as parts } from '@chakra-ui/anatomy'
import {
  mode,
  PartsStyleFunction,
  StyleFunctionProps,
  SystemStyleFunction,
  SystemStyleObject,
} from '@chakra-ui/theme-tools'
import variables from '@/theme/variables'

function filledStyle(props: StyleFunctionProps): SystemStyleObject {
  const { colorScheme, hasStripe } = props
  if (hasStripe) {
    return { bg: variables.gradiant }
  } else {
    return { bgColor: mode(`${colorScheme}.500`, `${colorScheme}.200`)(props) }
  }
}

const baseStyleFilledTrack: SystemStyleFunction = (props) => {
  return {
    ...filledStyle(props),
  }
}

const baseStyle: PartsStyleFunction<typeof parts> = (props) => ({
  filledTrack: baseStyleFilledTrack(props),
  track: {
    borderRadius: variables.borderRadius,
  },
})

const progress = {
  baseStyle,
}

export default progress
