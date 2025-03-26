/// <reference types="cypress" />
describe('Announcements Component (Minimal)', () => {
  beforeEach(() => {
    cy.visit('/#/announcement');
  });

  it('should display the hardcoded mock announcement', () => {
    cy.get('.announcement').should('exist');
    cy.get('.announcement h3').should('contain', 'Title1');
    cy.get('.announcement p').should('contain', 'Message1');
    cy.get('.announcement small').should('contain', 'Author1');
  });
});