import { ReactNode } from 'react'
import { useEffect } from 'react'
import { Outlet, useLocation, useNavigate } from 'react-router-dom'
import { Flex, Stack } from '@chakra-ui/react'
import { Sidenav, SidenavItem, variables } from '@koupr/ui'
import { BsPlug, BsToggleOn, BsToggleOff, BsTrash } from 'react-icons/bs'
import { GiSheep } from 'react-icons/gi'
import { IoFlashOffOutline, IoFlashOutline } from 'react-icons/io5'
import {
  IoTelescopeOutline,
  IoSync,
  IoGitCompareOutline,
} from 'react-icons/io5'
import Logo from '@/components/common/logo'

const Layout = () => {
  const location = useLocation()
  const navigate = useNavigate()

  useEffect(() => {
    if (location.pathname === '/') {
      navigate('/connections')
    }
  }, [location.pathname, navigate])

  return (
    <Stack direction="row" spacing={0} height="100vh">
      <Sidenav
        storage={{ prefix: 'rolldebee', namespace: 'main' }}
        logo={<Logo />}
        navigateFn={navigate}
      >
        <SidenavItem
          href="/connections"
          icon={<BsPlug fontSize="20px" />}
          primaryText="Connections"
          secondaryText="Add JDBC connections"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/introspection"
          icon={<IoTelescopeOutline fontSize="20px" />}
          primaryText="Introspection"
          secondaryText="Scan database objects"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/comparison"
          icon={<IoGitCompareOutline fontSize="20px" />}
          primaryText="Comparison"
          secondaryText="Compare two schemas"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/actions/clone"
          icon={<GiSheep fontSize="20px" />}
          primaryText="Clone"
          secondaryText="Clone a schema"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/actions/migrate"
          icon={<IoSync fontSize="20px" />}
          primaryText="Migrate"
          secondaryText="Make two schemas identical"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/actions/enable-constraints"
          icon={<BsToggleOn fontSize="20px" />}
          primaryText="Enable Constraints"
          secondaryText="Enable all recursively"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/actions/disable-constraints"
          icon={<BsToggleOff fontSize="20px" />}
          primaryText="Disable Constraints"
          secondaryText="Disable all recursively"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/actions/enable-triggers"
          icon={<IoFlashOutline fontSize="20px" />}
          primaryText="Enable Triggers"
          secondaryText="Turn on all triggers"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/actions/disable-triggers"
          icon={<IoFlashOffOutline fontSize="20px" />}
          primaryText="Disable Triggers"
          secondaryText="Turn off all triggers"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
        <SidenavItem
          href="/actions/drop-objects"
          icon={<BsTrash fontSize="20px" />}
          primaryText="Drop Objects"
          secondaryText="Drop all schema objects"
          pathnameFn={() => location.pathname}
          navigateFn={navigate}
        />
      </Sidenav>
      <Flex flexGrow={1} overflowY="scroll" overflowX="hidden">
        <Outlet />
      </Flex>
    </Stack>
  )
}

type ContainerProps = {
  topBar?: ReactNode
  children?: ReactNode
}

export const Container = ({ topBar, children }: ContainerProps) => (
  <Stack p={variables.spacing} spacing={variables.spacingMd} w="full">
    {topBar}
    {children}
  </Stack>
)

export default Layout
