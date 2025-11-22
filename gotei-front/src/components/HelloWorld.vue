<template>
  <div class="app">
    <header class="header">
      <h1>Galeria gRPC</h1>
      <p>Frontend Vue consumindo o service-main (HTTP) que fala com C → A/B via gRPC.</p>
    </header>

    <section class="controls">
      <label>
        User ID:
        <input v-model="userId" type="text" />
      </label>
    </section>

    <main class="grid">
      <!-- Card A -->
      <section class="card">
        <header class="card-header">
          <h2>Imagem do Service A</h2>
          <button @click="refreshA">Atualizar</button>
        </header>
        <div class="card-body">
          <img :src="imageUrlA" alt="Imagem Service A" />
        </div>
        <footer class="card-footer">
          <small>source=A • userId={{ userId }}</small>
        </footer>
      </section>

      <!-- Card B -->
      <section class="card">
        <header class="card-header">
          <h2>Imagem do Service B</h2>
          <button @click="refreshB">Atualizar</button>
        </header>
        <div class="card-body">
          <img :src="imageUrlB" alt="Imagem Service B" />
        </div>
        <footer class="card-footer">
          <small>source=B • userId={{ userId }}</small>
        </footer>
      </section>
    </main>
  </div>
</template>


<script setup>
import { ref, computed } from 'vue'

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// estado
const userId = ref('user-1')

// cache buster pra forçar o browser a recarregar a imagem
const versionA = ref(Date.now())
const versionB = ref(Date.now())

const imageUrlA = computed(() =>
  `${apiBaseUrl}/image?source=A&userId=${encodeURIComponent(userId.value)}&v=${versionA.value}`
)

const imageUrlB = computed(() =>
  `${apiBaseUrl}/image?source=B&userId=${encodeURIComponent(userId.value)}&v=${versionB.value}`
)

function refreshA() {
  versionA.value = Date.now()
}

function refreshB() {
  versionB.value = Date.now()
}
</script>

<style scoped>
.app {
  min-height: 100vh;
  padding: 2rem;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  background: #0f172a;
  color: #e5e7eb;
}

.header {
  margin-bottom: 1.5rem;
}

.header h1 {
  font-size: 2rem;
  margin-bottom: 0.25rem;
}

.header p {
  font-size: 0.9rem;
  color: #9ca3af;
}

.controls {
  margin-bottom: 1.5rem;
  display: flex;
  gap: 1rem;
  align-items: center;
}

.controls label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.controls input {
  padding: 0.4rem 0.6rem;
  border-radius: 0.375rem;
  border: 1px solid #4b5563;
  background: #020617;
  color: #e5e7eb;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
}

.card {
  background: #020617;
  border-radius: 0.75rem;
  border: 1px solid #1f2937;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.card-header {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #111827;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-header h2 {
  font-size: 1rem;
  margin: 0;
}

.card-header button {
  padding: 0.3rem 0.75rem;
  border-radius: 999px;
  border: 1px solid #4b5563;
  background: #1d4ed8;
  color: #f9fafb;
  cursor: pointer;
  font-size: 0.8rem;
}

.card-header button:hover {
  background: #2563eb;
}

.card-body {
  padding: 0.75rem;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #020617;
}

.card-body img {
  max-width: 100%;
  max-height: 320px;
  border-radius: 0.5rem;
  border: 1px solid #111827;
  object-fit: contain;
}

.card-footer {
  padding: 0.5rem 1rem 0.75rem;
  border-top: 1px solid #111827;
  font-size: 0.75rem;
  color: #6b7280;
}
</style>
