import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';
import { mergeConfig } from 'vite';
import { defineConfig as defineVitestConfig } from 'vitest/config';

export default mergeConfig(
  defineConfig({
    plugins: [svelte()],
  }),
  defineVitestConfig({
    test: {
      globals: true,
      environment: 'jsdom',
    },
  })
);
