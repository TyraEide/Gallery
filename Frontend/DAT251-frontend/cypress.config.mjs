import { defineConfig } from 'cypress';
import * as dotenv from 'dotenv';

// Load environment variables from .env file
dotenv.config();

console.log(`CYPRESS_WEB_SERVER_OF_SPA from process.env: ${process.env.CYPRESS_WEB_SERVER_OF_SPA}`);
console.log(`API_BASE_URL from process.env: ${process.env.VITE_API_BASE_URL}`);
console.log(`VITE_API_URL from process.env: ${process.env.VITE_API_URL}`);

export default defineConfig({
  e2e: {
    // Use the .env file to get the SPA URL of where we "host" the svelte frontend app
    baseUrl: process.env.CYPRESS_WEB_SERVER_OF_SPA || "http://localhost:5173",
    env: {
      VITE_API_BASE_URL: process.env.VITE_API_BASE_URL || "http://localhost:8080",
      VITE_API_URL: process.env.VITE_API_URL|| "http://localhost:8080/api",
    },
    supportFile : false, 
  },
});