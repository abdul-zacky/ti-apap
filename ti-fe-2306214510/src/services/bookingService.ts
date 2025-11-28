import api from './api'

export interface Booking {
  bookingID: string
  roomID: string
  propertyName: string
  roomName: string
  checkInDate: string
  checkOutDate: string
  customerID: string
  customerName: string
  customerEmail: string
  customerPhone: string
  isBreakfast: boolean
  capacity: number
  totalPrice: number
  status: number
  extraPay: number
  createdDate: string
  updatedDate: string
}

export interface BookingRequest {
  roomID: string
  checkInDate: string
  checkOutDate: string
  customerID: string
  customerName: string
  customerEmail: string
  customerPhone: string
  isBreakfast: boolean
  capacity: number
}

export const bookingService = {
  async getAllBookings(searchTerm?: string, status?: number) {
    const params = new URLSearchParams()
    if (searchTerm) params.append('searchTerm', searchTerm)
    if (status !== undefined) params.append('status', status.toString())
    const response = await api.get(`/booking?${params.toString()}`)
    return response.data?.data || response.data
  },

  async getBookingById(id: string) {
    const response = await api.get(`/booking/${id}`)
    return response.data?.data || response.data
  },

  async createBooking(booking: BookingRequest) {
    const response = await api.post('/booking/create', booking)
    return response.data?.data || response.data
  },

  async confirmPayment(id: string) {
    const response = await api.put(`/booking/confirm/${id}`)
    return response.data?.data || response.data
  },

  async cancelBooking(id: string) {
    const response = await api.put(`/booking/cancel/${id}`)
    return response.data?.data || response.data
  },

  async completeBooking(id: string) {
    const response = await api.put(`/booking/complete/${id}`)
    return response.data?.data || response.data
  },

  async updateBooking(id: string, booking: any) {
    const response = await api.put(`/booking/update/${id}`, booking)
    return response.data?.data || response.data
  },

  async processRefund(id: string) {
    const response = await api.put(`/booking/refund/${id}`)
    return response.data?.data || response.data
  }
}
