import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/properties',
      name: 'properties',
      component: () => import('../views/PropertiesView.vue')
    },
    {
      path: '/properties/create',
      name: 'create-property',
      component: () => import('../views/CreatePropertyView.vue')
    },
    {
      path: '/bookings',
      name: 'bookings',
      component: () => import('../views/BookingsView.vue')
    },
    {
      path: '/bookings/create',
      name: 'create-booking',
      component: () => import('../views/CreateBookingView.vue')
    },
    {
      path: '/bookings/create/:roomId',
      name: 'create-booking-with-room',
      component: () => import('../views/CreateBookingView.vue')
    },
    {
      path: '/statistics',
      name: 'statistics',
      component: () => import('../views/StatisticsView.vue')
    }
  ]
})

export default router
