/// <reference types="cypress" />

describe("Course Page", () => {
  const mockCourse = {
    id: 1,
    name: "Modern Software Development Methods",
    code: "DAT251",
    description: "This is the DAT251 course page."
  };
  const url = `/#/course/${mockCourse.id}`;

  it("displays the course details correctly", () => {
    cy.intercept(
      "GET",
      `**/api/courses/${mockCourse.id}`,
      { statusCode: 200, body: mockCourse }
    ).as("getCourse");

    cy.visit(url);
    cy.wait("@getCourse");

    cy.contains("h1", mockCourse.name).should("be.visible");
    cy.contains("p", `Code: ${mockCourse.code}`).should("be.visible");
    cy.contains("p", mockCourse.description).should("be.visible");
  });

  it("shows a loading message before the course details are loaded", () => {
    cy.intercept(
      "GET",
      `**/api/courses/${mockCourse.id}`,
      { statusCode: 200, body: mockCourse, delayMs: 1000 }
    ).as("getCourse");

    cy.visit(url);
    cy.contains("p", "Loading course details...").should("be.visible");
    cy.wait("@getCourse");
  });
});
