import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    baseUrl: "http://localhost:5173", 
    supportFile: "cypress/support/e2e.js",
    env: {
      // Default backend URL for local development
      backendUrl: "http://localhost:8080"
    }
  },
});