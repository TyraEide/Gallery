// Override backend URL from environment variable if provided
if (Cypress.env('backendUrl')) {
  // You can set this as a global variable or configure how your app loads the backend URL
  Cypress.config('backendUrl', Cypress.env('backendUrl'));
  console.log(`Using backend URL from environment: ${Cypress.env('backendUrl')}`);
}