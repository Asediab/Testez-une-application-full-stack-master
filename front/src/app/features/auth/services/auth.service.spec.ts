import {AuthService} from "./auth.service";
import {RegisterRequest} from "../interfaces/registerRequest.interface";
import {LoginRequest} from "../interfaces/loginRequest.interface";
import {TestBed} from "@angular/core/testing";
import { expect } from '@jest/globals';
import {HttpClient, HttpClientModule} from "@angular/common/http";

describe('AuthService', () => {
  let authService: AuthService;
  let httpClient: HttpClient;

  let requestRegister: RegisterRequest = {
    email: 'user@user.com',
    firstName: 'User',
    lastName: 'User',
    password: 'userPass'
  };

  let requestLogin: LoginRequest = {
    email: 'user@user.com',
    password: 'userPass',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [{
        provide: HttpClient, useValue: { post: jest.fn()}
      },],
    });
    authService = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
  });

  test('should create', () => {
    expect(authService).toBeTruthy();
  });

  test('Make POST request to the register endpoint', () => {
    authService.register(requestRegister);
    expect(httpClient.post).toHaveBeenCalledWith('api/auth/register', requestRegister);
  });

  test('Make GET request to the login endpoint', () => {
    authService.login(requestLogin);
    expect(httpClient.post).toHaveBeenCalledWith('api/auth/login', requestLogin);
  });
});
