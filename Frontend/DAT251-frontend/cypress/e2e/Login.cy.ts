/// <reference types="cypress" />

describe('User Login', () => {
    beforeEach(() => {
      cy.visit('/#/login');
      cy.intercept('POST', 'http://localhost:8080/api/login', {
        statusCode: 202,
        body:
            {
                token: "token123",
                user: {
                    email: "usr@mail"
                }
            }
        }).as('loginRequest');
    });

    it("Successful login", () => {

        cy.get('input[id="email"]').type('usr@mail');
        cy.get('input[id="password"]').type("psw123");
        cy.get('button[type="submit"]').click();
        cy.wait("@loginRequest");
        cy.contains("Login Successful", { timeout: 20000 }).should('be.visible');
    })

    it("Redirects after login", () => {

        cy.get('input[id="email"]').type('usr@mail');
        cy.get('input[id="password"]').type("psw123");
        cy.get('button[type="submit"]').click();
        cy.wait("@loginRequest");
        cy.url({timeout: 10000}).should('include', '/dashboard')
    })

});