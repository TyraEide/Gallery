/// <reference types="cypress" />

const mockCourses = [
  { id: 1, name: "Modern Software Development Methods", code: "DAT251"},
  { id: 2, name: "Programming Languages", code: "INF222"},
];

describe("Dashboard", () => {
  beforeEach(() => {

    cy.intercept('GET', '**/courses', {
      statusCode: 200,
      body: mockCourses,
    }).as('getCourses');

    cy.visit('/#/dashboard');
    cy.wait('@getCourses');
  });

  it("renders all mock courses as cards", () => {
    cy.get('.course-card').should('have.length', mockCourses.length);

    mockCourses.forEach((course, idx) => {
      cy.get('.course-card').eq(idx).within(() => {
        cy.contains('h3', course.name);
        cy.contains('p', course.code);
      });
    });
  });
});
