import * as cypress from "cypress";
import {login} from "../support/login";

describe('User visit Sessions spec', () => {

  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      {fixture: 'sessionsData'}
    ).as('session');
  });

  it('User sees sessions after login', () => {
    login('userRoleUser', 'user@user.com', 'userPass');

    cy.get('mat-card').find('.item').its('length').should('equal', 3);
  });

  it('Admin sees sessions with buttons Create and Details', () => {
    login('userRoleAdmin', 'yoga@studio.com', 'adminPass');

    cy.get('mat-card').find('.item').its('length').should('equal', 3);
    cy.contains('button', 'Create');
    cy.get('mat-card').find('.item').contains('button', 'Detail');
  });

  it('Creating a session', function () {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      {fixture: 'teachersData'}
    ).as('teachersData');

    cy.intercept(
      {
        method: 'POST',
        url: '/api/session',
      },
      {fixture: 'newSession'}
    ).as('newSession');

    login('userRoleAdmin', 'yoga@studio.com', 'adminPass');
    cy.contains('button', 'Create').click();

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      {fixture: 'sessionsGetById'}
    ).as('sessionsGetById');

    cy.url().should('include', '/create');

    cy.fixture('newSession').then((json) => {
      cy.get('input[formControlName=name]').type(json.name);
      cy.get('input[formControlName=date]').type(json.date);
      cy.get('textarea[formControlName=description]').type(json.description);
      cy.get('mat-select[formControlName=teacher_id]').click();
      cy.get('#mat-option-1').click();
      cy.contains('button', 'Save').click();
    });
  });

  it('Updating a session', function () {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      { fixture: 'teachersData' }
    ).as('teachersData');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/3',
      },
      { fixture: 'sessionsGetById' }
    ).as('sessionsGetById');

    cy.intercept(
      {
        method: 'PUT',
        url: '/api/session/3',
      },
      { fixture: 'sessionsUpdated' }
    ).as('sessionsUpdated');

    login('userRoleAdmin', 'yoga@studio.com', 'adminPass');

    cy.get('.mat-card').last()
      .contains('button', 'Edit')
      .click();

    cy.fixture('sessionsUpdated').then((json) => {
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        { fixture: 'sessionReadUpdated' }
      ).as('sessionUpdated');
      cy.get('input[formControlName=name]').type(json.name);
      cy.get('input[formControlName=date]').type(new Date(json.date).toISOString().split('T')[0]);
      cy.get('mat-select[formControlName=teacher_id]').click();
      cy.get('#mat-option-0').click();
      cy.get('textarea[formControlName=description]').type(json.description);
      cy.contains('button', 'Save').click();

      cy.get('.mat-card-title').last().should('contain', json.name);
      cy.get('.mat-card-content').last().should('contain', json.description);
    });
  });

  it('Deleting a session', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/3',
      },
      { fixture: 'sessionsGetById' }
    ).as('sessionsGetById');

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/session/3',
      },
      {}
    ).as('deleted');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      { fixture: 'teacher' }
    ).as('teacher');

    login('userRoleAdmin', 'yoga@studio.com', 'adminPass');

    cy.get('.mat-card').last()
      .contains('button', 'Detail')
      .click();
    cy.get('.mat-card').last()
      .contains('button', 'Delete')
      .click();
    cy.url().should('include', '/sessions');
  });

  it('User participate a session', () => {
    login('userRoleUser', 'user@user.com', 'user');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      { fixture: 'teacher' }
    ).as('teacher');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      { fixture: 'teacher' }
    ).as('teacher2');

    cy.intercept(
      {
        method: 'POST',
        url: '/api/session/1/participate/1',
      },
      []
    ).as('participate');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      { fixture: 'sessionsGetById' }
    ).as('sessionsGetById');

    cy.get('.mat-card')
      .first().contains('button', 'Detail')
      .click();

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      { fixture: 'sessionsUpdated' }
    ).as('sessionsUpdated');

    cy.get('.mat-card').last().contains('button', 'Participate').click();

    cy.get('.mat-card').first().contains('button', 'Do not participate');
  });

  it('User do not participate a session', () => {
    login('userRoleUser', 'user@user.com', 'user');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      { fixture: 'sessionsUpdated' }
    ).as('sessionsUpdated');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      { fixture: 'teacher' }
    ).as('teacher');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/2',
      },
      { fixture: 'teacher2' }
    ).as('teacher2');

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/session/1/participate/1',
      },
      []
    ).as('notParticipate');

    cy.get('.mat-card')
      .first().contains('button', 'Detail').click();

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      { fixture: 'sessionsGetById' }
    ).as('sessionsUpdated');

    cy.get('.mat-card').first().contains('button', 'Do not participate').click();

    cy.get('.mat-card').first().contains('button', 'Participate');
  });
});

