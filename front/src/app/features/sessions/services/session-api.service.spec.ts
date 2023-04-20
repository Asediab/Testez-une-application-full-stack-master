import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {Session} from "../interfaces/session.interface";

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClient: HttpClient;

  let session: Session = {
    id: 1,
    name: 'Session',
    description: 'Description',
    teacher_id: 1,
    users: [1, 2, 3],
    date: new Date(),
    createdAt: new Date(),
    updatedAt: new Date(),
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[ HttpClientModule ],
      providers: [{
        provide: HttpClient, useValue: {
          post: jest.fn(),
          get: jest.fn(),
          delete: jest.fn(),
          put: jest.fn()}
      },],
    });
    service = TestBed.inject(SessionApiService);
    httpClient = TestBed.inject(HttpClient);
  });

  test('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('Get request to the detail endpoint', () => {
    service.detail('1');
    expect(httpClient.get).toHaveBeenCalledWith('api/session/1');
  });

  test('Get request to the all endpoint', () => {
    service.all();
    expect(httpClient.get).toHaveBeenCalledWith('api/session');
  });

  test('Delete request to the delete endpoint', () => {
    service.delete('1');
    expect(httpClient.delete).toHaveBeenCalledWith('api/session/1');
  });

  test('Post request to the create endpoint', () => {
    service.create(session);
    expect(httpClient.post).toHaveBeenCalledWith('api/session', session);
  });

  test('Put request to the update endpoint', () => {
    service.update('1', session);
    expect(httpClient.put).toHaveBeenCalledWith('api/session/1', session);
  });

  test('Post request to the participate endpoint', () => {
    service.participate('1', '2');
    expect(httpClient.post).toHaveBeenCalledWith('api/session/1/participate/2', null);
  });

  test('Delete request to the unParticipate endpoint', () => {
    service.unParticipate('1', '2');
    expect(httpClient.delete).toHaveBeenCalledWith('api/session/1/participate/2');
  });
});
