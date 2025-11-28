import axios from 'axios'

// Use production backend URL, fallback to localhost for development
const baseURL = import.meta.env.VITE_API_URL ||
  (import.meta.env.MODE === 'production'
    ? 'http://2306214510-be.hafizmuh.site/api'
    : 'http://localhost:8080/api')

const api = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default api
