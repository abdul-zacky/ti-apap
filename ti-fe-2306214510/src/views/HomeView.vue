<template>
  <div class="home fade-in">
    <!-- Hero Section with Two Columns: Title Left, Image Right -->
    <div class="hero-banner-kraflab">
      <div class="hero-left">
        <h1 class="hero-main-title">
          Manage Your <br />
          <span class="hero-highlight">Accommodation</span> <br />
          Business
        </h1>
        <p class="hero-subtitle">Streamline bookings, properties, and guest management in one powerful platform</p>
        <div class="hero-buttons">
          <router-link to="/properties/create">
            <button class="glass-button primary">Get Started</button>
          </router-link>
          <router-link to="/properties">
            <button class="glass-button">Learn More</button>
          </router-link>
        </div>
      </div>
      <div class="hero-right">
        <div class="image-container">
          <img src="@/assets/burj-al-arab.png" alt="Burj Al Arab" class="hero-image-kraflab" />
        </div>
      </div>
    </div>

    <div v-if="loading" class="glass-card" style="text-align: center; padding: 3rem;">
      <div class="spinner" style="margin: 0 auto; margin-bottom: 1rem;"></div>
      <p>Loading dashboard...</p>
    </div>

    <div v-else>
      <!-- Stats Cards -->
      <div class="grid grid-cols-4 gap-4" style="margin-bottom: 2rem;">
        <div class="stat-card">
          <div class="stat-icon blue">
            <Building2 :size="32" />
          </div>
          <div class="stat-content">
            <div class="stat-label">Total Properties</div>
            <div class="stat-value">{{ stats.totalProperties || 0 }}</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon teal">
            <CheckCircle2 :size="32" />
          </div>
          <div class="stat-content">
            <div class="stat-label">Active Properties</div>
            <div class="stat-value">{{ stats.activeProperties || 0 }}</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon green">
            <Calendar :size="32" />
          </div>
          <div class="stat-content">
            <div class="stat-label">Total Bookings</div>
            <div class="stat-value">{{ stats.totalBookings || 0 }}</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon orange">
            <DollarSign :size="32" />
          </div>
          <div class="stat-content">
            <div class="stat-label">Total Income</div>
            <div class="stat-value">Rp {{ formatNumber(stats.totalIncome || 0) }}</div>
          </div>
        </div>
      </div>

      <!-- Quick Actions and System Info -->
      <div class="grid grid-cols-2 gap-4">
        <div class="glass-card">
          <h3 style="margin-bottom: 1.5rem; color: var(--primary-blue);">Quick Actions</h3>
          <div class="flex flex-col gap-2">
            <router-link to="/properties/create" style="text-decoration: none;">
              <button class="glass-button primary" style="width: 100%; justify-content: flex-start;">
                <Plus :size="20" />
                <span>Create Property</span>
              </button>
            </router-link>
            <router-link to="/bookings/create" style="text-decoration: none;">
              <button class="glass-button success" style="width: 100%; justify-content: flex-start;">
                <FileEdit :size="20" />
                <span>Create Booking</span>
              </button>
            </router-link>
            <router-link to="/statistics" style="text-decoration: none;">
              <button class="glass-button" style="width: 100%; justify-content: flex-start; color: var(--text-secondary);">
                <BarChart3 :size="20" />
                <span>View Statistics</span>
              </button>
            </router-link>
          </div>
        </div>

        <div class="glass-card">
          <h3 style="margin-bottom: 1.5rem; color: var(--teal-cyan);">System Status</h3>
          <div class="info-list">
            <div class="info-item">
              <span class="info-label">Confirmed Bookings</span>
              <span class="info-value" style="color: var(--primary-blue); font-size: 1.5rem; font-weight: 700;">{{ stats.confirmedBookings || 0 }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Backend Status</span>
              <span class="info-value status-badge" :class="backendStatus === 'connected' ? 'connected' : 'disconnected'">
                {{ backendStatus }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { statisticsService } from '@/services/statisticsService'
import { Building2, CheckCircle2, Calendar, DollarSign, Plus, FileEdit, BarChart3 } from 'lucide-vue-next'

const stats = ref<any>({})
const loading = ref(true)
const backendStatus = ref('connected')

const formatNumber = (num: number) => {
  return new Intl.NumberFormat('id-ID').format(num)
}

const loadStats = async () => {
  try {
    loading.value = true
    const response = await statisticsService.getStatisticsSummary()
    stats.value = response.data
    backendStatus.value = 'connected'
  } catch (error) {
    console.error('Error loading stats:', error)
    backendStatus.value = 'disconnected'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.home {
  animation: fadeIn 0.5s ease;
}

/* Hero Banner - Kraflab Style */
.hero-banner-kraflab {
  display: grid;
  grid-template-columns: 1fr 1.2fr;
  gap: 0;
  align-items: stretch;
  margin-bottom: 4rem;
  position: relative;
  padding: 0;
  background: transparent;
  min-height: 700px;
  margin-left: -2rem;
  margin-right: -2rem;
}

.hero-left {
  z-index: 10;
  padding: 4rem 3rem 4rem 2rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.hero-right {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  height: 100%;
  padding: 0;
  background: transparent;
}

.image-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  filter: drop-shadow(-40px 0px 100px rgba(74, 144, 226, 0.15));
}

.hero-image-kraflab {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 0;
  background: transparent;
  mask-image: linear-gradient(to bottom, transparent 0%, black 3%, black 97%, transparent 100%);
  -webkit-mask-image: linear-gradient(to bottom, transparent 0%, black 3%, black 97%, transparent 100%);
}

.hero-main-title {
  font-size: 3.5rem;
  font-weight: 700;
  line-height: 1.2;
  color: var(--text-primary);
  margin-bottom: 1.5rem;
  letter-spacing: -0.02em;
}

.hero-highlight {
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--teal-cyan) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 1.25rem;
  color: var(--text-secondary);
  margin-bottom: 2rem;
  line-height: 1.6;
  max-width: 500px;
}

.hero-buttons {
  display: flex;
  gap: 1rem;
  width: fit-content;
}

.hero-buttons .glass-button {
  padding: 1rem 2rem;
  font-size: 1rem;
  font-weight: 600;
}

.hero-buttons a {
  text-decoration: none;
}

/* Old Hero Styles - Hide */
.hero-banner {
  display: none;
}

.hero-image-wrapper {
  display: none;
}

.hero-overlay {
  display: none;
}

.hero-content-wrapper {
  display: none;
}

/* Stat Cards */
.stat-card {
  background: var(--bg-white);
  border-radius: 12px;
  border: 1px solid var(--border-light);
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1.5rem;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
  border-color: var(--primary-blue);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.75rem;
}

.stat-icon.blue {
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--primary-blue-light) 100%);
}

.stat-icon.teal {
  background: linear-gradient(135deg, var(--teal-cyan) 0%, var(--teal-cyan-light) 100%);
}

.stat-icon.green {
  background: linear-gradient(135deg, var(--success-green) 0%, var(--teal-cyan) 100%);
}

.stat-icon.orange {
  background: linear-gradient(135deg, var(--accent-orange) 0%, var(--accent-pink) 100%);
}

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 0.85rem;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.stat-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--text-primary);
}

