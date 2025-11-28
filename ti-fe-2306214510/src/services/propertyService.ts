import api from './api'

export interface RoomType {
  roomTypeID: string
  name: string
  capacity: number
  price: number
  description: string
  facility: string
}

export interface Property {
  propertyID: string
  propertyName: string
  type: number
  province: string
  address: string
  description: string
  ownerID: string
  ownerName: string
  totalRoom?: number
  activeStatus?: number
  income?: number
  listRoomType?: RoomType[]
}

export interface RoomTypeRequest {
  name: string
  price: number
  description: string
  capacity: number
  facility: string
  floor: number
  unit: number
}

export interface PropertyRequest {
  propertyName: string
  type: number
  province: string
  address: string
  description: string
  ownerID: string
  ownerName: string
  roomTypes: RoomTypeRequest[]
}

export const propertyService = {
  async getAllProperties(name?: string, type?: number, status?: number) {
    const params = new URLSearchParams()
    if (name) params.append('name', name)
    if (type) params.append('type', type.toString())
    if (status !== undefined) params.append('status', status.toString())
    const response = await api.get(`/property?${params.toString()}`)
    return response.data?.data || response.data
  },

  async getPropertyById(id: string) {
    const response = await api.get(`/property/${id}`)
    return response.data?.data || response.data
  },

  async createProperty(property: PropertyRequest) {
    const response = await api.post('/property/create', property)
    return response.data?.data || response.data
  },

  async updateProperty(property: Property) {
    const response = await api.put('/property/update', property)
    return response.data?.data || response.data
  },

  async deleteProperty(id: string) {
    const response = await api.delete(`/property/delete/${id}`)
    return response.data?.data || response.data
  },

  async countProperties() {
    const response = await api.get('/property/count')
    return response.data?.data || response.data
  },

  async getRoomsByPropertyId(propertyId: string) {
    console.log('🌐 [API] Calling GET /property/' + propertyId + '/rooms')
    const response = await api.get(`/property/${propertyId}/rooms`)
    console.log('🌐 [API] Response structure:', {
      hasData: !!response.data,
      dataType: typeof response.data,
      dataKeys: response.data ? Object.keys(response.data) : null,
      dataDataType: response.data?.data ? typeof response.data.data : null,
      isArray: Array.isArray(response.data?.data)
    })
    console.log('🌐 [API] Full response.data:', response.data)
    return response.data?.data || response.data
  },

  async addRoomType(propertyId: string, roomType: any) {
    const response = await api.post(`/property/updateroom?propertyID=${propertyId}`, roomType)
    return response.data?.data || response.data
  },

  async checkRoomAvailability(propertyId: string, checkIn: string, checkOut: string) {
    // Convert date format (YYYY-MM-DD) to ISO format (YYYY-MM-DDTHH:MM:SS)
    const checkInISO = `${checkIn}T00:00:00`
    const checkOutISO = `${checkOut}T23:59:59`
    
    const response = await api.get(
      `/property/${propertyId}/rooms/available?checkIn=${encodeURIComponent(checkInISO)}&checkOut=${encodeURIComponent(checkOutISO)}`
    )
    return response.data?.data || response.data
  },

  async getAllRooms(propertyId: string) {
    const response = await api.get(`/property/${propertyId}/all-rooms`)
    return response.data?.data || response.data
  },

  async syncTotalRooms(propertyId: string) {
    const response = await api.put(`/property/${propertyId}/sync-total-rooms`)
    return response.data?.data || response.data
  },

  async scheduleMaintenance(payload: any) {
    const response = await api.post('/property/maintenance/add', payload)
    return response.data?.data || response.data
  },

  async checkSingleRoomAvailability(roomId: string, checkIn: string, checkOut: string) {
    const response = await api.get(
      `/property/room/${roomId}/available?checkIn=${encodeURIComponent(checkIn)}&checkOut=${encodeURIComponent(checkOut)}`
    )
    return response.data?.data
  }
}