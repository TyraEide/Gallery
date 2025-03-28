import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      // setup code here
    },
    baseUrl: "http://localhost:5173", 
    supportFile: false,  
  },
});