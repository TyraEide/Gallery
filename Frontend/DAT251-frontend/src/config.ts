// Configuration values from environment variables (via Vite)
const config = {
  // The URL of the Spring Boot API server
  springApiUrl: import.meta.env.VITE_SPRING_API_URL || 'http://localhost:8080',
};

export default config;
