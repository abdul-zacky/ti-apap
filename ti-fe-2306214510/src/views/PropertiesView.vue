<template>
  <div class="properties fade-in">
    <div class="flex justify-between items-center" style="margin-bottom: 2rem;">
      <h1>Properties Management</h1>
      <router-link to="/properties/create">
        <button class="glass-button primary">Create Property</button>
      </router-link>
    </div>

    <!-- Filter Section -->
    <div class="glass-card" style="margin-bottom: 2rem;">
      <h3>Filters</h3>
      <div class="grid grid-cols-3 gap-2">
        <div class="form-group">
          <label class="form-label">Search by Name</label>
          <input
            v-model="filters.name"
            type="text"
            class="glass-input"
            placeholder="Property name..."
            @input="loadProperties"
          />
        </div>
        <div class="form-group">
          <label class="form-label">Type</label>
          <select v-model="filters.type" class="glass-input" @change="loadProperties">
            <option :value="undefined">All Types</option>
            <option :value="1">Hotel</option>
            <option :value="2">Villa</option>
            <option :value="3">Apartment</option>
          </select>
        </div>
        <div class="form-group">
          <label class="form-label">Status</label>
          <select v-model="filters.status" class="glass-input" @change="loadProperties">
            <option :value="undefined">All Status</option>
            <option :value="1">Active</option>
            <option :value="0">Inactive</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Properties List -->
    <div v-if="loading" class="glass-card">
      <p>Loading properties...</p>
    </div>

    <div v-else-if="properties.length === 0" class="glass-card">
      <p>No properties found. Create your first property!</p>
    </div>

    <div v-else class="grid grid-cols-3 gap-2">
      <div
        v-for="property in properties"
        :key="property.propertyID"
        class="property-card glass-card"
      >
        <div class="property-header">
          <h3>{{ property.propertyName }}</h3>
          <span class="property-type">{{ getPropertyType(property.type) }}</span>
        </div>

        <div class="property-info">
          <div class="info-row">
            <span class="label">ID:</span>
            <span class="value">{{ property.propertyID }}</span>
          </div>
          <div class="info-row">
            <span class="label">Owner:</span>
            <span class="value">{{ property.ownerName }}</span>
          </div>
          <div class="info-row">
            <span class="label">Total Rooms:</span>
            <span class="value">{{ property.totalRoom }}</span>
          </div>
          <div class="info-row">
            <span class="label">Income:</span>
            <span class="value">Rp {{ formatNumber(property.income) }}</span>
          </div>
          <div class="info-row">
            <span class="label">Status:</span>
            <span :class="['badge', property.activeStatus === 1 ? 'badge-success' : 'badge-danger']">
              {{ property.activeStatus === 1 ? 'Active' : 'Inactive' }}
            </span>
          </div>
        </div>

        <div class="property-actions">
          <button class="glass-button" @click="openViewModal(property)">
            View
          </button>
          <button class="glass-button danger" @click="deleteProperty(property.propertyID)">
            Delete
          </button>
        </div>
      </div>
    </div>

    <!-- View Property Modal -->
    <div v-if="showViewModal" class="modal-overlay" @click.self="closeViewModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>{{ selectedProperty?.propertyName }}</h2>
          <button class="modal-close" @click="closeViewModal">&times;</button>
        </div>
        <div class="modal-body" v-if="selectedProperty">
          <div class="detail-grid">
            <div class="detail-item">
              <label>Property ID</label>
              <p>{{ selectedProperty.propertyID }}</p>
            </div>
            <div class="detail-item">
              <label>Nama Properti</label>
              <p>{{ selectedProperty.propertyName }}</p>
            </div>
            <div class="detail-item">
              <label>Tipe</label>
              <p>{{ getPropertyType(selectedProperty.type) }}</p>
            </div>
            <div class="detail-item">
              <label>Provinsi</label>
              <p>{{ getProvinceName(selectedProperty.province) }}</p>
            </div>
            <div class="detail-item">
              <label>Alamat</label>
              <p>{{ selectedProperty.address }}</p>
            </div>
            <div class="detail-item">
              <label>Total Kamar</label>
              <p>{{ selectedProperty.totalRoom }}</p>
            </div>
            <div class="detail-item">
              <label>Nama Pemilik</label>
              <p>{{ selectedProperty.ownerName }}</p>
            </div>
            <div class="detail-item">
              <label>ID Pemilik</label>
              <p>{{ selectedProperty.ownerID }}</p>
            </div>
            <div class="detail-item">
              <label>Pendapatan</label>
              <p>Rp {{ formatNumber(selectedProperty.income) }}</p>
            </div>
            <div class="detail-item">
              <label>Status</label>
              <p>
                <span :class="['badge', selectedProperty.activeStatus === 1 ? 'badge-success' : 'badge-danger']">
                  {{ selectedProperty.activeStatus === 1 ? 'Active' : 'Inactive' }}
                </span>
              </p>
            </div>
            <div class="detail-item" style="grid-column: 1 / -1;">
              <label>Deskripsi Properti</label>
              <p>{{ selectedProperty.description }}</p>
            </div>
          </div>

          <!-- Room Filter Section -->
          <div class="room-filter-section">
            <div class="filter-header">
              <h4>Filter by Availability</h4>
              <span v-if="!filterApplied" style="color: var(--primary); font-size: 0.875rem;">
                ⚠️ Apply filter to check real-time availability
              </span>
            </div>
            <div class="filter-controls">
              <div class="form-group">
                <label class="form-label">Check In</label>
                <input
                  v-model="roomFilters.checkIn"
                  type="date"
                  class="form-input"
                  @change="updateRoomFilters"
                />
              </div>
              <div class="form-group">
                <label class="form-label">Check Out</label>
                <input
                  v-model="roomFilters.checkOut"
                  type="date"
                  class="form-input"
                  @change="updateRoomFilters"
                />
              </div>
              <div class="form-group" style="display: flex; align-items: flex-end;">
                <button class="glass-button primary" @click="applyRoomFilter">
                  Apply Filter
                </button>
              </div>
            </div>
          </div>

          <!-- Room List Section -->
          <div class="rooms-section">
            <h3>Room Types ({{ filteredPropertyRooms.length }} types)</h3>
            
            <div v-if="loadingRooms" class="loading-text">
              <p>Loading room types...</p>
            </div>
            <div v-else-if="propertyRooms.length === 0" class="empty-text">
              <p>No room types available</p>
            </div>
            <div v-else-if="filteredPropertyRooms.length === 0" class="empty-text">
              <p>No available rooms for selected dates</p>
            </div>
            <div v-else class="rooms-list">
              <div v-for="roomType in filteredPropertyRooms" :key="roomType.roomTypeID" class="room-type-card">
                <div class="room-type-header" @click="toggleRoomTypeExpand(roomType.roomTypeID)">
                  <div class="room-type-info">
                    <span class="expand-icon" :class="{ expanded: expandedRoomTypes.includes(roomType.roomTypeID) }">
                      ▼
                    </span>
                    <h4>{{ roomType.name }}</h4>
                    <span class="room-type-meta">
                      Capacity: {{ roomType.capacity }} guests | Rp {{ formatNumber(roomType.price) }}/night | Floor {{ roomType.floor }}
                    </span>
                  </div>
                  <div class="room-type-summary">
                    <span class="room-count">{{ roomType.totalRooms }} rooms</span>
                    <span v-if="filterApplied" :class="['room-status', roomType.availableRooms > 0 ? 'available' : 'unavailable']">
                      {{ roomType.availableRooms }}/{{ roomType.totalRooms }} available
                    </span>
                    <span v-else class="room-status status-default">
                      Apply filter to check
                    </span>
                  </div>
                </div>

                <!-- Individual Rooms Table (Expandable) -->
                <div v-if="expandedRoomTypes.includes(roomType.roomTypeID)" class="room-type-details">
                  <div v-if="filterApplied" class="filter-info-banner">
                    ℹ️ <strong>Note:</strong> Individual room status shown may not reflect real-time conflicts. 
                    {{ roomType.availableRooms }}/{{ roomType.totalRooms }} rooms are available for these dates. 
                    Try booking to check if specific room is available.
                  </div>
                  <table class="individual-rooms-table">
                    <thead>
                      <tr>
                        <th>Room Name</th>
                        <th>Status</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="room in getRoomsForType(roomType.roomTypeID)" :key="room.roomID">
                        <td>{{ room.name }}</td>
                        <td>
                          <span v-if="filterApplied" class="availability-badge status-default">
                            Check on booking
                          </span>
                          <span v-else class="availability-badge status-neutral">
                            -
                          </span>
                        </td>
                        <td>
                          <div class="room-action-buttons">
                            <button 
                              class="action-btn book-btn" 
                              @click="bookIndividualRoom(room, roomType)"
                              :title="filterApplied ? 'Try booking this room' : (room.availabilityStatus !== 1 ? 'Room not available' : 'Book this room')"
                            >
                              Book
                            </button>
                            <button 
                              class="action-btn maintenance-btn" 
                              @click="maintenanceRoom(room, roomType)"
                              :disabled="room.availabilityStatus !== 1 && !filterApplied"
                              :title="'Schedule maintenance'"
                            >
                              Maintenance
                            </button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="modal-actions">
          <button 
            v-if="selectedProperty?.activeStatus === 1"
            class="action-card" 
            @click="handleUpdateProperty"
          >
            <span>Update</span>
          </button>
          <button 
            v-if="selectedProperty?.activeStatus === 1"
            class="action-card danger" 
            @click="handleDeleteProperty"
          >
            <span>Delete</span>
          </button>
          <button 
            v-if="selectedProperty?.activeStatus === 1"
            class="action-card" 
            @click="handleAddRoom"
          >
            <span>Add Room</span>
          </button>
          <button 
            class="action-card" 
            @click="handleSyncTotalRooms"
            title="Sync Total Kamar count"
          >
            <span>Sync Total</span>
          </button>
          <button class="action-card" @click="closeViewModal">
            <span>Back</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Update Property Modal -->
    <div v-if="showUpdateModal" class="modal-overlay" @click.self="closeUpdateModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>Update Property</h2>
          <button class="modal-close" @click="closeUpdateModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitUpdateProperty" class="update-form">
            <!-- Property Details Section -->
            <h3 style="margin-bottom: 1rem;">Property Information</h3>
            
            <div class="form-group">
              <label class="form-label">Property ID</label>
              <input
                :value="selectedProperty?.propertyID"
                type="text"
                class="form-input"
                disabled
              />
            </div>

            <div class="form-group">
              <label class="form-label">Property Name *</label>
              <input
                v-model="updateForm.propertyName"
                type="text"
                class="form-input"
                placeholder="Enter property name"
                required
              />
            </div>

            <div class="grid grid-cols-2 gap-2">
              <div class="form-group">
                <label class="form-label">Type *</label>
                <select v-model.number="updateForm.type" class="form-input" required>
                  <option :value="1">Hotel</option>
                  <option :value="2">Villa</option>
                  <option :value="3">Apartment</option>
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Province *</label>
                <input
                  v-model="updateForm.province"
                  type="text"
                  class="form-input"
                  placeholder="Enter province"
                  required
                />
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Address *</label>
              <input
                v-model="updateForm.address"
                type="text"
                class="form-input"
                placeholder="Enter address"
                required
              />
            </div>

            <div class="form-group">
              <label class="form-label">Description *</label>
              <textarea
                v-model="updateForm.description"
                class="form-input"
                placeholder="Enter description"
                rows="3"
                required
              ></textarea>
            </div>

            <!-- Room Types Section -->
            <hr style="margin: 2rem 0; border-color: rgba(0,0,0,0.1);" />
            <h3 style="margin-bottom: 1rem;">Room Types</h3>
            
            <div
              v-for="(roomType, index) in updateForm.listRoomType"
              :key="index"
              class="glass-card"
              style="margin-bottom: 1rem; padding: 1rem;"
            >
              <h4 style="margin-bottom: 1rem;">{{ roomType.name }}</h4>

              <div class="form-group">
                <label class="form-label">Room Type ID</label>
                <input
                  :value="roomType.roomTypeID"
                  type="text"
                  class="form-input"
                  disabled
                />
              </div>

              <div class="grid grid-cols-2 gap-2">
                <div class="form-group">
                  <label class="form-label">Capacity *</label>
                  <input
                    v-model.number="roomType.capacity"
                    type="number"
                    class="form-input"
                    min="1"
                    required
                  />
                </div>

                <div class="form-group">
                  <label class="form-label">Price *</label>
                  <input
                    v-model.number="roomType.price"
                    type="number"
                    class="form-input"
                    min="0"
                    required
                  />
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">Description *</label>
                <textarea
                  v-model="roomType.description"
                  class="form-input"
                  placeholder="Enter description"
                  rows="2"
                  required
                ></textarea>
              </div>

              <div class="form-group">
                <label class="form-label">Facility *</label>
                <input
                  v-model="roomType.facility"
                  type="text"
                  class="form-input"
                  placeholder="e.g., WiFi, TV, AC"
                  required
                />
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" class="glass-button" @click="closeUpdateModal">Cancel</button>
              <button type="submit" class="glass-button primary">Update Property</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Add Room Modal -->
    <div v-if="showAddRoomModal" class="modal-overlay" @click.self="closeAddRoomModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>Add Room to: {{ selectedProperty?.propertyID }}</h2>
          <button class="modal-close" @click="closeAddRoomModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmitAddRoom" class="update-form">
            <div class="form-group">
              <label class="form-label">Room Type Name *</label>
              <select
                v-model="addRoomForm.roomTypeName"
                class="form-input"
                required
              >
                <option value="">-- Select Room Type --</option>
                <option
                  v-for="roomType in availableRoomTypes"
                  :key="roomType"
                  :value="roomType"
                >
                  {{ roomType }}
                </option>
              </select>
            </div>

            <div class="grid grid-cols-2 gap-2">
              <div class="form-group">
                <label class="form-label">Capacity (guests) *</label>
                <input
                  v-model.number="addRoomForm.capacity"
                  type="number"
                  class="form-input"
                  min="1"
                  required
                />
              </div>

              <div class="form-group">
                <label class="form-label">Price per Night *</label>
                <input
                  v-model.number="addRoomForm.price"
                  type="number"
                  class="form-input"
                  min="0"
                  required
                />
              </div>
            </div>

            <div class="grid grid-cols-2 gap-2">
              <div class="form-group">
                <label class="form-label">Floor *</label>
                <input
                  v-model.number="addRoomForm.floor"
                  type="number"
                  class="form-input"
                  min="1"
                  required
                />
              </div>

              <div class="form-group">
                <label class="form-label">Number of Rooms *</label>
                <input
                  v-model.number="addRoomForm.unit"
                  type="number"
                  class="form-input"
                  min="1"
                  required
                />
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Facility *</label>
              <input
                v-model="addRoomForm.facility"
                type="text"
                class="form-input"
                placeholder="e.g., WiFi, TV, AC"
                required
              />
            </div>

            <div class="form-group">
              <label class="form-label">Description *</label>
              <textarea
                v-model="addRoomForm.description"
                class="form-input"
                placeholder="Enter room description"
                rows="3"
                required
              ></textarea>
            </div>

            <div class="modal-footer">
              <button type="button" class="glass-button" @click="closeAddRoomModal">Cancel</button>
              <button type="submit" class="glass-button primary">Add Room</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Maintenance Modal -->
    <div v-if="showMaintenanceModal" class="modal-overlay" @click.self="closeMaintenanceModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>Schedule Maintenance</h2>
          <button class="modal-close" @click="closeMaintenanceModal">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmitMaintenance" class="update-form">
            <div v-if="selectedRoom && selectedRoomType" class="info-section">
              <p><strong>Room:</strong> {{ selectedRoom.name }}</p>
              <p><strong>Room Type:</strong> {{ selectedRoomType.name }}</p>
              <p><strong>Capacity:</strong> {{ selectedRoomType.capacity }} guests</p>
            </div>

            <div class="form-group">
              <label class="form-label">Maintenance Start Date & Time *</label>
              <div class="grid grid-cols-2 gap-2">
                <input
                  v-model="maintenanceForm.maintenanceStart"
                  type="date"
                  class="form-input"
                  required
                />
                <input
                  v-model="maintenanceForm.maintenanceStartTime"
                  type="time"
                  class="form-input"
                  required
                />
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">Maintenance End Date & Time *</label>
              <div class="grid grid-cols-2 gap-2">
                <input
                  v-model="maintenanceForm.maintenanceEnd"
                  type="date"
                  class="form-input"
                  required
                />
                <input
                  v-model="maintenanceForm.maintenanceEndTime"
                  type="time"
                  class="form-input"
                  required
                />
              </div>
            </div>

            <div class="note-section" style="background-color: #f0f4ff; padding: 1rem; border-radius: 0.5rem; margin: 1rem 0;">
              <p style="font-size: 0.9rem; color: #666;">
                <strong>Note:</strong> If there's already a maintenance schedule for this room, it will be replaced. 
                The system will check for booking conflicts automatically.
              </p>
            </div>

            <div class="modal-footer">
              <button type="button" class="glass-button" @click="closeMaintenanceModal">Cancel</button>
              <button type="submit" class="glass-button primary">Schedule Maintenance</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { propertyService, type Property, type RoomType } from '@/services/propertyService'
