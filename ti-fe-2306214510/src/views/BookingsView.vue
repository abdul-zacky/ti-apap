<template>
  <div class="bookings fade-in">
    <div class="flex justify-between items-center" style="margin-bottom: 2rem;">
      <h1>Bookings Management</h1>
      <router-link to="/bookings/create">
        <button class="glass-button primary">Create Booking</button>
      </router-link>
    </div>

    <!-- Filter Section -->
    <div class="glass-card" style="margin-bottom: 2rem;">
      <h3>Filters</h3>
      <div class="grid grid-cols-2 gap-2">
        <div class="form-group">
          <label class="form-label">Search</label>
          <input
            v-model="filters.searchTerm"
            type="text"
            class="glass-input"
            placeholder="Search by customer name..."
            @input="loadBookings"
          />
        </div>
        <div class="form-group">
          <label class="form-label">Status</label>
          <select v-model="filters.status" class="glass-input" @change="loadBookings">
            <option :value="undefined">All Status</option>
            <option :value="0">Waiting</option>
            <option :value="1">Confirmed</option>
            <option :value="2">Cancelled</option>
            <option :value="3">Refund</option>
            <option :value="4">Done</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Bookings Table -->
    <div v-if="loading" class="glass-card">
      <p>Loading bookings...</p>
    </div>

    <div v-else-if="bookings.length === 0" class="glass-card">
      <p>No bookings found.</p>
    </div>

    <div v-else class="glass-card">
      <table class="glass-table">
        <thead>
          <tr>
            <th>Booking ID</th>
            <th>Property Name</th>
            <th>Room Name</th>
            <th>Check-in</th>
            <th>Check-out</th>
            <th>Total Price</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="booking in bookings" :key="booking.bookingID">
            <td style="font-size: 0.9rem; font-weight: 500;">{{ booking.bookingID }}</td>
            <td>{{ booking.propertyName }}</td>
            <td>{{ booking.roomName }}</td>
            <td>{{ formatDate(booking.checkInDate) }}</td>
            <td>{{ formatDate(booking.checkOutDate) }}</td>
            <td>Rp {{ formatNumber(booking.totalPrice) }}</td>
            <td>
              <span :class="['badge', getStatusBadgeClass(booking.status)]">
                {{ getStatusText(booking.status) }}
              </span>
            </td>
            <td>
              <button
                class="text-button view"
                @click="viewDetails(booking.bookingID)"
                title="View booking details"
              >
                Detail
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- View Booking Modal -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="closeDetailModal">
      <div class="modal-content" style="max-height: 80vh; overflow-y: auto;">
        <div class="modal-header">
          <h2>Booking Details - {{ selectedBooking?.bookingID }}</h2>
          <button class="modal-close" @click="closeDetailModal">&times;</button>
        </div>
        <div class="modal-body" v-if="selectedBooking">
          <div class="detail-grid">
            <!-- Booking Info -->
            <div class="detail-item">
              <label>Booking ID</label>
              <p style="font-weight: 600;">{{ selectedBooking.bookingID }}</p>
            </div>
            
            <!-- Property & Room Info -->
            <div class="detail-item">
              <label>Nama Properti</label>
              <p>{{ selectedBooking.propertyName }}</p>
            </div>
            <div class="detail-item">
              <label>Nama Kamar</label>
              <p>{{ selectedBooking.roomName }}</p>
            </div>
            
            <!-- Customer Info -->
            <div class="detail-item">
              <label>ID Pelanggan</label>
              <p>{{ selectedBooking.customerID }}</p>
            </div>
            <div class="detail-item">
              <label>Nama Pelanggan</label>
              <p>{{ selectedBooking.customerName }}</p>
            </div>
            <div class="detail-item">
              <label>Email Pelanggan</label>
              <p>{{ selectedBooking.customerEmail }}</p>
            </div>
            <div class="detail-item">
              <label>Nomor Telepon Pelanggan</label>
              <p>{{ selectedBooking.customerPhone }}</p>
            </div>
            
            <!-- Booking Dates & Duration -->
            <div class="detail-item">
              <label>Tanggal Check-In</label>
              <p>{{ formatDate(selectedBooking.checkInDate) }}</p>
            </div>
            <div class="detail-item">
              <label>Tanggal Check-Out</label>
              <p>{{ formatDate(selectedBooking.checkOutDate) }}</p>
            </div>
            <div class="detail-item">
              <label>Total Hari</label>
              <p style="font-weight: 600;">{{ calculateTotalDays(selectedBooking.checkInDate, selectedBooking.checkOutDate) }} hari</p>
            </div>
            
            <!-- Breakfast & Pricing -->
            <div class="detail-item">
              <label>Sarapan</label>
              <p>{{ selectedBooking.isBreakfast ? 'Termasuk' : 'Tidak Termasuk' }}</p>
            </div>
            <div class="detail-item">
              <label>Total Harga</label>
              <p style="font-weight: 600; color: var(--primary-blue); font-size: 1.1rem;">Rp {{ formatNumber(selectedBooking.totalPrice) }}</p>
            </div>
            <div class="detail-item" v-if="selectedBooking.extraPay > 0">
              <label>Nominal Pembayaran Tambahan</label>
              <p style="font-weight: 600; color: #ff6b6b;">Rp {{ formatNumber(selectedBooking.extraPay) }}</p>
            </div>
            <div class="detail-item" v-if="selectedBooking.extraPay < 0">
              <label>Nominal Refund</label>
              <p style="font-weight: 600; color: #51cf66;">Rp {{ formatNumber(Math.abs(selectedBooking.extraPay)) }}</p>
            </div>
            
            <!-- Status -->
            <div class="detail-item">
              <label>Status Pemesanan</label>
              <p>
                <span :class="['badge', getStatusBadgeClass(selectedBooking.status)]">
                  {{ getStatusText(selectedBooking.status) }}
                </span>
              </p>
            </div>
            
            <!-- Timestamps -->
            <div class="detail-item">
              <label>Tanggal Dibuat</label>
              <p style="font-size: 0.9rem;">{{ formatFullDate(selectedBooking.createdDate) }}</p>
            </div>
            <div class="detail-item">
              <label>Tanggal Terakhir Diubah</label>
              <p style="font-size: 0.9rem;">{{ formatFullDate(selectedBooking.updatedDate) }}</p>
            </div>
          </div>
        </div>
                <div class="modal-footer" style="gap: 0.5rem; flex-wrap: wrap;">
          <!-- Status 0: Waiting for Payment -->
          <!-- Pay Button - for status 0 -->
          <button 
            v-if="selectedBooking?.status === 0"
            class="action-button confirm"
            @click="handlePay"
            title="Confirm payment"
          >
            Pay
          </button>
          
          <!-- Update Button - for status 0 or 1 with no extra pay/refund -->
          <button 
            v-if="((selectedBooking?.status === 0 || selectedBooking?.status === 1) && selectedBooking?.extraPay === 0)"
            class="action-button primary"
            @click="handleUpdate"
            title="Update booking details"
          >
            Update
          </button>
          
          <!-- Refund Button - for status 3 (Request Refund) with negative extraPay -->
          <button 
            v-if="selectedBooking?.status === 3 && selectedBooking?.extraPay < 0"
            class="action-button info"
            @click="handleRefund"
            title="Process refund"
          >
            Process Refund
          </button>
          
          <!-- Pay Extra Button - for status 0 (Waiting) with positive extraPay (extra payment needed) -->
          <button 
            v-if="selectedBooking?.status === 0 && selectedBooking?.extraPay > 0"
            class="action-button warning"
            @click="handlePayExtra"
            title="Pay extra amount"
          >
            Pay Extra
          </button>
          
          <!-- Cancel Button - for status 0, 1, or 3 -->
          <button 
            v-if="selectedBooking?.status === 0 || selectedBooking?.status === 1 || selectedBooking?.status === 3"
            class="text-button cancel"
            @click="handleCancel"
            title="Cancel booking"
          >
            Cancel
          </button>
          
          <!-- Back Button - always available -->
          <button 
            class="glass-button"
            @click="closeDetailModal"
            title="Back to bookings list"
          >
            Back
          </button>
        </div>
      </div>
    </div>

    <!-- Confirmation Modal for Status Changes -->
    <div v-if="showConfirmModal" class="modal-overlay" @click.self="closeConfirmModal">
      <div class="modal-content" style="max-width: 400px;">
        <div class="modal-header">
          <h2>{{ confirmDetails.title }}</h2>
          <button class="modal-close" @click="closeConfirmModal">&times;</button>
        </div>
        <div class="modal-body">
          <p>{{ confirmMessage }}</p>
          <div v-if="confirmDetails.amount" style="margin-top: 1rem; padding: 1rem; background: rgba(255,107,107,0.1); border-radius: 8px;">
            <p style="margin: 0; font-size: 0.9rem; color: #666;">Amount:</p>
            <p style="margin: 0.5rem 0 0 0; font-size: 1.2rem; font-weight: 600; color: #ff6b6b;">
              Rp {{ formatNumber(confirmDetails.amount) }}
            </p>
          </div>
          <div v-if="confirmDetails.reason" style="margin-top: 1rem; padding: 1rem; background: rgba(0,0,0,0.05); border-radius: 8px;">
            <p style="margin: 0; font-size: 0.85rem; color: #666;">{{ confirmDetails.reason }}</p>
          </div>
        </div>
        <div class="modal-footer">
          <button 
            v-if="confirmAction === 'pay'"
            class="action-button confirm"
            @click="confirmPaymentAction"
          >
            Confirm Payment
          </button>
          <button 
            v-if="confirmAction === 'cancel'"
            class="action-button danger"
            @click="confirmCancelAction"
          >
            Confirm Cancel
          </button>
          <button 
            v-if="confirmAction === 'refund'"
            class="action-button info"
            @click="confirmRefundAction"
          >
            Process Refund
          </button>
          <button 
            class="glass-button"
            @click="closeConfirmModal"
          >
            No, Cancel
          </button>
        </div>
      </div>
    </div>

    <!-- Create Booking Modal -->
    <div v-if="showCreateBookingModal" class="modal-overlay" @click.self="closeCreateBookingModal">
      <div class="modal-content" style="max-height: 90vh; overflow-y: auto;">
        <div class="modal-header">
          <h2>Create New Booking</h2>
          <button class="modal-close" @click="closeCreateBookingModal">&times;</button>
        </div>
        <div class="modal-body">
          <!-- Validation Errors Display -->
          <div v-if="validationErrors.length > 0" style="margin-bottom: 1rem; padding: 1rem; background: rgba(231, 76, 60, 0.1); border: 1px solid var(--danger-red); border-radius: 0.5rem;">
            <p style="color: var(--danger-red); font-weight: 600; margin: 0 0 0.5rem 0;">❌ Validation Errors:</p>
            <ul style="margin: 0; padding-left: 1.5rem; color: var(--danger-red);">
              <li v-for="(error, idx) in validationErrors" :key="idx">{{ error }}</li>
            </ul>
          </div>

          <div class="form-grid" style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
            
            <!-- SECTION 1: Property & Room Selection -->
            <div style="grid-column: 1 / -1;">
              <h4 style="margin: 1rem 0 0.75rem 0; color: var(--primary-blue); text-transform: uppercase; font-size: 0.85rem; font-weight: 700;">📍 Property & Room</h4>
            </div>

            <!-- Property Dropdown -->
            <div class="form-group">
              <label class="form-label">Nama Properti *</label>
              <select 
                v-model="newBooking.propertyID" 
                class="glass-input"
                @change="onPropertyChange"
                required
              >
                <option value="">-- Pilih Properti --</option>
                <option v-for="prop in availableProperties" :key="prop.propertyID" :value="prop.propertyID">
                  {{ prop.propertyName }}
                </option>
              </select>
              <small style="color: #666; margin-top: 0.25rem; display: block;">Hanya properti aktif</small>
            </div>

            <!-- Room Type Dropdown -->
            <div class="form-group">
              <label class="form-label">Tipe Kamar *</label>
              <select 
                v-model="newBooking.roomTypeID" 
                class="glass-input"
                @change="onRoomTypeChange"
                required
                :disabled="!newBooking.propertyID"
              >
                <option value="">-- Pilih Tipe Kamar --</option>
                <option v-for="type in availableRoomTypes" :key="type.roomTypeID" :value="type.roomTypeID">
                  {{ type.name }} (Kapasitas: {{ type.capacity }}, Rp {{ formatNumber(type.price) }}/malam)
                </option>
              </select>
            </div>

            <!-- Room Dropdown -->
            <div class="form-group">
              <label class="form-label">Nama Kamar *</label>
              <select 
                v-model="newBooking.roomID" 
                class="glass-input"
                @change="onRoomChange"
                required
                :disabled="!newBooking.roomTypeID"
              >
                <option value="">-- Pilih Kamar --</option>
                <option v-for="room in availableRooms" :key="room.roomID" :value="room.roomID">
                  {{ room.name }}
                </option>
              </select>
            </div>

            <!-- SECTION 2: Dates -->
            <div style="grid-column: 1 / -1; margin-top: 0.5rem;">
              <h4 style="margin: 1rem 0 0.75rem 0; color: var(--primary-blue); text-transform: uppercase; font-size: 0.85rem; font-weight: 700;">📅 Tanggal Check-in & Check-out</h4>
            </div>

            <!-- Check-in Date -->
            <div class="form-group">
              <label class="form-label">Tanggal Check-in (14:00) *</label>
              <input 
                v-model="newBooking.checkInDate" 
                type="date" 
                class="glass-input"
                :min="today"
                @change="validateCheckOutDate"
                required
              />
              <small style="color: #666; margin-top: 0.25rem; display: block;">Minimal hari ini</small>
            </div>

            <!-- Check-out Date -->
            <div class="form-group">
              <label class="form-label">Tanggal Check-out (12:00) *</label>
              <input 
                v-model="newBooking.checkOutDate" 
                type="date" 
                class="glass-input"
                :min="checkOutMinDate"
                required
              />
              <small style="color: #666; margin-top: 0.25rem; display: block;">Minimal 1 hari dari check-in</small>
            </div>

            <div style="grid-column: 1 / -1; font-size: 0.85rem; background: rgba(74, 144, 226, 0.05); padding: 0.75rem; border-radius: 0.4rem; margin-top: 0.25rem;">
              ℹ️ Check-in pukul 14:00 (2 siang) • Check-out pukul 12:00 (tengah hari)
            </div>

            <!-- SECTION 3: Customer Info -->
            <div style="grid-column: 1 / -1; margin-top: 0.5rem;">
              <h4 style="margin: 1rem 0 0.75rem 0; color: var(--primary-blue); text-transform: uppercase; font-size: 0.85rem; font-weight: 700;">👤 Data Pelanggan</h4>
            </div>

            <!-- Customer ID -->
            <div class="form-group">
              <label class="form-label">ID Pelanggan (UUID) *</label>
              <input 
                v-model="newBooking.customerID" 
                type="text" 
                class="glass-input"
                placeholder="550e8400-e29b-41d4-a716-446655440000"
                required
              />
            </div>

            <!-- Customer Name -->
            <div class="form-group">
              <label class="form-label">Nama Pelanggan *</label>
              <input 
                v-model="newBooking.customerName" 
                type="text" 
                class="glass-input"
                placeholder="e.g., John Doe"
                required
              />
            </div>

            <!-- Customer Email -->
            <div class="form-group">
              <label class="form-label">Email Pelanggan *</label>
              <input 
                v-model="newBooking.customerEmail" 
                type="email" 
                class="glass-input"
                placeholder="customer@example.com"
                required
              />
            </div>

            <!-- Customer Phone -->
            <div class="form-group">
              <label class="form-label">Nomor Telepon Pelanggan *</label>
              <input 
                v-model="newBooking.customerPhone" 
                type="tel" 
                class="glass-input"
                placeholder="+62 812 3456 7890"
                required
              />
            </div>

            <!-- SECTION 4: Booking Details -->
            <div style="grid-column: 1 / -1; margin-top: 0.5rem;">
              <h4 style="margin: 1rem 0 0.75rem 0; color: var(--primary-blue); text-transform: uppercase; font-size: 0.85rem; font-weight: 700;">🏨 Detail Pemesanan</h4>
            </div>

            <!-- Capacity -->
            <div class="form-group">
              <label class="form-label">Kapasitas (Jumlah Tamu) *</label>
              <input 
                v-model.number="newBooking.capacity" 
                type="number" 
                class="glass-input"
                min="1"
                :max="selectedRoomTypeCapacity"
                required
              />
              <small v-if="selectedRoomTypeCapacity" style="color: #666; margin-top: 0.25rem; display: block;">
                Kapasitas maksimal: {{ selectedRoomTypeCapacity }} tamu
              </small>
            </div>

            <!-- Breakfast Add-on -->
            <div class="form-group">
              <label class="form-label">Add-on Breakfast (+Rp 50.000/malam)</label>
              <select v-model="newBooking.isBreakfast" class="glass-input">
                <option :value="false">Tidak</option>
                <option :value="true">Ya</option>
              </select>
            </div>
          </div>

          <!-- Total Price Display -->
          <div style="margin-top: 1.5rem; padding: 1.25rem; background: linear-gradient(135deg, rgba(74, 144, 226, 0.1) 0%, rgba(23, 195, 195, 0.05) 100%); border: 1px solid rgba(74, 144, 226, 0.2); border-radius: 0.75rem;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.75rem;">
              <span style="font-weight: 600; font-size: 1rem;">Total Harga:</span>
              <span style="font-weight: 700; color: var(--primary-blue); font-size: 1.4rem;">
                Rp {{ formatNumber(calculateTotalPrice()) }}
              </span>
            </div>
            <div style="color: #666; font-size: 0.9rem; display: grid; gap: 0.25rem;">
              <div>{{ getTotalDays() }} malam × Rp {{ formatNumber(selectedRoomPrice) }}</div>
              <div v-if="newBooking.isBreakfast">+ Rp 50.000 × {{ getTotalDays() }} malam (Breakfast)</div>
            </div>
          </div>
        </div>

        <div class="modal-footer" style="gap: 1rem;">
          <button class="glass-button" @click="closeCreateBookingModal">Batal</button>
          <button class="glass-button primary" @click="handleCreateBooking" :disabled="validationErrors.length > 0">Buat Pemesanan</button>
        </div>
      </div>
    </div>

    <!-- Edit Booking Modal -->
    <div v-if="showEditBookingModal" class="modal-overlay" @click.self="closeEditBookingModal">
      <div class="modal-content" style="max-height: 90vh; overflow-y: auto;">
        <div class="modal-header">
          <h2>Update Booking Details - {{ editingBooking.bookingID }}</h2>
          <button class="modal-close" @click="closeEditBookingModal">&times;</button>
        </div>
        <div class="modal-body">
          <!-- Validation Errors Display -->
          <div v-if="validationErrors.length > 0" style="margin-bottom: 1rem; padding: 1rem; background: rgba(231, 76, 60, 0.1); border: 1px solid var(--danger-red); border-radius: 0.5rem;">
            <p style="color: var(--danger-red); font-weight: 600; margin: 0 0 0.5rem 0;">❌ Validation Errors:</p>
            <ul style="margin: 0; padding-left: 1.5rem; color: var(--danger-red);">
              <li v-for="(error, idx) in validationErrors" :key="idx">{{ error }}</li>
            </ul>
          </div>

          <div class="form-grid" style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
            
            <!-- SECTION 1: Property & Room Selection -->
            <div style="grid-column: 1 / -1;">
              <h4 style="margin: 1rem 0 0.75rem 0; color: var(--primary-blue); text-transform: uppercase; font-size: 0.85rem; font-weight: 700;">📍 Property & Room</h4>
            </div>

            <!-- Property Dropdown -->
            <div class="form-group">
              <label class="form-label">Nama Properti *</label>
              <select 
                v-model="editingBooking.propertyID" 
                class="glass-input"
                @change="onEditPropertyChange"
                required
              >
                <option value="">-- Pilih Properti --</option>
                <option v-for="prop in availableProperties" :key="prop.propertyID" :value="prop.propertyID">
                  {{ prop.propertyName }}
                </option>
              </select>
              <small style="color: #666; margin-top: 0.25rem; display: block;">Hanya properti aktif</small>
            </div>

            <!-- Room Type Dropdown -->
            <div class="form-group">
              <label class="form-label">Tipe Kamar *</label>
              <select 
                v-model="editingBooking.roomTypeID" 
                class="glass-input"
                @change="onEditRoomTypeChange"
                required
                :disabled="!editingBooking.propertyID"
              >
                <option value="">-- Pilih Tipe Kamar --</option>
                <option v-for="type in editAvailableRoomTypes" :key="type.roomTypeID" :value="type.roomTypeID">
                  {{ type.name }} (Kapasitas: {{ type.capacity }}, Rp {{ formatNumber(type.price) }}/malam)
                </option>
              </select>
            </div>

            <!-- Room Dropdown -->
            <div class="form-group">
              <label class="form-label">Nama Kamar *</label>
              <select 
                v-model="editingBooking.roomID" 
                class="glass-input"
                @change="onEditRoomChange"
                required
                :disabled="!editingBooking.roomTypeID"
              >
                <option value="">-- Pilih Kamar --</option>
                <option v-for="room in editAvailableRooms" :key="room.roomID" :value="room.roomID">
                  {{ room.name }}
                </option>
              </select>
            </div>

            <!-- SECTION 2: Dates -->
            <div style="grid-column: 1 / -1; margin-top: 0.5rem;">
              <h4 style="margin: 1rem 0 0.75rem 0; color: var(--primary-blue); text-transform: uppercase; font-size: 0.85rem; font-weight: 700;">📅 Tanggal Check-in & Check-out</h4>
            </div>

            <!-- Check-in Date -->
            <div class="form-group">
              <label class="form-label">Tanggal Check-in (14:00) *</label>
              <input 
                v-model="editingBooking.checkInDate" 
                type="date" 
                class="glass-input"
                :min="today"
                @change="validateEditCheckOutDate"
                required
              />
              <small style="color: #666; margin-top: 0.25rem; display: block;">Minimal hari ini</small>
            </div>

            <!-- Check-out Date -->
            <div class="form-group">
              <label class="form-label">Tanggal Check-out (12:00) *</label>
              <input 
                v-model="editingBooking.checkOutDate" 
                type="date" 
                class="glass-input"
                :min="editCheckOutMinDate"
                required
              />
              <small style="color: #666; margin-top: 0.25rem; display: block;">Minimal 1 hari dari check-in</small>
            </div>

            <!-- SECTION 3: Customer Info -->
            <div style="grid-column: 1 / -1; margin-top: 0.5rem;">
              <h4 style="margin: 1rem 0 0.75rem 0; color: var(--primary-blue); text-transform: uppercase; font-size: 0.85rem; font-weight: 700;">👤 Data Pelanggan</h4>
            </div>

            <!-- Customer ID (Read-only) -->
            <div class="form-group">
              <label class="form-label">ID Pelanggan (UUID) *</label>
              <input 
                v-model="editingBooking.customerID" 
                type="text" 
                class="glass-input"
                readonly
                style="background: #f5f5f5; cursor: not-allowed;"
              />
              <small style="color: #666; margin-top: 0.25rem; display: block;">Auto-generated (tidak dapat diubah)</small>
            </div>

            <!-- Customer Name -->
            <div class="form-group">
              <label class="form-label">Nama Pelanggan *</label>
              <input 
                v-model="editingBooking.customerName" 
                type="text" 
                class="glass-input"
                placeholder="e.g., John Doe"
                required
              />
            </div>

            <!-- Customer Email -->
            <div class="form-group">
              <label class="form-label">Email Pelanggan *</label>
              <input 
                v-model="editingBooking.customerEmail" 
                type="email" 
                class="glass-input"
                placeholder="e.g., john@example.com"
                required
              />
            </div>

            <!-- Customer Phone -->
            <div class="form-group">
              <label class="form-label">Nomor Telepon Pelanggan *</label>
              <input 
                v-model="editingBooking.customerPhone" 
                type="tel" 
                class="glass-input"
                placeholder="e.g., +62812345678"
                required
              />
            </div>

            <!-- SECTION 4: Booking Details -->
            <div style="grid-column: 1 / -1; margin-top: 0.5rem;">
              <h4 style="margin: 1rem 0 0.75rem 0; color: var(--primary-blue); text-transform: uppercase; font-size: 0.85rem; font-weight: 700;">🏨 Detail Pemesanan</h4>
            </div>

            <!-- Capacity -->
            <div class="form-group">
              <label class="form-label">Kapasitas (Jumlah Tamu) *</label>
              <input 
                v-model.number="editingBooking.capacity" 
                type="number" 
                class="glass-input"
                min="1"
                :max="editSelectedRoomTypeCapacity"
                required
              />
              <small v-if="editSelectedRoomTypeCapacity" style="color: #666; margin-top: 0.25rem; display: block;">
                Kapasitas maksimal: {{ editSelectedRoomTypeCapacity }} tamu
              </small>
            </div>

            <!-- Breakfast Add-on -->
            <div class="form-group">
              <label class="form-label">Add-on Breakfast (+Rp 50.000/malam)</label>
              <select v-model="editingBooking.isBreakfast" class="glass-input">
                <option :value="false">Tidak</option>
                <option :value="true">Ya</option>
              </select>
            </div>
          </div>

          <!-- Total Price Display -->
          <div style="margin-top: 1.5rem; padding: 1.25rem; background: linear-gradient(135deg, rgba(74, 144, 226, 0.1) 0%, rgba(23, 195, 195, 0.05) 100%); border: 1px solid rgba(74, 144, 226, 0.2); border-radius: 0.75rem;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.75rem;">
              <span style="font-weight: 600; font-size: 1rem;">Total Harga:</span>
              <span style="font-weight: 700; color: var(--primary-blue); font-size: 1.4rem;">
                Rp {{ formatNumber(calculateEditTotalPrice()) }}
              </span>
            </div>
            <div style="color: #666; font-size: 0.9rem; display: grid; gap: 0.25rem;">
              <div>{{ getEditTotalDays() }} malam × Rp {{ formatNumber(editSelectedRoomPrice) }}</div>
              <div v-if="editingBooking.isBreakfast">+ Rp 50.000 × {{ getEditTotalDays() }} malam (Breakfast)</div>
            </div>
          </div>
        </div>

        <div class="modal-footer" style="gap: 1rem;">
          <button class="glass-button" @click="closeEditBookingModal">Batal</button>
          <button class="glass-button primary" @click="handleUpdateBooking" :disabled="validationErrors.length > 0">Update Pemesanan</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { bookingService, type Booking } from '@/services/bookingService'
