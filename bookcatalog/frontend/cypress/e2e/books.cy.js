describe("Books App", () => {
  beforeEach(() => {
    cy.visit("http://localhost:3000"); // Adjust if your frontend runs on another port
  });

  it("should load books table", () => {
    cy.get("table").should("exist");
  });

  it("should add a new book", () => {
    cy.get('input[name="isbn"]').type("978-9999999999");
    cy.get('input[name="title"]').type("Test Book Title");
    cy.get('input[name="author"]').type("Test Author");

    cy.get('button[type="submit"]').click();

    cy.contains("Test Book Title").should("exist");
    cy.contains("Test Author").should("exist");
  });

  it("should update a book", () => {
    cy.contains("Test Book Title")
      .parent("tr")
      .within(() => {
        cy.get("button.edit-button").click();
      });

    cy.get('input[name="title"]').clear().type("Updated Book Title");
    cy.get('button[type="submit"]').click();

    cy.contains("Updated Book Title").should("exist");
  });

  it("should delete a book", () => {
    cy.contains("Updated Book Title")
      .parent("tr")
      .within(() => {
        cy.get("button.delete-button").click();
      });

    cy.contains("Updated Book Title").should("not.exist");
  });
});