import { provinceService, type Province } from '@/services/provinceService'
import { getRoomTypesByPropertyType } from '@/constants/roomTypes'

interface Room {
  roomTypeID: string
  name: string
  capacity: number
  price: number
  floor: number
  description: string
  facility: string
  totalRooms: number
  availableRooms: number
}

interface IndividualRoom {
  roomID: string
  name: string
  availabilityStatus: number
  activeRoom: number
  roomTypeID: string
}

const properties = ref<Property[]>([])
const provinces = ref<Province[]>([])
const loading = ref(true)
const showViewModal = ref(false)
const showUpdateModal = ref(false)
const showAddRoomModal = ref(false)
const selectedProperty = ref<Property | null>(null)
const propertyRooms = ref<Room[]>([])
const filteredPropertyRooms = ref<Room[]>([])
const loadingRooms = ref(false)
const expandedRoomTypes = ref<string[]>([])
const allIndividualRooms = ref<IndividualRoom[]>([])
const roomFilters = ref({
  checkIn: '',
  checkOut: ''
})
const filterApplied = ref(false)

// Map province code to name
const getProvinceName = (code: string): string => {
  if (!code) return 'N/A'
  const province = provinces.value.find(p => p.code === code)
  return province ? province.name : code
}
const updateForm = ref<{
  propertyName: string
  type: number
  province: string
  address: string
  description: string
  ownerName: string
  listRoomType: RoomType[]
}>({
  propertyName: '',
  type: 1,
  province: '',
  address: '',
  description: '',
  ownerName: '',
  listRoomType: []
})
const addRoomForm = ref({
  roomTypeName: '',
  capacity: 1,
  price: 0,
  floor: 1,
  unit: 1,
  description: '',
  facility: ''
})
const filters = ref({
  name: '',
  type: undefined as number | undefined,
  status: undefined as number | undefined
})

