# TravelAPAP Accommodation - Frontend

Vue.js frontend dengan glassmorphism design untuk TravelAPAP Accommodation Management System.

## Tech Stack

- **Vue 3** - Progressive JavaScript Framework
- **TypeScript** - Type-safe JavaScript
- **Vite** - Fast build tool
- **Vue Router** - Official routing
- **Pinia** - State management
- **Axios** - HTTP client
- **Chart.js + vue-chartjs** - Data visualization

## Features

- 🎨 **Glassmorphism Design** - Modern UI dengan glass effect dan gradient backgrounds
- 🏨 **Property Management** - Create, view, update, delete properties dengan room types
- 📅 **Booking System** - Manage bookings dengan berbagai status (Waiting, Confirmed, Cancelled, Refund, Done)
- 📊 **Statistics Dashboard** - Visualisasi data dengan Chart.js (Pie chart, Bar chart)
- 🔍 **Advanced Filtering** - Filter properties dan bookings berdasarkan kriteria
- 📱 **Responsive Design** - Mobile-friendly interface

## Project Setup

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## Application akan berjalan di:
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080

## File Structure

```
src/
├── assets/
│   └── main.css          # Glassmorphism styles
├── components/           # Reusable components
├── views/               # Page components
│   ├── HomeView.vue     # Dashboard
│   ├── PropertiesView.vue
│   ├── CreatePropertyView.vue
│   ├── BookingsView.vue
│   ├── CreateBookingView.vue
│   └── StatisticsView.vue
├── services/            # API services
│   ├── api.ts
│   ├── propertyService.ts
│   ├── bookingService.ts
│   └── statisticsService.ts
├── router/
│   └── index.ts         # Route definitions
├── App.vue              # Root component
└── main.ts              # Entry point
```

## API Integration

Backend REST API endpoint: `http://localhost:8080/api`

Endpoints yang digunakan:
- `/property` - Property CRUD operations
- `/booking` - Booking management
- `/statistics` - Dashboard statistics

## Design System

### Colors
- Primary Gradient: `#667eea → #764ba2`
- Secondary Gradient: `#f093fb → #f5576c`
- Success Gradient: `#4facfe → #00f2fe`

### Glassmorphism Effect
- Background: `rgba(255, 255, 255, 0.1)`
- Backdrop filter: `blur(20px)`
- Border: `1px solid rgba(255, 255, 255, 0.18)`
- Box shadow: `0 8px 32px rgba(31, 38, 135, 0.37)`

## Development

Pastikan backend sudah berjalan di port 8080 sebelum menjalankan frontend.

```bash
# Terminal 1 - Backend
cd accommodation-be
./gradlew bootRun

# Terminal 2 - Frontend
cd accommodation-fe
npm run dev
```
