import React from 'react'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { Provider } from 'react-redux'
import { ChakraProvider } from '@chakra-ui/react'
import { theme } from '@koupr/ui'
import ClonePage from '@/pages/actions/clone'
import DisableConstraintsPage from '@/pages/actions/disable-constraints'
import DisableTriggersPage from '@/pages/actions/disable-triggers'
import DropObjectsPage from '@/pages/actions/drop-objects'
import EnableConstraintsPage from '@/pages/actions/enable-constraints'
import EnableTriggersPage from '@/pages/actions/enable-triggers'
import MigratePage from '@/pages/actions/migrate'
import ComparisonIndexPage from '@/pages/comparison'
import ComparisonViewPage from '@/pages/comparison/view'
import ConnectionEditPage from '@/pages/connection/edit'
import ConnectionIndexPage from '@/pages/connection/index'
import ConnectionNewPage from '@/pages/connection/new'
import IntrospectionIndexPage from '@/pages/introspection'
import IntrospectionViewPage from '@/pages/introspection/view'
import ReactDOM from 'react-dom/client'
import store from '@/store/index'
import Layout from '@/components/layout'

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        path: '/connections',
        element: <ConnectionIndexPage />,
      },
      {
        path: '/connections/new',
        element: <ConnectionNewPage />,
      },
      {
        path: '/connections/edit/:id',
        element: <ConnectionEditPage />,
      },
      {
        path: '/introspection',
        element: <IntrospectionIndexPage />,
      },
      {
        path: '/introspection/:id',
        element: <IntrospectionViewPage />,
      },
      {
        path: '/comparison',
        element: <ComparisonIndexPage />,
      },
      {
        path: '/comparison/:sourceId/:targetId',
        element: <ComparisonViewPage />,
      },
      {
        path: '/actions/clone',
        element: <ClonePage />,
      },
      {
        path: '/actions/migrate',
        element: <MigratePage />,
      },
      {
        path: '/actions/enable-constraints',
        element: <EnableConstraintsPage />,
      },
      {
        path: '/actions/disable-constraints',
        element: <DisableConstraintsPage />,
      },
      {
        path: '/actions/enable-triggers',
        element: <EnableTriggersPage />,
      },
      {
        path: '/actions/disable-triggers',
        element: <DisableTriggersPage />,
      },
      {
        path: '/actions/drop-objects',
        element: <DropObjectsPage />,
      },
    ],
  },
])

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <Provider store={store}>
      <ChakraProvider theme={theme}>
        <RouterProvider router={router} />
      </ChakraProvider>
    </Provider>
  </React.StrictMode>,
)
