import { useCallback, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import {
  Heading,
  IconButton,
  Stack,
  FormControl,
  FormErrorMessage,
  Button,
  Input,
  useToast,
  InputGroup,
  InputRightElement,
  useBoolean,
  FormLabel,
  HStack,
} from '@chakra-ui/react'
import { variables } from '@koupr/ui'
import { SectionSpinner } from '@koupr/ui'
import { Field, FieldAttributes, Form, Formik } from 'formik'
import * as Yup from 'yup'
import { BsEye, BsEyeSlash } from 'react-icons/bs'
import { FiChevronLeft } from 'react-icons/fi'
import ConnectionAPI from '@/api/connection'
import TopBar from '@/components/common/top-bar'
import { Container } from '@/components/layout'
import Delete from './delete'

type EditProps = {
  id: string
}

const Edit = ({ id }: EditProps) => {
  const navigate = useNavigate()
  const toast = useToast()
  const [showPassword, setShowPassword] = useBoolean()
  const { data: connection, mutate } = ConnectionAPI.useGetById(id as string)
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false)

  const formSchema = Yup.object().shape({
    name: Yup.string().required('Name is required'),
    jdbcUrl: Yup.string().required('JDBC URL type is required'),
    jdbcUsername: Yup.string().required('JDBC username type is required'),
    jdbcPassword: Yup.string().optional(),
  })

  const handleSubmit = useCallback(
    async (values: any, { setSubmitting }: any) => {
      setSubmitting(true)
      try {
        await ConnectionAPI.update(id as string, {
          name: values.name,
          jdbcUrl: values.jdbcUrl,
          jdbcUsername: values.jdbcUsername,
          jdbcPassword: values.jdbcPassword,
        })
        setSubmitting(false)
        navigate('/connections')
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
    [id, toast, navigate],
  )

  if (!connection) {
    return <SectionSpinner />
  }

  return (
    <Container
      topBar={
        <TopBar
          heading={
            <HStack spacing={variables.spacing}>
              <IconButton
                as={Link}
                to="/connections"
                variant="ghost"
                icon={<FiChevronLeft fontSize="20px" />}
                aria-label=""
              />
              <Heading size="md">Edit {connection.name}</Heading>
            </HStack>
          }
        />
      }
    >
      <Formik
        enableReinitialize={true}
        initialValues={{
          name: connection.name,
          jdbcUrl: connection.jdbcUrl,
          jdbcUsername: connection.jdbcUsername,
          jdbcPassword: connection.jdbcPassword,
        }}
        validationSchema={formSchema}
        onSubmit={handleSubmit}
      >
        {({ errors, touched, isSubmitting }) => (
          <Form>
            <Stack spacing={variables.spacingLg}>
              <Stack spacing={variables.spacing}>
                <Field name="name">
                  {({ field }: FieldAttributes<any>) => (
                    <FormControl
                      isInvalid={errors.name && touched.name ? true : false}
                    >
                      <FormLabel htmlFor="name">Name</FormLabel>
                      <Input {...field} w="400px" disabled={isSubmitting} />
                      <FormErrorMessage>{errors.name}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="jdbcUrl">
                  {({ field }: FieldAttributes<any>) => (
                    <FormControl
                      isInvalid={
                        errors.jdbcUrl && touched.jdbcUrl ? true : false
                      }
                    >
                      <FormLabel htmlFor="jdbcUrl">JDBC URL</FormLabel>
                      <Input {...field} w="400px" disabled={isSubmitting} />
                      <FormErrorMessage>{errors.jdbcUrl}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
                <Field name="jdbcUsername">
                  {({ field }: FieldAttributes<any>) => (
                    <FormControl
                      isInvalid={
                        errors.jdbcUsername && touched.jdbcUsername
                          ? true
                          : false
                      }
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
                  {({ field }: FieldAttributes<any>) => (
                    <FormControl
                      isInvalid={
                        errors.jdbcPassword && touched.jdbcPassword
                          ? true
                          : false
                      }
                    >
                      <FormLabel htmlFor="jdbcPassword">
                        JDBC password
                      </FormLabel>
                      <InputGroup w="400px">
                        <Input
                          {...field}
                          isDisabled={isSubmitting}
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
              </Stack>
              <Stack direction="row" spacing={variables.spacingSm}>
                <Button
                  type="submit"
                  variant="solid"
                  colorScheme="blue"
                  isLoading={isSubmitting}
                >
                  Save
                </Button>
                <Button
                  variant="solid"
                  colorScheme="red"
                  isDisabled={isSubmitting}
                  onClick={() => setOpenDeleteDialog(true)}
                >
                  Delete
                </Button>
              </Stack>
            </Stack>
          </Form>
        )}
      </Formik>
      <Delete
        id={id}
        open={openDeleteDialog}
        onComplete={() => {
          mutate()
          setOpenDeleteDialog(false)
          navigate('/connections')
        }}
        onDismiss={() => setOpenDeleteDialog(false)}
      />
    </Container>
  )
}

export default Edit