// Maintenance modal and form state
const showMaintenanceModal = ref(false)
const selectedRoom = ref<IndividualRoom | null>(null)
const selectedRoomType = ref<Room | null>(null)
const maintenanceForm = ref({
  maintenanceStart: '',
  maintenanceStartTime: '00:00',
  maintenanceEnd: '',
  maintenanceEndTime: '00:00'
})

const getPropertyType = (type: number) => {
  const types: Record<number, string> = { 1: 'Hotel', 2: 'Villa', 3: 'Apartment' }
  return types[type] || 'Unknown'
}

const availableRoomTypes = computed(() => {
  if (!selectedProperty.value) return []
  return getRoomTypesByPropertyType(selectedProperty.value.type)
})

const formatNumber = (num: number | undefined) => {
  if (num === undefined) return '0'
  return new Intl.NumberFormat('id-ID').format(num)
}

const loadProperties = async () => {
  try {
    loading.value = true
    const response = await propertyService.getAllProperties(
      filters.value.name || undefined,
      filters.value.type,
      filters.value.status
    )
    properties.value = response
  } catch (error) {
    console.error('Error loading properties:', error)
    alert('Failed to load properties')
  } finally {
    loading.value = false
  }
}

const openViewModal = (property: Property) => {
  selectedProperty.value = property
  showViewModal.value = true
  loadPropertyRooms(property.propertyID)
}

