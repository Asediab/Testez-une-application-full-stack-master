import * as cypress from "cypress";
import {login} from "../support/login";

describe('User info spec', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      { fixture: 'sessionsData' }
    ).as('sessionsData');
  });

  it('Displaying account info', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      { fixture: 'userRoleUser' }
    ).as('information');
    login('userRoleUser', 'user@user.com', 'user');
    cy.get('span').contains('Account').click();
    cy.contains('button', 'Detail').should('exist');
    cy.fixture('userRoleUser').then((json) => {
      const name = json.firstName + ' ' + json.lastName.toUpperCase();
      cy.contains('p', name);
      cy.contains('p', json.email);
    });
  });
})
