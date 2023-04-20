import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {AuthService} from "../../services/auth.service";
import {LoginRequest} from "../../interfaces/loginRequest.interface";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {By} from "@angular/platform-browser";
import {Router} from "@angular/router";
import {of, throwError} from "rxjs";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  let request: LoginRequest = {
    email: 'yoga@studio.com',
    password: 'test!1234',
  };

  let response: SessionInformation = {
    token:
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE2Nzg5NjQwNjQsImV4cCI6MTY3OTA1MDQ2NH0.ZHTp0XHK6IaWLzTKLHwyJXeDOAsyu9RPLgUBUjaniDulLLttRlWP-nVfZCpjIqn6Zb-xAumtPPLVxR9L2L17gA',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        AuthService,
        { provide: Router, useValue: { navigate: jest.fn() } },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('Submit button is disable if user didnt put a password and a login', () => {
    let button = fixture.debugElement.query(By.css('button[type="submit"]'));
    fixture.detectChanges();
    expect(button.nativeElement.disabled).toBeTruthy();
  });

  test('Form control with an invalid email', () => {
    let object = component.form.controls['email'];
    object.setValue('user.com')
    fixture.detectChanges();
    expect(object.valid).toBeFalsy();
  });

  test('Form control with a valid email', () => {
    let object = component.form.controls['email'];
    object.setValue('user@user.com')
    fixture.detectChanges();
    expect(object.valid).toBeTruthy();
  });

  test('Form control with an empty password', () => {
    let object = component.form.controls['password'];
    object.setValue('')
    fixture.detectChanges();
    expect(object.valid).toBeFalsy();
  });

  test('Form control with a password', () => {
    let object = component.form.controls['password'];
    object.setValue('password123')
    fixture.detectChanges();
    expect(object.valid).toBeTruthy();
  });

  test('Login with a valid loginRequest',async() => {
    let authServiceSpy = jest.spyOn(authService, 'login').mockReturnValue(of(response));
    let sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    let routerSpy = jest.spyOn(router, 'navigate');
    let submitSpy = jest.spyOn(component, 'submit');

    sessionService.logIn(response);
    authService.login(request).subscribe();

    let form = fixture.debugElement.query(By.css('.login-form'));
    form.triggerEventHandler('submit', null);
    fixture.detectChanges();

    expect(component.submit).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  test('Login with an invalid loginRequest', async() => {
    let authServiceSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('')));
    let routerSpy = jest.spyOn(router, 'navigate');

    authService.login({email: '', password: ''}).subscribe();

    let form = fixture.debugElement.query(By.css('.login-form'));
    form.triggerEventHandler('submit', {});
    fixture.detectChanges();

    let error = fixture.debugElement.query(By.css('.error')).nativeElement;
    expect(error).toBeTruthy();
    expect(routerSpy).not.toBeCalled();

  });
});
