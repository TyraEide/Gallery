// Configuration values from environment variables (via Vite)
const config = {
  // The URL of the Spring Boot API server
  API_BASE_URL: import.meta.env.API_BASE_URL || 'http://localhost:8080',
};

export default config;