import { propertyService } from '@/services/propertyService'

const bookings = ref<Booking[]>([])
const loading = ref(true)
const showDetailModal = ref(false)
const selectedBooking = ref<Booking | null>(null)
const showCreateBookingModal = ref(false)
const filters = ref({
  searchTerm: '',
  status: undefined as number | undefined
})

// New Booking Form Data
const newBooking = ref({
  propertyID: '',
  roomTypeID: '',
  roomID: '',
  checkInDate: '',
  checkOutDate: '',
  customerID: '',
  customerName: '',
  customerEmail: '',
  customerPhone: '',
  capacity: 1,
  isBreakfast: false
})

// Dropdowns Data
const availableProperties = ref<any[]>([])
const availableRoomTypes = ref<any[]>([])
const availableRooms = ref<any[]>([])

// Helper computed values
const today = ref('')
const checkOutMinDate = ref('')
const selectedRoomTypeCapacity = ref(0)
const selectedRoomPrice = ref(0)
const validationErrors = ref<string[]>([])

// Edit Booking Form Data
const showEditBookingModal = ref(false)
const editingBooking = ref({
  bookingID: '',
  propertyID: '',
  roomTypeID: '',
  roomID: '',
  checkInDate: '',
  checkOutDate: '',
  customerID: '',
  customerName: '',
  customerEmail: '',
  customerPhone: '',
  capacity: 1,
  isBreakfast: false
})