/* Info List */
.info-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--border-light);
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  color: var(--text-secondary);
  font-size: 0.95rem;
  font-weight: 500;
}

.info-value {
  font-weight: 600;
  color: var(--text-primary);
}

.status-badge {
  display: inline-block;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 700;
  text-transform: capitalize;
}

.status-badge.connected {
  background: linear-gradient(135deg, rgba(46, 204, 113, 0.1) 0%, rgba(32, 201, 201, 0.1) 100%);
  color: var(--success-green);
  border: 1px solid rgba(46, 204, 113, 0.3);
}

.status-badge.disconnected {
  background: linear-gradient(135deg, rgba(231, 76, 60, 0.1) 0%, rgba(255, 105, 180, 0.1) 100%);
  color: var(--danger-red);
  border: 1px solid rgba(231, 76, 60, 0.3);
}

/* Responsive */
@media (max-width: 1024px) {
  .grid.grid-cols-4 {
    grid-template-columns: repeat(2, 1fr);
  }

  .grid.grid-cols-2 {
    grid-template-columns: 1fr;
  }

  .hero-banner-kraflab {
    grid-template-columns: 1fr;
    gap: 0;
    min-height: auto;
    padding: 0;
    margin-left: -1.5rem;
    margin-right: -1.5rem;
  }

  .hero-left {
    padding: 2.5rem 2rem 2.5rem 1.5rem;
  }

  .hero-right {
    height: 350px;
  }

  .hero-main-title {
    font-size: 2.5rem;
  }

  .image-container {
    filter: drop-shadow(-30px 0px 80px rgba(74, 144, 226, 0.12));
  }
}

@media (max-width: 768px) {
  .hero-banner-kraflab {
    grid-template-columns: 1fr;
    gap: 0;
    padding: 0;
    margin-bottom: 2rem;
    min-height: auto;
    margin-left: -1rem;
    margin-right: -1rem;
  }

  .hero-left {
    padding: 1.5rem;
  }

  .hero-main-title {
    font-size: 1.75rem;
    margin-bottom: 1rem;
  }

  .hero-subtitle {
    font-size: 0.95rem;
    margin-bottom: 1.5rem;
  }

  .hero-buttons {
    flex-direction: column;
    width: 100%;
  }

  .hero-buttons .glass-button {
    width: 100%;
  }

  .hero-right {
    height: 250px;
    padding: 0;
  }

  .image-container {
    filter: drop-shadow(-20px 0px 60px rgba(74, 144, 226, 0.1));
  }

  .grid.grid-cols-4 {
    grid-template-columns: 1fr;
  }

  .stat-card {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }

  .stat-icon {
    width: 48px;
    height: 48px;
  }
}

</style>
