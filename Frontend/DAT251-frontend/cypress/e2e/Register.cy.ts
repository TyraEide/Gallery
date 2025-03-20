/// <reference types="cypress" />

describe('User Registration', () => {
    beforeEach(() => {
      cy.visit('http://localhost:5173/#/register');; // Visit the registration page
    });
  
    it('Registers a user successfully', () => {
      cy.intercept('POST', 'http://localhost:8080/api/users').as('registerUser');
  
      cy.get('input[id="username"]').type('testuser1');
      cy.get('input[id="email"]').type('test1@example.com');
      cy.get('input[id="password"]').type('SecurePass123!');
      cy.get('input[id="confirmPassword"]').type('SecurePass123!');
      cy.get('button[type="submit"]').click();
  
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
      
      cy.contains('Please provide a valid email address.').should('be.visible');;
    });

    it('Fails with invalid email', () => {
      cy.get('input[id="username"]').type('testuser5');
      cy.get('input[id="email"]').type('invalidEmailhere.');
      cy.get('input[id="password"]').type('SecurePass123!');
      cy.get('input[id="confirmPassword"]').type('SecurePass123!').should('be.visible');;
      cy.get('button[type="submit"]').click();
      
      cy.contains('Please provide a valid email address.');
    });
  });
  