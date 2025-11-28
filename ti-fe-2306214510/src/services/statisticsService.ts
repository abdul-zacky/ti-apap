import api from './api'

export const statisticsService = {
  async getPropertiesByType() {
    const response = await api.get('/statistics/properties-by-type')
    return response.data?.data || response.data
  },

  async getBookingsByStatus() {
    const response = await api.get('/statistics/bookings-by-status')
    return response.data?.data || response.data
  },

  async getTopPropertiesByIncome() {
    const response = await api.get('/statistics/top-properties-by-income')
    return response.data?.data || response.data
  },

  async getStatisticsSummary() {
    const response = await api.get('/statistics/summary')
    return response.data?.data || response.data
  },

  async getBookingChart(month: number, year: number) {
    const response = await api.get(`/booking/chart?month=${month}&year=${year}`)
    return response.data?.data || response.data
  }
}
