<template>
  <div class="create-booking fade-in">
    <div class="flex justify-between items-center" style="margin-bottom: 2rem;">
      <h1>{{ isPrefilledBooking ? 'Create Booking with Room' : 'Create New Booking' }}</h1>
      <router-link to="/bookings">
        <button class="glass-button">Back to Bookings</button>
      </router-link>
    </div>

    <div class="glass-card">
      <form @submit.prevent="handleSubmit" class="booking-form">
        <!-- Booking Dates (FIRST - Required before room selection) -->
        <div class="form-section">
          <h3>Tanggal Pemesanan</h3>
          <div v-if="!isPrefilledBooking" class="info-banner">
            ℹ️ <strong>Pilih tanggal check-in dan check-out terlebih dahulu</strong> untuk melihat kamar yang tersedia
          </div>
          <div class="form-grid">
            <div class="form-group">
              <label class="form-label">Tanggal Check-In <span class="required">*</span></label>
              <input
                v-model="formData.checkInDate"
                type="date"
                class="glass-input"
                required
                @change="onDateChange"
              />
            </div>
            <div class="form-group">
              <label class="form-label">Tanggal Check-Out <span class="required">*</span></label>
              <input
                v-model="formData.checkOutDate"
                type="date"
                class="glass-input"
                required
                :min="formData.checkInDate"
                @change="onDateChange"
              />
            </div>
          </div>
        </div>

        <!-- Room Selection (Only if NOT prefilled) - DISABLED until dates selected -->
        <div v-if="!isPrefilledBooking" class="form-section">
          <h3>Property & Room</h3>
          <div v-if="!areDatesValid" class="warning-banner">
            ⚠️ Isi tanggal check-in dan check-out terlebih dahulu
          </div>
          <div class="form-grid">
            <div class="form-group">
              <label class="form-label">Nama Properti <span class="required">*</span></label>
              <select 
                v-model="selectedPropertyId" 
                class="glass-input" 
                required
                :disabled="!areDatesValid"
                @change="onPropertyChange"
              >
                <option value="">-- Pilih Properti --</option>
                <option v-for="property in properties" :key="property.propertyID" :value="property.propertyID">
                  {{ property.propertyName }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Tipe Kamar <span class="required">*</span></label>
              <select 
                v-model="selectedRoomTypeId" 
                class="glass-input" 
                required
                :disabled="!selectedPropertyId || !areDatesValid"
                @change="onRoomTypeChange"
              >
                <option value="">-- Pilih Tipe Kamar --</option>
                <option v-for="roomType in roomTypes" :key="roomType.roomTypeID" :value="roomType.roomTypeID">
                  {{ roomType.name }} (Kapasitas: {{ roomType.capacity }}, Rp {{ formatNumber(roomType.price) }})
                </option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Nama Kamar <span class="required">*</span></label>
              <select 
                v-model="formData.roomID" 
                class="glass-input" 
                required
                :disabled="!selectedRoomTypeId || !areDatesValid"
                @change="onRoomChange"
              >
                <option value="">-- Pilih Kamar --</option>
                <option v-for="room in availableRooms" :key="room.roomID" :value="room.roomID">
                  {{ room.name }}
                </option>
              </select>
              <small v-if="loadingRooms" class="loading-text">⏳ Mengecek ketersediaan kamar...</small>
              <small v-else-if="selectedRoomTypeId && availableRooms.length === 0" class="error-text">
                ❌ Tidak ada kamar tersedia untuk tanggal yang dipilih
              </small>
            </div>
          </div>
        </div>

        <!-- Room Information (If prefilled - disabled) -->
        <div v-else class="form-section">
          <h3>Room Information</h3>
          <div class="form-grid">
            <div class="form-group">
              <label class="form-label">ID Kamar</label>
              <input
                v-model="formData.roomID"
                type="text"
                class="glass-input"
                disabled
              />
            </div>
            <div class="form-group">
              <label class="form-label">Nomor Kamar</label>
              <input
                v-model="formData.roomName"
                type="text"
                class="glass-input"
                disabled
              />
            </div>
          </div>
        </div>

        <!-- Customer Information -->
        <div class="form-section">
          <h3>Informasi Pelanggan</h3>
          <div class="form-grid">
            <div class="form-group">
              <label class="form-label">ID Pelanggan (UUID) <span class="required">*</span></label>
              <input
                v-model="formData.customerID"
                type="text"
                class="glass-input"
                placeholder="550e8400-e29b-41d4-a716-446655440001"
                required
                pattern="[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"
                title="Must be a valid UUID format"
              />
            </div>
            <div class="form-group">
              <label class="form-label">Nama Pelanggan <span class="required">*</span></label>
              <input
                v-model="formData.customerName"
                type="text"
                class="glass-input"
                placeholder="John Doe"
                required
              />
            </div>
            <div class="form-group">
              <label class="form-label">Email Pelanggan <span class="required">*</span></label>
              <input
                v-model="formData.customerEmail"
                type="email"
                class="glass-input"
                placeholder="john.doe@example.com"
                required
              />
            </div>
            <div class="form-group">
              <label class="form-label">Nomor Telepon Pelanggan <span class="required">*</span></label>
              <input
                v-model="formData.customerPhone"
                type="tel"
                class="glass-input"
                placeholder="08123456789"
                required
              />
            </div>
          </div>
        </div>

        <!-- Booking Details -->
        <div class="form-section">
          <h3>Detail Pemesanan</h3>
          <div class="form-grid">
            <div class="form-group">
              <label class="form-label">Kapasitas (Jumlah Tamu) <span class="required">*</span></label>
              <input
                v-model.number="formData.capacity"
                type="number"
                class="glass-input"
                min="1"
                required
              />
            </div>
            <div class="form-group">
              <label class="form-label">Add-on Breakfast <span class="required">*</span></label>
              <select v-model="formData.isBreakfast" class="glass-input" required @change="calculatePrice">
                <option :value="false">Tidak</option>
                <option :value="true">Ya (Rp 50,000/hari)</option>
              </select>
            </div>
          </div>
        </div>

        <!-- Price Summary -->
        <div v-if="priceSummary.totalDays > 0" class="price-summary">
          <h3>Ringkasan Harga</h3>
          <div class="summary-grid">
            <div class="summary-row">
              <span>Total Hari:</span>
              <span>{{ priceSummary.totalDays }} hari</span>
            </div>
            <div class="summary-row">
              <span>Harga Kamar (per malam):</span>
              <span>Rp {{ formatNumber(priceSummary.roomPrice) }}</span>
            </div>
            <div class="summary-row">
              <span>Subtotal (Kamar):</span>
              <span>Rp {{ formatNumber(priceSummary.roomSubtotal) }}</span>
            </div>
            <div v-if="formData.isBreakfast" class="summary-row">
              <span>Add-on Sarapan:</span>
              <span>Rp {{ formatNumber(priceSummary.breakfastTotal) }}</span>
            </div>
            <div class="summary-row total">
              <span><strong>Total Harga:</strong></span>
              <span><strong>Rp {{ formatNumber(priceSummary.totalPrice) }}</strong></span>
            </div>
          </div>
        </div>

        <!-- Form Actions -->
        <div class="form-actions">
          <router-link to="/bookings" class="glass-button">
            Batal
          </router-link>
          <button type="submit" class="glass-button primary" :disabled="loading">
            {{ loading ? 'Membuat...' : 'Buat Pemesanan' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { bookingService } from '@/services/bookingService'
import { propertyService } from '@/services/propertyService'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const loadingRooms = ref(false)
const isPrefilledBooking = ref(false)

// For non-prefilled booking (from Bookings page)
const properties = ref<any[]>([])
const roomTypes = ref<any[]>([])
const availableRooms = ref<any[]>([])
const selectedPropertyId = ref('')
const selectedRoomTypeId = ref('')

// Computed: Check if dates are valid
const areDatesValid = computed(() => {
  if (!formData.value.checkInDate || !formData.value.checkOutDate) {
    return false
  }
  const checkIn = new Date(formData.value.checkInDate)
  const checkOut = new Date(formData.value.checkOutDate)
  return checkOut > checkIn
})

const formData = ref({
  roomID: '',
  roomName: '',
  checkInDate: '',
  checkOutDate: '',
  customerID: '',
  customerName: '',
  customerEmail: '',
  customerPhone: '',
  capacity: 1,
  isBreakfast: false,
  roomPrice: 0
})

const priceSummary = computed(() => {
  const checkIn = new Date(formData.value.checkInDate)
  const checkOut = new Date(formData.value.checkOutDate)
  
  if (isNaN(checkIn.getTime()) || isNaN(checkOut.getTime()) || checkOut <= checkIn) {
    return {
      totalDays: 0,
      roomPrice: formData.value.roomPrice,
      roomSubtotal: 0,
      breakfastTotal: 0,
      totalPrice: 0
    }
  }
  
  const totalDays = Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24))
  const roomSubtotal = formData.value.roomPrice * totalDays
  const breakfastTotal = formData.value.isBreakfast ? 50000 * totalDays : 0
  const totalPrice = roomSubtotal + breakfastTotal
  
  return {
    totalDays,
    roomPrice: formData.value.roomPrice,
    roomSubtotal,
    breakfastTotal,
    totalPrice
  }
})

const formatNumber = (num: number) => {
  return new Intl.NumberFormat('id-ID').format(num)
}

const calculatePrice = () => {
  // Price calculation is reactive via computed property
  console.log('Price recalculated:', priceSummary.value)
}

const onDateChange = () => {
  console.log('Dates changed:', formData.value.checkInDate, formData.value.checkOutDate)
  
  // Reset selections when dates change
  if (!isPrefilledBooking.value) {
    selectedPropertyId.value = ''
    selectedRoomTypeId.value = ''
    formData.value.roomID = ''
    roomTypes.value = []
    availableRooms.value = []
  }
  
  calculatePrice()
}

const loadProperties = async () => {
  try {
    properties.value = await propertyService.getAllProperties()
    console.log('Loaded properties:', properties.value.length)
  } catch (error) {
    console.error('Error loading properties:', error)
    alert('Failed to load properties')
  }
}

const onPropertyChange = async () => {
  console.log('Property changed:', selectedPropertyId.value)
  selectedRoomTypeId.value = ''
  formData.value.roomID = ''
  roomTypes.value = []
  availableRooms.value = []
  
  if (!selectedPropertyId.value) return
  
  try {
    // Load room types for selected property
    roomTypes.value = await propertyService.getRoomsByPropertyId(selectedPropertyId.value)
    console.log('Loaded room types:', roomTypes.value.length)
  } catch (error) {
    console.error('Error loading room types:', error)
    alert('Failed to load room types')
  }
}

const onRoomTypeChange = async () => {
  console.log('Room type changed:', selectedRoomTypeId.value)
  formData.value.roomID = ''
  availableRooms.value = []
  
  if (!selectedRoomTypeId.value) return
  
  // Find selected room type to get price
  const selectedRoomType = roomTypes.value.find(rt => rt.roomTypeID === selectedRoomTypeId.value)
  if (selectedRoomType) {
    formData.value.roomPrice = selectedRoomType.price
  }
  
  // Check if dates are valid
  if (!areDatesValid.value) {
    alert('⚠️ Pilih tanggal check-in dan check-out terlebih dahulu')
    return
  }
  
  loadingRooms.value = true
  
  try {
    // Load ALL individual rooms for selected room type
    const allRooms = await propertyService.getAllRooms(selectedPropertyId.value)
    const roomsOfType = allRooms.filter((room: any) => room.roomTypeID === selectedRoomTypeId.value)
    
    console.log(`Found ${roomsOfType.length} rooms of type ${selectedRoomTypeId.value}`)
    
    // Check availability for each room
    // Pass date only (YYYY-MM-DD) - service will convert to ISO
    const availabilityData = await propertyService.checkRoomAvailability(
      selectedPropertyId.value,
      formData.value.checkInDate,
      formData.value.checkOutDate
    )
    
    // Find availability for this room type
    const roomTypeAvailability = availabilityData.find((a: any) => a.roomTypeID === selectedRoomTypeId.value)
    
    if (!roomTypeAvailability || roomTypeAvailability.availableRooms === 0) {
      // No rooms available for this type
      availableRooms.value = []
      console.log('❌ No available rooms for selected dates')
    } else {
      // Check each individual room's availability
      console.log(`Checking ${roomsOfType.length} individual rooms...`)
      const checkInDateTime = formData.value.checkInDate + 'T14:00:00'
      const checkOutDateTime = formData.value.checkOutDate + 'T12:00:00'
      
      const availabilityChecks = await Promise.all(
        roomsOfType.map(async (room: any) => {
          try {
            const isAvailable = await propertyService.checkSingleRoomAvailability(
              room.roomID,
              checkInDateTime,
              checkOutDateTime
            )
            return { ...room, isAvailable }
          } catch (error) {
            console.error(`Error checking room ${room.roomID}:`, error)
            return { ...room, isAvailable: false }
          }
        })
      )
      
      // Only show rooms that are actually available
      availableRooms.value = availabilityChecks.filter((room: any) => room.isAvailable)
      
      console.log(`✅ ${availableRooms.value.length}/${roomsOfType.length} rooms available for these dates`)
      
      if (availableRooms.value.length === 0) {
        alert('⚠️ All rooms of this type are booked for selected dates. Please try different dates or room type.')
      }
    }
  } catch (error) {
    console.error('Error checking room availability:', error)
    alert('❌ Gagal mengecek ketersediaan kamar')
    availableRooms.value = []
  } finally {
    loadingRooms.value = false
  }
}

const onRoomChange = () => {
  const selectedRoom = availableRooms.value.find(r => r.roomID === formData.value.roomID)
  if (selectedRoom) {
    formData.value.roomName = selectedRoom.name
  }
}

const handleSubmit = async () => {
  // Validate dates
  if (new Date(formData.value.checkOutDate) <= new Date(formData.value.checkInDate)) {
    alert('❌ Tanggal check-out harus setelah tanggal check-in')
    return
  }
  
  // Validate UUID format
  const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i
  if (!uuidRegex.test(formData.value.customerID)) {
    alert('❌ Masukkan UUID yang valid untuk ID Pelanggan')
    return
  }
  
  loading.value = true
  
  try {
    // Convert dates to ISO datetime format (backend expects ISO string)
    const checkInDateTime = formData.value.checkInDate + 'T14:00:00'
    const checkOutDateTime = formData.value.checkOutDate + 'T12:00:00'
    
    const bookingPayload = {
      roomID: formData.value.roomID,
      checkInDate: checkInDateTime,
      checkOutDate: checkOutDateTime,
      customerID: formData.value.customerID,
      customerName: formData.value.customerName,
      customerEmail: formData.value.customerEmail,
      customerPhone: formData.value.customerPhone,
      isBreakfast: formData.value.isBreakfast,
      capacity: formData.value.capacity
    }
    
    console.log('Creating booking with payload:', bookingPayload)
    
    await bookingService.createBooking(bookingPayload)
    
    alert('✅ Pemesanan berhasil dibuat!')
    
    // Set flag for Properties page
    localStorage.setItem('bookingJustCreated', 'true')
    
    // Navigate to bookings list
    router.push('/bookings')
  } catch (error: any) {
    console.error('Error creating booking:', error)
    const errorMsg = error.response?.data?.message || 'Gagal membuat pemesanan'
    alert('❌ ' + errorMsg)
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  console.log('CreateBookingView mounted')
  console.log('Route params:', route.params)
  
  // Check if roomId is provided in route
  const roomId = route.params.roomId as string
  
  // Load booking context from localStorage (set by Properties page)
  const bookingContextStr = localStorage.getItem('bookingContext')
  
  if (bookingContextStr) {
    // PREFILLED BOOKING (from Properties page)
    isPrefilledBooking.value = true
    
    try {
      const bookingContext = JSON.parse(bookingContextStr)
      console.log('Loaded booking context:', bookingContext)
      
      // Prefill form with context data
      formData.value.roomID = bookingContext.roomID || ''
      formData.value.roomName = bookingContext.roomName || ''
      formData.value.checkInDate = bookingContext.checkInDate || ''
      formData.value.checkOutDate = bookingContext.checkOutDate || ''
      formData.value.roomPrice = bookingContext.roomTypePrice || 0
      
      // Clear the context after loading
      localStorage.removeItem('bookingContext')
    } catch (error) {
      console.error('Error parsing booking context:', error)
    }
  } else if (roomId) {
    // If no context but roomId provided, just set the roomID
    isPrefilledBooking.value = true
    formData.value.roomID = roomId
    alert('⚠️ Silakan isi detail pemesanan')
  } else {
    // NON-PREFILLED BOOKING (from Bookings page)
    isPrefilledBooking.value = false
    console.log('Non-prefilled booking - loading properties')
    
    // Load properties for dropdown
    await loadProperties()
  }
})
</script>

<style scoped>
.create-booking {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.booking-form {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-section h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--primary);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text-secondary);
}

