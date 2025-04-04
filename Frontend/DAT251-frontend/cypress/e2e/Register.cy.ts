/// <reference types="cypress" />

describe('User Registration', () => {
  beforeEach(() => {
    cy.visit('/#/register');
  });

  Cypress.on('window:console', (type, message) => {
    if (type === 'log' || type === 'error' || type === 'warn') {
      cy.task('log', `[${type.toUpperCase()}] ${message}`);
    }
  });

  it('Registers a user successfully', () => {
    // Intercept any request to the users endpoint, without specifying full URL
    cy.log(Cypress.env("baseUrl"))
    cy.intercept('POST', "http://something/api/users").as('registerUser');
    const randomness = Math.random().toString();
    cy.get('input[id="username"]').type('testuser1'+randomness);
    cy.get('input[id="email"]').type('test1'+randomness+'@example.com');
    cy.get('input[id="password"]').type('SecurePass123!');
    cy.get('input[id="confirmPassword"]').type('SecurePass123!');
    cy.get('button[type="submit"]').click();
    cy.log(Cypress.env("baseUrl"))

    cy.wait('@registerUser');
    cy.contains('Registration successful!', { timeout: 20000 }).should('be.visible');
  });

  it('Fails with short password', () => {
    cy.get('input[id="username"]').type('testuser2');
    cy.get('input[id="email"]').type('test2@example.com');
    cy.get('input[id="password"]').type('123');
    cy.get('input[id="confirmPassword"]').type('123');
    cy.get('button[type="submit"]').click();

    cy.contains('Password must be at least 8 characters.').should('be.visible');
  });

  it('Fails with mismatched passwords', () => {
    cy.get('input[id="username"]').type('testuser3');
    cy.get('input[id="email"]').type('test3@example.com');
    cy.get('input[id="password"]').type('SecurePass123!');
    cy.get('input[id="confirmPassword"]').type('DifferentPass!');
    cy.get('button[type="submit"]').click();

    cy.contains('Passwords do not match.').should('be.visible');
  });

  it('Fails with invalid email', () => {
    cy.get('input[id="username"]').type('testuser4');
    cy.get('input[id="email"]').type('invalidEmail@here');
    cy.get('input[id="password"]').type('SecurePass123!');
    cy.get('input[id="confirmPassword"]').type('SecurePass123!');
    cy.get('button[type="submit"]').click();
    
    cy.contains('Please provide a valid email address.').should('be.visible');
  });

  it('Fails with invalid email', () => {
    cy.get('input[id="username"]').type('testuser5');
    cy.get('input[id="email"]').type('invalidEmailhere.');
    cy.get('input[id="password"]').type('SecurePass123!');
    cy.get('input[id="confirmPassword"]').type('SecurePass123!').should('be.visible');
    cy.get('button[type="submit"]').click();
    
    cy.contains('Please provide a valid email address.');
  });
});
