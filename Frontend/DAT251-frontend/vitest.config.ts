import { defineConfig } from 'vitest/config';
import { svelte } from '@sveltejs/vite-plugin-svelte'

export default defineConfig({
  plugins: [svelte()],
  test: {
    globals: true,          // For global test functions like `describe`, `it`, `expect`
    environment: 'jsdom',   // Set the test environment to jsdom (for browser-like testing)
  },

});