const loadPropertyRooms = async (propertyId: string) => {
  try {
    console.log('🔄 [LOAD ROOMS] Starting to load rooms for property:', propertyId)
    loadingRooms.value = true
    
    // propertyService.getRoomsByPropertyId already returns the array directly
    const roomsData = await propertyService.getRoomsByPropertyId(propertyId)
    console.log('📦 [LOAD ROOMS] Received rooms data:', roomsData)
    console.log('📦 [LOAD ROOMS] roomsData type:', typeof roomsData, 'isArray:', Array.isArray(roomsData))
    
    propertyRooms.value = Array.isArray(roomsData) ? roomsData : []
    filteredPropertyRooms.value = propertyRooms.value
    console.log('✅ [LOAD ROOMS] Loaded', propertyRooms.value.length, 'room types')
    
    filterApplied.value = false
    roomFilters.value = { checkIn: '', checkOut: '' }
    
    // Load individual rooms - this also returns array directly
    const allRoomsData = await propertyService.getAllRooms(propertyId)
    allIndividualRooms.value = Array.isArray(allRoomsData) ? allRoomsData : []
    console.log('✅ [LOAD ROOMS] Loaded', allIndividualRooms.value.length, 'individual rooms')
    
    // Reset expanded room types
    expandedRoomTypes.value = []
  } catch (error) {
    console.error('❌ [LOAD ROOMS] Error loading property rooms:', error)
    propertyRooms.value = []
    filteredPropertyRooms.value = []
    allIndividualRooms.value = []
  } finally {
    loadingRooms.value = false
  }
}

const updateRoomFilters = () => {
  // This function is called on date change, just placeholder
  // Actual filtering happens on Apply Filter click
}

const applyRoomFilter = async () => {
  console.log('🔍 [FILTER] Apply Filter button clicked')
  console.log('Check-In:', roomFilters.value.checkIn)
  console.log('Check-Out:', roomFilters.value.checkOut)
  
  if (!roomFilters.value.checkIn || !roomFilters.value.checkOut) {
    alert('Please select both check-in and check-out dates')
    return
  }

  if (new Date(roomFilters.value.checkIn) >= new Date(roomFilters.value.checkOut)) {
    alert('Check-out date must be after check-in date')
    return
  }

  try {
    // Call backend to check availability for the date range
    if (!selectedProperty.value) return
    
    console.log('🌐 [FILTER] Calling API:', {
      propertyID: selectedProperty.value.propertyID,
      checkIn: roomFilters.value.checkIn,
      checkOut: roomFilters.value.checkOut
    })
    
    const availabilityData = await propertyService.checkRoomAvailability(
      selectedProperty.value.propertyID,
      roomFilters.value.checkIn,
      roomFilters.value.checkOut
    )
    
    console.log('📦 [FILTER] API Response received:', availabilityData)
    console.log('📋 [FILTER] Is array?', Array.isArray(availabilityData))
    console.log('� [FILTER] API Response received:', availabilityData)
    console.log('📋 [FILTER] Is array?', Array.isArray(availabilityData))
    console.log('�📋 [FILTER] Availability Data length:', availabilityData?.length || 0)
    
    // Ensure availabilityData is an array
    const availabilityArray = Array.isArray(availabilityData) ? availabilityData : []
    
    if (availabilityArray.length === 0) {
      console.warn('⚠️ [FILTER] No availability data received from API')
      alert('❌ Failed to check room availability. Please try again.')
      return
    }
    
    // Filter room types: only show those with available rooms (availableRooms > 0)
    filteredPropertyRooms.value = propertyRooms.value.map((room: any) => {
      // Find matching room availability from response
      const availabilityInfo = availabilityArray.find((a: any) => a.roomTypeID === room.roomTypeID)
      console.log(`  Room Type: ${room.name} (${room.roomTypeID})`, {
        apiAvailable: availabilityInfo?.availableRooms,
        apiTotal: availabilityInfo?.totalRooms,
        localTotal: room.totalRooms
      })
      // Update with latest availability info
      return {
        ...room,
        availableRooms: availabilityInfo?.availableRooms ?? 0,
        totalRooms: availabilityInfo?.totalRooms ?? room.totalRooms
      }
    }).filter((room: any) => room.availableRooms > 0) // Only show rooms with availability
    
    // Update individual rooms availability based on filter dates
    // Mark rooms as unavailable if their room type has 0 available rooms
    console.log('🔄 [FILTER] Updating individual rooms availability...')
    allIndividualRooms.value = allIndividualRooms.value.map((room: any) => {
      // Find the room type summary to check if this room type has availability
      const roomTypeSummary = availabilityArray.find((a: any) => a.roomTypeID === room.roomTypeID)
      
      // If room type has 0 available rooms, mark ALL rooms in this type as unavailable
      // If room type has availability, mark ALL rooms as available (since API doesn't tell which specific ones)
      // User can try to book and backend will validate
      if (!roomTypeSummary || roomTypeSummary.availableRooms === 0) {
        return {
          ...room,
          availabilityStatus: 0 // Mark as unavailable for UI display
        }
      } else {
        // Room type has availability - mark as available
        // Note: This is optimistic. Some individual rooms may still be booked/maintenance
        // but we show them as available and let backend validate on booking attempt
        return {
          ...room,
          availabilityStatus: 1 // Mark as available
        }
      }
    })
    
    console.log('✨ [FILTER] Filtered rooms (after filtering):', filteredPropertyRooms.value)
    console.log('✨ [FILTER] Updated individual rooms count:', allIndividualRooms.value.length)
    
    filterApplied.value = true
    
    if (filteredPropertyRooms.value.length === 0) {
      alert('❌ No available rooms for the selected dates')
    } else {
      alert(`✅ Found ${filteredPropertyRooms.value.length} available room types`)
    }
  } catch (error) {
    console.error('❌ [FILTER] Error checking availability:', error)
    alert('Failed to check room availability')
  }
}

const bookRoom = (room: Room) => {
  if (!selectedProperty.value || !roomFilters.value.checkIn || !roomFilters.value.checkOut) {
    alert('Please select dates first')
    return
  }
  
  // Navigate to booking or open booking modal
  alert(`Booking room type: ${room.name} for ${selectedProperty.value.propertyName}`)
}

