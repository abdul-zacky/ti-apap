import api from './api'

export interface Province {
  code: string
  name: string
}

export const provinceService = {
  async getAllProvinces() {
    const response = await api.get<any>('/province')
    // Backend returns { status, message, data: [...] }
    return response.data.data as Province[]
  }
}

