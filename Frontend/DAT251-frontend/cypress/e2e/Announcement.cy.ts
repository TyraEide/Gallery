/// <reference types="cypress" />
describe('Announcements', () => {
  beforeEach(() => {
    cy.intercept('GET', '/announcements', {
      body: [
        {
          title: "Title",
          message: "Message.",
          author: { name: "Author" },
          posted_at: "2024-03-26T12:00:00Z",
          is_announcement: true
        }
      ]
    }).as('getAnnouncements');

    cy.visit('/#/announcement');
  });

  it('should display announcements from the API', () => {
    cy.wait('@getAnnouncements');
    cy.get('.announcement').should('exist');
    cy.get('.announcement h3').should('contain', 'Title');
    cy.get('.announcement p').should('contain', 'Message.');
    cy.get('.announcement small').should('contain', 'Author');
  });
});