// Toggle room type expansion
const toggleRoomTypeExpand = async (roomTypeID: string) => {
  const index = expandedRoomTypes.value.indexOf(roomTypeID)
  if (index > -1) {
    expandedRoomTypes.value.splice(index, 1)
  } else {
    expandedRoomTypes.value.push(roomTypeID)
    
    // If filter is applied, update individual room availability status
    if (filterApplied.value && selectedProperty.value && roomFilters.value.checkIn && roomFilters.value.checkOut) {
      console.log('🔍 [EXPAND] Checking individual room availability for room type:', roomTypeID)
      await updateIndividualRoomAvailability(roomTypeID)
    }
  }
}

// Update individual room availability status for a specific room type
const updateIndividualRoomAvailability = async (roomTypeID: string) => {
  try {
    const checkInDateTime = roomFilters.value.checkIn + 'T14:00:00'
    const checkOutDateTime = roomFilters.value.checkOut + 'T12:00:00'
    
    // Get all individual rooms for this room type
    const roomsOfType = allIndividualRooms.value.filter((r: any) => r.roomTypeID === roomTypeID)
    
    console.log(`Checking ${roomsOfType.length} rooms for availability...`)
    
    // Check availability for each room by calling backend
    // Backend isRoomAvailable checks for conflicting bookings and maintenance
    for (const room of roomsOfType) {
      try {
        // Call backend to check if this specific room is available
        const isAvailable = await checkSingleRoomAvailability(room.roomID, checkInDateTime, checkOutDateTime)
        
        // Update the room's availability status in allIndividualRooms
        const roomIndex = allIndividualRooms.value.findIndex((r: any) => r.roomID === room.roomID)
        if (roomIndex !== -1) {
          allIndividualRooms.value[roomIndex] = {
            ...allIndividualRooms.value[roomIndex],
            availabilityStatus: isAvailable ? 1 : 0
          }
        }
        
        console.log(`  Room ${room.roomID} (${room.name}): ${isAvailable ? 'Available' : 'Not Available'}`)
      } catch (error) {
        console.error(`Error checking room ${room.roomID}:`, error)
        // On error, mark as unavailable to be safe
        const roomIndex = allIndividualRooms.value.findIndex((r: any) => r.roomID === room.roomID)
        if (roomIndex !== -1) {
          allIndividualRooms.value[roomIndex] = {
            ...allIndividualRooms.value[roomIndex],
            availabilityStatus: 0
          }
        }
      }
    }
    
    console.log('✅ [EXPAND] Updated individual room availability')
  } catch (error) {
    console.error('❌ [EXPAND] Error updating individual room availability:', error)
  }
}

// Check if a single room is available for given dates
// This calls the backend endpoint: GET /api/property/room/{roomID}/available
const checkSingleRoomAvailability = async (roomID: string, checkIn: string, checkOut: string): Promise<boolean> => {
  try {
    const isAvailable = await propertyService.checkSingleRoomAvailability(roomID, checkIn, checkOut)
    return isAvailable === true
  } catch (error) {
    console.error(`Error checking availability for room ${roomID}:`, error)
    return false // On error, assume not available to be safe
  }
}

// Get individual rooms for a specific room type
const getRoomsForType = (roomTypeID: string): IndividualRoom[] => {
  return allIndividualRooms.value.filter(room => room.roomTypeID === roomTypeID)
}

// Book individual room
const bookIndividualRoom = (room: IndividualRoom, roomType: Room) => {
  console.log('📌 [BOOK ROOM] Book button clicked for room:', room.roomID)
  
  if (room.availabilityStatus !== 1) {
    alert('This room is not available')
    return
  }
  
  if (!roomFilters.value.checkIn || !roomFilters.value.checkOut) {
    alert('Please apply filter with check-in and check-out dates first')
    return
  }
  
  // Store booking context in localStorage for prefill
  const bookingContext = {
    roomID: room.roomID,
    roomName: room.name,
    checkInDate: roomFilters.value.checkIn,
    checkOutDate: roomFilters.value.checkOut,
    propertyName: selectedProperty.value?.propertyName || '',
    roomTypeName: roomType.name,
    roomTypePrice: roomType.price
  }
  
  console.log('💾 [BOOK ROOM] Storing booking context:', bookingContext)
  localStorage.setItem('bookingContext', JSON.stringify(bookingContext))
  
  console.log('🔀 [BOOK ROOM] Navigating to /bookings/create/' + room.roomID)
  // Navigate to Create Booking page with room ID
  window.location.href = `/bookings/create/${room.roomID}`
}

// Schedule maintenance for room
const maintenanceRoom = (room: IndividualRoom, roomType: Room) => {
  if (room.availabilityStatus !== 1) {
    alert('This room is not available for scheduling maintenance')
    return
  }
  
  selectedRoom.value = room
  selectedRoomType.value = roomType
  
  // Set default dates to today
  const today = new Date().toISOString().split('T')[0]
  maintenanceForm.value = {
    maintenanceStart: today,
    maintenanceStartTime: '00:00',
    maintenanceEnd: today,
    maintenanceEndTime: '23:59'
  }
  
  showViewModal.value = false
  showMaintenanceModal.value = true
}

const closeViewModal = () => {
  showViewModal.value = false
  selectedProperty.value = null
}

const handleUpdateProperty = () => {
  if (!selectedProperty.value) return
  // Load property data ke form
  updateForm.value = {
    propertyName: selectedProperty.value.propertyName,
    type: selectedProperty.value.type,
    province: selectedProperty.value.province,
    address: selectedProperty.value.address,
    description: selectedProperty.value.description || '',
    ownerName: selectedProperty.value.ownerName,
    listRoomType: selectedProperty.value.listRoomType || []
  }
  showViewModal.value = false
  showUpdateModal.value = true
}

const handleDeleteProperty = () => {
  if (!selectedProperty.value) return
  deleteProperty(selectedProperty.value.propertyID)
  closeViewModal()
}

const handleAddRoom = () => {
  if (!selectedProperty.value) return
  addRoomForm.value = {
    roomTypeName: '',
    capacity: 1,
    price: 0,
    floor: 1,
    unit: 1,
    description: '',
    facility: ''
  }
  showViewModal.value = false
  showAddRoomModal.value = true
}

