describe('User', function () {
  it('werden angezeigt', function () {
    cy.visit('/')
    cy.get("table").children("tr").should('have.length', 51)
  })
  it('können nach Ländern gefiltert werden', function () {
    cy.visit('/')
    cy.get("table").children("tr").should('have.length', 51)
    cy.get("table").children("tr").last().children().last().invoke("text")
      .then((country) => {
        cy.get('select').select(country)

        cy.get("table").children("tr").should('have.length.above', 1)
        cy.get("table").children("tr").should('have.length.below', 51)
      })
  })
})
