import * as cypress from "cypress";
import {login} from "../support/login";

describe('User Login spec', () => {
  it('Login successful', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    login('userRoleAdmin', 'yoga@studio.com', 'test!1234');

    cy.url().should('include', '/sessions')
    cy.get('span').contains('Logout').click();
  })
});
