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