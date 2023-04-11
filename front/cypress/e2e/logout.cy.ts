import * as cypress from "cypress";
import {login} from "../support/login";

describe('User Logout spec', () => {
  it('Logout passes', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    login('userRoleAdmin', 'yoga@studio.com', 'test!1234');
    cy.get('span').contains('Logout').click();
    cy.get('span').contains('Login');
  })
})
