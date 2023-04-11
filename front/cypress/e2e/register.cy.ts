import * as cypress from "cypress";

describe('User Registration spec', () => {
  it('Register passes', () => {
    cy.intercept('POST', '/api/auth/register', {
      body: {
        message: 'User registered successfully!',
      },
    }).as('register');

    cy.visit('/register');

    cy.get('input[formControlName=firstName]').type('user');
    cy.get('input[formControlName=lastName]').type('user');
    cy.get('input[formControlName=email]').type('user@user.com');
    cy.get('input[formControlName=password]').type(`${'userTest'}{enter}{enter}`);

    cy.url().should('include', '/login');
    cy.get('span').contains('Login');
  })
})