const handleSubmitAddRoom = async () => {
  if (!selectedProperty.value) return

  // Validate all required fields are filled
  if (!addRoomForm.value.roomTypeName || !addRoomForm.value.description || !addRoomForm.value.facility) {
    alert('Please fill all required fields (Room Type Name, Description, Facility)')
    return
  }

  // Validate numeric fields
  if (!addRoomForm.value.capacity || addRoomForm.value.capacity < 1) {
    alert('Capacity must be at least 1')
    return
  }

  if (!addRoomForm.value.price || addRoomForm.value.price < 0) {
    alert('Price must be 0 or greater')
    return
  }

  if (!addRoomForm.value.floor || addRoomForm.value.floor < 1) {
    alert('Floor must be at least 1')
    return
  }

  // Validate unit (minimal 1 unit room)
  if (!addRoomForm.value.unit || addRoomForm.value.unit < 1) {
    alert('Must add at least 1 unit room')
    return
  }

  // Check for duplicate room type on the same floor
  const existingRoomTypes = filteredPropertyRooms.value
  const isDuplicate = existingRoomTypes.some(rt => 
    rt.name === addRoomForm.value.roomTypeName && 
    rt.floor === addRoomForm.value.floor
  )
  
  if (isDuplicate) {
    alert(`Room type "${addRoomForm.value.roomTypeName}" already exists on floor ${addRoomForm.value.floor}`)
    return
  }

  try {
    const response = await propertyService.addRoomType(selectedProperty.value.propertyID, {
      name: addRoomForm.value.roomTypeName,
      capacity: addRoomForm.value.capacity,
      price: addRoomForm.value.price,
      floor: addRoomForm.value.floor,
      unit: addRoomForm.value.unit,
      description: addRoomForm.value.description,
      facility: addRoomForm.value.facility
    })
    
    if (response.status !== undefined || response.status === 0) {
      alert('✅ Room type added successfully!')
      showAddRoomModal.value = false
      // Reload properties to refresh the list
      if (selectedProperty.value) {
        await loadProperties()
        openViewModal(selectedProperty.value)
      }
    }
  } catch (error: any) {
    console.error('Error adding room:', error)
    const errorMsg = error.response?.data?.message || 'Failed to add room type'
    alert(`❌ ${errorMsg}`)
  }
}

const closeAddRoomModal = () => {
  showAddRoomModal.value = false
}

const closeMaintenanceModal = () => {
  showMaintenanceModal.value = false
  selectedRoom.value = null
  selectedRoomType.value = null
}

const handleSubmitMaintenance = async () => {
  if (!selectedRoom.value || !selectedProperty.value) return

  // Validate required fields
  if (!maintenanceForm.value.maintenanceStart || !maintenanceForm.value.maintenanceEnd) {
    alert('Please fill start and end dates')
    return
  }

  // Combine date and time
  const startDateTime = new Date(`${maintenanceForm.value.maintenanceStart}T${maintenanceForm.value.maintenanceStartTime}:00`)
  const endDateTime = new Date(`${maintenanceForm.value.maintenanceEnd}T${maintenanceForm.value.maintenanceEndTime}:00`)

  // Validate end date >= start date
  if (endDateTime < startDateTime) {
    alert('End date & time must be after or equal to start date & time')
    return
  }

  // Validate end time not earlier than start time on same day
  if (maintenanceForm.value.maintenanceStart === maintenanceForm.value.maintenanceEnd) {
    if (maintenanceForm.value.maintenanceEndTime <= maintenanceForm.value.maintenanceStartTime) {
      alert('On the same day, end time must be later than start time')
      return
    }
  }

  try {
    const response = await propertyService.scheduleMaintenance({
      roomID: selectedRoom.value.roomID,
      maintenanceStart: startDateTime.toISOString(),
      maintenanceEnd: endDateTime.toISOString()
    })

    if (response) {
      alert(`✅ Maintenance scheduled successfully for room ${selectedRoom.value.name}!\n\n⚠️ Room availability has been updated. Please re-apply filter to see changes.`)
      closeMaintenanceModal()
      
      // Clear filter and reload property rooms
      if (selectedProperty.value) {
        filterApplied.value = false
        roomFilters.value = { checkIn: '', checkOut: '' }
        await loadProperties()
        await loadPropertyRooms(selectedProperty.value.propertyID)
        showViewModal.value = true
      }
    }
  } catch (error: any) {
    console.error('Error scheduling maintenance:', error)
    const errorMsg = error.response?.data?.message || 'Failed to schedule maintenance'
    alert(`❌ ${errorMsg}`)
  }
}

const handleSyncTotalRooms = async () => {
  if (!selectedProperty.value) return
  
  const oldTotal = selectedProperty.value.totalRoom
  
  try {
    const response = await propertyService.syncTotalRooms(selectedProperty.value.propertyID)
    console.log('🔄 Sync response:', response)
    
    if (response && response.totalRoom !== undefined) {
      const newTotal = response.totalRoom
      selectedProperty.value.totalRoom = newTotal
      
      if (oldTotal !== newTotal) {
        alert(`✅ Total Rooms updated: ${oldTotal} → ${newTotal}`)
      } else {
        alert(`✅ Total Rooms already correct: ${newTotal}`)
      }
      
      // Reload properties list to reflect changes
      await loadProperties()
    } else {
      alert('✅ Sync completed successfully')
      await loadProperties()
    }
  } catch (error) {
    console.error('❌ Error syncing total rooms:', error)
    alert('❌ Failed to sync total rooms. Please try again.')
  }
}

const handleAddMaintenance = () => {
  if (!selectedProperty.value) return
  alert(`Add maintenance to: ${selectedProperty.value.propertyID}`)
  closeViewModal()
}

const handleCreateBooking = () => {
  if (!selectedProperty.value) return
  alert(`Create booking for: ${selectedProperty.value.propertyID}`)
  closeViewModal()
}

const closeUpdateModal = () => {
  showUpdateModal.value = false
  updateForm.value = {
    propertyName: '',
    type: 1,
    province: '',
    address: '',
    description: '',
    ownerName: '',
    listRoomType: []
  }
}

const submitUpdateProperty = async () => {
  if (!selectedProperty.value) return
  
  // Validate no empty fields
  if (!updateForm.value.propertyName || !updateForm.value.address || !updateForm.value.description) {
    alert('Please fill all required property fields')
    return
  }

  // Validate room types
  for (const roomType of updateForm.value.listRoomType) {
    if (!roomType.name || !roomType.capacity || !roomType.price || !roomType.description || !roomType.facility) {
      alert('Please fill all required fields for all room types')
      return
    }
  }
  
  try {
    // Prepare update payload
    const payload = {
      propertyID: selectedProperty.value.propertyID,
      propertyName: updateForm.value.propertyName,
      type: updateForm.value.type,
      province: updateForm.value.province,
      address: updateForm.value.address,
      description: updateForm.value.description,
      ownerID: selectedProperty.value.ownerID,
      ownerName: updateForm.value.ownerName,
      listRoomType: updateForm.value.listRoomType
    }

    await propertyService.updateProperty(payload)
    alert('Property updated successfully!')
    closeUpdateModal()
    loadProperties()
  } catch (error: any) {
    console.error('Error updating property:', error)
    alert(error.response?.data?.message || 'Failed to update property')
  }
}