// Edit Booking Dropdowns Data
const editAvailableRoomTypes = ref<any[]>([])
const editAvailableRooms = ref<any[]>([])
const editCheckOutMinDate = ref('')
const editSelectedRoomTypeCapacity = ref(0)
const editSelectedRoomPrice = ref(0)

// Confirmation Modal State
const showConfirmModal = ref(false)
const confirmAction = ref<'pay' | 'cancel' | 'refund' | null>(null)
const confirmMessage = ref('')
const confirmDetails = ref<{ title: string; amount?: number; reason?: string }>({
  title: '',
})

const getStatusText = (status: number) => {
  const statuses: Record<number, string> = {
    0: 'Waiting',
    1: 'Confirmed',
    2: 'Cancelled',
    3: 'Refund',
    4: 'Done'
  }
  return statuses[status] || 'Unknown'
}

const getStatusBadgeClass = (status: number) => {
  const classes: Record<number, string> = {
    0: 'badge-warning',
    1: 'badge-success',
    2: 'badge-danger',
    3: 'badge-info',
    4: 'badge-primary'
  }
  return classes[status] || ''
}

const formatNumber = (num: number) => {
  return new Intl.NumberFormat('id-ID').format(num)
}

const formatDate = (dateStr: string) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('id-ID', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const formatFullDate = (dateStr: string) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('id-ID', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const calculateTotalDays = (checkInStr: string, checkOutStr: string) => {
  const checkIn = new Date(checkInStr)
  const checkOut = new Date(checkOutStr)
  const diffTime = Math.abs(checkOut.getTime() - checkIn.getTime())
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return diffDays
}

const getTotalDays = () => {
  if (!newBooking.value.checkInDate || !newBooking.value.checkOutDate) return 0
  return calculateTotalDays(newBooking.value.checkInDate, newBooking.value.checkOutDate)
}

const loadBookings = async () => {
  console.log('📚 [LOAD BOOKINGS] Loading bookings from API...')
  try {
    loading.value = true
    const response = await bookingService.getAllBookings(
      filters.value.searchTerm || undefined,
      filters.value.status
    )
    console.log('📚 [LOAD BOOKINGS] Bookings loaded:', response.length, 'bookings')
    response.forEach((b: any) => {
      console.log(`  - ${b.bookingID}: Status=${b.status} Room=${b.room.roomID} CheckIn=${b.checkInDate}`)
    })
    bookings.value = response
  } catch (error) {
    console.error('❌ [LOAD BOOKINGS] Error loading bookings:', error)
    alert('Failed to load bookings')
  } finally {
    loading.value = false
  }
}

const loadProperties = async () => {
  try {
    console.log('🏢 [LOAD PROPERTIES] Loading active properties...')
    const response = await propertyService.getAllProperties(undefined, undefined, 1) // status 1 = active
    console.log('🏢 [LOAD PROPERTIES] Full response:', response)
    
    // Response is already the data array, not wrapped in {data: ...}
    const propsData = Array.isArray(response) ? response : (response?.data || [])
    availableProperties.value = propsData
    console.log('🏢 [LOAD PROPERTIES] Loaded', availableProperties.value.length, 'properties')
    availableProperties.value.forEach(p => console.log(`  - ${p.propertyID}: ${p.propertyName}`))
  } catch (error) {
    console.error('❌ [LOAD PROPERTIES] Error:', error)
    alert('Failed to load properties')
  }
}

const onPropertyChange = async () => {
  console.log('🏘️ [ON PROPERTY CHANGE] Property selected:', newBooking.value.propertyID)
  
  // Reset room types and rooms
  availableRoomTypes.value = []
  availableRooms.value = []
  newBooking.value.roomTypeID = ''
  newBooking.value.roomID = ''
  selectedRoomTypeCapacity.value = 0
  selectedRoomPrice.value = 0
  
  if (!newBooking.value.propertyID) return
  
  try {
    // Get room types for this property
    console.log('🏘️ [ON PROPERTY CHANGE] Loading room types from /rooms endpoint...')
    const roomTypesResponse = await propertyService.getRoomsByPropertyId(newBooking.value.propertyID)
    console.log('🏘️ [ON PROPERTY CHANGE] Room types response:', roomTypesResponse)
    
    const roomTypesData = Array.isArray(roomTypesResponse) ? roomTypesResponse : (roomTypesResponse?.data || [])
    availableRoomTypes.value = roomTypesData
    console.log('🏘️ [ON PROPERTY CHANGE] Room types loaded:', availableRoomTypes.value.length)
    availableRoomTypes.value.forEach(rt => console.log(`  - ${rt.roomTypeID}: ${rt.name} (${rt.capacity} capacity)`))
    
    // Also get individual rooms for later use
    console.log('🏘️ [ON PROPERTY CHANGE] Loading individual rooms from /all-rooms endpoint...')
    const allRoomsResponse = await propertyService.getAllRooms(newBooking.value.propertyID)
    console.log('🏘️ [ON PROPERTY CHANGE] All rooms response:', allRoomsResponse)
    const allRoomsData = Array.isArray(allRoomsResponse) ? allRoomsResponse : (allRoomsResponse?.data || [])
    console.log('🏘️ [ON PROPERTY CHANGE] Individual rooms loaded:', allRoomsData?.length || 0)
    
  } catch (error) {
    console.error('❌ [ON PROPERTY CHANGE] Error:', error)
    alert('Failed to load room types')
  }
}

const onRoomTypeChange = async () => {
  console.log('🏠 [ON ROOM TYPE CHANGE] Room type selected:', newBooking.value.roomTypeID)
  
  // Reset rooms
  availableRooms.value = []
  newBooking.value.roomID = ''
  selectedRoomTypeCapacity.value = 0
  selectedRoomPrice.value = 0
  newBooking.value.capacity = 1
  
  if (!newBooking.value.roomTypeID || !newBooking.value.propertyID) return
  
  try {
    // Get all individual rooms for this property
    console.log('🏠 [ON ROOM TYPE CHANGE] Loading individual rooms...')
    const allRoomsResponse = await propertyService.getAllRooms(newBooking.value.propertyID)
    const allRoomsData = Array.isArray(allRoomsResponse) ? allRoomsResponse : (allRoomsResponse?.data || [])
    console.log('🏠 [ON ROOM TYPE CHANGE] All rooms response:', allRoomsData)
    
    // Filter rooms by selected room type
    const filteredRooms = allRoomsData?.filter((room: any) => 
      room.roomTypeID === newBooking.value.roomTypeID
    ) || []
    
    availableRooms.value = filteredRooms
    console.log('🏠 [ON ROOM TYPE CHANGE] Filtered rooms:', availableRooms.value.length)
    availableRooms.value.forEach(r => console.log(`  - ${r.roomID}: ${r.name}`))
    
    // Get room type details for capacity and price
    const roomType = availableRoomTypes.value.find(rt => rt.roomTypeID === newBooking.value.roomTypeID)
    if (roomType) {
      selectedRoomTypeCapacity.value = roomType.capacity
      selectedRoomPrice.value = roomType.price
      console.log('🏠 [ON ROOM TYPE CHANGE] Room type details - Capacity:', selectedRoomTypeCapacity.value, 'Price:', selectedRoomPrice.value)
    }
  } catch (error) {
    console.error('❌ [ON ROOM TYPE CHANGE] Error:', error)
    alert('Failed to load rooms')
  }
}

const onRoomChange = () => {
  console.log('🚪 [ON ROOM CHANGE] Room selected:', newBooking.value.roomID)
  validateCheckOutDate()
}

const validateCheckOutDate = () => {
  if (newBooking.value.checkInDate) {
    const checkInDate = new Date(newBooking.value.checkInDate)
    checkInDate.setDate(checkInDate.getDate() + 1) // Minimum 1 hari
    checkOutMinDate.value = checkInDate.toISOString().split('T')[0]
  }
}

const validateForm = (): boolean => {
  validationErrors.value = []
  const errors: string[] = []

  // Required fields validation
  if (!newBooking.value.propertyID) errors.push('Pilih properti')
  if (!newBooking.value.roomTypeID) errors.push('Pilih tipe kamar')
  if (!newBooking.value.roomID) errors.push('Pilih kamar')
  if (!newBooking.value.checkInDate) errors.push('Masukkan tanggal check-in')
  if (!newBooking.value.checkOutDate) errors.push('Masukkan tanggal check-out')
  if (!newBooking.value.customerID) errors.push('Masukkan ID pelanggan')
  if (!newBooking.value.customerName) errors.push('Masukkan nama pelanggan')
  if (!newBooking.value.customerEmail) errors.push('Masukkan email pelanggan')
  if (!newBooking.value.customerPhone) errors.push('Masukkan nomor telepon pelanggan')
  if (newBooking.value.capacity <= 0) errors.push('Kapasitas minimal 1 tamu')

  // Email validation
  if (newBooking.value.customerEmail && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(newBooking.value.customerEmail)) {
    errors.push('Format email tidak valid')
  }

  // Date validation
  if (newBooking.value.checkInDate && newBooking.value.checkOutDate) {
    const checkInDate = new Date(newBooking.value.checkInDate)
    const checkOutDate = new Date(newBooking.value.checkOutDate)

    // Check if today
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    if (checkInDate < today) {
      errors.push('Tanggal check-in tidak boleh sebelum hari ini')
    }

    // Check checkout >= checkin
    if (checkOutDate <= checkInDate) {
      errors.push('Tanggal check-out harus lebih akhir dari check-in (minimal 1 hari)')
    }
  }

  // Capacity validation
  if (newBooking.value.capacity > selectedRoomTypeCapacity.value) {
    errors.push(`Kapasitas tidak boleh melebihi ${selectedRoomTypeCapacity.value} tamu`)
  }

  validationErrors.value = errors
  return errors.length === 0
}

const calculateTotalPrice = () => {
  if (!newBooking.value.checkInDate || !newBooking.value.checkOutDate || !selectedRoomPrice.value) return 0
  
  const totalDays = getTotalDays()
  let totalPrice = totalDays * selectedRoomPrice.value
  
  if (newBooking.value.isBreakfast) {
    totalPrice += 50000 * totalDays
  }
  
  return totalPrice
}

const handleCreateBooking = async () => {
  console.log('📝 [CREATE BOOKING] handleCreateBooking called')
  
  // Validate form
  if (!validateForm()) {
    console.error('❌ [CREATE BOOKING] Validation failed')
    return
  }

  try {
    // Convert date format to ISO datetime
    const checkInDateTime = newBooking.value.checkInDate + 'T14:00:00'
    const checkOutDateTime = newBooking.value.checkOutDate + 'T12:00:00'
    
    console.log('🌐 [CREATE BOOKING] Sending to API:', {
      roomID: newBooking.value.roomID,
      checkInDate: checkInDateTime,
      checkOutDate: checkOutDateTime,
      customerName: newBooking.value.customerName,
      capacity: newBooking.value.capacity
    })
    
    await bookingService.createBooking({
      roomID: newBooking.value.roomID,
      checkInDate: checkInDateTime,
      checkOutDate: checkOutDateTime,
      customerID: newBooking.value.customerID,
      customerName: newBooking.value.customerName,
      customerEmail: newBooking.value.customerEmail,
      customerPhone: newBooking.value.customerPhone,
      isBreakfast: newBooking.value.isBreakfast,
      capacity: newBooking.value.capacity
    })
    
    console.log('✅ [CREATE BOOKING] Booking created successfully!')
    
    // Set flag for Properties page to know booking was created
    localStorage.setItem('bookingJustCreated', 'true')
    
    alert('✅ Pemesanan berhasil dibuat!')
    closeCreateBookingModal()
    loadBookings()
  } catch (error: any) {
    console.error('❌ [CREATE BOOKING] Error creating booking:', error)
    alert('❌ ' + (error.response?.data?.message || 'Gagal membuat pemesanan'))
  }
}

const confirmPayment = async (id: string) => {
  if (!confirm('Confirm payment for this booking?')) return

  try {
    await bookingService.confirmPayment(id)
    alert('Payment confirmed successfully')
    loadBookings()
  } catch (error: any) {
    console.error('Error confirming payment:', error)
    alert(error.response?.data?.message || 'Failed to confirm payment')
  }
}

const cancelBooking = async (id: string) => {
  if (!confirm('Are you sure you want to cancel this booking?')) return

  try {
    await bookingService.cancelBooking(id)
    alert('Booking cancelled successfully')
    loadBookings()
  } catch (error: any) {
    console.error('Error cancelling booking:', error)
    alert(error.response?.data?.message || 'Failed to cancel booking')
  }
}

const completeBooking = async (id: string) => {
  if (!confirm('Mark this booking as completed?')) return

  try {
    await bookingService.completeBooking(id)
    alert('Booking completed successfully')
    loadBookings()
  } catch (error: any) {
    console.error('Error completing booking:', error)
    alert(error.response?.data?.message || 'Failed to complete booking')
  }
}

const viewDetails = (id: string) => {
  const booking = bookings.value.find(b => b.bookingID === id)
  if (booking) {
    selectedBooking.value = booking
    showDetailModal.value = true
  }
}

const closeDetailModal = () => {
  showDetailModal.value = false
  selectedBooking.value = null
}

const closeConfirmModal = () => {
  showConfirmModal.value = false
  confirmAction.value = null
  confirmMessage.value = ''
  confirmDetails.value = { title: '' }
}

const handlePay = async () => {
  if (!selectedBooking.value) return
  
  confirmAction.value = 'pay'
  confirmDetails.value = {
    title: 'Confirm Payment?',
    amount: selectedBooking.value.totalPrice + selectedBooking.value.extraPay,
    reason: selectedBooking.value.extraPay > 0 
      ? 'Including extra pay: Rp ' + formatNumber(selectedBooking.value.extraPay)
      : 'Confirm to process payment for this booking'
  }
  confirmMessage.value = `Are you sure you want to confirm payment for booking ${selectedBooking.value.bookingID}?`
  showConfirmModal.value = true
}

const confirmPaymentAction = async () => {
  if (!selectedBooking.value) return

  try {
    console.log('💳 Processing payment for booking:', selectedBooking.value.bookingID)
    await bookingService.confirmPayment(selectedBooking.value.bookingID)
    alert('✅ Pembayaran berhasil dikonfirmasi! Status pesanan diubah menjadi "Confirmed"')
    closeConfirmModal()
    closeDetailModal()
    loadBookings()
  } catch (error: any) {
    console.error('❌ Error confirming payment:', error)
    alert('❌ ' + (error.response?.data?.message || 'Gagal mengkonfirmasi pembayaran'))
  }
}

const handleUpdate = async () => {
  if (!selectedBooking.value) return
  
  // Check if booking has extra pay or refund
  if (selectedBooking.value.extraPay > 0) {
    alert('❌ Tidak dapat mengubah booking yang memiliki extra pay atau refund')
    return
  }
  
  // Open edit modal
  await openEditBookingModal(selectedBooking.value)
}

const handleRefund = async () => {
  if (!selectedBooking.value) return
  
  confirmAction.value = 'refund'
  confirmDetails.value = {
    title: 'Process Refund?',
    amount: selectedBooking.value.extraPay,
    reason: `Refund amount for booking ${selectedBooking.value.bookingID}`
  }
  confirmMessage.value = `Are you sure you want to process refund? This action cannot be undone.`
  showConfirmModal.value = true
}

const confirmRefundAction = async () => {
  if (!selectedBooking.value) return

  try {
    console.log('💰 Processing refund for booking:', selectedBooking.value.bookingID)
    const response = await bookingService.processRefund(selectedBooking.value.bookingID)
    alert('✅ Refund berhasil diproses! Property income berkurang sesuai nominal refund')
    closeConfirmModal()
    closeDetailModal()
    loadBookings()
  } catch (error: any) {
    console.error('❌ Error processing refund:', error)
    alert('❌ ' + (error.response?.data?.message || 'Gagal memproses refund'))
  }
}

const handlePayExtra = async () => {
  if (!selectedBooking.value) return
  
  confirmAction.value = 'pay'
  confirmDetails.value = {
    title: 'Pay Extra Amount?',
    amount: selectedBooking.value.extraPay,
  }
  confirmMessage.value = `You need to pay an additional Rp ${formatNumber(selectedBooking.value.extraPay)} for the updated booking.`
  showConfirmModal.value = true
}

const handleCancel = async () => {
  if (!selectedBooking.value) return
  
  let reasonText = ''
  if (selectedBooking.value.status === 0) {
    reasonText = 'Pesanan akan dibatalkan (Status: Waiting for Payment → Cancelled)'
  } else if (selectedBooking.value.status === 1) {
    reasonText = 'Pesanan akan dibatalkan dan property income akan dikurangi'
  } else if (selectedBooking.value.status === 3) {
    reasonText = 'Pesanan akan dibatalkan dan property income akan dikurangi dengan refund'
  }
  
  confirmAction.value = 'cancel'
  confirmDetails.value = {
    title: 'Cancel Booking?',
    reason: reasonText
  }
  confirmMessage.value = `Are you sure you want to cancel booking ${selectedBooking.value.bookingID}? This action cannot be undone.`
  showConfirmModal.value = true
}

const confirmCancelAction = async () => {
  if (!selectedBooking.value) return

  try {
    console.log('❌ Cancelling booking:', selectedBooking.value.bookingID)
    await bookingService.cancelBooking(selectedBooking.value.bookingID)
    alert('✅ Pemesanan berhasil dibatalkan!')
    closeConfirmModal()
    closeDetailModal()
    loadBookings()
  } catch (error: any) {
    console.error('❌ Error cancelling booking:', error)
    alert('❌ ' + (error.response?.data?.message || 'Gagal membatalkan pemesanan'))
  }
}

const handleChangeStatus = () => {
  if (!selectedBooking.value) return
  alert(`Redirect to change status for: ${selectedBooking.value.bookingID}`)
  closeDetailModal()
}

const openCreateBookingModal = () => {
  showCreateBookingModal.value = true
  loadProperties()
}

const closeCreateBookingModal = () => {
  showCreateBookingModal.value = false
  validationErrors.value = []
  newBooking.value = {
    propertyID: '',
    roomTypeID: '',
    roomID: '',
    checkInDate: '',
    checkOutDate: '',
    customerID: '',
    customerName: '',
    customerEmail: '',
    customerPhone: '',
    capacity: 1,
    isBreakfast: false
  }
  availableRoomTypes.value = []
  availableRooms.value = []
  selectedRoomTypeCapacity.value = 0
  selectedRoomPrice.value = 0
}

// ===== EDIT BOOKING FUNCTIONS =====
const openEditBookingModal = async (booking: Booking) => {
  if (!booking) return
  console.log('✏️ [OPEN EDIT] Opening edit modal for booking:', booking.bookingID)
  console.log('✏️ [OPEN EDIT] Booking data:', {
    roomID: booking.roomID,
    roomName: booking.roomName,
    propertyName: booking.propertyName
  })
  
  // Initialize basic form data
  editingBooking.value = {
    bookingID: booking.bookingID,
    propertyID: '',
    roomTypeID: '',
    roomID: booking.roomID,
    checkInDate: booking.checkInDate?.split('T')[0] || '',
    checkOutDate: booking.checkOutDate?.split('T')[0] || '',
    customerID: booking.customerID,
    customerName: booking.customerName,
    customerEmail: booking.customerEmail,
    customerPhone: booking.customerPhone,
    capacity: booking.capacity || 1,
    isBreakfast: booking.isBreakfast || false
  }
  
  validationErrors.value = []
  editAvailableRoomTypes.value = []
  editAvailableRooms.value = []
  editSelectedRoomTypeCapacity.value = 0
  editSelectedRoomPrice.value = 0
  
  showEditBookingModal.value = true
  
  // Load properties first
  console.log('✏️ [OPEN EDIT] Loading properties...')
  await loadProperties()
  console.log('✏️ [OPEN EDIT] Available properties count:', availableProperties.value.length)
  
  // Find property by propertyName from booking
  try {
    console.log('✏️ [OPEN EDIT] Searching for property by name:', booking.propertyName)
    
    const matchedProperty = availableProperties.value.find(
      (prop: any) => prop.propertyName === booking.propertyName
    )
    
    if (matchedProperty) {
      console.log('✏️ [OPEN EDIT] Found matching property:', matchedProperty.propertyID, matchedProperty.propertyName)
      editingBooking.value.propertyID = matchedProperty.propertyID
      
      // Load room types for this property
      await onEditPropertyChange()
      console.log('✏️ [OPEN EDIT] Room types loaded:', editAvailableRoomTypes.value.length)
      
      // Find room and its roomType by searching through loaded room types and rooms
      const allRoomsResponse = await propertyService.getAllRooms(matchedProperty.propertyID)
      const allRooms = Array.isArray(allRoomsResponse) ? allRoomsResponse : (allRoomsResponse?.data || [])
      
      const matchedRoom = allRooms.find((room: any) => room.roomID === booking.roomID)
      
      if (matchedRoom && matchedRoom.roomTypeID) {
        console.log('✏️ [OPEN EDIT] Found room with roomTypeID:', matchedRoom.roomTypeID)
        editingBooking.value.roomTypeID = matchedRoom.roomTypeID
        
        // Load rooms for this room type
        await onEditRoomTypeChange()
        console.log('✏️ [OPEN EDIT] Rooms loaded for room type')
        console.log('✏️ [OPEN EDIT] Cascading completed successfully!')
      } else {
        console.warn('✏️ [OPEN EDIT] Could not find room or roomTypeID')
      }
    } else {
      console.warn('✏️ [OPEN EDIT] Could not find property by name:', booking.propertyName)
      console.warn('✏️ [OPEN EDIT] User will need to select property manually')
    }
  } catch (error) {
    console.error('❌ [OPEN EDIT] Error during property/room search:', error)
    // Continue anyway - user can manually select if search fails
  }
}

const closeEditBookingModal = () => {
  showEditBookingModal.value = false
  validationErrors.value = []
  editingBooking.value = {
    bookingID: '',
    propertyID: '',
    roomTypeID: '',
    roomID: '',
    checkInDate: '',
    checkOutDate: '',
    customerID: '',
    customerName: '',
    customerEmail: '',
    customerPhone: '',
    capacity: 1,
    isBreakfast: false
  }
  editAvailableRoomTypes.value = []
  editAvailableRooms.value = []
  editCheckOutMinDate.value = ''
  editSelectedRoomTypeCapacity.value = 0
  editSelectedRoomPrice.value = 0
}

const onEditPropertyChange = async () => {
  const propertyID = editingBooking.value.propertyID
  console.log('🏘️ [EDIT PROPERTY] Property selected:', propertyID)
  
  editAvailableRoomTypes.value = []
  editAvailableRooms.value = []
  editingBooking.value.roomTypeID = ''
  editingBooking.value.roomID = ''
  editSelectedRoomTypeCapacity.value = 0
  editSelectedRoomPrice.value = 0
  
  if (!propertyID) {
    console.warn('🏘️ [EDIT PROPERTY] No property ID provided')
    return
  }
  
  try {
    console.log('🏘️ [EDIT PROPERTY] Loading room types for propertyID:', propertyID)
    
    // Use getRoomsByPropertyId which returns room types directly
    const roomTypes = await propertyService.getRoomsByPropertyId(propertyID)
    
    console.log('🏘️ [EDIT PROPERTY] Room types loaded:', roomTypes)
    console.log('🏘️ [EDIT PROPERTY] Room types count:', roomTypes?.length)
    
    if (Array.isArray(roomTypes) && roomTypes.length > 0) {
      editAvailableRoomTypes.value = roomTypes
      console.log('🏘️ [EDIT PROPERTY] ✅ Successfully loaded', roomTypes.length, 'room types')
      console.log('🏘️ [EDIT PROPERTY] Room types:', roomTypes.map((rt: any) => ({
        roomTypeID: rt.roomTypeID,
        name: rt.name,
        capacity: rt.capacity,
        price: rt.price
      })))
    } else {
      console.warn('🏘️ [EDIT PROPERTY] No room types found for property')
      editAvailableRoomTypes.value = []
    }
  } catch (error) {
    console.error('❌ [EDIT PROPERTY] Error loading room types:', error)
    editAvailableRoomTypes.value = []
  }
}

const onEditRoomTypeChange = async () => {
  const roomTypeID = editingBooking.value.roomTypeID
  const propertyID = editingBooking.value.propertyID
  
  console.log('🏠 [EDIT ROOM TYPE] Change triggered')
  console.log('🏠 [EDIT ROOM TYPE] Current roomTypeID:', roomTypeID)
  console.log('🏠 [EDIT ROOM TYPE] Current propertyID:', propertyID)
  
  editAvailableRooms.value = []
  editingBooking.value.roomID = ''
  editSelectedRoomTypeCapacity.value = 0
  editSelectedRoomPrice.value = 0
  editingBooking.value.capacity = 1
  
  if (!roomTypeID) {
    console.warn('🏠 [EDIT ROOM TYPE] No room type selected, exiting')
    return
  }
  
  if (!propertyID) {
    console.warn('🏠 [EDIT ROOM TYPE] No property selected, exiting')
    return
  }
  
  try {
    console.log('🏠 [EDIT ROOM TYPE] Fetching all rooms for property:', propertyID)
    const response = await propertyService.getAllRooms(propertyID)
    const roomsData = Array.isArray(response) ? response : (response?.data || [])
    
    console.log('🏠 [EDIT ROOM TYPE] API response received')
    console.log('🏠 [EDIT ROOM TYPE] Response data count:', roomsData?.length)
    
    if (!roomsData || roomsData.length === 0) {
      console.warn('🏠 [EDIT ROOM TYPE] No rooms in response')
      return
    }
    
    const filteredRooms: any[] = []
    
    roomsData?.forEach((room: any, index: number) => {
      // Extract roomTypeID from room using multiple fallback paths
      const extractedRoomTypeID = room.roomType?.roomTypeID || room.type?.roomTypeID || room.roomTypeID
      
      console.log(`🏠 [EDIT ROOM TYPE] Room ${index}:`, {
        roomID: room.roomID,
        name: room.name,
        extractedRoomTypeID: extractedRoomTypeID,
        targetRoomTypeID: roomTypeID,
        matches: extractedRoomTypeID === roomTypeID
      })
      
      if (extractedRoomTypeID === roomTypeID) {
        console.log(`🏠 [EDIT ROOM TYPE] ✅ MATCH FOUND - Room ${index} matches requested room type`)
        filteredRooms.push(room)
      }
    })
    
    console.log('🏠 [EDIT ROOM TYPE] ========== FILTERING SUMMARY ==========')
    console.log('🏠 [EDIT ROOM TYPE] Total rooms fetched:', roomsData?.length)
    console.log('🏠 [EDIT ROOM TYPE] Rooms matching roomTypeID ${roomTypeID}:', filteredRooms.length)
    console.log('🏠 [EDIT ROOM TYPE] Filtered rooms:', filteredRooms.map(r => ({
      roomID: r.roomID,
      name: r.name
    })))
    console.log('🏠 [EDIT ROOM TYPE] ========== END FILTERING ==========')
    
    // Check availability for each room based on selected dates
    if (editingBooking.value.checkInDate && editingBooking.value.checkOutDate && filteredRooms.length > 0) {
      console.log('🏠 [EDIT ROOM TYPE] Checking availability for filtered rooms...')
      const checkInDateTime = editingBooking.value.checkInDate + 'T14:00:00'
      const checkOutDateTime = editingBooking.value.checkOutDate + 'T12:00:00'
      
      const availabilityChecks = await Promise.all(
        filteredRooms.map(async (room: any) => {
          try {
            const isAvailable = await propertyService.checkSingleRoomAvailability(
              room.roomID,
              checkInDateTime,
              checkOutDateTime
            )
            // If editing current room, consider it available (can update same booking)
            const isCurrentRoom = room.roomID === editingBooking.value.roomID
            return { ...room, isAvailable: isAvailable || isCurrentRoom }
          } catch (error) {
            console.error(`Error checking room ${room.roomID}:`, error)
            // If it's the current room being edited, allow it
            return { ...room, isAvailable: room.roomID === editingBooking.value.roomID }
          }
        })
      )
      
      // Only show available rooms (or current room being edited)
      const availableRooms = availabilityChecks.filter((room: any) => room.isAvailable)
      editAvailableRooms.value = availableRooms
      
      console.log(`🏠 [EDIT ROOM TYPE] ✅ ${availableRooms.length}/${filteredRooms.length} rooms available for these dates`)
      
      if (availableRooms.length === 0) {
        alert('⚠️ No rooms of this type are available for selected dates. Please choose different dates or room type.')
      }
    } else {
      // No dates selected yet, show all rooms
      editAvailableRooms.value = filteredRooms
      console.log('🏠 [EDIT ROOM TYPE] No dates selected, showing all rooms')
    }
    
    // Find room type details from editAvailableRoomTypes
    const roomType = editAvailableRoomTypes.value.find(rt => rt.roomTypeID === roomTypeID)
    console.log('🏠 [EDIT ROOM TYPE] Looking for room type in editAvailableRoomTypes')
    console.log('🏠 [EDIT ROOM TYPE] Available room types count:', editAvailableRoomTypes.value.length)
    console.log('🏠 [EDIT ROOM TYPE] Available room types:', editAvailableRoomTypes.value.map(rt => rt.roomTypeID))
    console.log('🏠 [EDIT ROOM TYPE] Found room type:', roomType)
    
    if (roomType) {
      editSelectedRoomTypeCapacity.value = roomType.capacity || 0
      editSelectedRoomPrice.value = roomType.price || 0
      console.log('🏠 [EDIT ROOM TYPE] Set capacity:', editSelectedRoomTypeCapacity.value, 'and price:', editSelectedRoomPrice.value)
    } else {
      console.warn('🏠 [EDIT ROOM TYPE] Room type details not found for ID:', roomTypeID)
    }
    
  } catch (error) {
    console.error('❌ [EDIT ROOM TYPE] Error occurred:', error)
    console.error('❌ [EDIT ROOM TYPE] Error type:', (error as any)?.constructor?.name)
    console.error('❌ [EDIT ROOM TYPE] Error message:', (error as any)?.message)
  }
}

const onEditRoomChange = () => {
  console.log('🚪 [EDIT ROOM CHANGE] Room selected:', editingBooking.value.roomID)
  validateEditCheckOutDate()
}

const validateEditCheckOutDate = () => {
  if (editingBooking.value.checkInDate) {
    const checkInDate = new Date(editingBooking.value.checkInDate)
    checkInDate.setDate(checkInDate.getDate() + 1)
    editCheckOutMinDate.value = checkInDate.toISOString().split('T')[0]
  }
}

const validateEditForm = (): boolean => {
  validationErrors.value = []
  const booking = editingBooking.value
  
  // 1. Check all required fields
  if (!booking.propertyID) validationErrors.value.push('Pilih properti')
  if (!booking.roomTypeID) validationErrors.value.push('Pilih tipe kamar')
  if (!booking.roomID) validationErrors.value.push('Pilih kamar')
  if (!booking.checkInDate) validationErrors.value.push('Masukkan tanggal check-in')
  if (!booking.checkOutDate) validationErrors.value.push('Masukkan tanggal check-out')
  if (!booking.customerName) validationErrors.value.push('Masukkan nama pelanggan')
  if (!booking.customerEmail) validationErrors.value.push('Masukkan email pelanggan')
  if (!booking.customerPhone) validationErrors.value.push('Masukkan nomor telepon pelanggan')
  if (booking.capacity < 1) validationErrors.value.push('Kapasitas minimal 1 tamu')
  
  // 2. Email format validation
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (booking.customerEmail && !emailRegex.test(booking.customerEmail)) {
    validationErrors.value.push('Format email tidak valid')
  }
  
  // 3. Check-in date >= today
  const today = new Date().toISOString().split('T')[0]
  if (booking.checkInDate < today) {
    validationErrors.value.push('Tanggal check-in tidak boleh lebih awal dari hari ini')
  }
  
  // 4. Check-out > check-in + 1 day
  if (booking.checkInDate && booking.checkOutDate) {
    const checkIn = new Date(booking.checkInDate)
    const checkOut = new Date(booking.checkOutDate)
    checkIn.setHours(0, 0, 0, 0)
    checkOut.setHours(0, 0, 0, 0)
    const minCheckOut = new Date(checkIn)
    minCheckOut.setDate(minCheckOut.getDate() + 1)
    
    if (checkOut < minCheckOut) {
      validationErrors.value.push('Check-out harus minimal 1 hari setelah check-in')
    }
  }
  
  // 5. Capacity <= room type max capacity
  if (booking.capacity > editSelectedRoomTypeCapacity.value) {
    validationErrors.value.push(`Kapasitas tidak boleh melebihi ${editSelectedRoomTypeCapacity.value} tamu`)
  }
  
  console.log('✏️ [EDIT VALIDATION] Errors:', validationErrors.value)
  return validationErrors.value.length === 0
}

const getEditTotalDays = () => {
  if (!editingBooking.value.checkInDate || !editingBooking.value.checkOutDate) return 0
  return calculateTotalDays(editingBooking.value.checkInDate, editingBooking.value.checkOutDate)
}

const calculateEditTotalPrice = () => {
  if (!editingBooking.value.checkInDate || !editingBooking.value.checkOutDate) return 0
  
  const days = getEditTotalDays()
  let total = days * editSelectedRoomPrice.value
  
  if (editingBooking.value.isBreakfast) {
    total += days * 50000
  }
  
  console.log(`💰 [EDIT PRICE] Days: ${days}, Price: ${editSelectedRoomPrice.value}, Breakfast: ${editingBooking.value.isBreakfast}, Total: ${total}`)
  return total
}

const handleUpdateBooking = async () => {
  if (!selectedBooking.value || !editingBooking.value) return
  
  console.log('📝 [UPDATE BOOKING] Validating form...')
  if (!validateEditForm()) {
    console.log('❌ [UPDATE BOOKING] Validation failed')
    return
  }
  
  // Double-check: no extra pay and refund
  if (selectedBooking.value.extraPay > 0) {
    alert('❌ Tidak dapat mengubah booking yang memiliki extra pay atau refund')
    return
  }
  
  if (!confirm('Apakah Anda yakin ingin mengubah pemesanan ini?')) return
  
  try {
    console.log('📝 [UPDATE BOOKING] Preparing payload...')
    const payload = {
      bookingID: editingBooking.value.bookingID,
      roomID: editingBooking.value.roomID,
      checkInDate: `${editingBooking.value.checkInDate}T14:00:00`,
      checkOutDate: `${editingBooking.value.checkOutDate}T12:00:00`,
      customerID: selectedBooking.value.customerID, // Add customerID from original booking
      customerName: editingBooking.value.customerName,
      customerEmail: editingBooking.value.customerEmail,
      customerPhone: editingBooking.value.customerPhone,
      capacity: editingBooking.value.capacity,
      isBreakfast: editingBooking.value.isBreakfast,
      totalPrice: calculateEditTotalPrice()
    }
    
    console.log('📝 [UPDATE BOOKING] Payload:', payload)
    console.log('🌐 [UPDATE BOOKING] Calling API: PUT /bookings/update')
    
    await bookingService.updateBooking(editingBooking.value.bookingID, payload)
    
    console.log('✅ [UPDATE BOOKING] Success!')
    alert('✅ Pemesanan berhasil diperbarui!')
    
    closeEditBookingModal()
    closeDetailModal()
    loadBookings()
  } catch (error: any) {
    console.error('❌ [UPDATE BOOKING] Error:', error)
    const errorMsg = error.response?.data?.message || error.message || 'Failed to update booking'
    alert('❌ Gagal mengubah pemesanan: ' + errorMsg)
  }
}

onMounted(() => {
  console.log('� [MOUNTED] BookingsView mounted')
  
  // Set today and tomorrow dates
  const todayDate = new Date()
  today.value = todayDate.toISOString().split('T')[0]
  
  const tomorrowDate = new Date(todayDate)
  tomorrowDate.setDate(tomorrowDate.getDate() + 1)
  checkOutMinDate.value = tomorrowDate.toISOString().split('T')[0]
  
  loadBookings()
  loadProperties() // Always load properties on mount
  
  // Check if there's pending booking data from Properties page
  const pendingBookingStr = localStorage.getItem('pendingBooking')
  console.log('📍 [MOUNTED] pendingBooking from localStorage:', pendingBookingStr)
  
  if (pendingBookingStr) {
    try {
      const pendingBooking = JSON.parse(pendingBookingStr)
      console.log('📍 [MOUNTED] Parsed pending booking:', pendingBooking)
      
      newBooking.value.propertyID = pendingBooking.propertyID
      newBooking.value.roomTypeID = pendingBooking.roomTypeID
      newBooking.value.roomID = pendingBooking.roomID
      newBooking.value.checkInDate = pendingBooking.checkInDate
      newBooking.value.checkOutDate = pendingBooking.checkOutDate
      
      console.log('📍 [MOUNTED] Updated newBooking form:', newBooking.value)
      
      // Open create booking modal
      showCreateBookingModal.value = true
      
      // Load room types and rooms
      setTimeout(async () => {
        await onPropertyChange()
        if (newBooking.value.roomTypeID) {
          await onRoomTypeChange()
        }
      }, 100)
      
      // Clear localStorage
      localStorage.removeItem('pendingBooking')
    } catch (error) {
      console.error('❌ [MOUNTED] Error loading pending booking:', error)
    }
  }
})
</script>

<style scoped>
.glass-table {
  width: 100%;
  border-collapse: collapse;
}

.glass-table thead {
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--teal-cyan) 100%);
  border: none;
}

