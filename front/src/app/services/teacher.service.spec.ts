import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import {UserService} from "./user.service";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [
        { provide: HttpClient, useValue: {
            get: jest.fn()
        }}]
    });
    service = TestBed.inject(TeacherService);
    httpClient = TestBed.inject(HttpClient);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('Make GET request to the all endpoint', () => {
    service.all();
    expect(httpClient.get).toHaveBeenCalledWith('api/teacher');
  });

  test('Make GET request to the detail endpoint', () => {
    service.detail('1');
    expect(httpClient.get).toHaveBeenCalledWith('api/teacher/1');
  });
});
