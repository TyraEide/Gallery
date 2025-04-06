/// <reference types="cypress" />

describe('Announcements', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/courses/announcements', {
      body: [
        {
          title: "Title",
          message: "Message.",
          author: { name: "Author" },
          posted_at: "2024-03-26T12:00:00Z",
          is_announcement: true
        },
        {
          title: "Another Title",
          message: "Another Message.",
          author: { name: "Another Author" },
          posted_at: "2024-03-27T12:00:00Z",
          is_announcement: true
        }
      ]
    }).as('getAnnouncements');

    cy.visit('/#/announcement');
  });

  it('should display announcements from the API', () => {
    cy.wait('@getAnnouncements');
    cy.get('.announcement').should('have.length', 2);
    cy.get('.announcement h3').eq(0).should('contain', 'Title');
    cy.get('.announcement p').eq(0).should('contain', 'Message.');
    cy.get('.announcement small').eq(0).should('contain', 'Author');
    cy.get('.announcement h3').eq(1).should('contain', 'Another Title');
    cy.get('.announcement p').eq(1).should('contain', 'Another Message.');
    cy.get('.announcement small').eq(1).should('contain', 'Another Author');
  });

  it('should display the full announcement when clicked', () => {
    cy.wait('@getAnnouncements');
    cy.get('.announcement-button').first().click();
    cy.get('.selected-topic').should('exist');
    cy.get('.selected-topic h2').should('contain', 'Title');
    cy.get('.selected-topic p').should('contain', 'Message.');
    cy.get('.selected-topic small').should('contain', 'Author');
  });

  it('should hide other announcements when viewing a selected announcement', () => {
    cy.wait('@getAnnouncements');
    cy.get('.announcement-button').first().click();
    cy.get('.selected-topic').should('exist');
    cy.get('.announcement').should('not.exist');
  });
});
