/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const componentsRouter = {
  path: '/components',
  component: Layout,
  redirect: 'noRedirect',
  name: 'Component',
  meta: {
    title: 'Category',
    icon: 'component'
  },
  children: [
    {
      path: 'users',
      component: () => import('@/views/categories/Users'),
      name: 'Users',
      meta: { title: 'Users' }
    },
    {
      path: 'farms',
      component: () => import('@/views/categories/Farms'),
      name: 'farms',
      meta: { title: 'Farms' }
    },
    {
      path: 'devices',
      component: () => import('@/views/categories/Devices'),
      name: 'devices',
      meta: { title: 'Devices' }
    },
    {
      path: 'plants',
      component: () => import('@/views/categories/Plants'),
      name: 'plants',
      meta: { title: 'Plants' }
    },
    {
      path: 'crops',
      component: () => import('@/views/categories/Crops'),
      name: 'crops',
      meta: { title: 'Crops' }
    },
    {
      path: 'invoices',
      component: () => import('@/views/categories/Invoices'),
      name: 'invoices',
      meta: { title: 'Invoices' }
    },
    {
      path: 'payments',
      component: () => import('@/views/categories/Payments'),
      name: 'payments',
      meta: { title: 'Payments' }
    }
  ]
}

export default componentsRouter
