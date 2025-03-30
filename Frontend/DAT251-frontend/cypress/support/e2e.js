// Import commands.js using ES2015 syntax:
// import './commands'

// Make sure the Cypress.env values are properly available to tests
// by logging them at the start of each run
Cypress.on('test:before:run', () => {
  const backendUrl = Cypress.env('backendUrl');
  if (backendUrl) {
    console.log(`Using Spring API URL from environment: ${backendUrl}`);
  } else {
    console.warn('No backendUrl found in Cypress environment, will use fallback URL');
  }
});

// Override the app's config.springApiUrl with Cypress.env('backendUrl') if available
// This forces the app to use the correct Spring API URL during tests
Cypress.on('window:before:load', (win) => {
  if (Cypress.env('backendUrl')) {
    // Create a fake env variable to match what Vite would normally provide
    win.VITE_SPRING_API_URL = Cypress.env('backendUrl');
  }
});
