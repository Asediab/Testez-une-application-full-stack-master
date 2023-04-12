import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {SessionService} from 'src/app/services/session.service';

import {MeComponent} from './me.component';
import {UserService} from "../../services/user.service";
import {By} from "@angular/platform-browser";
import {User} from "../../interfaces/user.interface";
import {of} from "rxjs";
import {Router} from "@angular/router";
import {RouterTestingModule} from "@angular/router/testing";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let service: UserService;
  let serviceSession: SessionService;
  let bar: MatSnackBar;
  let router: Router;

  let user: User = {
    id: 1,
    email: 'user@user.com',
    lastName: 'User',
    firstName: 'User',
    admin: false,
    password: 'userPass',
    createdAt: new Date('2023-04-01T00:00:00')
  };


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule
      ],
      providers: [
        {
          provide: SessionService,
          useValue: {
            sessionInformation: user,
            logOut: jest.fn()
          }
        },
        {
          provide: UserService,
          useValue: {
            getById: jest.fn().mockReturnValue(of(user)),
            delete: jest.fn().mockReturnValue(of(user)),
          }
        },
        {
          provide: MatSnackBar,
          useValue: {
            open: jest.fn()
          }
        }
      ],
    })
      .compileComponents();

    service = TestBed.inject(UserService);
    serviceSession = TestBed.inject(SessionService);
    bar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  test('should create', () => {
    // @ts-ignore
    expect(component).toBeTruthy();
  });

  test('User can see snackbar \'Your account has been deleted ! \' after deleting a account', fakeAsync(async () => {

    const logOutSpy = jest.spyOn(serviceSession, 'logOut');
    const deleteSpy = jest.spyOn(service, 'delete');
    const openSpy = jest.spyOn(bar, 'open');
    const routSpy = jest.spyOn(router, 'navigate');

    const button = fixture.debugElement.query(
      By.css('button[color="warn"]')
    );
    button.triggerEventHandler('click', null);
    tick(100);
    fixture.detectChanges();

    expect(logOutSpy).toHaveBeenCalled();
    expect(deleteSpy).toHaveBeenCalled();
    expect(bar.open).toHaveBeenCalledWith(
      'Your account has been deleted !', 'Close',
      {duration: 3000}
    );
    expect(routSpy).toHaveBeenCalledWith(['/']);
  }));
});
