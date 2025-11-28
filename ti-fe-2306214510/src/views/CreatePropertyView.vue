<template>
  <div class="create-property fade-in">
    <h1>Create New Property</h1>

    <form @submit.prevent="submitForm" class="glass-card">
      <h3>Property Information</h3>

      <div class="grid grid-cols-2 gap-2">
        <div class="form-group">
          <label class="form-label">Property Name *</label>
          <input
            v-model="form.propertyName"
            type="text"
            class="glass-input"
            placeholder="Enter property name"
            required
          />
        </div>

        <div class="form-group">
          <label class="form-label">Type *</label>
          <select v-model="form.type" class="glass-input" required>
            <option :value="1">Hotel</option>
            <option :value="2">Villa</option>
            <option :value="3">Apartment</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Province *</label>
          <select v-model="form.province" class="glass-input" required>
            <option value="" disabled>Select Province</option>
            <option v-for="province in provinces" :key="province.code" :value="province.code">
              {{ province.name }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Address *</label>
          <input
            v-model="form.address"
            type="text"
            class="glass-input"
            placeholder="Enter address"
            required
          />
        </div>
      </div>

      <div class="form-group">
        <label class="form-label">Description *</label>
        <textarea
          v-model="form.description"
          class="glass-input"
          placeholder="Enter property description"
          rows="3"
          required
        ></textarea>
      </div>

      <div class="grid grid-cols-2 gap-2">
        <div class="form-group">
          <label class="form-label">Owner ID *</label>
          <input
            v-model="form.ownerID"
            type="text"
            class="glass-input"
            placeholder="UUID format"
            required
          />
        </div>

        <div class="form-group">
          <label class="form-label">Owner Name *</label>
          <input
            v-model="form.ownerName"
            type="text"
            class="glass-input"
            placeholder="Enter owner name"
            required
          />
        </div>
      </div>

      <hr style="border-color: rgba(255, 255, 255, 0.1); margin: 2rem 0;" />

      <h3>Room Types</h3>
      <div
        v-for="(roomType, index) in form.roomTypes"
        :key="index"
        class="room-type-section"
      >
        <div class="flex justify-between items-center" style="margin-bottom: 1rem;">
          <h4>Room Type {{ index + 1 }}</h4>
          <button
            type="button"
            class="glass-button danger"
            @click="removeRoomType(index)"
            v-if="form.roomTypes.length > 1"
          >
            Remove
          </button>
        </div>

        <div class="grid grid-cols-3 gap-2">
          <div class="form-group">
            <label class="form-label">Name *</label>
            <select
              v-model="roomType.name"
              class="glass-input"
              required
            >
              <option value="">-- Select Room Type --</option>
              <option
                v-for="roomTypeName in availableRoomTypes"
                :key="roomTypeName"
                :value="roomTypeName"
              >
                {{ roomTypeName }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">Floor *</label>
            <input
              v-model.number="roomType.floor"
              type="number"
              class="glass-input"
              placeholder="Floor number"
              min="1"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Price *</label>
            <input
              v-model.number="roomType.price"
              type="number"
              class="glass-input"
              placeholder="Price per night"
              min="0"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Capacity *</label>
            <input
              v-model.number="roomType.capacity"
              type="number"
              class="glass-input"
              placeholder="Max guests"
              min="1"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Unit (Rooms) *</label>
            <input
              v-model.number="roomType.unit"
              type="number"
              class="glass-input"
              placeholder="Number of rooms"
              min="1"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Facility *</label>
            <input
              v-model="roomType.facility"
              type="text"
              class="glass-input"
              placeholder="e.g., AC, TV, WiFi"
              required
            />
          </div>

          <div class="form-group" style="grid-column: span 3;">
            <label class="form-label">Description *</label>
            <input
              v-model="roomType.description"
              type="text"
              class="glass-input"
              placeholder="Room type description"
              required
            />
          </div>
        </div>
      </div>

      <button type="button" class="glass-button" @click="addRoomType" style="width: 100%; margin-top: 1rem;">
        Add Room Type
      </button>

      <div class="form-actions">
        <router-link to="/properties">
          <button type="button" class="glass-button">Cancel</button>
        </router-link>
        <button type="submit" class="glass-button primary" :disabled="submitting">
          {{ submitting ? 'Creating...' : 'Create Property' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { propertyService, type PropertyRequest, type RoomTypeRequest } from '@/services/propertyService'
import { provinceService, type Province } from '@/services/provinceService'
import { getRoomTypesByPropertyType } from '@/constants/roomTypes'

const router = useRouter()
const submitting = ref(false)
const provinces = ref<Province[]>([])

const form = ref<PropertyRequest>({
  propertyName: '',
  type: 1,
  province: '',
  address: '',
  description: '',
  ownerID: '',
  ownerName: '',
  roomTypes: [
    {
      name: '',
      price: 0,
      description: '',
      capacity: 1,
      facility: '',
      floor: 1,
      unit: 1
    }
  ]
})

const availableRoomTypes = computed(() => {
  return getRoomTypesByPropertyType(form.value.type)
})

const loadProvinces = async () => {
  try {
    const response = await provinceService.getAllProvinces()
    provinces.value = response
  } catch (error) {
    console.error('Error loading provinces:', error)
  }
}

onMounted(() => {
  loadProvinces()
})

const addRoomType = () => {
  form.value.roomTypes.push({
    name: '',
    price: 0,
    description: '',
    capacity: 1,
    facility: '',
    floor: 1,
    unit: 1
  })
}

const removeRoomType = (index: number) => {
  form.value.roomTypes.splice(index, 1)
}

const submitForm = async () => {
  try {
    submitting.value = true
    await propertyService.createProperty(form.value)
    alert('Property created successfully!')
    router.push('/properties')
  } catch (error: any) {
    console.error('Error creating property:', error)
    alert(error.response?.data?.message || 'Failed to create property')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.create-property {
  max-width: 1000px;
  margin: 0 auto;
}

.room-type-section {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
  padding: 1.5rem;
  border-radius: 10px;
  margin-bottom: 1rem;
  transition: all 0.2s ease;
}

.room-type-section:hover {
  border-color: rgba(255, 255, 255, 0.12);
  background: rgba(255, 255, 255, 0.05);
}

.room-type-section h4 {
  color: #ff6b35;
  margin-bottom: 1rem;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

textarea.glass-input {
  resize: vertical;
  min-height: 100px;
  font-family: inherit;
}
</style>