.glass-table th {
  padding: 1rem;
  text-align: left;
  font-weight: 700;
  color: #ffffff;
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.glass-table td {
  padding: 1rem;
  border-bottom: 1px solid var(--border-light);
  color: var(--text-primary);
}

.glass-table tbody tr:hover {
  background: rgba(74, 144, 226, 0.05);
}

.badge-warning {
  background: linear-gradient(135deg, rgba(243, 156, 18, 0.1) 0%, rgba(255, 179, 71, 0.1) 100%);
  color: var(--warning-yellow);
  border: 1px solid rgba(243, 156, 18, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 700;
  font-size: 0.85rem;
}

.badge-info {
  background: linear-gradient(135deg, rgba(74, 144, 226, 0.1) 0%, rgba(23, 195, 195, 0.1) 100%);
  color: var(--primary-blue);
  border: 1px solid rgba(74, 144, 226, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 700;
  font-size: 0.85rem;
}

.badge-primary {
  background: linear-gradient(135deg, rgba(46, 204, 113, 0.1) 0%, rgba(32, 201, 201, 0.1) 100%);
  color: var(--success-green);
  border: 1px solid rgba(46, 204, 113, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 700;
  font-size: 0.85rem;
}

/* Action Buttons - Simple Style */
.action-button {
  padding: 0.5rem 1rem;
  font-size: 0.85rem;
  font-weight: 600;
  border: 1px solid;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: transparent;
  text-transform: capitalize;
}

/* Text Buttons - Simple Style */
.text-button {
  padding: 0.5rem 0.75rem;
  font-size: 0.85rem;
  font-weight: 600;
  border: 1px solid;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: transparent;
  text-transform: capitalize;
}

.text-button.view {
  color: var(--primary-blue);
  border-color: var(--primary-blue);
}

.text-button.view:hover {
  background: rgba(74, 144, 226, 0.1);
}

.text-button.cancel {
  color: var(--danger-red);
  border-color: var(--danger-red);
}

.text-button.cancel:hover {
  background: rgba(231, 76, 60, 0.1);
}

.text-button.complete {
  color: var(--success-green);
  border-color: var(--success-green);
}

.text-button.complete:hover {
  background: rgba(46, 204, 113, 0.1);
}

.action-button.confirm {
  color: var(--success-green);
  border-color: var(--success-green);
  background: rgba(46, 204, 113, 0.05);
}

.action-button.confirm:hover {
  background: rgba(46, 204, 113, 0.1);
  border-color: var(--success-green);
}

.action-button.cancel {
  color: var(--danger-red);
  border-color: var(--danger-red);
  background: rgba(231, 76, 60, 0.05);
}

.action-button.cancel:hover {
  background: rgba(231, 76, 60, 0.1);
  border-color: var(--danger-red);
}

.action-button.complete {
  color: var(--primary-blue);
  border-color: var(--primary-blue);
  background: rgba(74, 144, 226, 0.05);
}

.action-button.complete:hover {
  background: rgba(74, 144, 226, 0.1);
  border-color: var(--primary-blue);
}

.action-button.danger {
  color: var(--danger-red);
  border-color: var(--danger-red);
  background: rgba(231, 76, 60, 0.05);
}

.action-button.danger:hover {
  background: rgba(231, 76, 60, 0.1);
  border-color: var(--danger-red);
}

.action-button.info {
  color: #2196f3;
  border-color: #2196f3;
  background: rgba(33, 150, 243, 0.05);
}

.action-button.info:hover {
  background: rgba(33, 150, 243, 0.1);
  border-color: #2196f3;
}

.action-button.warning {
  color: #ff9800;
  border-color: #ff9800;
  background: rgba(255, 152, 0, 0.05);
}

.action-button.warning:hover {
  background: rgba(255, 152, 0, 0.1);
  border-color: #ff9800;
}

/* Icon Button for View Details */
.icon-button {
  width: 36px;
  height: 36px;
  padding: 0;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  background: rgba(74, 144, 226, 0.1);
}

.icon-button:hover {
  background: rgba(74, 144, 226, 0.2);
  transform: scale(1.1);
}

.icon-button:active {
  transform: scale(0.95);
}

/* Modern Action Buttons */
.modern-button {
  width: 36px;
  height: 36px;
  padding: 0;
  border: 2px solid;
  border-radius: 50%;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  background: transparent;
}

.modern-button.cancel {
  color: var(--danger-red);
  border-color: var(--danger-red);
}

.modern-button.cancel:hover {
  background: rgba(231, 76, 60, 0.1);
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(231, 76, 60, 0.2);
}

.modern-button.cancel:active {
  transform: scale(0.95);
}

.modern-button.complete {
  color: var(--success-green);
  border-color: var(--success-green);
}

.modern-button.complete:hover {
  background: rgba(46, 204, 113, 0.1);
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(46, 204, 113, 0.2);
}

.modern-button.complete:active {
  transform: scale(0.95);
}

.action-button.detail {
  color: var(--primary-blue);
  border-color: var(--primary-blue);
  background: rgba(74, 144, 226, 0.05);
}

.action-button.detail:hover {
  background: rgba(74, 144, 226, 0.1);
  border-color: var(--primary-blue);
}

.glass-table button {
  margin: 0 0.25rem;
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
  backdrop-filter: blur(4px);
}

.modal-content {
  background: var(--bg-white);
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  max-width: 600px;
  width: 90%;
  max-height: 85vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  padding: 2rem;
  border-bottom: 2px solid var(--border-light);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  margin: 0;
  color: var(--text-primary);
  font-size: 1.5rem;
}

.modal-close {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  color: var(--text-secondary);
  padding: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.modal-close:hover {
  color: var(--danger-red);
  transform: scale(1.1);
}

.modal-body {
  padding: 2rem;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
}

.detail-item label {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 0.5rem;
}

.detail-item p {
  font-size: 1rem;
  color: var(--text-primary);
  margin: 0;
  word-break: break-word;
}

.modal-footer {
  padding: 1.5rem 2rem;
  border-top: 2px solid var(--border-light);
  display: flex;
  justify-content: space-between;
  gap: 1rem;
}

.modal-footer .glass-button {
  min-width: 120px;
  flex: 1;
}

.glass-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
}
</style>
