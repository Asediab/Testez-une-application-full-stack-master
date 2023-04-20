import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [
        { provide: HttpClient, useValue: {
          get: jest.fn(),
            delete: jest.fn(),
        }}
      ]
    });
    service = TestBed.inject(UserService);
    httpClient = TestBed.inject(HttpClient);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('Make GET request to the getById endpoint', () => {
    service.getById('1');
    expect(httpClient.get).toHaveBeenCalledWith('api/user/1');
  });

  test('Make DELETE request to the delete endpoint', () => {
    service.delete('1');
    expect(httpClient.delete).toHaveBeenCalledWith('api/user/1');
  });
});
