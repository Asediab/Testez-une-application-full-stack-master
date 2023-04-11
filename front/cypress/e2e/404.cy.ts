import * as cypress from "cypress";

describe('404 not found spec', () => {
  it('Not Found successful', () => {
    cy.request({url: '/notregistered', failOnStatusCode: false})
      .its('status')
      .should('equal', 404);
    cy.visit('/notregistered', {failOnStatusCode: false});
    cy.get('h1').contains('Page not found !');
  })
})
