<template>
  <div id="app">
    <!-- Navigation Bar -->
    <nav class="navbar">
      <div class="navbar-container">
        <RouterLink to="/" class="navbar-brand">
          <span style="background: linear-gradient(135deg, var(--primary-blue) 0%, var(--teal-cyan) 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">
            APAP Hotels
          </span>
        </RouterLink>
        <div class="nav-links">
          <RouterLink to="/properties" class="nav-link">Properties</RouterLink>
          <RouterLink to="/bookings" class="nav-link">Bookings</RouterLink>
          <RouterLink to="/statistics" class="nav-link">Statistics</RouterLink>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <main class="main-content">
      <div class="container">
        <RouterView />
      </div>
    </main>

    <!-- Footer (Optional) -->
    <footer class="app-footer">
      <div class="container">
        <p>&copy; 2024 APAP Hotels. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<style scoped>
:root {
  /* Kraflab-inspired Color Palette */
  --primary-blue: #4A90E2;
  --primary-blue-light: #5BA3F5;
  --teal-cyan: #17C3C3;
  --teal-cyan-light: #20D9D9;
  --success-green: #2ECC71;
  --danger-red: #E74C3C;
  --warning-yellow: #F39C12;
  --info-blue: #3498DB;
  --accent-pink: #FF69B4;
  --accent-orange: #FFB347;
  
  --text-primary: #1a1a2e;
  --text-secondary: #424242;
  --text-muted: #757575;
  
  --bg-light: #f8fafc;
  --bg-white: #ffffff;
  --bg-card: #ffffff;
  
  --border-light: #e0e7ff;
  --border-subtle: #f0f4f8;
  
  --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.08);
  --shadow-md: 0 4px 16px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 8px 32px rgba(0, 0, 0, 0.12);
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.navbar {
  background: var(--bg-white);
  border-bottom: 1px solid var(--border-light);
  box-shadow: var(--shadow-sm);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: 700;
  letter-spacing: -0.02em;
  text-decoration: none;
  cursor: pointer;
  transition: opacity 0.2s ease;
}

.navbar-brand:hover {
  opacity: 0.8;
}

.nav-links {
  display: flex;
  gap: 2rem;
  list-style: none;
}

.nav-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: 500;
  font-size: 0.95rem;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 1rem;
  right: 1rem;
  height: 2px;
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--teal-cyan) 100%);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.nav-link:hover {
  color: var(--primary-blue);
}

.nav-link:hover::after {
  transform: scaleX(1);
}

.nav-link.router-link-active {
  color: var(--primary-blue);
  font-weight: 700;
}

.nav-link.router-link-active::after {
  transform: scaleX(1);
}

.main-content {
  flex: 1;
  padding: 2rem 0;
}

.app-footer {
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--teal-cyan) 100%);
  color: white;
  text-align: center;
  padding: 2rem;
  margin-top: 4rem;
  border-top: 1px solid var(--border-light);
}

.app-footer p {
  margin: 0;
  font-weight: 500;
  opacity: 0.9;
}

/* Responsive */
@media (max-width: 768px) {
  .navbar-container {
    padding: 1rem;
    flex-direction: column;
    gap: 1rem;
  }

  .nav-links {
    gap: 1rem;
    flex-wrap: wrap;
    justify-content: center;
  }

  .main-content {
    padding: 1rem 0;
  }
}
</style>
