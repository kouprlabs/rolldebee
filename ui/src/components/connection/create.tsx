import { useCallback } from 'react'
import {
  Heading,
  IconButton,
  Stack,
  FormControl,
  FormErrorMessage,
  Select,
  Button,
  Input,
  useToast,
  InputGroup,
  InputRightElement,
  useBoolean,
  FormLabel,
} from '@chakra-ui/react'
import { variables } from '@koupr/ui'
import {
  Field,
  FieldAttributes,
  FieldProps,
  Form,
  Formik,
  FormikHelpers,
} from 'formik'
import * as Yup from 'yup'
import { BsEye, BsEyeSlash } from 'react-icons/bs'
import { FiChevronLeft } from 'react-icons/fi'
import ConnectionAPI from '@/api/connection'

type CreateProps = {
  onComplete: () => void
  onDismiss: () => void
}

type FormValues = {
  name: string
  jdbcUrl: string
  jdbcUsername: string
  jdbcPassword: string
  databaseType: string
}

const Create = ({ onComplete, onDismiss }: CreateProps) => {
  const toast = useToast()
  const [showPassword, setShowPassword] = useBoolean()

  const formSchema = Yup.object().shape({
    name: Yup.string().required('Name is required'),
    jdbcUrl: Yup.string().required('JDBC URL type is required'),
    jdbcUsername: Yup.string().required('JDBC username type is required'),
    jdbcPassword: Yup.string().optional(),
    databaseType: Yup.string().required('Database type is required'),
  })

  const handleSubmit = useCallback(
    async (
      values: FormValues,
      { setSubmitting }: FormikHelpers<FormValues>,
    ) => {
      setSubmitting(true)
      try {
        await ConnectionAPI.create({
          name: values.name,
          jdbcUrl: values.jdbcUrl,
          jdbcUsername: values.jdbcUsername,
          jdbcPassword: values.jdbcPassword,
          databaseType: values.databaseType,
        })
        setSubmitting(false)
        onComplete()
      } catch (e) {
        toast({
          title: e as string,
          status: 'error',
          isClosable: true,
        })
      } finally {
        setSubmitting(false)
      }
    },
    [toast, onComplete],
  )

  return (
    <Stack direction="column" spacing={variables.spacingLg}>
      <Stack direction="row" alignItems="center">
        <IconButton
          variant="ghost"
          icon={<FiChevronLeft fontSize="20px" />}
          aria-label=""
          onClick={() => onDismiss()}
        />
        <Heading size="md">New connection</Heading>
      </Stack>
      <Formik
        enableReinitialize={true}
        initialValues={{
          name: '',
          jdbcUrl: '',
          jdbcUsername: '',
          jdbcPassword: '',
          databaseType: '',
        }}
        validationSchema={formSchema}
        onSubmit={handleSubmit}
      >
        {({ errors, touched, isSubmitting }) => (
          <Form>
            <Stack spacing={variables.spacingLg}>
              <Stack spacing={variables.spacing}>
                <Field name="name">
                  {({ field }: FieldAttributes<FieldProps>) => (
                    <FormControl
                      isInvalid={Boolean(errors.name && touched.name)}
                    >
                      <FormLabel htmlFor="name">Name</FormLabel>
                      <Input {...field} w="400px" disabled={isSubmitting} />
                      <FormErrorMessage>{errors.name}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="jdbcUrl">
                  {({ field }: FieldAttributes<FieldProps>) => (
                    <FormControl
                      isInvalid={Boolean(errors.jdbcUrl && touched.jdbcUrl)}
                    >
                      <FormLabel htmlFor="jdbcUrl">JDBC URL</FormLabel>
                      <Input {...field} w="650px" disabled={isSubmitting} />
                      <FormErrorMessage>{errors.jdbcUrl}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="jdbcUsername">
                  {({ field }: FieldAttributes<FieldProps>) => (
                    <FormControl
                      isInvalid={Boolean(
                        errors.jdbcUsername && touched.jdbcUsername,
                      )}
                    >
                      <FormLabel htmlFor="jdbcUsername">
                        JDBC username
                      </FormLabel>
                      <Input {...field} w="400px" disabled={isSubmitting} />
                      <FormErrorMessage>{errors.jdbcUsername}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="jdbcPassword">
                  {({ field }: FieldAttributes<FieldProps>) => (
                    <FormControl
                      isInvalid={Boolean(
                        errors.jdbcPassword && touched.jdbcPassword,
                      )}
                    >
                      <FormLabel htmlFor="jdbcPassword">
                        JDBC password
                      </FormLabel>
                      <InputGroup w="400px">
                        <Input
                          {...field}
                          disabled={isSubmitting}
                          type={showPassword ? 'text' : 'password'}
                        />
                        <InputRightElement width="4.5rem">
                          <Button
                            h="28px"
                            mr="5px"
                            size="sm"
                            leftIcon={
                              showPassword ? (
                                <BsEyeSlash fontSize="14px" />
                              ) : (
                                <BsEye fontSize="14px" />
                              )
                            }
                            onClick={setShowPassword.toggle}
                          >
                            {showPassword ? 'Hide' : 'Show'}
                          </Button>
                        </InputRightElement>
                      </InputGroup>
                      <FormErrorMessage>{errors.jdbcPassword}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="databaseType">
                  {({ field }: FieldAttributes<FieldProps>) => (
                    <FormControl
                      isInvalid={Boolean(
                        errors.databaseType && touched.databaseType,
                      )}
                    >
                      <FormLabel htmlFor="databaseType">
                        Database type
                      </FormLabel>
                      <Select
                        {...field}
                        placeholder="Select a database type"
                        w="250px"
                      >
                        <option value="postgres" disabled>
                          Postgres (coming soon)
                        </option>
                        <option value="mysql" disabled>
                          MySQL (coming soon)
                        </option>
                        <option value="red">Oracle</option>
                      </Select>
                      <FormErrorMessage>{errors.databaseType}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
              </Stack>
              <Stack direction="row">
                <Button
                  type="submit"
                  variant="solid"
                  colorScheme="blue"
                  isLoading={isSubmitting}
                >
                  Create
                </Button>
              </Stack>
            </Stack>
          </Form>
        )}
      </Formik>
    </Stack>
  )
}

export default Create
