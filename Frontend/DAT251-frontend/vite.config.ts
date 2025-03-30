import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';
import { mergeConfig } from 'vite';
import { defineConfig as defineVitestConfig } from 'vitest/config';

export default mergeConfig(
  defineConfig({
    plugins: [svelte()],
    resolve: {
      conditions: process.env.NODE_ENV === 'test' ? ['browser'] : [],
    },
  }),
  defineVitestConfig({
    test: {
      globals: true,
      environment: 'jsdom',
    },
  })
);
