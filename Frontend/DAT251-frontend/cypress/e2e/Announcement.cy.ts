describe('Announcements', () => {
  beforeEach(() => {
    cy.intercept('GET', 'announcements', {
      body: [
        {
          title: "Title",
          message: "Message from UiB.",
          author: { name: "UiB Author" },
          posted_at: "2024-03-26T12:00:00Z",
          is_announcement: true
        },
        {
          title: "Title",
          message: "Message from HVL.",
          author: { name: "HVL Author" },
          posted_at: "2024-03-27T12:00:00Z",
          is_announcement: true
        }
      ]
    }).as('getAnnouncements');

    cy.visit('/#/announcements');
  });

  it('should display announcements from the API', () => {
    cy.wait('@getAnnouncements');
    cy.get('.announcement').should('have.length', 2);
    cy.get('.announcement h3').eq(0).should('contain', 'Title');
    cy.get('.announcement p').eq(0).should('contain', 'Message from UiB.');
    cy.get('.announcement small').eq(0).should('contain', 'UiB Author');
    cy.get('.announcement h3').eq(1).should('contain', 'Title');
    cy.get('.announcement p').eq(1).should('contain', 'Message from HVL.');
    cy.get('.announcement small').eq(1).should('contain', 'HVL Author');
  });

  it('should display the full announcement when clicked', () => {
    cy.wait('@getAnnouncements');
    cy.get('.announcement-button').first().click();
    cy.get('.selected-topic').should('exist');
    cy.get('.selected-topic h2').should('contain', 'Title');
    cy.get('.selected-topic p').should('contain', 'Message from UiB.');
    cy.get('.selected-topic small').should('contain', 'UiB Author');
  });

  it('should hide other announcements when viewing a selected announcement', () => {
    cy.wait('@getAnnouncements');
    cy.get('.announcement-button').first().click();
    cy.get('.selected-topic').should('exist');
    cy.get('.announcement').should('not.exist');
  });
});