.required {
  color: var(--danger);
}

.glass-input:disabled {
  background: var(--bg-light);
  cursor: not-allowed;
  opacity: 0.7;
}

.info-banner {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border-left: 4px solid #2196f3;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  color: #1565c0;
  font-size: 0.9rem;
}

.warning-banner {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  border-left: 4px solid #ff9800;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  color: #e65100;
  font-size: 0.9rem;
}

.loading-text {
  color: #2196f3;
  font-size: 0.85rem;
  font-style: italic;
}

.error-text {
  color: #d32f2f;
  font-size: 0.85rem;
  font-weight: 500;
}

.price-summary {
  background: linear-gradient(135deg, var(--primary-light) 0%, var(--bg-white) 100%);
  border: 2px solid var(--primary);
  border-radius: 12px;
  padding: 1.5rem;
}

.price-summary h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--primary);
  margin-bottom: 1rem;
}

.summary-grid {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid var(--border-light);
}

.summary-row.total {
  border-bottom: none;
  border-top: 2px solid var(--primary);
  margin-top: 0.5rem;
  padding-top: 1rem;
  font-size: 1.125rem;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  padding-top: 1rem;
  border-top: 1px solid var(--border-light);
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
  
  .form-actions {
    flex-direction: column;
  }
}
</style>
