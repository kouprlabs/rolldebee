import { useRef, useCallback, useState, useEffect } from 'react'
import {
  Button,
  AlertDialog,
  AlertDialogBody,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogContent,
  AlertDialogOverlay,
  useDisclosure,
} from '@chakra-ui/react'
import ActionAPI from '@/api/action'
import variables from '@/theme/variables'

type DeleteProps = {
  id: string
  open: boolean
  onComplete: () => void
  onDismiss: () => void
}

const Delete = ({ id, open, onComplete, onDismiss }: DeleteProps) => {
  const { isOpen, onOpen, onClose } = useDisclosure()
  const cancelRef = useRef(null)
  const [deleteLoading, setDeleteLoading] = useState(false)

  useEffect(() => {
    if (open) {
      onOpen()
    }
  }, [open, onOpen])

  const handleDelete = useCallback(
    async (id: string) => {
      setDeleteLoading(true)
      try {
        await ActionAPI.delete(id)
        onComplete()
        onClose()
      } finally {
        setDeleteLoading(false)
      }
    },
    [onComplete, onClose]
  )

  return (
    <AlertDialog
      isOpen={isOpen}
      leastDestructiveRef={cancelRef}
      onClose={() => {
        onClose()
        onDismiss()
      }}
    >
      <AlertDialogOverlay>
        <AlertDialogContent>
          <AlertDialogHeader fontSize="lg" fontWeight="bold">
            Delete action
          </AlertDialogHeader>
          <AlertDialogBody>
            Are you sure you want delete this action?
          </AlertDialogBody>
          <AlertDialogFooter>
            <Button
              ref={cancelRef}
              onClick={() => {
                onClose()
                onDismiss()
              }}
            >
              Cancel
            </Button>
            <Button
              colorScheme="red"
              isLoading={deleteLoading}
              ml={variables.spacing}
              onClick={() => handleDelete(id)}
            >
              Delete
            </Button>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialogOverlay>
    </AlertDialog>
  )
}

export default Delete
