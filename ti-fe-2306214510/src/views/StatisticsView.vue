<template>
  <div class="statistics fade-in">
    <h1>Statistika Booking per Periode</h1>

    <div class="filter-section">
      <div class="filter-group">
        <label for="month">Bulan:</label>
        <select id="month" v-model="selectedMonth">
          <option value="1">Januari</option>
          <option value="2">Februari</option>
          <option value="3">Maret</option>
          <option value="4">April</option>
          <option value="5">Mei</option>
          <option value="6">Juni</option>
          <option value="7">Juli</option>
          <option value="8">Agustus</option>
          <option value="9">September</option>
          <option value="10">Oktober</option>
          <option value="11">November</option>
          <option value="12">Desember</option>
        </select>
      </div>

      <div class="filter-group">
        <label for="year">Tahun:</label>
        <select id="year" v-model="selectedYear">
          <option v-for="year in yearOptions" :key="year" :value="year">
            {{ year }}
          </option>
        </select>
      </div>

      <button @click="loadChart" class="btn-show">Show Statistic</button>
    </div>

    <div v-if="loading" class="glass-card">
      <p>Loading statistics...</p>
    </div>

    <div v-else>
      <div class="glass-card" style="width: 100%;">
        <h3>Property Income Statistic</h3>
        <div class="chart-container">
          <Bar :data="bookingChartData" :options="chartOptions" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import { statisticsService } from '@/services/statisticsService'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

const loading = ref(true)
const selectedMonth = ref(String(new Date().getMonth() + 1))
const selectedYear = ref(String(new Date().getFullYear()))
const bookingChartData = ref<any>({})

const yearOptions = computed(() => {
  const currentYear = new Date().getFullYear()
  const years = []
  for (let i = currentYear - 5; i <= currentYear + 5; i++) {
    years.push(i)
  }
  return years
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: true,
  indexAxis: 'y' as const,
  plugins: {
    legend: {
      labels: {
        color: '#1a1a2e',
        font: {
          size: 12
        },
        padding: 20,
        usePointStyle: true,
        pointStyle: 'circle'
      }
    },
    tooltip: {
      callbacks: {
        label: function (context: any) {
          return 'Income: Rp ' + context.parsed.x.toLocaleString('id-ID')
        }
      }
    }
  },
  scales: {
    x: {
      ticks: {
        color: '#424242',
        font: {
          size: 11
        },
        callback: function (value: any) {
          return 'Rp ' + value.toLocaleString('id-ID')
        }
      },
      grid: {
        color: 'rgba(74, 144, 226, 0.05)',
        drawBorder: false
      }
    },
    y: {
      ticks: {
        color: '#424242',
        font: {
          size: 11
        }
      },
      grid: {
        color: 'rgba(74, 144, 226, 0.05)',
        drawBorder: false
      }
    }
  }
}

const loadChart = async () => {
  try {
    console.log(`📊 Loading chart for ${selectedMonth.value}/${selectedYear.value}`)
    loading.value = true

    const response = await statisticsService.getBookingChart(
      Number(selectedMonth.value),
      Number(selectedYear.value)
    )

    console.log('📊 API Response:', response)

    // Handle both response formats
    const chartDataByProperty = response?.data || response || {}

    // If no data, show empty chart
    if (!chartDataByProperty || Object.keys(chartDataByProperty).length === 0) {
      console.log('⚠️ No data for selected period')
      bookingChartData.value = {
        labels: [],
        datasets: [
          {
            label: `Most Property Income (${selectedMonth.value}/${selectedYear.value})`,
            data: [],
            backgroundColor: 'rgba(74, 144, 226, 0.7)',
            borderColor: 'rgba(74, 144, 226, 1)',
            borderWidth: 2,
            borderRadius: 4
          }
        ]
      }
      loading.value = false
      return
    }

    // Sort by income (value) in descending order
    const sortedEntries = Object.entries(chartDataByProperty)
      .sort(([, a], [, b]) => (b as number) - (a as number))

    const labels = sortedEntries.map(([name]) => name)
    const incomeData = sortedEntries.map(([, income]) => income)

    bookingChartData.value = {
      labels,
      datasets: [
        {
          label: `Most Property Income (${selectedMonth.value}/${selectedYear.value})`,
          data: incomeData,
          backgroundColor: 'rgba(74, 144, 226, 0.7)',
          borderColor: 'rgba(74, 144, 226, 1)',
          borderWidth: 2,
          borderRadius: 4
        }
      ]
    }
  } catch (error) {
    console.error('❌ Error loading chart:', error)
    alert('Failed to load statistics')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadChart()
})
</script>

<style scoped>
.statistics {
  animation: fadeIn 0.5s ease;
}

.statistics h1 {
  margin-bottom: 2rem;
  color: var(--text-primary);
  font-size: 2rem;
  font-weight: 700;
}

.filter-section {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  align-items: flex-end;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-group label {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.95rem;
}

.filter-group select {
  padding: 0.75rem 1rem;
  border: 1px solid var(--border-light);
  border-radius: 8px;
  background-color: var(--bg-white);
  color: var(--text-primary);
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 150px;
}

.filter-group select:hover {
  border-color: var(--primary-blue);
}

.filter-group select:focus {
  outline: none;
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
}

.btn-show {
  padding: 0.75rem 2rem;
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--teal-cyan) 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.95rem;
}

.btn-show:hover {
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
  transform: translateY(-2px);
}

.btn-show:active {
  transform: translateY(0);
}

.glass-card {
  background: var(--bg-white);
  border: 1px solid var(--border-light);
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
}

.glass-card:hover {
  box-shadow: var(--shadow-md);
  border-color: var(--primary-blue);
}

.glass-card h3 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  color: var(--text-primary);
  font-weight: 700;
  font-size: 1.25rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.glass-card h3::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 20px;
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--teal-cyan) 100%);
  border-radius: 2px;
}

.chart-container {
  position: relative;
  height: 500px;
  padding: 1rem 0;
}

@media (max-width: 768px) {
  .filter-section {
    flex-direction: column;
  }

  .filter-group select {
    min-width: 100%;
  }

  .btn-show {
    width: 100%;
  }

  .chart-container {
    height: 400px;
  }
}
</style>