const viewProperty = (id: string) => {
  alert(`View property: ${id}`)
}

const deleteProperty = async (id: string) => {
  if (!confirm('Are you sure you want to delete this property?')) return

  try {
    await propertyService.deleteProperty(id)
    alert('Property deleted successfully')
    loadProperties()
  } catch (error: any) {
    console.error('Error deleting property:', error)
    alert(error.response?.data?.message || 'Failed to delete property')
  }
}

const loadProvinces = async () => {
  try {
    provinces.value = await provinceService.getAllProvinces()
  } catch (error) {
    console.error('Error loading provinces:', error)
  }
}

onMounted(async () => {
  await loadProvinces()
  await loadProperties()
  
  // Check if user just created a booking
  const justBooked = localStorage.getItem('bookingJustCreated')
  if (justBooked === 'true') {
    localStorage.removeItem('bookingJustCreated')
    alert('✅ Booking created successfully!\n\n⚠️ Room availability has been updated. Please re-apply filter to see current availability.')
  }
  
  // Clear any pending booking data
  localStorage.removeItem('pendingBooking')
})
</script>

<style scoped>
.property-card {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  background: var(--bg-white);
  border: 1px solid var(--border-light);
  border-radius: 12px;
  padding: 1.5rem;
  transition: all 0.3s ease;
  height: 100%;
}

.property-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
  border-color: var(--primary-blue);
}

.property-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  border-bottom: 2px solid var(--border-light);
  padding-bottom: 1rem;
  gap: 1rem;
}

.property-header h3 {
  margin: 0;
  font-size: 1.25rem;
  color: var(--text-primary);
  font-weight: 700;
}

.property-type {
  font-size: 0.85rem;
  font-weight: 700;
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--teal-cyan) 100%);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  white-space: nowrap;
}

.property-info {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  flex: 1;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: rgba(74, 144, 226, 0.04);
  border-radius: 8px;
  border: 1px solid var(--border-light);
}

.info-row .label {
  color: var(--text-secondary);
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: capitalize;
}

.info-row .value {
  font-weight: 700;
  color: var(--text-primary);
  text-align: right;
}

.property-actions {
  display: flex;
  gap: 0.75rem;
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-light);
}

.property-actions button {
  flex: 1;
  padding: 0.75rem 1rem;
  font-size: 0.9rem;
}

.badge {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 700;
  display: inline-block;
  text-transform: capitalize;
}

.badge-success {
  background: linear-gradient(135deg, rgba(46, 204, 113, 0.1) 0%, rgba(32, 201, 201, 0.1) 100%);
  color: var(--success-green);
  border: 1px solid rgba(46, 204, 113, 0.3);
}

.badge-danger {
  background: linear-gradient(135deg, rgba(231, 76, 60, 0.1) 0%, rgba(255, 105, 180, 0.1) 100%);
  color: var(--danger-red);
  border: 1px solid rgba(231, 76, 60, 0.3);
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.2s ease;
}

.modal-content {
  background: var(--bg-white);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
  max-width: 1000px;
  width: 95%;
  max-height: 90vh;
  border: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2rem;
  border-bottom: 2px solid var(--border-light);
  gap: 1rem;
}

.modal-header h2 {
  margin: 0;
  color: var(--text-primary);
  font-size: 1.75rem;
  font-weight: 700;
}

.modal-close {
  background: none;
  border: none;
  font-size: 2rem;
  color: var(--text-muted);
  cursor: pointer;
  padding: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.modal-close:hover {
  background: rgba(74, 144, 226, 0.1);
  color: var(--primary-blue);
}

.modal-body {
  padding: 2rem;
  flex: 1;
  overflow-y: auto;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.detail-item label {
  font-size: 0.85rem;
  font-weight: 700;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.detail-item p {
  font-size: 1.1rem;
  color: var(--text-primary);
  margin: 0;
  font-weight: 600;
}

/* Modal Actions */
.modal-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  padding: 1.5rem 2rem;
  border-top: 2px solid var(--border-light);
  background: var(--bg-white);
  flex-shrink: 0;
}

.modal-actions .action-card:nth-child(5) {
  grid-column: 1 / -1;
  max-width: 50%;
}

.action-card {
  padding: 0.75rem 1.25rem;
  background: transparent;
  border: 1px solid var(--primary-blue);
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  color: var(--primary-blue);
  transition: all 0.2s ease;
  font-size: 0.9rem;
}

.action-card:hover {
  background: rgba(74, 144, 226, 0.1);
}

.action-card.danger {
  border-color: var(--danger-red);
  color: var(--danger-red);
}

.action-card.danger:hover {
  background: rgba(231, 76, 60, 0.1);
}

.modal-footer {
  padding: 1.5rem 2rem;
  border-top: 1px solid var(--border-light);
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  background: var(--bg-white);
  flex-shrink: 0;
}

.modal-footer .glass-button {
  min-width: 120px;
}

/* Update Form */
.update-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-label {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 0.5rem;
}

.form-input {
  padding: 0.75rem 1rem;
  border: 1px solid var(--border-light);
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-primary);
  font-size: 0.95rem;
  transition: all 0.2s ease;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-blue);
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
}

.form-input::placeholder {
  color: var(--text-secondary);
  opacity: 0.7;
}

.grid {
  display: grid;
}

.grid-cols-2 {
  grid-template-columns: repeat(2, 1fr);
}

.gap-2 {
  gap: 1rem;
}

@media (max-width: 768px) {
  .detail-grid {
    grid-template-columns: 1fr;
  }

  .modal-header h2 {
    font-size: 1.5rem;
  }

  .grid-cols-2 {
    grid-template-columns: 1fr;
  }
}

/* Rooms Section */
.rooms-section {
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 2px solid var(--border-light);
}

.rooms-section h3 {
  color: var(--text-primary);
  margin-bottom: 1.5rem;
  font-size: 1.1rem;
}

.loading-text,
.empty-text {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
}

.rooms-table-wrapper {
  overflow-x: auto;
}

.rooms-table {
  width: 100%;
  border-collapse: collapse;
}

.rooms-table thead {
  background: rgba(74, 144, 226, 0.1);
  border-bottom: 2px solid var(--border-light);
}

