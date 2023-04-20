import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import {BehaviorSubject, of} from "rxjs";
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;

  let sessionInformation: SessionInformation = {
    token:
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE2Nzg5NjQwNjQsImV4cCI6MTY3OTA1MDQ2NH0.ZHTp0XHK6IaWLzTKLHwyJXeDOAsyu9RPLgUBUjaniDulLLttRlWP-nVfZCpjIqn6Zb-xAumtPPLVxR9L2L17gA',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('logIn change sessionInformation and isLogin', () => {
    service.logIn(sessionInformation);
    expect(service.sessionInformation).toBe(sessionInformation);
    expect(service.isLogged).toBeTruthy();
  });

  test('logOut change sessionInformation and isLogin', () => {
    service.logOut();
    expect(service.sessionInformation).toBe(undefined);
    expect(service.isLogged).toBeFalsy();
  });
});
