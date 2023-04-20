import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import {Router} from "@angular/router";
import {TeacherService} from "../../../../services/teacher.service";
import {Teacher} from "../../../../interfaces/teacher.interface";
import {Session} from "../../interfaces/session.interface";
import {of} from "rxjs";
import {By} from "@angular/platform-browser";

let component: FormComponent;
let fixture: ComponentFixture<FormComponent>;
let router: Router;
let spyNavigate: any;
let serviceTeacher: TeacherService;
let serviceAPISession: SessionApiService;
let snackBar: MatSnackBar;

let teacher: Teacher = {
  id: 1,
  lastName: 'Teacher',
  firstName: 'Teacher',
  createdAt: new Date(),
  updatedAt: new Date(),
};

let teacher2: Teacher = {
  id: 2,
  lastName: 'Teacher2',
  firstName: 'Teacher2',
  createdAt: new Date(),
  updatedAt: new Date(),
};

let session: Session = {
  id: 1,
  name: 'Session',
  description: 'Description',
  date: new Date(),
  teacher_id: 1,
  users: [1, 2],
  createdAt: new Date(),
  updatedAt: new Date(),
};

const mockSessionService = {
  sessionInformation: {
    admin: true,
  },};

let teachers: Teacher[] = [teacher, teacher2];
describe('FormComponent Create as Admin', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService},
        { provide: SessionApiService, useValue: {
          create: jest.fn().mockReturnValue(of(session)),
          update: jest.fn().mockReturnValue(of(session))}},
        { provide: MatSnackBar, useValue: { open: jest.fn().mockReturnValue(undefined)}},
        { provide: TeacherService, useValue: { all: jest.fn().mockReturnValue(of(teachers))}},
      ],
      declarations: [FormComponent]
    })
      .compileComponents();
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    serviceTeacher = TestBed.inject(TeacherService);
    serviceAPISession = TestBed.inject(SessionApiService);
    snackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('After send to create the valid form, the notification confirm it, and we redirected to the /sessions', async () => {
    jest.spyOn(component, 'submit');
    jest.spyOn(serviceAPISession, 'create');
    const navigateSpy = jest.spyOn(router, 'navigate');
    const form = fixture.debugElement.query(By.css('form'));
    fixture.ngZone!.run(() => {
      form.triggerEventHandler('submit', {});
    });
    fixture.detectChanges();
    expect(snackBar.open).toHaveBeenCalledWith('Session created !', 'Close', {
      duration: 3000,
    });
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });
});

describe('FormComponent Update as Admin', () => {
  const mockSession = {
    name: 'Session',
    date: new Date(),
    attendees: 2,
  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService},
        { provide: SessionApiService, useValue: {
            detail: jest.fn().mockReturnValue(of(mockSession)),
            update: jest.fn().mockReturnValue(of(session))}},
        { provide: MatSnackBar, useValue: { open: jest.fn().mockReturnValue(undefined)}},
        { provide: TeacherService, useValue: { all: jest.fn().mockReturnValue(of(teachers))}},
      ],
      declarations: [FormComponent]
    })
      .compileComponents();
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    serviceTeacher = TestBed.inject(TeacherService);
    serviceAPISession = TestBed.inject(SessionApiService);
    snackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/1');
    component.ngOnInit();
  });

  test('After send to update the valid form, the notification confirm it, and we redirected to the /sessions', async () => {
    jest.spyOn(component, 'submit');
    jest.spyOn(serviceAPISession, 'update');
    const navigateSpy = jest.spyOn(router, 'navigate');
    const form = fixture.debugElement.query(By.css('form'));
    fixture.ngZone!.run(() => {
      form.triggerEventHandler('submit', {});
    });
    fixture.detectChanges();
    expect(serviceAPISession.update).toHaveBeenCalled();
    expect(component.submit).toHaveBeenCalled();
    expect(snackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', {
      duration: 3000,
    });
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });
});