.rooms-table th {
  padding: 1.2rem;
  text-align: left;
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.95rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.rooms-table td {
  padding: 1.2rem;
  border-bottom: 1px solid var(--border-light);
  color: var(--text-primary);
  font-size: 0.95rem;
}

.rooms-table tbody tr:hover {
  background: rgba(74, 144, 226, 0.05);
}

.room-status {
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-size: 0.85rem;
  font-weight: 600;
}

.room-status.available {
  background: rgba(46, 204, 113, 0.2);
  color: var(--success-green);
}

.room-status.unavailable {
  background: rgba(231, 76, 60, 0.2);
  color: var(--danger-red);
}

.room-status.status-default {
  background: rgba(158, 158, 158, 0.1);
  color: var(--text-muted);
  font-style: italic;
  font-weight: 500;
}

.room-actions {
  display: flex;
  gap: 0.5rem;
}

.room-btn {
  padding: 0.4rem 0.8rem;
  font-size: 0.85rem;
  border-radius: 4px;
  border: 1px solid;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 600;
}

.room-btn.book {
  background: rgba(74, 144, 226, 0.1);
  border-color: var(--primary-blue);
  color: var(--primary-blue);
}

.room-btn.book:hover {
  background: rgba(74, 144, 226, 0.2);
}

.room-btn.manage {
  background: rgba(255, 193, 7, 0.1);
  border-color: var(--warning-yellow);
  color: var(--warning-yellow);
}

.room-btn.manage:hover {
  background: rgba(255, 193, 7, 0.2);
}

/* Room Filter Section */
.room-filter-section {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: rgba(74, 144, 226, 0.05);
  border: 1px solid rgba(74, 144, 226, 0.2);
  border-radius: 8px;
}

.filter-header {
  margin-bottom: 1rem;
}

.filter-header h4 {
  margin: 0;
  color: var(--text-primary);
  font-size: 1rem;
  font-weight: 600;
}

.filter-controls {
  display: grid;
  grid-template-columns: 1fr 1fr auto;
  gap: 1rem;
  align-items: flex-end;
}

.filter-controls .form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-controls .form-label {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-secondary);
}

.filter-controls .form-input {
  padding: 0.75rem;
  border: 1px solid var(--border-light);
  border-radius: 6px;
  font-size: 0.95rem;
  background: var(--bg-white);
}

.filter-controls .glass-button {
  padding: 0.75rem 1.5rem;
  white-space: nowrap;
}

@media (max-width: 768px) {
  .filter-controls {
    grid-template-columns: 1fr;
  }
  
  .filter-controls .form-group:last-child {
    display: flex;
    align-items: center;
  }
}

/* Room Type Card Styles */
.rooms-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.room-type-card {
  border: 1px solid var(--border-light);
  border-radius: 8px;
  overflow: hidden;
  background: var(--bg-white);
  box-shadow: var(--shadow-sm);
  transition: all 0.2s ease;
}

.room-type-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.25rem;
  background: linear-gradient(135deg, rgba(74, 144, 226, 0.05) 0%, rgba(32, 201, 201, 0.05) 100%);
  cursor: pointer;
  user-select: none;
  transition: background 0.2s ease;
  border-bottom: 1px solid var(--border-light);
}

.room-type-header:hover {
  background: linear-gradient(135deg, rgba(74, 144, 226, 0.1) 0%, rgba(32, 201, 201, 0.1) 100%);
}

.room-type-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
}

.expand-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  color: var(--primary-blue);
  font-size: 0.75rem;
  transition: transform 0.2s ease;
  font-weight: bold;
}

.expand-icon.expanded {
  transform: rotate(-180deg);
}

.room-type-info h4 {
  margin: 0;
  color: var(--text-primary);
  font-size: 1.1rem;
  font-weight: 700;
}

.room-type-meta {
  font-size: 0.85rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.room-type-summary {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.room-count {
  background: rgba(74, 144, 226, 0.1);
  color: var(--primary-blue);
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-size: 0.85rem;
  font-weight: 600;
}

.room-type-details {
  padding: 1rem;
  background: rgba(0, 0, 0, 0.02);
}

.filter-info-banner {
  background: linear-gradient(135deg, #fff9c4 0%, #fff59d 100%);
  border-left: 4px solid #fbc02d;
  padding: 0.75rem 1rem;
  border-radius: 6px;
  margin-bottom: 1rem;
  color: #f57f17;
  font-size: 0.85rem;
  line-height: 1.5;
}

.individual-rooms-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.95rem;
}

.individual-rooms-table thead {
  background: rgba(74, 144, 226, 0.08);
}

.individual-rooms-table th {
  padding: 0.75rem;
  text-align: left;
  font-weight: 700;
  color: var(--text-primary);
  border-bottom: 2px solid var(--border-light);
}

.individual-rooms-table td {
  padding: 0.75rem;
  border-bottom: 1px solid var(--border-light);
  color: var(--text-secondary);
}

.individual-rooms-table tbody tr:hover {
  background: rgba(74, 144, 226, 0.04);
}

.availability-badge {
  display: inline-block;
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 600;
  white-space: nowrap;
}

.availability-badge.available {
  background: rgba(46, 204, 113, 0.2);
  color: var(--success-green);
}

.availability-badge.unavailable {
  background: rgba(231, 76, 60, 0.2);
  color: var(--danger-red);
}

.availability-badge.status-default {
  background: rgba(158, 158, 158, 0.1);
  color: var(--text-secondary);
}

.availability-badge.status-neutral {
  background: transparent;
  color: var(--text-muted);
  font-style: italic;
}

.status-icon {
  font-size: 1.2rem;
}

.status-icon.status-active {
  color: var(--success-green);
}

.status-icon.status-inactive {
  color: var(--danger-red);
}

.room-action-buttons {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  padding: 0.5rem 0.9rem;
  font-size: 0.8rem;
  border: 1px solid;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 600;
  white-space: nowrap;
}

.action-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.action-btn.book-btn {
  background: rgba(74, 144, 226, 0.1);
  border-color: var(--primary-blue);
  color: var(--primary-blue);
}

.action-btn.book-btn:hover:not(:disabled) {
  background: rgba(74, 144, 226, 0.2);
}

.action-btn.maintenance-btn {
  background: rgba(255, 193, 7, 0.1);
  border-color: var(--warning-yellow);
  color: var(--warning-yellow);
}

.action-btn.maintenance-btn:hover:not(:disabled) {
  background: rgba(255, 193, 7, 0.2);
}

.info-section {
  background-color: #f5f5f5;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-bottom: 1.5rem;
  border-left: 4px solid #007bff;
}

.info-section p {
  margin: 0.5rem 0;
  color: #333;
  font-size: 0.95rem;
}

.note-section {
  background-color: #f0f4ff;
  padding: 1rem;
  border-radius: 0.5rem;
  border-left: 4px solid #4a90e2;
  margin: 1rem 0;
}

.note-section p {
  margin: 0;
  font-size: 0.85rem;
  color: #555;
  line-height: 1.5;
}
</style>
