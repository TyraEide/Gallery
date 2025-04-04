import { defineConfig } from 'cypress';
import * as dotenv from 'dotenv';

// Load environment variables from .env file
dotenv.config();

export default defineConfig({
  e2e: {
    // Use the .env file to get the SPA URL of where we "host" the svelte frontend app
    baseUrl: process.env.CYPRESS_WEB_SERVER_OF_SPA || "http://localhost:5173",
    supportFile : false, 
  },
});